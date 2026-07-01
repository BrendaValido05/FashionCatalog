package es.ulpgc.eite.da.fashioncatalog.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


@RunWith(RobolectricTestRunner.class)
public class CatalogRepositoryTest {

    private Context context;
    private RepositoryContract repository;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        // Empezamos cada test con una base de datos y unas preferencias limpias
        context.deleteDatabase(CatalogRepository.DB_FILE);
        context.getSharedPreferences("catalog_prefs", Context.MODE_PRIVATE)
                .edit().clear().commit();
        CatalogRepository.resetInstance();
        repository = CatalogRepository.getInstance(context);
    }

    @After
    public void tearDown() {
        CatalogRepository.resetInstance();
    }

    private UserItem newUser(String name, String email, String password) {
        UserItem user = new UserItem();
        user.nombre = name;
        user.email = email;
        user.password = password;
        return user;
    }

    @Test
    public void registerUserSucceedsForNewEmail() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean success = new AtomicBoolean(false);
        final AtomicBoolean duplicate = new AtomicBoolean(true);

        repository.registerUser(newUser("Ana", "ana@ulpgc.es", "123456"),
                (ok, dup) -> {
                    success.set(ok);
                    duplicate.set(dup);
                    latch.countDown();
                });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertTrue(success.get());
        assertFalse(duplicate.get());
    }

    @Test
    public void registerUserFailsForDuplicateEmail() throws InterruptedException {
        registerAndWait("Ana", "ana@ulpgc.es", "123456");

        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean success = new AtomicBoolean(true);
        final AtomicBoolean duplicate = new AtomicBoolean(false);

        repository.registerUser(newUser("Otra Ana", "ana@ulpgc.es", "otraPass"),
                (ok, dup) -> {
                    success.set(ok);
                    duplicate.set(dup);
                    latch.countDown();
                });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertFalse(success.get());
        assertTrue(duplicate.get());
    }

    @Test
    public void getUserListReturnsRegisteredUsers() throws InterruptedException {
        registerAndWait("Ana", "ana@ulpgc.es", "123456");

        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<List<UserItem>> result = new AtomicReference<>();
        repository.getUserList(users -> {
            result.set(users);
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertEquals(1, result.get().size());
        assertEquals("ana@ulpgc.es", result.get().get(0).email);
    }

    @Test
    public void loadCatalogOnlyReadsJsonOnFirstRun() throws InterruptedException {
        // Primera carga: debe leer el JSON e insertar categorías/productos en Room
        loadCatalogAndWait();

        final CountDownLatch latch1 = new CountDownLatch(1);
        final AtomicInteger categoryCount = new AtomicInteger(-1);
        repository.getCategoryList(categories -> {
            categoryCount.set(categories.size());
            latch1.countDown();
        });
        assertTrue(latch1.await(5, TimeUnit.SECONDS));
        assertTrue("El catálogo debería tener al menos una categoría tras la primera carga",
                categoryCount.get() > 0);

        // Borramos manualmente las categorías de Room para distinguir si una segunda
        // llamada a loadCatalog() vuelve a leer el JSON (las repondría) o no (deben seguir a 0)
        deleteAllCategoriesDirectly();

        loadCatalogAndWait();

        final CountDownLatch latch2 = new CountDownLatch(1);
        final AtomicInteger categoryCountAfterSecondLoad = new AtomicInteger(-1);
        repository.getCategoryList(categories -> {
            categoryCountAfterSecondLoad.set(categories.size());
            latch2.countDown();
        });
        assertTrue(latch2.await(5, TimeUnit.SECONDS));
        assertEquals("Una segunda llamada a loadCatalog() no debe volver a leer el JSON",
                0, categoryCountAfterSecondLoad.get());
    }

    @Test
    public void favoritesAreScopedToTheirUser() throws InterruptedException {
        loadCatalogAndWait();
        int userId = registerAndWait("Ana", "ana@ulpgc.es", "123456");

        final CountDownLatch latchCategories = new CountDownLatch(1);
        final AtomicReference<Integer> productId = new AtomicReference<>();
        repository.getCategoryList(categories -> {
            repository.getProductList(categories.get(0), products -> {
                productId.set(products.get(0).id);
                latchCategories.countDown();
            });
        });
        assertTrue(latchCategories.await(5, TimeUnit.SECONDS));

        final CountDownLatch latchAdd = new CountDownLatch(1);
        repository.addFavorite(userId, productId.get(), latchAdd::countDown);
        assertTrue(latchAdd.await(5, TimeUnit.SECONDS));

        final CountDownLatch latchCheck = new CountDownLatch(1);
        final AtomicBoolean isFavorite = new AtomicBoolean(false);
        repository.isProductFavorite(userId, productId.get(), isFav -> {
            isFavorite.set(isFav);
            latchCheck.countDown();
        });
        assertTrue(latchCheck.await(5, TimeUnit.SECONDS));
        assertTrue(isFavorite.get());

        // Otro usuario no comparte ese favorito
        int otherUserId = registerAndWait("Luis", "luis@ulpgc.es", "654321");
        final CountDownLatch latchCheckOther = new CountDownLatch(1);
        final AtomicBoolean isFavoriteForOther = new AtomicBoolean(true);
        repository.isProductFavorite(otherUserId, productId.get(), isFav -> {
            isFavoriteForOther.set(isFav);
            latchCheckOther.countDown();
        });
        assertTrue(latchCheckOther.await(5, TimeUnit.SECONDS));
        assertFalse(isFavoriteForOther.get());
    }

    @Test
    public void sessionPersistsAcrossRepositoryInstances() {
        repository.saveSessionUserId(42);
        assertEquals(42, repository.getSessionUserId());

        // Simulamos un reinicio de la app: se crea una nueva instancia del repositorio
        CatalogRepository.resetInstance();
        RepositoryContract reloaded = CatalogRepository.getInstance(context);

        assertEquals(42, reloaded.getSessionUserId());

        reloaded.clearSessionUserId();
        assertEquals(-1, reloaded.getSessionUserId());
    }



    private int registerAndWait(String name, String email, String password) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        repository.registerUser(newUser(name, email, password), (ok, dup) -> latch.countDown());
        assertTrue(latch.await(5, TimeUnit.SECONDS));

        final CountDownLatch latchUser = new CountDownLatch(1);
        final AtomicInteger userId = new AtomicInteger(-1);
        repository.getUserList(users -> {
            for (UserItem user : users) {
                if (user.email.equals(email)) {
                    userId.set(user.id);
                    break;
                }
            }
            latchUser.countDown();
        });
        latchUser.await(5, TimeUnit.SECONDS);
        return userId.get();
    }

    private void loadCatalogAndWait() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        repository.loadCatalog(false, error -> latch.countDown());
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    private void deleteAllCategoriesDirectly() throws InterruptedException {
        // No existe un métdo público para esto en el Contract: lo hacemos a través de
        // deleteCategory() para cada categoría devuelta, usando el propio Repository.
        final CountDownLatch latch = new CountDownLatch(1);
        repository.getCategoryList(categories -> {
            if (categories.isEmpty()) {
                latch.countDown();
                return;
            }
            final CountDownLatch innerLatch = new CountDownLatch(categories.size());
            for (CategoryItem category : categories) {
                repository.deleteCategory(category, innerLatch::countDown);
            }
            try {
                innerLatch.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            }
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}

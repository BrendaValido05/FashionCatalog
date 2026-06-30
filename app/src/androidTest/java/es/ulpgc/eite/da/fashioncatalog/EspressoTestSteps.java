package es.ulpgc.eite.da.fashioncatalog;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.util.Log;

import androidx.test.core.app.ApplicationProvider;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import es.ulpgc.eite.da.fashioncatalog.app.CatalogMediator;
import es.ulpgc.eite.da.fashioncatalog.data.CatalogRepository;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;
import es.ulpgc.eite.da.fashioncatalog.data.UserItem;

//la sincronización con las tareas en background se hace mediante el
//CatalogRepository.IDLING_RESOURCE registrado en EspressoTests.
public class EspressoTestSteps {

    public static final String TEST_EMAIL = "test@ulpgc.es";
    public static final String TEST_PASSWORD = "123456";

    //Crea el usuario de prueba directamente a través del repositorio real,
    //usando la misma BD que usará la app durante el test. No se hace a través de la UI
    //para no acoplar la fase de "arrange" del test a la pantalla de registro.
    public void seedTestUser() throws InterruptedException {
        RepositoryContract repository = CatalogRepository.getInstance(
                ApplicationProvider.getApplicationContext());

        UserItem user = new UserItem();
        user.nombre = "Test";
        user.email = TEST_EMAIL;
        user.password = TEST_PASSWORD;

        final CountDownLatch latch = new CountDownLatch(1);
        repository.registerUser(user, (success, duplicateEmail) -> latch.countDown());
        latch.await(5, TimeUnit.SECONDS);
    }

    // catalog.db NO se recrea entre tests (solo se limpia la sesión de SharedPreferences),
    // así que un favorito marcado en un test (p.ej. test09) sigue ahí en los siguientes
    // (p.ej. test11) y un click sobre el mismo producto lo desmarcaría en vez de marcarlo.
    // Por eso dejamos sin favoritos al usuario de prueba antes de cada test.
    public void clearFavorites() throws InterruptedException {
        RepositoryContract repository = CatalogRepository.getInstance(
                ApplicationProvider.getApplicationContext());

        final CountDownLatch latch = new CountDownLatch(1);
        repository.getUserList(users -> {
            for (UserItem u : users) {
                if (TEST_EMAIL.equals(u.email)) {
                    repository.clearFavoritesForUser(u.id, latch::countDown);
                    return;
                }
            }
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    public void entrarComoGuest() {
        onView(withId(R.id.GuestButton)).perform(click());
    }

    public void entrarComoLoginValido() {
        onView(withId(R.id.emailPlainText)).perform(replaceText(TEST_EMAIL));
        onView(withId(R.id.passwordPlainText)).perform(replaceText(TEST_PASSWORD));
        onView(withId(R.id.logInButton)).perform(click());
    }

    public void introducirCredenciales(String email, String password) {
        onView(withId(R.id.emailPlainText)).perform(replaceText(email));
        onView(withId(R.id.passwordPlainText)).perform(replaceText(password));
    }

    public void mostramosPantallaDeLogin() {
        onView(withId(R.id.logInButton)).check(matches(isDisplayed()));
    }

    public void pulsamosBotonGuest() {
        onView(withId(R.id.GuestButton)).perform(click());
    }

    public void pulsamosBotonLogin() {
        onView(withId(R.id.logInButton)).perform(click());
    }

    public void pulsamosBotonRegistro() {
        onView(withId(R.id.signUpButton)).perform(click());
    }

    public void mostramosPantallaDeRegistro() {
        onView(withId(R.id.registerConfirmButton)).check(matches(isDisplayed()));
    }

    public void rellenamosFormularioDeRegistro(String nombre, String email, String password) {
        onView(withId(R.id.nameRegisterPlainText)).perform(replaceText(nombre));
        onView(withId(R.id.emailRegisterPlainText)).perform(replaceText(email));
        onView(withId(R.id.passwordRegisterPlainText)).perform(replaceText(password));
    }

    public void pulsamosBotonConfirmarRegistro() {
        onView(withId(R.id.registerConfirmButton)).perform(click());
    }

    public void mostramosListaDeCategorias() {
        onView(withId(R.id.category_list)).check(matches(isDisplayed()));
    }

    public void pulsamosBotonEnListaDeCategoriasEnPosicion(String pos) {
        int position = Integer.parseInt(pos);
        onView(withId(R.id.category_list))
                .perform(actionOnItemAtPosition(position, click()));
    }

    public void mostramosListaDeProductos() {
        onView(withId(R.id.product_list)).check(matches(isDisplayed()));
    }

    public void pulsamosBotonEnListaDeProductosEnPosicion(String pos) {
        int position = Integer.parseInt(pos);
        onView(withId(R.id.product_list))
                .perform(actionOnItemAtPosition(position, click()));
    }

    public void mostramosDetalleDeProducto() {
        onView(withId(R.id.product_detail)).check(matches(isDisplayed()));
    }

    public void pulsamosBotonFavorito() {
        onView(withId(R.id.favouriteButtonFB)).perform(click());
    }

    public void mostramosBotonFavorito() {
        onView(withId(R.id.favouriteButtonFB)).check(matches(isDisplayed()));
    }

    public void mostramosBotonFavoritos() {
        onView(withId(R.id.goTofavouriteButtonFB)).check(matches(isDisplayed()));
    }

    public void noMostramosBotonFavoritos() {
        onView(withId(R.id.goTofavouriteButtonFB)).check(matches(
                org.hamcrest.Matchers.not(isDisplayed())));
    }

    public void pulsamosBotonFavoritos() {
        onView(withId(R.id.goTofavouriteButtonFB)).perform(click());
    }

    public void mostramosListaDeFavoritos() {
        onView(withId(R.id.favorite_list)).check(matches(isDisplayed()));
    }

    // Distinto de pulsamosBotonEnListaDeProductosEnPosicion(): la pantalla de Favoritos
    // usa su propio RecyclerView (R.id.favorite_list), no R.id.product_list.
    public void pulsamosBotonEnListaDeFavoritosEnPosicion(String pos) {
        int position = Integer.parseInt(pos);
        onView(withId(R.id.favorite_list))
                .perform(actionOnItemAtPosition(position, click()));
    }

    public void pulsamosLogout() {
        // Abre el menú de opciones (por si el item aparece en el overflow) y pulsa logout.
        // IMPORTANTE: los items mostrados dentro del popup de overflow de AppCompat no llevan
        // el id del MenuItem (R.id.action_logout) en ninguna vista de su jerarquía -- solo lo
        // llevan los items que se muestran directamente en la Toolbar. Por eso hay que
        // localizarlo por su texto, no por withId().
        try {
            androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu(
                    ApplicationProvider.getApplicationContext());
        } catch (Exception ignored) {
        }
        onView(withText(R.string.action_logout)).perform(click());
    }

    public void clearSession() {
        try {
            RepositoryContract repo = CatalogRepository.getInstance(
                    ApplicationProvider.getApplicationContext());
            repo.clearSessionUserId();
        } catch (Exception e) {
            Log.e("EspressoTestSteps", "Error clearing session", e);
        }
    }

    // Actualiza resetearTest()
    public void resetearTest() {
        CatalogMediator.resetInstance();
        CatalogRepository.resetInstance();


        clearSession();   // Limpia la sesión persistida en SharedPreferences
    }
}
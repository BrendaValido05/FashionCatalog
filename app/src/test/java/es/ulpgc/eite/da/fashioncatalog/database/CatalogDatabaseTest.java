package es.ulpgc.eite.da.fashioncatalog.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.FavoriteItem;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;
import es.ulpgc.eite.da.fashioncatalog.data.UserItem;


@RunWith(RobolectricTestRunner.class)
public class CatalogDatabaseTest {

    private CatalogDatabase database;

    @Before
    public void createDb() {
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(), CatalogDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void insertAndLoadCategory() {
        CategoryItem category = new CategoryItem();
        category.id = 1;
        category.content = "Bolsos";
        category.details = "Bolsos de mano";

        database.categoryDao().insertCategory(category);

        CategoryItem loaded = database.categoryDao().loadCategory(1);
        assertNotNull(loaded);
        assertEquals("Bolsos", loaded.content);
    }

    @Test
    public void deleteAllCategoriesDoesNotAffectUsers() {
        CategoryItem category = new CategoryItem();
        category.id = 1;
        category.content = "Bolsos";
        database.categoryDao().insertCategory(category);

        UserItem user = new UserItem();
        user.nombre = "Test";
        user.email = "test@ulpgc.es";
        user.password = "123456";
        database.userDao().insertUser(user);

        database.categoryDao().deleteAllCategories();

        assertEquals(0, database.categoryDao().loadCategories().size());
        assertEquals(1, database.userDao().loadUsers().size());
    }

    @Test
    public void insertProductLinkedToCategory() {
        CategoryItem category = new CategoryItem();
        category.id = 1;
        category.content = "Bolsos";
        database.categoryDao().insertCategory(category);

        ProductItem product = new ProductItem();
        product.id = 10;
        product.content = "Bolso mini";
        product.categoryId = 1;
        database.productDao().insertProduct(product);

        List<ProductItem> products = database.productDao().loadProducts(1);
        assertEquals(1, products.size());
        assertEquals("Bolso mini", products.get(0).content);
    }

    @Test
    public void deleteAllProductsDoesNotAffectCategories() {
        CategoryItem category = new CategoryItem();
        category.id = 1;
        category.content = "Bolsos";
        database.categoryDao().insertCategory(category);

        ProductItem product = new ProductItem();
        product.id = 10;
        product.categoryId = 1;
        database.productDao().insertProduct(product);

        database.productDao().deleteAllProducts();

        assertEquals(0, database.productDao().loadProducts().size());
        assertEquals(1, database.categoryDao().loadCategories().size());
    }

    @Test
    public void insertAndFindUserByEmail() {
        UserItem user = new UserItem();
        user.nombre = "Ana";
        user.email = "ana@ulpgc.es";
        user.password = "secreto";
        database.userDao().insertUser(user);

        UserItem found = database.userDao().loadUserByEmail("ana@ulpgc.es");
        assertNotNull(found);
        assertEquals("Ana", found.nombre);

        assertNull(database.userDao().loadUserByEmail("noexiste@ulpgc.es"));
    }

    @Test
    public void addAndCheckFavoriteForUser() {
        UserItem user = new UserItem();
        user.nombre = "Ana";
        user.email = "ana@ulpgc.es";
        user.password = "secreto";
        database.userDao().insertUser(user);
        UserItem stored = database.userDao().loadUserByEmail("ana@ulpgc.es");

        CategoryItem category = new CategoryItem();
        category.id = 1;
        database.categoryDao().insertCategory(category);

        ProductItem product = new ProductItem();
        product.id = 10;
        product.categoryId = 1;
        database.productDao().insertProduct(product);

        FavoriteItem favorite = new FavoriteItem();
        favorite.userId = stored.id;
        favorite.productId = product.id;
        database.favoriteDao().insertFavoriteItem(favorite);

        assertTrue(database.favoriteDao().isFavorite(stored.id, product.id));
        List<ProductItem> favoriteProducts =
                database.favoriteDao().getFavoriteProductsByUserId(stored.id);
        assertEquals(1, favoriteProducts.size());

        FavoriteItem fetched =
                database.favoriteDao().getFavoriteByUserAndProduct(stored.id, product.id);
        database.favoriteDao().deleteFavoriteItem(fetched);
        assertTrue(database.favoriteDao().getFavoriteProductsByUserId(stored.id).isEmpty());
    }
}

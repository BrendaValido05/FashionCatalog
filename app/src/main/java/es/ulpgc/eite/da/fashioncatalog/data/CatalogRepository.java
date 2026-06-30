package es.ulpgc.eite.da.fashioncatalog.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import es.ulpgc.eite.da.fashioncatalog.database.CatalogDatabase;
import es.ulpgc.eite.da.fashioncatalog.database.CategoryDao;
import es.ulpgc.eite.da.fashioncatalog.database.ProductDao;
import es.ulpgc.eite.da.fashioncatalog.database.UserDao;
import es.ulpgc.eite.da.fashioncatalog.database.FavoriteDao;

public class CatalogRepository implements RepositoryContract {

    public static String TAG = CatalogRepository.class.getSimpleName();


    public static final String DB_FILE = "catalog.db";
    //Importamos el JSON
    public static final String JSON_FILE = "catalog.json";
    //Importamos la ruta del JSON
    public static final String JSON_ROOT = "categories";

    public static final String JSON_ROOT_FAVORITES = "favoritos";

    //Preferencias para saber si el catálogo ya se ha cargado alguna vez desde el JSON.
    //Esto permite cumplir el requisito de que el JSON solo se lee en la primera ejecución
    //de la app; en las siguientes ejecuciones los datos se leen exclusivamente de Room.
    private static final String PREFS_NAME = "catalog_prefs";
    private static final String KEY_CATALOG_LOADED = "catalog_loaded";

    //Clave usada para persistir el id del usuario logueado entre ejecuciones de la app
    private static final String KEY_SESSION_USER_ID = "session_user_id";
    private static final int NO_SESSION = -1;

    private static CatalogRepository INSTANCE;

    //Idling resource usado por los tests de Espresso para esperar a que terminen las
    //tareas en background lanzadas por el repositorio (AsyncTask) antes de comprobar la UI.
    //En producción permanece siempre "idle" y no tiene ningún efecto sobre el comportamiento de la app.
    public static final CountingIdlingResource IDLING_RESOURCE =
            new CountingIdlingResource("CatalogRepository");

    //Base de Datos del Catalogo
    private CatalogDatabase database;
    private Context context;


    public static RepositoryContract getInstance(Context context) {
        Log.d(TAG, "getInstance()");
        //En caso que sea null
        if(INSTANCE == null){
            //Creamos una nueva instancia
            INSTANCE = new CatalogRepository(context);
        }
        //Retornamos la instancia
        return INSTANCE;
    }
    //Permite a los tests forzar la creación de una nueva instancia (por ejemplo tras
    //cerrar la base de datos en un test de Robolectric) sin afectar a los datos persistidos.
    public static void resetInstance() {
        INSTANCE = null;
    }

    private CatalogRepository(Context context) {
        // Usamos el contexto de aplicación para evitar fugas de memoria:
        // este repositorio es un singleton que vive más que cualquier Activity,
        // así que nunca debe retener un Context de Activity.
        this.context = context.getApplicationContext();

        database = Room.databaseBuilder(this.context, CatalogDatabase.class, DB_FILE)
                .fallbackToDestructiveMigration()
                .build();

    }

    //Implementamos el metodo que carga el catalogo
    //IMPORTANTE: el parámetro "clearFirst" se mantiene en la firma por compatibilidad con el
    //Contract, pero la decisión real de si se debe (re)cargar el JSON ya NO depende de él ni
    //de si la tabla está vacía en ese instante: depende de si el catálogo ya se cargó alguna
    //vez en esta instalación (flag persistido en SharedPreferences). Así, el JSON se lee
    //únicamente la primera ejecución; en las siguientes, los datos se leen solo de Room.
    @Override
    public void loadCatalog(
            final boolean clearFirst, final FetchCatalogDataCallback callback) {

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                boolean error = false;

                if (!isCatalogAlreadyLoaded()) {
                    Log.d(TAG, "loadCatalog() -> primera ejecución, cargando catálogo desde JSON");
                    error = !loadCatalogFromJSON(loadJSONFromAsset(JSON_FILE));
                    if (!error) {
                        markCatalogAsLoaded();
                    }
                } else {
                    Log.d(TAG, "loadCatalog() -> catálogo ya cargado anteriormente, se usa Room");
                }
                //IMPORTANTE: los usuarios NO se siembran desde JSON. Los usuarios se crean
                //exclusivamente a través de la pantalla de Registro y se persisten en Room (UserDao).
                //En caso que no haya error al cargar el catalogo  de manera asíncrona
                if(callback != null) {
                    //Cargamos el catalogo con un método del callback, como variable el boolean

                    //error que indica si se cargo correctamente el catalogo
                    callback.onCatalogDataFetched(error);
                }
            }
        });

    }

    @Override
    public void getProductList(
            final CategoryItem category, final GetProductListCallback callback) {
        if (category == null) {
            Log.e(TAG, "getProductList: CategoryItem es null");
            if (callback != null) {
                callback.setProductList(null); // o maneja el error según tu callback
            }
            return;
        }
        getProductList(category.id, callback);
    }


    @Override
    public void getProductList(
            final int categoryId, final GetProductListCallback callback) {

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                if(callback != null) {
                    callback.setProductList(getProductDao().loadProducts(categoryId));
                }
            }
        });

    }

    @Override
    public void getUserList(final GetUserListCallback callback) {

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                if(callback != null) {
                    callback.setUserList(getUserDao().loadUsers());
                }
            }
        });

    }


    @Override
    public void getProduct(final int id, final GetProductCallback callback) {

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                if(callback != null) {
                    callback.setProduct(getProductDao().loadProduct(id));
                }
            }
        });
    }

    @Override
    public void getCategory(final int id, final GetCategoryCallback callback) {

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                if(callback != null) {
                    callback.setCategory(getCategoryDao().loadCategory(id));
                }
            }
        });

    }

    @Override
    public void getUser(final int id, final GetUserCallback callback) {

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                if(callback != null) {
                    callback.setUser(getUserDao().loadUser(id));
                }
            }
        });

    }

    @Override
    public void getCategoryList(final GetCategoryListCallback callback) {
        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                if(callback != null) {
                    callback.setCategoryList(getCategoryDao().loadCategories());
                }
            }
        });

    }


    @Override
    public void deleteProduct(
            final ProductItem product, final DeleteProductCallback callback) {

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                if(callback != null) {
                    getProductDao().deleteProduct(product);
                    callback.onProductDeleted();
                }
            }
        });
    }

    @Override
    public void getFavoriteByUserAndProduct(int userId, int productId, FavoriteItemCallback callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Obtiene el FavoriteItem correspondiente al userId y productId
                FavoriteItem favoriteItem = getFavoriteDao().getFavoriteByUserAndProduct(userId, productId);

                // Llama al callback con el FavoriteItem
                if (callback != null) {
                    callback.onCallback(favoriteItem);
                }
            }
        });
    }

    @Override
    public void isProductFavorite(int userId, int productId, IsProductFavoriteCallback callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                boolean isFavorite = getFavoriteDao().isFavorite(userId, productId);
                if (callback != null) {
                    callback.onIsProductFavoriteChecked(isFavorite);
                }
            }
        });
    }
    @Override
    public void getFavoriteProductsByUserId(final int userId, final FetchFavoriteProductsCallback callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<ProductItem> productsFavorites = getFavoriteDao().getFavoriteProductsByUserId(userId);
                if (callback != null) {
                    callback.onFavoriteProductsFetched(productsFavorites);
                }
            }
        });
    }

    @Override
    public void addFavorite(final int userId, final int productId, final AddFavoriteCallback callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FavoriteItem favoriteItem = new FavoriteItem();
                favoriteItem.userId = userId;
                favoriteItem.productId = productId;
                getFavoriteDao().insertFavoriteItem(favoriteItem);
                if (callback != null) {
                    callback.onFavoriteAdded();
                }
            }
        });
    }

    @Override
    public void removeFavorite(final int userId, final int productId, final RemoveFavoriteCallback callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FavoriteItem favoriteItem = getFavoriteDao().getFavoriteByUserAndProduct(userId, productId);
                if (favoriteItem != null) {
                    getFavoriteDao().deleteFavoriteItem(favoriteItem);
                }
                if (callback != null) {
                    callback.onFavoriteRemoved();
                }
            }
        });
    }

    @Override
    public void clearFavoritesForUser(final int userId, final ClearFavoritesCallback callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                getFavoriteDao().deleteFavoritesByUserId(userId);
                if (callback != null) {
                    callback.onFavoritesCleared();
                }
            }
        });
    }

    @Override
    public void updateProduct(
            final ProductItem product, final UpdateProductCallback callback) {

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                if(callback != null) {
                    getProductDao().updateProduct(product);
                    callback.onProductUpdated();
                }
            }
        });
    }


    @Override
    public void deleteCategory(
            final CategoryItem category, final DeleteCategoryCallback callback) {

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                if(callback != null) {
                    getCategoryDao().deleteCategory(category);
                    callback.onCategoryDeleted();
                }
            }
        });
    }

    @Override
    public void updateCategory(
            final CategoryItem category, final UpdateCategoryCallback callback) {

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                if(callback != null) {
                    getCategoryDao().updateCategory(category);
                    callback.onCategoryUpdated();
                }
            }
        });
    }


    private CategoryDao getCategoryDao() {
        return database.categoryDao();
    }

    //Comprueba si el catálogo ya se cargó alguna vez desde el JSON en esta instalación
    private boolean isCatalogAlreadyLoaded() {
        return getPrefs().getBoolean(KEY_CATALOG_LOADED, false);
    }

    //Marca el catálogo como cargado para que no se vuelva a leer el JSON en próximas ejecuciones
    private void markCatalogAsLoaded() {
        getPrefs().edit().putBoolean(KEY_CATALOG_LOADED, true).apply();
    }

    private SharedPreferences getPrefs() {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private UserDao getUserDao() {
        return database.userDao();
    }

    private ProductDao getProductDao() {
        return database.productDao();
    }
    private FavoriteDao getFavoriteDao() {
        return database.favoriteDao();
    }




    @Override
    public void registerUser(final UserItem user, final RegisterUserCallback callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                UserItem existing = getUserDao().loadUserByEmail(user.email);
                if (existing != null) {
                    //Ya existe un usuario con ese email: no se inserta
                    if (callback != null) {
                        callback.onUserRegistered(false, true);
                    }
                    return;
                }
                //Insertamos el usuario nuevo (id autogenerado por Room)
                getUserDao().insertUser(user);
                if (callback != null) {
                    callback.onUserRegistered(true, false);
                }
            }
        });
    }

    //Persistencia de sesión
    //Se guarda en las mismas SharedPreferences que el flag de carga del catálogo.
    //No usamos la BD para esto porque es un dato  de "estado de la app",
    //no un dato de catálogo, y así sobrevive a un fallbackToDestructiveMigration().
    @Override
    public void saveSessionUserId(int userId) {
        getPrefs().edit().putInt(KEY_SESSION_USER_ID, userId).apply();
    }

    @Override
    public int getSessionUserId() {
        return getPrefs().getInt(KEY_SESSION_USER_ID, NO_SESSION);
    }

    @Override
    public void clearSessionUserId() {
        getPrefs().edit().remove(KEY_SESSION_USER_ID).apply();
    }

    private boolean loadCatalogFromJSON(String json) {
        Log.d(TAG, "loadCatalogFromJSON()");

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        try {

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(JSON_ROOT);

            if (jsonArray.length() > 0) {

                final List<CategoryItem> categories = Arrays.asList(
                        gson.fromJson(jsonArray.toString(), CategoryItem[].class)
                );

                for (CategoryItem category: categories) {
                    getCategoryDao().insertCategory(category);
                }

                for (CategoryItem category: categories) {
                    for (ProductItem product: category.items) {
                        product.categoryId = category.id;
                        getProductDao().insertProduct(product);
                    }
                }



                return true;
            }

        } catch (JSONException error) {
            Log.e(TAG, "error: " + error);
        }

        return false;
    }

    private String loadJSONFromAsset(String fileName) {

        String json = null;

        try {

            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException error) {
            Log.e(TAG, "error: " + error);
        }

        return json;
    }

}
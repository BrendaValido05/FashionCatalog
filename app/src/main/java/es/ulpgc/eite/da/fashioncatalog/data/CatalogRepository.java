package es.ulpgc.eite.da.fashioncatalog.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

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

    private static CatalogRepository INSTANCE;

    //Base de Datos del Catalogo
    private CatalogDatabase database;
    private Context context;


    public static RepositoryContract getInstance(Context context) {
        Log.e(TAG, "getInstance()");
        //En caso que sea null
        if(INSTANCE == null){
            //Creamos una nueva instancia
            INSTANCE = new CatalogRepository(context);
        }
        //Retornamos la instancia
        return INSTANCE;
    }

    private CatalogRepository(Context context) {
        this.context = context;

        database = Room.databaseBuilder(context, CatalogDatabase.class, DB_FILE)
                .fallbackToDestructiveMigration()
                .build();

    }

    //Implementamos el metodo que carga el catalogo
    @Override
    public void loadCatalog(
            final boolean clearFirst, final FetchCatalogDataCallback callback) {

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                //En caso que sea necesario limpiar la base de datos
                if(clearFirst) {
                    //Limpiamos la base de datos
                    database.clearAllTables();
                }
                //Declaramos una variable de tipo boolean para indicar si hay error
                //al cargar el catalogo a través del JSON
                boolean error = false;
                //Utilizamos como condicional si la lista de categorias esta vacia
                if(getCategoryDao().loadCategories().size() == 0 ) {
                    //Cambiamos el valor del boolean error dependiendo si se cargo correctamente
                    //el catalogo desde el JSON siendo el contrario de cargar el JSON
                    error = !loadCatalogFromJSON(loadJSONFromAsset(JSON_FILE));
                }
                //IMPORTANTE: los usuarios NO se siembran desde JSON. Los usuarios se crean
                //exclusivamente a través de la pantalla de Registro y se persisten en Room (UserDao).
                //En caso que no haya error al cargar el catalogo  de manera asíncrona
                if(callback != null) {
                    //? ¿Cómo funciona exactamente este método dependiendo del error?
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

    private boolean loadCatalogFromJSON(String json) {
        Log.e(TAG, "loadCatalogFromJSON()");

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
        //Log.e(TAG, "loadJSONFromAsset()");

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
package es.ulpgc.eite.da.fashioncatalog.data;

import java.util.List;

public interface RepositoryContract {

    interface FetchCatalogDataCallback {
        void onCatalogDataFetched(boolean error);
    }

    interface GetProductListCallback {
        void setProductList(List<ProductItem> products);
    }
    interface GetUserListCallback {
        void setUserList(List<UserItem> users);
    }

    interface GetProductCallback {
        void setProduct(ProductItem product);
    }

    interface GetCategoryListCallback {
        void setCategoryList(List<CategoryItem> categories);
    }

    interface GetCategoryCallback {
        void setCategory(CategoryItem category);
    }

    interface DeleteCategoryCallback {
        void onCategoryDeleted();
    }

    interface UpdateCategoryCallback {
        void onCategoryUpdated();
    }

    interface DeleteProductCallback {
        void onProductDeleted();
    }

    interface UpdateProductCallback {
        void onProductUpdated();
    }

    interface FetchFavoriteProductsCallback {
        void onFavoriteProductsFetched(List<ProductItem> products);
    }
    interface IsProductFavoriteCallback {
        void onIsProductFavoriteChecked(boolean isFavorite);
    }
    public interface FavoriteItemCallback {
        void onCallback(FavoriteItem favoriteItem);
    }
    interface AddFavoriteCallback {
        void onFavoriteAdded();
    }
    interface RemoveFavoriteCallback {
        void onFavoriteRemoved();
    }
    interface ClearFavoritesCallback {
        void onFavoritesCleared();
    }

    void getFavoriteProductsByUserId(int userId, FetchFavoriteProductsCallback callback);

    void isProductFavorite(int userId, int productId, IsProductFavoriteCallback callback);

    void getFavoriteByUserAndProduct(int userId, int productId, FavoriteItemCallback callback);

    //Añade el producto a favoritos del usuario indicado
    void addFavorite(int userId, int productId, AddFavoriteCallback callback);

    //Elimina el producto de favoritos del usuario indicado
    void removeFavorite(int userId, int productId, RemoveFavoriteCallback callback);

    //Elimina todos los favoritos del usuario indicado (usado por los tests para partir
    //de un estado conocido, ya que la base de datos persiste entre ejecuciones de test)
    void clearFavoritesForUser(int userId, ClearFavoritesCallback callback);



    void loadCatalog(
            boolean clearFirst, CatalogRepository.FetchCatalogDataCallback callback);

    void getProductList(
            CategoryItem category, CatalogRepository.GetProductListCallback callback);

    void getProductList(
            int categoryId, CatalogRepository.GetProductListCallback callback);
    void getUserList(CatalogRepository.GetUserListCallback callback);

    void getProduct(int id, CatalogRepository.GetProductCallback callback);
    void getCategory(int id, CatalogRepository.GetCategoryCallback callback);
    void getCategoryList(CatalogRepository.GetCategoryListCallback callback);
    void getUser(int id, GetUserCallback callback);

    void deleteProduct(
            ProductItem product, CatalogRepository.DeleteProductCallback callback);

    void updateProduct(
            ProductItem product, CatalogRepository.UpdateProductCallback callback);

    void deleteCategory(
            CategoryItem category, CatalogRepository.DeleteCategoryCallback callback);

    void updateCategory(
            CategoryItem category, CatalogRepository.UpdateCategoryCallback callback);
    interface FetchUserListCallback {
        void onUserListFetched(boolean error);
    }
    interface GetUserCallback {
        void setUser(UserItem user);

    }

    interface RegisterUserCallback {
        //success=true si se ha creado el usuario; duplicateEmail=true si el email ya existía
        void onUserRegistered(boolean success, boolean duplicateEmail);
    }

    void registerUser(UserItem user, RegisterUserCallback callback);


    //Guarda el id del usuario logueado para poder restaurar la sesión al reabrir la app
    void saveSessionUserId(int userId);

    //Devuelve el id del usuario con sesión guardada, o -1 si no hay sesión activa
    int getSessionUserId();

    //Elimina la sesión guardada (logout)
    void clearSessionUserId();

}
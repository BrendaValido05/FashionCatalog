package es.ulpgc.eite.da.fashioncatalog.product;

import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;

public class ProductDetailModel implements ProductDetailContract.Model {

    public static String TAG = "AdvMasterDetail.ProductDetailModel";

    private RepositoryContract repository;

    public ProductDetailModel(RepositoryContract repository) {
        this.repository = repository;
    }

    @Override
    public void isProductFavorite(int userId, int productId, RepositoryContract.IsProductFavoriteCallback callback) {
        repository.isProductFavorite(userId, productId, callback);
    }

    @Override
    public void addFavorite(int userId, int productId, RepositoryContract.AddFavoriteCallback callback) {
        repository.addFavorite(userId, productId, callback);
    }

    @Override
    public void removeFavorite(int userId, int productId, RepositoryContract.RemoveFavoriteCallback callback) {
        repository.removeFavorite(userId, productId, callback);
    }

}
package es.ulpgc.eite.da.fashioncatalog.favorites;

import android.util.Log;

import java.util.List;

import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;

public class FavoriteListModel implements FavoriteListContract.Model {

  public static String TAG = FavoriteListModel.class.getSimpleName();

  private RepositoryContract repository;

  public FavoriteListModel(RepositoryContract repository) {
    this.repository = repository;
  }

  @Override
  public void fetchFavoriteListData(CategoryItem category, RepositoryContract.FetchFavoriteProductsCallback callback) {
    // Implementación de la lógica para obtener los productos favoritos desde el repositorio.
    repository.getFavoriteProductsByUserId(category.id, callback);
  }

  @Override
  public void getFavoriteProductsByUserId(int userId, RepositoryContract.FetchFavoriteProductsCallback callback) {
    repository.getFavoriteProductsByUserId(userId, new RepositoryContract.FetchFavoriteProductsCallback() {
      @Override
      public void onFavoriteProductsFetched(List<ProductItem> products) {
        if (products != null) {
          Log.d(TAG, "Productos favoritos obtenidos correctamente");
          callback.onFavoriteProductsFetched(products);
        } else {
          Log.d(TAG, "No se encontraron productos favoritos");
          callback.onFavoriteProductsFetched(null);
        }
      }
    });
  }
}

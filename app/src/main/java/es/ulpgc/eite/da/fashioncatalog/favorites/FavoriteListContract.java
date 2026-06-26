package es.ulpgc.eite.da.fashioncatalog.favorites;

import java.lang.ref.WeakReference;
import java.util.List;

import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;
import es.ulpgc.eite.da.fashioncatalog.products.ProductListViewModel;

//Contrato de ProductList
interface FavoriteListContract {
  //Métodos de la vista
  interface View {
    void injectPresenter(Presenter presenter);

    void displayProductListData(FavoriteListViewModel viewModel);

    void navigateToProductDetailScreen();

    void showFavoriteButton();

    void displayFavoriteListData(FavoriteListState state);

    void displayProductListData(ProductListViewModel viewModel);
  }
  //Métodos del presenter
  interface Presenter {
    void injectView(WeakReference<View> view);
    void injectModel(Model model);
    void selectProductListData(ProductItem item);

      void onResume();

    void onRestart();

    void fetchFavoriteListData();
    void setProductList(List<ProductItem> products);
  }
  //Métodos del model
  interface Model {
    void fetchFavoriteListData(CategoryItem category, RepositoryContract.FetchFavoriteProductsCallback callback);
    void getFavoriteProductsByUserId(int userId, RepositoryContract.FetchFavoriteProductsCallback callback);
  }

}
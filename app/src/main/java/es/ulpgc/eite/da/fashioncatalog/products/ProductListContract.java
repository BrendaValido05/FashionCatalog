package es.ulpgc.eite.da.fashioncatalog.products;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;

//Contrato de ProductList
interface ProductListContract {
    //Métodos de la vista
    interface View {
        void hideFavoriteButton();

        void injectPresenter(Presenter presenter);

        void displayProductListData(ProductListViewModel viewModel);

        void navigateToProductDetailScreen();

        void showFavoriteButton();
    }
    //Métodos del presenter
    interface Presenter {


        void injectView(WeakReference<View> view);
        void injectModel(Model model);
        void fetchProductListData();
        void selectProductListData(ProductItem item);

    }
    //Métodos del model
    interface Model {
        void fetchProductListData(
                CategoryItem category, RepositoryContract.GetProductListCallback callback);
    }

}
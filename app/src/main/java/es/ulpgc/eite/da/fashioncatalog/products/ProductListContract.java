package es.ulpgc.eite.da.fashioncatalog.products;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;


interface ProductListContract {

    interface View {
        void hideFavoriteButton();

        void injectPresenter(Presenter presenter);

        void displayProductListData(ProductListViewModel viewModel);

        void navigateToProductDetailScreen();

        void navigateToFavoriteListScreen();

        void showFavoriteButton();
    }

    interface Presenter {


        void injectView(WeakReference<View> view);
        void injectModel(Model model);
        void fetchProductListData();
        void selectProductListData(ProductItem item);

    }

    interface Model {
        void fetchProductListData(
                CategoryItem category, RepositoryContract.GetProductListCallback callback);
    }

}
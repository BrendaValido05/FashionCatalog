package es.ulpgc.eite.da.fashioncatalog.products;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import es.ulpgc.eite.da.fashioncatalog.app.CatalogMediator;
import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;
import es.ulpgc.eite.da.fashioncatalog.data.UserItem;



public class ProductListPresenter implements ProductListContract.Presenter {

    public static String TAG = ProductListPresenter.class.getSimpleName();

    private WeakReference<ProductListContract.View> view;
    private ProductListState state;
    private ProductListContract.Model model;
    private CatalogMediator mediator;
    private UserItem user;

    public ProductListPresenter(CatalogMediator mediator) {
        this.mediator = mediator;
        state = mediator.getProductListState();
        user = mediator.getUser();
    }

    @Override
    public void injectView(WeakReference<ProductListContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(ProductListContract.Model model) {
        this.model = model;
    }

    public void showFavoriteButton() {
        Log.e(TAG, "showFavoriteButton()");
        if (user!=null) {
            view.get().showFavoriteButton();
        } else {
            view.get().hideFavoriteButton();
        }
    }


    @Override
    public void fetchProductListData() {
        Log.e(TAG, "fetchProductListData()");

        CategoryItem category = getDataFromCategoryListScreen();

        if (category != null) {

            state.category = category;
        }
        showFavoriteButton();

        model.fetchProductListData(state.category, new RepositoryContract.GetProductListCallback() {

            @Override
            public void setProductList(List<ProductItem> products) {
                state.products = products;

                view.get().displayProductListData(state);
            }
        });

    }

    private void passDataToProductDetailScreen(ProductItem item) {
        mediator.setProduct(item);
    }

    private CategoryItem getDataFromCategoryListScreen() {
        CategoryItem category = mediator.getCategory();
        return category;
    }


    public void sendUserToProductDetail(UserItem user) {
        if (user!=null) {
            mediator.setUser(user);
            Log.e(TAG, "Se ha enviado el usuario " + user.email);
        } else {
            Log.e(TAG, "Se ha entrado como invitado");
        }

    }


    @Override
    public void selectProductListData(ProductItem item) {
        passDataToProductDetailScreen(item);
        sendUserToProductDetail(user);
        view.get().navigateToProductDetailScreen();
    }


}

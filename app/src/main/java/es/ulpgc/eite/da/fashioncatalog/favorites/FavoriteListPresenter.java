package es.ulpgc.eite.da.fashioncatalog.favorites;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import es.ulpgc.eite.da.fashioncatalog.app.CatalogMediator;
import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;
import es.ulpgc.eite.da.fashioncatalog.data.UserItem;


public class FavoriteListPresenter implements FavoriteListContract.Presenter {

    public static String TAG = FavoriteListPresenter.class.getSimpleName();

    private WeakReference<FavoriteListContract.View> view;
    private FavoriteListState state;
    private FavoriteListContract.Model model;
    private CatalogMediator mediator;
    private UserItem user;


    public FavoriteListPresenter(CatalogMediator mediator) {
        this.mediator = mediator;
        state = mediator.getFavoriteListState();
        user = mediator.getUser();
    }

    @Override
    public void injectView(WeakReference<FavoriteListContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(FavoriteListContract.Model model) {
        this.model = model;
    }

    @Override
    public void selectProductListData(ProductItem item) {
        passDataToProductDetailScreen(item);
        sendUserToProductDetail(user);
        view.get().navigateToProductDetailScreen();
    }
    @Override
    public void onResume() {
        fetchFavoriteListData();
    }

    @Override
    public void onRestart() {
        state = mediator.getFavoriteListState();
        view.get().displayFavoriteListData(state);
    }

    @Override
    public void fetchFavoriteListData() {
        Log.e(TAG, "fetchFavoriteListData()");
        CategoryItem category = getDataFromCategoryListScreen();

        if (category != null) {
            state.category = category;
        }

        if (user == null) {
            Log.e(TAG, "fetchFavoriteListData() -> usuario nulo, no se cargan favoritos");
            return;
        }

        model.getFavoriteProductsByUserId(user.id, new RepositoryContract.FetchFavoriteProductsCallback() {
            @Override
            public void onFavoriteProductsFetched(List<ProductItem> products) {
                // Callback en background (AsyncTask): comprobamos que la vista siga viva
                // antes de actualizar la UI, igual que en ProductDetailPresenter.
                state.products = products;
                FavoriteListContract.View safeView = view.get();
                if (safeView == null) {
                    Log.e(TAG, "onFavoriteProductsFetched() -> la vista ya no existe");
                    return;
                }
                if (state.products != null) {
                    safeView.displayFavoriteListData(state);
                } else {
                    Log.e(TAG, "state.products es null");
                }
            }
        });
    }


    @Override
    public void setProductList(List<ProductItem> products) {
        state.products = products;
        view.get().displayProductListData(state);
    }

    private void passDataToProductDetailScreen(ProductItem item) {
        mediator.setProduct(item);
    }

    private CategoryItem getDataFromCategoryListScreen() {
        CategoryItem category = mediator.getCategory();
        return category;
    }


    public void sendUserToProductDetail(UserItem user) {
        if (user != null) {
            mediator.setUser(user);
            Log.e(TAG, "Se ha enviado el usuario " + user.email);
        } else {
            Log.e(TAG, "Se ha entrado como invitado");
        }
    }
}
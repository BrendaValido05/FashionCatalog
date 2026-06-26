package es.ulpgc.eite.da.fashioncatalog.favorites;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import es.ulpgc.eite.da.fashioncatalog.app.CatalogMediator;
import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;
import es.ulpgc.eite.da.fashioncatalog.data.UserItem;

// Presenter del FavoriteList
public class FavoriteListPresenter implements FavoriteListContract.Presenter {

    public static String TAG = FavoriteListPresenter.class.getSimpleName();

    // Declaramos la vista
    private WeakReference<FavoriteListContract.View> view;
    // Declaramos el estado
    private FavoriteListState state;
    // Declaramos el model
    private FavoriteListContract.Model model;
    // Declaramos el mediator
    private CatalogMediator mediator;
    private UserItem user;

    // Constructor del FavoriteListPresenter
    public FavoriteListPresenter(CatalogMediator mediator) {
        this.mediator = mediator;
        // Recogemos el state del mediator
        state = mediator.getFavoriteListState();
        // Recogemos el usuario de la pantalla login, indicando que se ha logeado correctamente
        user = mediator.getUser();
    }

    @Override
    // Se inyecta la vista
    public void injectView(WeakReference<FavoriteListContract.View> view) {
        this.view = view;
    }

    @Override
    // Se inyecta el model
    public void injectModel(FavoriteListContract.Model model) {
        this.model = model;
    }

    // Método para que al pulsar una producto ir a la pantalla de ProductDetail
    // y enviar también el usuario
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

        // En caso de que la categoria sea nula
        if (category != null) {
            // Establecemos la categoria en el estado
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

    // Creamos el método para enviar el usuario al ProductDetail
    public void sendUserToProductDetail(UserItem user) {
        if (user != null) {
            mediator.setUser(user);
            Log.e(TAG, "Se ha enviado el usuario " + user.email);
        } else {
            Log.e(TAG, "Se ha entrado como invitado");
        }
    }
}
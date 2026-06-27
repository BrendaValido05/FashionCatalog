package es.ulpgc.eite.da.fashioncatalog.product;

import android.util.Log;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.app.CatalogMediator;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;
import es.ulpgc.eite.da.fashioncatalog.data.UserItem;


public class ProductDetailPresenter implements ProductDetailContract.Presenter {

    public static String TAG = "AdvMasterDetail.ProductDetailPresenter";

    private WeakReference<ProductDetailContract.View> view;
    private ProductDetailState state;
    private ProductDetailContract.Model model;
    private CatalogMediator mediator;
    private UserItem user;
    private boolean isFavorite;

    public ProductDetailPresenter(CatalogMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void onCreateCalled() {
        // Log.e(TAG, "onCreateCalled");

        state = new ProductDetailState();
        //Releemos el usuario del mediator en cada creación: puede haber cambiado
        //(login/logout) desde que se construyó este Presenter
        this.user = mediator.getUser();
    }

    @Override
    public void onRecreateCalled() {
        // Log.e(TAG, "onRecreateCalled");

        state = mediator.getProductDetailState();
        this.user = mediator.getUser();
    }

    @Override
    public void onPauseCalled() {
        Log.e(TAG, "onPauseCalled()");

        mediator.setProductDetailState(state);
    }


    private ProductItem getDataFromProductListScreen() {

        // set passed state
        ProductItem product = mediator.getProduct();
        return product;
    }


    @Override
    public void fetchProductDetailData() {
        // Log.e(TAG, "fetchProductDetailData()");

        // set passed state
        ProductItem product = getDataFromProductListScreen();
        if(product != null) {
            state.product = product;
        }

        view.get().displayProductDetailData(state);

        updateFavoriteButtonVisibility();
    }

    //Muestra/oculta el botón de favoritos según si hay usuario logeado (invitado = oculto)
    //y, si lo hay, comprueba si el producto actual ya es favorito para pintar el icono correcto
    private void updateFavoriteButtonVisibility() {
        ProductDetailContract.View safeView = view.get();
        if (safeView == null) {
            return;
        }

        if (user == null || state.product == null) {
            safeView.hideFavoriteButton();
            return;
        }

        safeView.showFavoriteButton();

        model.isProductFavorite(user.id, state.product.id, new RepositoryContract.IsProductFavoriteCallback() {
            @Override
            public void onIsProductFavoriteChecked(boolean favorite) {
                isFavorite = favorite;
                ProductDetailContract.View safeView = view.get();
                if (safeView != null) {
                    safeView.updateFavoriteIcon(isFavorite);
                }
            }
        });
    }

    @Override
    public void onFavoriteButtonClicked() {
        ProductDetailContract.View safeView = view.get();

        if (user == null) {
            //Defensa adicional: un invitado no puede marcar/desmarcar favoritos
            if (safeView != null) {
                safeView.showGuestFavoriteError();
            }
            return;
        }

        if (state.product == null) {
            return;
        }

        if (isFavorite) {
            model.removeFavorite(user.id, state.product.id, new RepositoryContract.RemoveFavoriteCallback() {
                @Override
                public void onFavoriteRemoved() {
                    isFavorite = false;
                    ProductDetailContract.View safeView = view.get();
                    if (safeView != null) {
                        safeView.updateFavoriteIcon(false);
                    }
                }
            });
        } else {
            model.addFavorite(user.id, state.product.id, new RepositoryContract.AddFavoriteCallback() {
                @Override
                public void onFavoriteAdded() {
                    isFavorite = true;
                    ProductDetailContract.View safeView = view.get();
                    if (safeView != null) {
                        safeView.updateFavoriteIcon(true);
                    }
                }
            });
        }
    }


    @Override
    public void injectView(WeakReference<ProductDetailContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(ProductDetailContract.Model model) {
        this.model = model;
    }

}
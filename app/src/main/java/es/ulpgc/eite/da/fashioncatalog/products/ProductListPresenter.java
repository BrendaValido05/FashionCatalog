package es.ulpgc.eite.da.fashioncatalog.products;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import es.ulpgc.eite.da.fashioncatalog.app.CatalogMediator;
import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.ProductItem;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;
import es.ulpgc.eite.da.fashioncatalog.data.UserItem;


//Presenter del ProductList
public class ProductListPresenter implements ProductListContract.Presenter {

    public static String TAG = ProductListPresenter.class.getSimpleName();

    //Declaramos la vista
    private WeakReference<ProductListContract.View> view;
    //Declaramos el estado
    private ProductListState state;
    //Declaramos el model
    private ProductListContract.Model model;
    //Declaramos el mediator
    private CatalogMediator mediator;
    private UserItem user;

    //Constructor del ProductListPresenter
    public ProductListPresenter(CatalogMediator mediator) {
        this.mediator = mediator;
        //Recogemos el state del mediator
        state = mediator.getProductListState();
        //Recogemos el usuario de la pantalla login, indicando que se ha logeado correctamente
        user = mediator.getUser();
    }

    @Override
    //Se inyecta la vista
    public void injectView(WeakReference<ProductListContract.View> view) {
        this.view = view;
    }

    @Override
    //Se inyecta el model
    public void injectModel(ProductListContract.Model model) {
        this.model = model;
    }

    //Creamos el método para ocultar/mostrar el boton de favoritos
    //dependiendo de si hemos recibido el usuario o no
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
        //Recogemos la categoria seleccionada en la pantalla anterior
        CategoryItem category = getDataFromCategoryListScreen();
        //En caso de que la categoria sea nula
        if (category != null) {
            //Establecemos la categoria en el estado
            state.category = category;
        }
        showFavoriteButton();
        // call the model
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

    //Creamos el método para enviar el usuario al ProductDetail
    public void sendUserToProductDetail(UserItem user) {
        if (user!=null) {
            mediator.setUser(user);
            Log.e(TAG, "Se ha enviado el usuario " + user.email);
        } else {
            Log.e(TAG, "Se ha entrado como invitado");
        }

    }


    //Método para que al pulsar una producto ir a la pantalla de ProductDetail
    //y enviar también el usuario
    @Override
    public void selectProductListData(ProductItem item) {
        passDataToProductDetailScreen(item);
        sendUserToProductDetail(user);
        view.get().navigateToProductDetailScreen();
    }


}

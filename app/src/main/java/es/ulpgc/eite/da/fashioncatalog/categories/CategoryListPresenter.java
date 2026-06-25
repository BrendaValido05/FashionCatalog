package es.ulpgc.eite.da.fashioncatalog.categories;

import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import es.ulpgc.eite.da.fashioncatalog.app.CatalogMediator;
import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;
import es.ulpgc.eite.da.fashioncatalog.data.UserItem;


public class CategoryListPresenter implements CategoryListContract.Presenter {

    public static String TAG = CategoryListPresenter.class.getSimpleName();

    //Variable que almacena la vista
    private WeakReference<CategoryListContract.View> view;
    //Variable que almacena el estado
    private CategoryListState state;
    //Variable que almacena el modelo
    private CategoryListContract.Model model;
    //Variable que almacena el mediator
    private CatalogMediator mediator;
    private UserItem user;


    //constructor de CategoryListPresenter
    public CategoryListPresenter(CatalogMediator mediator) {
        this.mediator = mediator;
        //Decimos que el state provenga del mediator
        state = mediator.getCategoryListState();
        //Recogemos el usuario de la pantalla login, indicando que se ha logeado correctamente
        user = mediator.getUser();
    }
    //Método que llama al modelo
    @Override
    public void fetchCategoryListData() {
        Log.e(TAG, "fetchCategoryListData()");
        // Recogemos los datos del modelo
        model.fetchCategoryListData(new RepositoryContract.GetCategoryListCallback() {
            // Método para colocar la lista de categorías en el state y colocarlo en la pantalla
            @Override
            public void setCategoryList(List<CategoryItem> categories) {
                Log.e(TAG, "setCategoryList()");
                // Asignamos las categorías al state
                state.categories = categories;
                // Mostramos la lista de categorías del state en la pantalla
                view.get().displayCategoryListData(state);
            }
        });
        // Ocultamos el botón de favoritos si el usuario es nulo
        showFavoriteButton();
    }

    public void showFavoriteButton() {
        Log.e(TAG, "showFavoriteButton()");
        if (user != null) {
            view.get().showFavoriteButton();
        } else {
            view.get().hideFavoriteButton();
        }
    }



    @Override
    public void selectCategoryListData(CategoryItem item) {
        Log.e(TAG, "selectCategoryListData()");
        //Método para pasar los datos a la siguiente pantalla
        passDataToProductListScreen(item);
        //Método para navegar a la siguiente pantalla
        view.get().navigateToProductListScreen();
    }
    //Método para pasar los datos a la siguiente pantalla
    private void passDataToProductListScreen(CategoryItem item) {
        //Pasamos los datos a la siguiente pantalla con el mediator
        mediator.setCategory(item);
    }

    //Método para inyectar la vista
    @Override
    public void injectView(WeakReference<CategoryListContract.View> view) {
        this.view = view;
    }
    //Método para inyectar el modelo
    @Override
    public void injectModel(CategoryListContract.Model model) {
        this.model = model;
    }


}
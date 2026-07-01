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

    private WeakReference<CategoryListContract.View> view;
    private CategoryListState state;
    private CategoryListContract.Model model;
    private CatalogMediator mediator;
    private UserItem user;

    public CategoryListPresenter(CatalogMediator mediator) {
        this.mediator = mediator;

        state = mediator.getCategoryListState();
        //Recogemos el usuario de la pantalla login, indicando que se ha logeado correctamente
        //user = mediator.getUser();
    }


    @Override
    public void fetchCategoryListData() {
        Log.d(TAG, "fetchCategoryListData()");

        model.fetchCategoryListData(new RepositoryContract.GetCategoryListCallback() {
            @Override
            public void setCategoryList(List<CategoryItem> categories) {
                Log.d(TAG, "setCategoryList()");
                state.categories = categories;
                view.get().displayCategoryListData(state);
            }
        });

        showFavoriteButton();
    }

    public void showFavoriteButton() {
        UserItem currentUser = mediator.getUser();
        Log.d(TAG, "showFavoriteButton() - User from mediator: " +
                (currentUser != null ? currentUser.email : "null"));

        if (currentUser != null) {
            view.get().showFavoriteButton();
        } else {
            view.get().hideFavoriteButton();
        }
    }



    @Override
    public void selectCategoryListData(CategoryItem item) {
        Log.d(TAG, "selectCategoryListData()");

        passDataToProductListScreen(item);
        view.get().navigateToProductListScreen();
    }

    //Cierra la sesión del usuario actual: la limpia del Mediator (memoria) y de
    //SharedPreferences (persistencia), y vuelve a Login
    @Override
    public void logout() {
        Log.d(TAG, "logout()");
        mediator.setUser(null);
        user = null;
        model.clearSession();
        view.get().navigateToLoginScreen();
    }

    private void passDataToProductListScreen(CategoryItem item) {
        mediator.setCategory(item);
    }

    @Override
    public void injectView(WeakReference<CategoryListContract.View> view) {
        this.view = view;
    }

    @Override
    public void injectModel(CategoryListContract.Model model) {
        this.model = model;
    }


}
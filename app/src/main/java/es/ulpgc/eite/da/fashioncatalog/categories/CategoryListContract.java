package es.ulpgc.eite.da.fashioncatalog.categories;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;

interface CategoryListContract {

    interface View {
        void injectPresenter(Presenter presenter);
        void displayCategoryListData(CategoryListViewModel viewModel);
        void navigateToProductListScreen();
        void navigateToFavoriteListScreen();
        void showFavoriteButton();
        void hideFavoriteButton();

        //Navega de vuelta a la pantalla de Login (usada tras hacer Logout)
        void navigateToLoginScreen();
    }


    interface Presenter {
        void injectView(WeakReference<View> view);
        void injectModel(Model model);
        void fetchCategoryListData();

        //seleccionar una categoría
        void selectCategoryListData(CategoryItem item);

        //Cierra la sesión del usuario actual y vuelve a Login
        void logout();
        void showFavoriteButton();
    }


    interface Model {
        void fetchCategoryListData(
                RepositoryContract.GetCategoryListCallback callback);

        //Elimina la sesión persistida (usado en logout)
        void clearSession();
    }



}
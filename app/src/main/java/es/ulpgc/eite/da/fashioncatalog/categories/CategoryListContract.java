package es.ulpgc.eite.da.fashioncatalog.categories;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.data.CategoryItem;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;

//Contrato de CategoryList con los métodos de la vista, el modelo y el presenter
interface CategoryListContract {

    interface View {
        //Método para inyectar el presenter
        void injectPresenter(Presenter presenter);
        //Métodon para mostrar la lista de categorías
        void displayCategoryListData(CategoryListViewModel viewModel);



        //Método para navegar a la pantalla de productos
        void navigateToProductListScreen();
        void navigateToFavoriteListScreen();

        void showFavoriteButton();

        void hideFavoriteButton();

        //Navega de vuelta a la pantalla de Login (usada tras hacer Logout)
        void navigateToLoginScreen();

    }

    interface Presenter {
        //Método para inyectar la vista
        void injectView(WeakReference<View> view);
        //Método para inyectar el modelo
        void injectModel(Model model);
        //Método para obtener la lista de categorías
        void fetchCategoryListData();
        //Método para seleccionar una categoría
        void selectCategoryListData(CategoryItem item);

        //Cierra la sesión del usuario actual y vuelve a Login
        void logout();
        void showFavoriteButton();
    }
    interface Model {
        //Método para recoger la lista de categorías
        void fetchCategoryListData(
                RepositoryContract.GetCategoryListCallback callback);

        //Elimina la sesión persistida (usado en logout)
        void clearSession();
    }



}
package es.ulpgc.eite.da.fashioncatalog.login;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;

//Contrato de CategoryList con los métodos de la vista, el modelo y el presenter
interface LoginListContract {

    interface View {


        void navigateToCategoryListScreen();

        void navigateToRegisterScreen();

        //Método para inyectar el presenter
        void injectPresenter(Presenter presenter);

        void onDataUpdate(LoginListState state);

        void showLoginError();
        void showEmptyText();

        void showEmailErrorText();
        //Métodon para mostrar la lista de categorías
        //void displayCategoryListData(LoginListViewModel viewModel);
        //Método para navegar a la pantalla de productos
        //void navigateToProductListScreen();
    }

    interface Presenter {
        //Método para inyectar la vista
        void injectView(WeakReference<View> view);
        //Método para inyectar el modelo
        void injectModel(Model model);


        void checkLogin(String email, String password);

        //Comprueba si existe una sesión guardada (login previo) y, si la hay, recupera
        //el usuario y navega directamente a Categorías sin pedir credenciales otra vez
        void restoreSession();

        //void injectRouter(Router router);

    }
    interface Model {
        //Método para recoger la lista de categorías
        void fetchCategoryListData(RepositoryContract.GetCategoryListCallback callback);

        void fetchUserListData(RepositoryContract.GetUserListCallback callback);

        //Id de usuario con sesión persistida, o -1 si no hay sesión guardada
        int getSessionUserId();

        //Recupera el UserItem completo a partir del id de sesión persistido
        void fetchUserById(int userId, RepositoryContract.GetUserCallback callback);

        //Persiste el id del usuario logueado para restaurar sesión en próximos arranques
        void saveSessionUserId(int userId);

        //Elimina la sesión guardada (logout o sesión inválida)
        void clearSession();
    }


}
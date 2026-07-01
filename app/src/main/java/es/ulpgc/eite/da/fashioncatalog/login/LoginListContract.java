package es.ulpgc.eite.da.fashioncatalog.login;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;


interface LoginListContract {

    interface View {


        void navigateToCategoryListScreen();

        void navigateToRegisterScreen();

        void injectPresenter(Presenter presenter);

        void onDataUpdate(LoginListState state);

        void showLoginError();
        void showEmptyText();

        void showEmailErrorText();

        //void displayCategoryListData(LoginListViewModel viewModel);

        //void navigateToProductListScreen();
    }

    interface Presenter {
        void injectView(WeakReference<View> view);
        void injectModel(Model model);


        void checkLogin(String email, String password);

        //Comprueba si existe una sesión guardada (login previo) y, si la hay, recupera
        //el usuario y navega directamente a Categorías sin pedir credenciales otra vez
        void restoreSession();


    }
    interface Model {

        void fetchCategoryListData(RepositoryContract.GetCategoryListCallback callback);

        void fetchUserListData(RepositoryContract.GetUserListCallback callback);

        //Id de usuario con sesión persistida, o -1 si no hay sesión guardada
        int getSessionUserId();

        void fetchUserById(int userId, RepositoryContract.GetUserCallback callback);

        void saveSessionUserId(int userId);

        void clearSession();
    }


}
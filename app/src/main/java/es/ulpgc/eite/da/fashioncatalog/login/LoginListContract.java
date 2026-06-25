package es.ulpgc.eite.da.fashioncatalog.login;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;

//Contrato de CategoryList con los métodos de la vista, el modelo y el presenter
interface LoginListContract {

  interface View {


    void navigateToCategoryListScreen();

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

    //void injectRouter(Router router);

  }
  interface Model {
    //Método para recoger la lista de categorías
    void fetchCategoryListData(RepositoryContract.GetCategoryListCallback callback);

      void fetchUserListData(RepositoryContract.GetUserListCallback callback);
  }


}
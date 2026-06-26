package es.ulpgc.eite.da.fashioncatalog.register;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;
import es.ulpgc.eite.da.fashioncatalog.data.UserItem;

//Contrato de Register con los métodos de la vista, el modelo y el presenter
interface RegisterContract {

  interface View {
    //Método para inyectar el presenter
    void injectPresenter(Presenter presenter);

    void onDataUpdate(RegisterState state);

    //Navega de vuelta a Login tras un registro correcto
    void navigateToLoginScreen();

    void showEmptyFieldsError();

    void showInvalidEmailError();

    void showDuplicateEmailError();

    void showRegisterSuccess();

    void showRegisterError();
  }

  interface Presenter {
    //Método para inyectar la vista
    void injectView(WeakReference<View> view);

    //Método para inyectar el modelo
    void injectModel(Model model);

    void registerUser(String name, String email, String password);
  }

  interface Model {
    void registerUser(UserItem user, RepositoryContract.RegisterUserCallback callback);
  }

}
package es.ulpgc.eite.da.fashioncatalog.register;

import android.util.Log;
import android.util.Patterns;

import java.lang.ref.WeakReference;

import es.ulpgc.eite.da.fashioncatalog.app.CatalogMediator;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;
import es.ulpgc.eite.da.fashioncatalog.data.UserItem;

public class RegisterPresenter implements RegisterContract.Presenter {

  public static String TAG = RegisterPresenter.class.getSimpleName();

  //Variable que almacena la vista
  private WeakReference<RegisterContract.View> view;
  //Variable que almacena el modelo
  private RegisterContract.Model model;
  //Variable que almacena el state (proviene del mediator, igual que el resto de pantallas)
  private RegisterState state;
  //Variable que almacena el mediator
  private CatalogMediator mediator;

  //Constructor de RegisterPresenter
  public RegisterPresenter(CatalogMediator mediator) {
    this.mediator = mediator;
    state = mediator.getRegisterState();
  }

  @Override
  public void injectView(WeakReference<RegisterContract.View> view) {
    this.view = view;
  }

  @Override
  public void injectModel(RegisterContract.Model model) {
    this.model = model;
  }

  @Override
  public void registerUser(final String name, final String email, final String password) {
    Log.e(TAG, "registerUser() name=" + name + " email=" + email);

    if (name == null || name.trim().isEmpty()
        || email == null || email.trim().isEmpty()
        || password == null || password.trim().isEmpty()) {
      view.get().showEmptyFieldsError();
      return;
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      view.get().showInvalidEmailError();
      return;
    }

    UserItem user = new UserItem();
    user.nombre = name;
    user.email = email;
    user.password = password;

    model.registerUser(user, new RepositoryContract.RegisterUserCallback() {
      @Override
      public void onUserRegistered(boolean success, boolean duplicateEmail) {
        if (success) {
          state.name = name;
          state.email = email;
          view.get().showRegisterSuccess();
          view.get().navigateToLoginScreen();
        } else if (duplicateEmail) {
          view.get().showDuplicateEmailError();
        } else {
          view.get().showRegisterError();
        }
      }
    });
  }
}

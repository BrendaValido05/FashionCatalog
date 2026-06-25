package es.ulpgc.eite.da.fashioncatalog.login;

import java.lang.ref.WeakReference;
import java.util.List;

import es.ulpgc.eite.da.fashioncatalog.app.CatalogMediator;
import es.ulpgc.eite.da.fashioncatalog.data.RepositoryContract;
import es.ulpgc.eite.da.fashioncatalog.data.UserItem;


public class LoginListPresenter implements LoginListContract.Presenter {

  public static String TAG = LoginListPresenter.class.getSimpleName();

  //Variable que almacena la vista
  private WeakReference<LoginListContract.View> view;
  //Variable que almacena el estado
  private LoginListState state;
  //Variable que almacena el modelo
  private LoginListContract.Model model;
  //Variable que almacena el mediator
  private CatalogMediator mediator;

  private UserItem user;


  //constructor de CategoryListPresenter
  public LoginListPresenter(CatalogMediator mediator) {
    this.mediator = mediator;
    //Decimos que el state provenga del mediator
    state = mediator.getLoginListState();

  }
  //Método para inyectar la vista
  @Override
  public void injectView(WeakReference<LoginListContract.View> view) {
    this.view = view;
  }
  //Método para inyectar el modelo
  @Override
  public void injectModel(LoginListContract.Model model) {
    this.model = model;
  }



  public void checkLogin(String email, String password) {
    model.fetchUserListData(new RepositoryContract.GetUserListCallback() {
      @Override
      public void setUserList(List<UserItem> users) {
        if (email.isEmpty() || password.isEmpty()) {
          view.get().showEmptyText();
        }
        boolean found = false;
        for (UserItem user : users) {
          if (user.email.equals(email) && user.password.equals(password)) {
            found = true;
            mediator.setUser(user);
            view.get().navigateToCategoryListScreen();
          }
        }

        if (found == false) {
          view.get().showLoginError();
        }

      }
    });


  }



}

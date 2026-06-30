package es.ulpgc.eite.da.fashioncatalog.login;

import java.lang.ref.WeakReference;
import java.util.List;

import android.util.Log;
import android.util.Patterns;

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
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            view.get().showEmptyText();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.get().showEmailErrorText();
            return;
        }

        model.fetchUserListData(new RepositoryContract.GetUserListCallback() {
            @Override
            public void setUserList(List<UserItem> users) {
                boolean found = false;
                for (UserItem userItem : users) {
                    if (userItem.email.equals(email) && userItem.password.equals(password)) {
                        found = true;
                        mediator.setUser(userItem);
                        model.saveSessionUserId(userItem.id);

                        Log.d(TAG, "Login exitoso - Usuario guardado en mediator: " + userItem.email);

                        // Pequeño delay para asegurar que el mediator se actualice antes de navegar
                        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                            view.get().navigateToCategoryListScreen();
                        }, 150);

                        break;
                    }
                }

                if (!found) {
                    view.get().showLoginError();
                }
            }
        });
    }

    @Override
    public void restoreSession() {
        int sessionUserId = model.getSessionUserId();
        if (sessionUserId < 0) {
            //No hay sesión guardada: el usuario debe loguearse manualmente
            return;
        }

        model.fetchUserById(sessionUserId, new RepositoryContract.GetUserCallback() {
            @Override
            public void setUser(UserItem user) {
                if (user != null) {
                    mediator.setUser(user);
                    LoginListContract.View safeView = view.get();
                    if (safeView != null) {
                        safeView.navigateToCategoryListScreen();
                    }
                } else {
                    //El usuario de la sesión guardada ya no existe en BD: limpiamos la sesión inválida
                    model.clearSession();
                }
            }
        });
    }

}
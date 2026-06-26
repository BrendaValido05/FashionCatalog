package es.ulpgc.eite.da.fashioncatalog.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.ulpgc.eite.da.fashioncatalog.R;
import es.ulpgc.eite.da.fashioncatalog.categories.CategoryListActivity;
import es.ulpgc.eite.da.fashioncatalog.register.RegisterActivity;



//Activity de CategoryList
public class LoginListActivity extends AppCompatActivity implements LoginListContract.View {

  public static String TAG = LoginListActivity.class.getSimpleName();

  //Presenter de CategoryList
  LoginListContract.Presenter presenter;

  //Adapter de CategoryList
  private LoginListAdapter listAdapter;

  //Declaramos los botones y los EditText para trabajar con ellos
  private Button loginButton;
  private Button registerButton;

  private Button guestButton;
  private EditText emailEditText;
  private EditText passwordEditText;


  //onCreate de CategoryList
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    //Recogemos el estado recibido por parámetro
    super.onCreate(savedInstanceState);
    //Actualizamos el layout
    setContentView(R.layout.activity_login);
    //setTitle(R.string.title_login_screen);


    //Asociamos los botones y los EditText con los del layout
    loginButton = findViewById(R.id.logInButton);
    guestButton = findViewById(R.id.GuestButton);
    registerButton = findViewById(R.id.signUpButton);
    emailEditText = findViewById(R.id.emailPlainText);
    passwordEditText = findViewById(R.id.passwordPlainText);


    //Añadimos la función de entrar como invitado
    guestButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
       navigateToCategoryListScreen();
      }
    });

    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.e(TAG, "Email: " + emailEditText.getText().toString()+ " Contraseña: " + passwordEditText.getText().toString());
        presenter.checkLogin(emailEditText.getText().toString(), passwordEditText.getText().toString());
      }
    });

    registerButton.setOnClickListener(v -> navigateToRegisterScreen());




    // do the setup
    LoginListScreen.configure(this);


  }


  @Override
  public void navigateToCategoryListScreen() {
    Intent intent = new Intent(this, CategoryListActivity.class);
    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  @Override
  public void navigateToRegisterScreen() {
      Intent intent = new Intent(this, RegisterActivity.class);
      startActivity(intent);
    }

  @Override
  public void injectPresenter(LoginListContract.Presenter presenter) {
    this.presenter = presenter;
  }

  @Override
  public void onDataUpdate(LoginListState state) {

  }

  // Reemplaza este método en LoginListActivity.java
  @Override
  public void showLoginError() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(LoginListActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
      }
    });
  }



  public void showEmptyText() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(LoginListActivity.this, "Debe rellenar todos los campos", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public void showEmailErrorText() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(LoginListActivity.this, "El correo no tiene el formato correcto", Toast.LENGTH_SHORT).show();
      }
    });
  }


}

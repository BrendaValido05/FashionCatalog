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


public class LoginListActivity extends AppCompatActivity implements LoginListContract.View {

    public static String TAG = LoginListActivity.class.getSimpleName();

    LoginListContract.Presenter presenter;

    private LoginListAdapter listAdapter;
    private Button loginButton;
    private Button registerButton;
    private Button guestButton;
    private EditText emailEditText;
    private EditText passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginButton = findViewById(R.id.logInButton);
        guestButton = findViewById(R.id.GuestButton);
        registerButton = findViewById(R.id.signUpButton);
        emailEditText = findViewById(R.id.emailPlainText);
        passwordEditText = findViewById(R.id.passwordPlainText);



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

        //Si ya existe una sesión persistida de una ejecución anterior, navegamos
        //directamente a Categorías sin pedir login otra vez
        presenter.restoreSession();

    }


    @Override
    public void navigateToCategoryListScreen() {
        Intent intent = new Intent(this, CategoryListActivity.class);
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
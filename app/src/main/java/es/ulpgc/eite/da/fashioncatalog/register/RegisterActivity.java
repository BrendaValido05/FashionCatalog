package es.ulpgc.eite.da.fashioncatalog.register;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.ulpgc.eite.da.fashioncatalog.R;

//Activity de Register
public class RegisterActivity extends AppCompatActivity implements RegisterContract.View {

    public static String TAG = RegisterActivity.class.getSimpleName();

    //Presenter de Register
    RegisterContract.Presenter presenter;

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.nameRegisterPlainText);
        emailEditText = findViewById(R.id.emailRegisterPlainText);
        passwordEditText = findViewById(R.id.passwordRegisterPlainText);
        registerButton = findViewById(R.id.registerConfirmButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.registerUser(
                        nameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        // do the setup
        RegisterScreen.configure(this);
    }

    @Override
    public void injectPresenter(RegisterContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDataUpdate(RegisterState state) {

    }

    @Override
    public void navigateToLoginScreen() {
        finish();
    }

    @Override
    public void showEmptyFieldsError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, "Debe rellenar todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showInvalidEmailError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, "El correo no tiene el formato correcto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showDuplicateEmailError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, "Ya existe un usuario con ese correo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showRegisterSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showRegisterError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, "No se ha podido registrar el usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

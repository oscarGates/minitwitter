package curso.android.minitwitter.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import curso.android.minitwitter.R;
import curso.android.minitwitter.common.Constantes;
import curso.android.minitwitter.common.SharedPreferencesManager;
import curso.android.minitwitter.retrofit.MiniTwitterClient;
import curso.android.minitwitter.retrofit.MiniTwitterService;
import curso.android.minitwitter.retrofit.requests.RequestSignup;
import curso.android.minitwitter.retrofit.response.ResponseAuth;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSignUp;
    TextView textViewGoLogin;
    EditText etUsername, etPassword, etEmail;
    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sing_up);
        retrofitInit();
        findViews();
        events();


    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    private void findViews() {
        btnSignUp = findViewById(R.id.buttonSignUp);
        textViewGoLogin = findViewById(R.id.textViewGoLogin);
        etUsername = findViewById(R.id.editTextUsername);
        etPassword = findViewById(R.id.editTextPassword);
        etEmail = findViewById(R.id.editTextEmail);
    }

    private void events() {
        btnSignUp.setOnClickListener(this);
        textViewGoLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.buttonSignUp:
                gotoSignUp();
                break;
            case R.id.textViewGoLogin:
                goToLogin();
                break;
        }
    }

    private void gotoSignUp() {
        String email = etEmail.getText().toString();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if(email.isEmpty()){
            etEmail.setError("El email es requerido");
        }else if(username.isEmpty()){
            etUsername.setError("El nombre de usuario es requerido");
        }else if(password.isEmpty() || password.length() < 4){
            etPassword.setError("El password es requerido");
        }else{
            String code = "UDEMYANDROID";
            final RequestSignup requestSignup = new RequestSignup(username, email, password,code);
            Call<ResponseAuth> call = miniTwitterService.doSignUp(requestSignup);
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        SharedPreferencesManager.setStringValue(Constantes.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setStringValue(Constantes.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setStringValue(Constantes.PREF_PHOTO_URL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setStringValue(Constantes.PREF_USER, response.body().getUsername());
                        SharedPreferencesManager.setStringValue(Constantes.PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setBooleanValue(Constantes.PREF_CREATED, response.body().getActive());
                        Intent i = new Intent(SingUpActivity.this, DashboardActivity.class);
                        startActivity(i);
                        finish();
                    }else {
                        Toast.makeText(SingUpActivity.this, "Algo anda mal, revisa los datos", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(SingUpActivity.this, "Error de conexión intenta mas tarde", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void goToLogin() {
        Intent i = new Intent(SingUpActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}

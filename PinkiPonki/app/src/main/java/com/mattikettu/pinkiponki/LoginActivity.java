package com.mattikettu.pinkiponki;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mattikettu.pinkiponki.networkapi.NetworkLogic;
import com.mattikettu.pinkiponki.util.Injector;
import com.mattikettu.pinkiponki.util.ToastCreator;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.BindView;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGINACTIVITY";
    private ProgressDialog progressDialog;
    private long startTime;
    private static Context ctx;

    @BindView(R.id.input_username)
    EditText _usernameText;

    @BindView(R.id.input_password)
    EditText _passwordText;

    @BindView(R.id.login_button)
    Button login_button;

    @BindView(R.id.register_button)
    TextView register_button;

    @Inject
    protected ToastCreator toastCreator;

    @Inject
    protected NetworkLogic NWL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        Injector.inject(this);

        ctx = this;

        login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                login();
            }
        });

        register_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplication(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    private void login(){
        Log.d(TAG, "Attempting login");

        if(!validate()){
            onLoginFailure();
            return;
        }

        login_button.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String username = _usernameText.getText().toString().toLowerCase(); //all usernames are lowercase only
        String password = _passwordText.getText().toString();

        //here we handle the NWL section.

        startTime = System.currentTimeMillis();
        NWL.login(username, password, this); //This runs async to UI anyway.
    }


    private void onLoginSuccess(){
        //toastCreator.showToastLong(ctx, "Login success.");
        //login_button.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);

    }

    private void onLoginFailure(){
        //toastCreator.showToastLong("Login failed.");
        login_button.setEnabled(true);
    }

    private boolean validate(){
        boolean valid = true;

        String username = _usernameText.getText().toString().toLowerCase(); //all usernames are lowercase only
        String password = _passwordText.getText().toString();

        if(username.isEmpty()){
            valid = false;
        }
        if(password.isEmpty() || password.length()<8 || password.length()>32 ){
            _passwordText.setError("between 8 and 32 alphanumeric characters");
            valid = false;
        }

        return valid;
    }

    public void loginOK() {
        progressDialog.dismiss();
        onLoginSuccess();
        Log.d(TAG, "Total time: " + (System.currentTimeMillis()-startTime));
    }

    public void loginFail() {
        progressDialog.dismiss();
        onLoginFailure();
        Log.d(TAG, "Total time: " + (System.currentTimeMillis()-startTime));
    }
}

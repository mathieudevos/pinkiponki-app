package com.mattikettu.pinkiponki;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mattikettu.pinkiponki.networkapi.NetworkLogic;
import com.mattikettu.pinkiponki.objects.Username;
import com.mattikettu.pinkiponki.util.Injector;
import com.mattikettu.pinkiponki.util.SharedPreferenceManager;
import com.mattikettu.pinkiponki.util.ToastCreator;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.BindView;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGINACTIVITY";
    private ProgressDialog progressDialog;
    private long startTime;
    private Handler handler;

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

    @Inject
    protected SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        Injector.inject(this);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 200:
                        //Got the good response
                        loginOK((Username) msg.obj);
                        break;
                    case 401:
                        loginFail();
                        break;
                    default:
                        loginFail();
                }
            }
        };

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
            loginFail();
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
        NWL.login(username, password, handler); //This runs async to UI anyway.
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

    public void loginOK(Username username) {
        progressDialog.dismiss();
        Log.d(TAG, "Total time: " + (System.currentTimeMillis()-startTime));
        toastCreator.showToastLong("Login success.");
        login_button.setEnabled(true);
        sharedPreferenceManager.setCurrentUsername(username);
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
    }

    public void loginFail() {
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
            Log.d(TAG, "Total time: " + (System.currentTimeMillis()-startTime));
        }
        toastCreator.showToastLong("Login failed.");
        login_button.setEnabled(true);
    }
}

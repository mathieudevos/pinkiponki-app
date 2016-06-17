package com.mattikettu.pinkiponki;

import android.app.ProgressDialog;
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
import com.mattikettu.pinkiponki.util.Constants;
import com.mattikettu.pinkiponki.util.Injector;
import com.mattikettu.pinkiponki.util.SharedPreferenceManager;
import com.mattikettu.pinkiponki.util.ToastCreator;

import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.BindView;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "REGISTERACTIVITY";
    private ProgressDialog progressDialog;
    private long startTime;
    private Handler handler;

    @BindView(R.id.input_username)
    EditText _usernameText;

    @BindView(R.id.input_email)
    EditText _emailText;

    @BindView(R.id.input_password)
    EditText _passwordText;

    @BindView(R.id.register_button)
    Button register_button;

    @BindView(R.id.login_button)
    TextView login_button;

    @Inject
    protected NetworkLogic NWL;

    @Inject
    protected ToastCreator toastCreator;

    @Inject
    protected SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        Injector.inject(this);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 200:
                        //Got the good response
                        registerOK((Username) msg.obj);
                        break;
                    case 401:
                        registerFail();
                        break;
                    default:
                        registerFail();
                }
            }
        };

        register_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                register();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplication(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void register(){
        Log.d(TAG, "Register called");

        if (!validate()) {
            registerFail();
            return;
        }

        progressDialog = new ProgressDialog(RegisterActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        String username = _usernameText.getText().toString().toLowerCase(); //all usernames are lowercase only
        String password = _passwordText.getText().toString();
        String email = _emailText.getText().toString();

        //here we handle the NWL section.

        startTime = System.currentTimeMillis();
        NWL.register(username, email, password, handler); //This runs async to UI anyway.
    }

    private boolean validate(){
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        Pattern p = Pattern.compile(Constants.username_pattern); //alphanumeric
        Pattern p_pw = Pattern.compile(Constants.password_pattern);

        if (username.isEmpty() || username.length() < 4 || username.length()>20 || !p.matcher(username).find()) {
            _usernameText.setError("between 4 and 20 alphanumeric character");
            valid = false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        }
        if (password.isEmpty() || password.length() < 8 || password.length() > 32 || !p_pw.matcher(password).find()) {
            _passwordText.setError("between 8 and 32 alphanumeric characters including: @-_^!?+-/*.,:;<>");
            valid = false;
        }

        return valid;
    }

    public void registerOK(Username username){
        progressDialog.dismiss();
        Log.d(TAG, "Total time: " + (System.currentTimeMillis()-startTime));
        toastCreator.showToastLong("Welcome user: " + username.getUsername());
        sharedPreferenceManager.setCurrentUsername(username);
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
    }

    public void registerFail(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
            Log.d(TAG, "Total time: " + (System.currentTimeMillis()-startTime));
        }
        toastCreator.showToastLong("Register failed");
    }
}

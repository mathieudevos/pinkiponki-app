package com.mattikettu.pinkiponki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mattikettu.pinkiponki.util.Injector;

//import java.util.regex.Pattern;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "REGISTERACTIVITY";

    @BindView(R.id.input_username_register)
    EditText _usernameText;

    @BindView(R.id.input_email_register)
    EditText _emailText;

    @BindView(R.id.input_password_register)
    EditText _passwordText;

    @BindView(R.id.login_button_register)
    Button login_button;

    @BindView(R.id.register_button_register)
    TextView register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.setDebug(true);
        ButterKnife.bind(this);
        Injector.inject(this);

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
            onSignupFailed();
            return;
        }


    }

    private boolean validate(){
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        Pattern p = Pattern.compile("[a-zA-Z0-9]"); //alphanumeric
        Pattern p_pw = Pattern.compile("[a-zA-Z0-9@-_^!?+-/*.,:;<>]");

        if (username.isEmpty() || username.length() < 4 || username.length()>20 || p.matcher(username).find()) {
            _usernameText.setError("between 4 and 20 alphanumeric character");
            valid = false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        }
        if (password.isEmpty() || password.length() < 8 || password.length() > 32 || p_pw.matcher(password).find()) {
            _passwordText.setError("between 8 and 32 alphanumeric characters including: @-_^!?+-/*.,:;<>");
            valid = false;
        }

        return valid;
    }

    private void onSignupSuccess(){

    }

    private void onSignupFailed(){

    }
}

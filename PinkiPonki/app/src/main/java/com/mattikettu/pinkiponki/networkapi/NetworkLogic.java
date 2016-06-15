package com.mattikettu.pinkiponki.networkapi;

import android.os.StrictMode;
import android.util.Log;

import com.mattikettu.pinkiponki.LoginActivity;
import com.mattikettu.pinkiponki.objects.LoginUser;
import com.mattikettu.pinkiponki.objects.Username;
import com.mattikettu.pinkiponki.util.Injector;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mathieu on 10/06/2016.
 */
public class NetworkLogic {

    private static final String TAG = "NETWORKLOGIC";
    private static final String contenttype = "application/json";

    @Inject
    protected APIService apiService;
    @Inject
    protected CurrentUser currentUser;

    public NetworkLogic(){
        Log.d(TAG, TAG + "created, use as Singleton");
        Injector.inject(this);

        // Fuck up all networking <3
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void login(String username, String password, final LoginActivity loginActivity){
        String hash_pw = sha256(password);
        LoginUser loginUser = new LoginUser(username, password);

        Call<Username> call = apiService.login(loginUser);
        Log.d(TAG, call.toString());
        call.enqueue(new Callback<Username>() {
           @Override
           public void onResponse(Call<Username> call, Response<Username> response) {
               Log.d(TAG, "Response: " + response.body().getUsername());
               loginActivity.loginOK();
           }

           @Override
           public void onFailure(Call<Username> call, Throwable t) {
               Log.d(TAG, "Failed: " + t.getMessage());
               loginActivity.loginFail();
           }
       });
    }

    private String sha256(String input){
        String output = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(input.getBytes());
            byte msgDigest[] = digest.digest();

            output = new String(msgDigest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "sha256: " + output);
        return output;
    }

}

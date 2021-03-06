package com.mattikettu.pinkiponki.networkapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;

import com.mattikettu.pinkiponki.LoginActivity;
import com.mattikettu.pinkiponki.RegisterActivity;
import com.mattikettu.pinkiponki.objects.GameObject;
import com.mattikettu.pinkiponki.objects.UserObject;
import com.mattikettu.pinkiponki.objects.Username;
import com.mattikettu.pinkiponki.util.Injector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

/**
 * Created by mathieu on 10/06/2016.
 */
public class NetworkLogic {

    private static final String TAG = "NETWORKLOGIC";
    private static final String contenttype = "application/json";

    @Inject
    protected APIService apiService;
    @Inject
    protected APIFileService apiFileService;
    @Inject
    protected CurrentUser currentUser;
    @Inject
    protected Context appContext;

    public NetworkLogic(){
        Log.d(TAG, TAG + "created, use as Singleton");
        Injector.inject(this);

        // Fuck up all networking <3
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void login(String username, String password, final Handler handler){
        String hash_pw = sha256(password);
        UserObject userObject = new UserObject();
        userObject.setUsername(username);
        userObject.setPassword(hash_pw);

        Call<Username> call = apiService.login(userObject);
        call.enqueue(new Callback<Username>() {
           @Override
           public void onResponse(Call<Username> call, Response<Username> response) {
               Log.d(TAG, "Responsecode: " + response.code());
               Message msg;
               if(response.code()==200){
                   msg = handler.obtainMessage(response.code(), response.body());
               }else{
                   msg = handler.obtainMessage(response.code());
               }
               msg.sendToTarget();
           }

           @Override
           public void onFailure(Call<Username> call, Throwable t) {
               Log.d(TAG, "Failed: " + t.getMessage());
               Message msg = handler.obtainMessage(0); //0 for errors
               msg.sendToTarget();
           }
       });
    }

    public void register(String username, String email,String password, final Handler handler){
        String hash_pw = sha256(password);
        UserObject userObject = new UserObject();
        userObject.setUsername(username);
        userObject.setEmail(email);
        userObject.setPassword(hash_pw);

        Call<Username> call = apiService.register(userObject);
        call.enqueue(new Callback<Username>() {
            @Override
            public void onResponse(Call<Username> call, Response<Username> response) {
                Log.d(TAG, "Responsecode: " + response.code());
                Message msg;
                if(response.code()==200){
                    msg = handler.obtainMessage(response.code(), response.body());
                } else {
                    msg = handler.obtainMessage(response.code());
                }
                msg.sendToTarget();
            }

            @Override
            public void onFailure(Call<Username> call, Throwable t) {
                Log.d(TAG, "Failed: " + t.getMessage());
                Message msg = handler.obtainMessage(0); //0 for errors
                msg.sendToTarget();
            }
        });
    }

    public void getUser(String username, final Handler handler){
        Call<UserObject> call = apiService.getUser(username);
        call.enqueue(new Callback<UserObject>() {
            @Override
            public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                Log.d(TAG, "Responsecode: " + response.code());
                Message msg;
                if(response.code()==200){
                    msg = handler.obtainMessage(response.code(), 1, 0, response.body());
                } else {
                    msg = handler.obtainMessage(response.code());
                }
                msg.sendToTarget();
            }

            @Override
            public void onFailure(Call<UserObject> call, Throwable t) {
                Log.d(TAG, "Failed: " + t.getMessage());
                Message msg = handler.obtainMessage(0); //0 for errors
                msg.sendToTarget();
            }
        });
    }

    public void updateProfile(UserObject userObject, final Handler handler){
        Call<Username> call = apiService.updateProfile(userObject);
        call.enqueue(new Callback<Username>() {
            @Override
            public void onResponse(Call<Username> call, Response<Username> response) {
                Log.d(TAG, "Responsecode: " + response.code());
                Message msg;
                if(response.code()==200){
                    msg = handler.obtainMessage(response.code(), 0, 0, response.body());
                } else {
                    msg = handler.obtainMessage(response.code());
                }
                msg.sendToTarget();
            }

            @Override
            public void onFailure(Call<Username> call, Throwable t) {
                Log.d(TAG, "Failed: " + t.getMessage());
                Message msg = handler.obtainMessage(0); //0 for errors
                msg.sendToTarget();
            }
        });
    }

    public void updateProfilePicture(String filename, Bitmap bm, final Handler handler){
        File file = new File(appContext.getCacheDir(), filename);
        try {
            file.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bmdata = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bmdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), "Profile picture: " + filename);

        RequestBody rbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imageBody = MultipartBody.Part.createFormData("image", file.getName(), rbody);

        Call<com.mattikettu.pinkiponki.objects.Message> call = apiFileService.updateProfilePicture(description, imageBody);
        call.enqueue(new Callback<com.mattikettu.pinkiponki.objects.Message>() {
            @Override
            public void onResponse(Call<com.mattikettu.pinkiponki.objects.Message> call, Response<com.mattikettu.pinkiponki.objects.Message> response) {
                Log.d(TAG, "Responsecode: " + response.code());
                Message msg;
                if(response.code()==200){
                    msg = handler.obtainMessage(response.code(), 1, 0, response.body());
                } else {
                    msg = handler.obtainMessage(response.code());
                }
                msg.sendToTarget();
            }

            @Override
            public void onFailure(Call<com.mattikettu.pinkiponki.objects.Message> call, Throwable t) {
                Log.d(TAG, "Failed: " + t.getMessage());
                Message msg = handler.obtainMessage(0); //0 for errors
                msg.sendToTarget();
            }
        });
    }

    public void getGames(int amount, final Handler handler){
        Call<List<GameObject>> call = apiService.getGames(amount);
        call.enqueue(new Callback<List<GameObject>>() {
            @Override
            public void onResponse(Call<List<GameObject>> call, Response<List<GameObject>> response) {
                Log.d(TAG, "Responsecode: " + response.code());
                Message msg;
                if(response.code()==200){
                    msg = handler.obtainMessage(response.code(), 2, 0, response.body());
                } else {
                    msg = handler.obtainMessage(response.code());
                }
                msg.sendToTarget();
            }

            @Override
            public void onFailure(Call<List<GameObject>> call, Throwable t) {
                Log.d(TAG, "Failed: " + t.getMessage());
                Message msg = handler.obtainMessage(0); //0 for errors
                msg.sendToTarget();
            }
        });

    }

    public void getUsernames(final Handler handler){
        Call<List<String>> call = apiService.getUsernames();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                Log.d(TAG, "Responsecode: " + response.code());
                Message msg;
                if(response.code()==200){
                    msg = handler.obtainMessage(response.code(), 3, 0, response.body());
                } else {
                    msg = handler.obtainMessage(response.code());
                }
                msg.sendToTarget();
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.d(TAG, "Failed: " + t.getMessage());
                Message msg = handler.obtainMessage(0); //0 for errors
                msg.sendToTarget();
            }
        });
    }

    public void getLocations(final Handler handler){
        Call<List<String>> call = apiService.getLocations();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                Log.d(TAG, "Responsecode: " + response.code());
                Message msg;
                if(response.code()==200){
                    msg = handler.obtainMessage(response.code(), 4, 0, response.body());
                } else {
                    msg = handler.obtainMessage(response.code());
                }
                msg.sendToTarget();
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.d(TAG, "Failed: " + t.getMessage());
                Message msg = handler.obtainMessage(0); //0 for errors
                msg.sendToTarget();
            }
        });
    }

    public void postGame(GameObject game, final Handler handler){
        Call<GameObject> call = apiService.postGame(game);
        call.enqueue(new Callback<GameObject>() {
            @Override
            public void onResponse(Call<GameObject> call, Response<GameObject> response) {
                Log.d(TAG, "Responsecode: " + response.code());
                Message msg;
                if(response.code()==200){
                    msg = handler.obtainMessage(response.code(), 0, 0, response.body());
                } else {
                    msg = handler.obtainMessage(response.code());
                }
                msg.sendToTarget();
            }

            @Override
            public void onFailure(Call<GameObject> call, Throwable t) {
                Log.d(TAG, "Failed: " + t.getMessage());
                Message msg = handler.obtainMessage(0); //0 for errors
                msg.sendToTarget();
            }
        });
    }



    // general usage
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

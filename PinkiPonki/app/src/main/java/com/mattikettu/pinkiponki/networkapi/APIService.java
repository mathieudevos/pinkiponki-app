package com.mattikettu.pinkiponki.networkapi;

/**
 * Created by mathieu on 09/06/2016.
 */
import com.mattikettu.pinkiponki.objects.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.*;


/**
 * Interface for defining all the communications with the parser
 */

public interface APIService {

    // LOGIN & REGISTER
    @POST("/login")
    Call<Username> login(@Body UserObject userObject);

    @POST("/register")
    Call<Username> register(@Body UserObject userObject);

    // @GET statements
    @GET("/users/{username}")
    void getUser(@Path("username") String username, Callback<UserObject> userObjectCallback);

}

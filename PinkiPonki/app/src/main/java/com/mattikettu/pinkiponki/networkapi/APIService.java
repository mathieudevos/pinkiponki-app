package com.mattikettu.pinkiponki.networkapi;

/**
 * Created by mathieu on 09/06/2016.
 */
import com.mattikettu.pinkiponki.objects.*;

import java.util.List;

import okhttp3.RequestBody;
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

    // @GET STATEMENTS //

    // User related
    @GET("/users/{username}")
    Call<UserObject> getUser(@Path("username") String username);

    @POST("/users")
    Call<Username> updateProfile(@Body UserObject userObject);

    // Usernames
    @GET("/usernames")
    Call<List<String>> getUsernames();

    // Locations
    @GET("/locations")
    Call<List<String>> getLocations();


    // Game related
    @GET("/games_all/{amount}")
    Call<List<GameObject>> getGames(@Path("amount") int amount);


    // @POST STATEMENTS //

    // Game related
    @POST("/games")
    Call<GameObject> postGame(@Body GameObject gameObject);

    @Multipart
    @POST("/upload/profile")
    Call<Message> updateProfilePicture(@Part("file")RequestBody file);
}

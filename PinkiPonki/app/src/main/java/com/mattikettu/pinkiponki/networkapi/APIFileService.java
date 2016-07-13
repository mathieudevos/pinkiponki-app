package com.mattikettu.pinkiponki.networkapi;

import com.mattikettu.pinkiponki.objects.Message;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by mathieu on 13/07/2016.
 */
public interface APIFileService {


    @Multipart
    @POST("/uploads/profile")
    Call<Message> updateProfilePicture(@Part("description") RequestBody description, @Part MultipartBody.Part file);
}

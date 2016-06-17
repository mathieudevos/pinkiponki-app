package com.mattikettu.pinkiponki.util;

/**
 * Created by mathieu on 10/06/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.net.CookieHandler;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.mattikettu.pinkiponki.LoginActivity;
import com.mattikettu.pinkiponki.RegisterActivity;
import com.mattikettu.pinkiponki.WelcomeActivity;
import com.mattikettu.pinkiponki.networkapi.APIService;
import com.mattikettu.pinkiponki.ui.tabs.TabHome;
import com.squareup.otto.Bus;
import com.mattikettu.pinkiponki.MainActivity;
import com.mattikettu.pinkiponki.networkapi.CAservice;
import com.mattikettu.pinkiponki.networkapi.CurrentUser;
import com.mattikettu.pinkiponki.networkapi.NetworkLogic;

@Module(
        complete = false,
        library = true,

        injects = {
                //Inject all the classes that will be needing the singletons that are provided below.
                //It's magic.

                //ui
                TabHome.class,

                //util
                BootstrapApplication.class,
                ToastCreator.class,
                SharedPreferenceManager.class,

                //networkapi
                NetworkLogic.class,
                CAservice.class,
                CurrentUser.class,

                //Acitivities
                MainActivity.class,
                LoginActivity.class,
                RegisterActivity.class,
                WelcomeActivity.class
        }
)


public class BootstrapModule {

    private static final String TAG = "BOOTSTRAPMODULE";

    @Provides
    @Singleton
    Bus providebus() { return new PostFromAnyThreadBus(); }

    @Provides
    @Singleton
    APIService provideAPIService(Context context){

        Executor executor = Executors.newSingleThreadExecutor();

        CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();


        //Interceptor, in case we need it
        //okHttpClient.interceptors().add(new Interceptor() {
        //    @Override
        //    public Response intercept(Chain chain) throws IOException {
        //        return null;
        //    }
        //});

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.basepath)
                .client(okHttpClient)
                .callbackExecutor(executor)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(APIService.class);
    }

    @Provides
    @Singleton
    CurrentUser provideCurrentUser(Bus bus){return new CurrentUser();}

    @Provides
    @Singleton
    ToastCreator provideToastCreator(Bus bus){return new ToastCreator();}

    @Provides
    @Singleton
    SharedPreferenceManager sharedPreferenceManager(Bus bus){ return new SharedPreferenceManager();}
    
    //// TODO: 10/06/2016 add databaseHelper (check which db to use) 

}

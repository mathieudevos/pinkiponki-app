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
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.mattikettu.pinkiponki.AddGameActivity;
import com.mattikettu.pinkiponki.LoginActivity;
import com.mattikettu.pinkiponki.ProfileActivity;
import com.mattikettu.pinkiponki.RegisterActivity;
import com.mattikettu.pinkiponki.WelcomeActivity;
import com.mattikettu.pinkiponki.networkapi.APIService;
import com.mattikettu.pinkiponki.ui.SelectPlayerDialog;
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
                SelectPlayerDialog.class,

                //util
                BootstrapApplication.class,
                ToastCreator.class,
                SharedPreferenceManager.class,
                UsernamesList.class,
                LocationsList.class,
                ImageSelector.class,
                DateTimeUtil.class,

                //networkapi
                NetworkLogic.class,
                CAservice.class,
                CurrentUser.class,

                //Acitivities
                MainActivity.class,
                LoginActivity.class,
                RegisterActivity.class,
                WelcomeActivity.class,
                AddGameActivity.class,
                ProfileActivity.class
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
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

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

    @Provides
    @Singleton
    UsernamesList usernamesList(Bus bus){ return new UsernamesList(); }

    @Provides
    @Singleton
    LocationsList locationsList(Bus bus){ return new LocationsList(); }

    @Provides
    @Singleton
    ImageSelector imageSelector(Bus bus){ return new ImageSelector(); }

    @Provides
    @Singleton
    DateTimeUtil dateTimeUtil(Bus bus){ return new DateTimeUtil(); }

    @Provides
    @Singleton
    NetworkLogic networkLogic(Bus bus){ return new NetworkLogic(); }

    
    //// TODO: 10/06/2016 add databaseHelper (check which db to use) 

}

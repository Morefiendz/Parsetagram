package com.plusultra.parsetagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.plusultra.parsetagram.model.Post;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class InstaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Xstagram") // should correspond to APP_ID env variable
                .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
                .clientBuilder(builder)
                .server("https://xstagram.herokuapp.com/parse/").build());
    }
}

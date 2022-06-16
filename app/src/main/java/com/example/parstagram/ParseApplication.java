package com.example.parstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("EQMr5JWZLU7Skoc0VIzOHOaO7jyUGfdZPwWeh7xv")
                .clientKey("qVD8TU8JYBD4VNqRTZNmgS6IoqibiOM2LERgmiR3")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}

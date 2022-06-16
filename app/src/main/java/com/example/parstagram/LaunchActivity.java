package com.example.parstagram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class LaunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // app is launching

        if(ParseUser.getCurrentUser() != null) {
            // user is already logged in from last launch
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            // user is not logged in
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}

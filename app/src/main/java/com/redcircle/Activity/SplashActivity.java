package com.redcircle.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.redcircle.R;

import java.net.URISyntaxException;

public class SplashActivity extends AppCompatActivity {
    private RelativeLayout mPv1;


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide status bar
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        View bView = getWindow().getDecorView();// hide hardware buttons
        bView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);// hide hardware buttons

        try
        {
            this.getSupportActionBar().hide();// hide status bar
        }
        catch (NullPointerException e){}


        mPv1 = (RelativeLayout) findViewById(R.id.backs);


       mPv1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                SplashActivity.this.startActivity(intent);
                finish();
            }
        }, 5000);



    }
}
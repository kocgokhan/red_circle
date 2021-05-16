package com.redcircle.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;
import com.redcircle.Pojo.Songs;
import com.redcircle.Pojo.User;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import static com.redcircle.Util.StaticFields.BASE_URL;
import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class LoginActivity extends AppCompatActivity {

    private RequestQueue queue;

    private static final String CLIENT_ID = "eab1006d63ce4dfaa37ade914acc567d";

    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "com.redcircle://callback";
    private static final String TAG = "login";


    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;

    ImageView spotify_login_button;
    String osi;
    private  String token;
    private int expired=0;
    private  boolean login = false;
    private  boolean deger = false;
    final int random = new Random().nextInt(61) + 20;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide status bar


        View bView = getWindow().getDecorView();// hide hardware buttons
        bView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);// hide hardware buttons
        try
        {
            this.getSupportActionBar().hide();// hide status bar
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_login);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OSDeviceState device = OneSignal.getDeviceState();


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String email = preferences.getString("email", "error");



        firebaseAuth.createUserWithEmailAndPassword(email,"123123123123123123213").addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"başarılı",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    firebaseAuth.signInWithEmailAndPassword(email, "123123123123123123213")
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();

                            }
                            else {
                                //Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });
        osi = device.getUserId();
        if(osi==null){
            osi = " ";
        }

        boolean enabled = device.areNotificationsEnabled();
        boolean subscribed = device.isSubscribed();
        boolean pushDisabled = device.isPushDisabled();


        String loginResponse = MyApplication.get().getPreferences().getString("loginResponse", null);
        if (loginResponse != null) {
            try {
                JSONObject jsonObject = new JSONObject(loginResponse);
                User userFirst = new User(jsonObject,true);

                spotify_login();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }






        spotify_login_button = findViewById(R.id.spotify_login_button);
        spotify_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spotify_login();
            }
        });
    }

    private void requestJson(String token, String osi) {
        JSONObject params = new JSONObject();

        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Hoş Geldiniz");
        progressDialog.show();
        try {
            params.put("token", token);
            params.put("osi", osi);


            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);


                    SharedPreferences.Editor editor = MyApplication.get().getPreferencesEditor();
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        JSONObject users_data = jsonArray.getJSONObject(0);

                        editor.putString("loginResponse", response + "");
                        editor.putString("user_id", users_data.getString("id"));
                        editor.putString("name", users_data.getString("display_name"));
                        editor.putString("images", users_data.getString("images"));
                        editor.putString("email", users_data.getString("email"));
                        editor.putString("username",users_data.getString("username") );
                        editor.putString("bio",users_data.getString("bio") );
                        editor.putString("count_of_following",users_data.getString("count_of_following") );
                        editor.putString("count_of_followers",users_data.getString("count_of_followers") );
                        editor.putString("count_of_like",users_data.getString("count_of_like") );
                        editor.apply();

                        if(users_data.getString("username").equals("null")){
                            login=false;
                        }else{
                            login=true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    finish();
                    if(login==true){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        startActivity(new Intent(LoginActivity.this, FirstSettingActivity.class));
                    }
                    MyApplication.get().getRequestQueue().getCache().clear();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf(TAG, "onErrorResponse : " + error);
                    Toast.makeText(LoginActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                }
            };
            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "user_login", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, "Hata", Toast.LENGTH_SHORT).show();
        }
    }
    private void spotify_login(){
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private,user-read-email,user-read-currently-playing,user-modify-playback-state", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    SharedPreferences.Editor editor = MyApplication.get().getPreferencesEditor();
                    try {
                        expired = response.getExpiresIn();
                        editor.putString("loginResponse", response + "");
                        editor.putString("token", response.getAccessToken());
                        editor.putInt("expired", response.getExpiresIn());
                        editor.apply();
                        if(osi !="Error"){
                          requestJson(response.getAccessToken(), osi);
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                        }
                        MyApplication.get().getRequestQueue().getCache().clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;
                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }





}

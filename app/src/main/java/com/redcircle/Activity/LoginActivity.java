package com.redcircle.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;
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


        osi = device.getUserId();

        Log.wtf(TAG, "osi : "+osi);

        boolean enabled = device.areNotificationsEnabled();
        boolean subscribed = device.isSubscribed();
        boolean pushDisabled = device.isPushDisabled();

        spotify_login();

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
        login=true;

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
                        editor.apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
        builder.setScopes(new String[]{"user-read-private,user-read-email,user-read-currently-playing", "streaming"});
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

package com.redcircle.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.firebase.ui.auth.AuthUI;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.tasks.Task;
import com.redcircle.R;
import com.redcircle.Util.MyApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.redcircle.Util.StaticFields.BASE_URL;
import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class SettingsActivity extends AppCompatActivity {
    private ImageButton back_views,go_logout,go_notif,go_match,go_permis,go_block,go_privacy,go_delete;
    private ImageView user_img;
    private String user_id;
    private String TAG = "SettingAct";
    private TextView name_user,username_user;
    private Socket socket;
    private static final String CLIENT_ID = "eab1006d63ce4dfaa37ade914acc567d";

    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "com.redcircle://callback";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide status bar
        MyApplication app = (MyApplication) getApplicationContext();
        socket = app.getSocket();
        View bView = getWindow().getDecorView();// hide hardware buttons
        bView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);// hide hardware buttons
        try
        {
            this.getSupportActionBar().hide();// hide status bar
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_settings);
        back_views=(ImageButton) findViewById(R.id.back_views);
        back_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        go_notif=(ImageButton) findViewById(R.id.go_notif);
        go_match=(ImageButton) findViewById(R.id.go_match);
        go_permis=(ImageButton) findViewById(R.id.go_permis);
        go_block=(ImageButton) findViewById(R.id.go_block);
        go_privacy=(ImageButton) findViewById(R.id.go_privacy);
        go_logout=(ImageButton) findViewById(R.id.go_logout);
        go_delete=(ImageButton) findViewById(R.id.go_delete);
        go_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("loginResponse");
                editor.remove("user_id");
                editor.remove("name");
                editor.remove("images");
                editor.remove("email");
                editor.remove("username");
                editor.remove("bio");
                editor.remove("count_of_following");
                editor.remove("count_of_followers");
                editor.remove("count_of_like");
                editor.remove("token");
                editor.apply();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));

                socket.disconnect();

                socket.off(Socket.EVENT_CONNECT);
                socket.off(Socket.EVENT_DISCONNECT, onDisconnect);




                AuthUI.getInstance().signOut(getApplicationContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Çıkış yaptınız", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id = preferences.getString("user_id", "Error");
        getUserDetail(user_id);

        user_img = (ImageView) findViewById(R.id.user_img);
        name_user = (TextView) findViewById(R.id.name_user);
        username_user = (TextView) findViewById(R.id.username_user);

    }

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "diconnected");
                }
            });
        }
    };
    public void getUserDetail(String id_one){

        JSONObject params = new JSONObject();
        try {
            params.put("user_id", id_one);


            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            String display_name = jsonObject.getString("display_name");
                            String username = jsonObject.getString("username");
                            String images = jsonObject.getString("images");
                            String profile_lock = jsonObject.getString("profile_lock");

                            Picasso.get().load("https://spotify.krakersoft.com/upload_user_pic/"+images).into(user_img);

                            name_user.setText(display_name);
                            username_user.setText("@"+username);
                        }
                        MyApplication.get().getRequestQueue().getCache().clear();
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }



                    MyApplication.get().getRequestQueue().getCache().clear();
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf(TAG, "onErrorResponse : " + error);
                    Toast.makeText(SettingsActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                }
            };
            com.redcircle.Request.AqJSONObjectRequest aqJSONObjectRequest = new com.redcircle.Request.AqJSONObjectRequest(TAG, BASE_URL + "my_information", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(SettingsActivity.this, "Hata", Toast.LENGTH_SHORT).show();
        }


    }
}
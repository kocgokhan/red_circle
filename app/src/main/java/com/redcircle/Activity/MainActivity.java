package com.redcircle.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.redcircle.Fragment.DashboardFragment;
import com.redcircle.Fragment.HomeFragment;
import com.redcircle.Fragment.MusicListFragment;
import com.redcircle.Fragment.NotificationFragment;
import com.redcircle.Fragment.PostFragment;
import com.redcircle.Fragment.ProfileFragment;
import com.redcircle.R;
import com.redcircle.Util.MyApplication;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import static com.redcircle.Util.StaticFields.BASE_URL;
import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "eab1006d63ce4dfaa37ade914acc567d";

    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "com.redcircle://callback";
    private ImageButton post_btn,music_list;
    private String user_id,osi,newtoken;
    private boolean send_token=false;
    private int expired;
    private String TAG="MainAct";
    private Long tsLong;

    private Socket socket;
    {
        try {
            socket = IO.socket("https://www.spotisocket.krakersoft.com:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    post_btn.setImageResource(R.mipmap.post_icon);
                    fragment = new HomeFragment();
                    break;
                case R.id.navigation_notification:
                    post_btn.setImageResource(R.mipmap.post_icon);
                    fragment = new NotificationFragment();
                    break;
                case R.id.navigation_dashboard:
                    post_btn.setImageResource(R.mipmap.post_icon);
                    fragment = new DashboardFragment();
                    break;
                case R.id.navigation_profile:
                    post_btn.setImageResource(R.mipmap.post_icon);
                    fragment = new ProfileFragment();
                    break;
                case R.id.navigation_music_list:
                    post_btn.setImageResource(R.mipmap.post_icon);
                    fragment = new MusicListFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide status bar
        socket.connect();


        View bView = getWindow().getDecorView();// hide hardware buttons
        bView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);// hide hardware buttons
        try
        {
            this.getSupportActionBar().hide();// hide status bar
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_main);

        loadFragment(new DashboardFragment());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setItemIconTintList(null);



        post_btn = findViewById(R.id.post_button);
        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), PostActivity.class);
                startActivity(i);

            }
        });


        music_list = (ImageButton) findViewById(R.id.music_list);
        music_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), ChatUserActivity.class);
                startActivity(i);

            }
        });

        TextView myTextView = findViewById(R.id.playing_song);
        myTextView.setSingleLine(true);
        myTextView.setMarqueeRepeatLimit(-1);
        myTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        myTextView.setSelected(true);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id = preferences.getString("user_id", "Error");
        osi = preferences.getString("osi", "Error");

        socket.on("connect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        socket.emit("register", user_id);
                    }
                });
            }
        });

        socket.on("event", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        myTextView.setVisibility(View.VISIBLE);
                        myTextView.setText(String.valueOf(args[0]));


                    }
                });
            }
        });
        socket.on("match_song", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {

                        Log.wtf(TAG, String.valueOf(args[0]));
                        if(args[0].equals("eşleşmen var kardeş !")){
                            FancyToast.makeText(MainActivity.this, "Eşleşme var", FancyToast.LENGTH_LONG, FancyToast.CONFUSING, R.drawable.spls, false).show();
                        }


                    }
                });
            }
        });

        socket.on("get_token", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        if(args[0].equals("get token bro")){
                            spotify_logins();
                        }
                    }
                });
            }
        });

    }

    private void getnewTokenJson(String token, String user_id) {
        JSONObject params = new JSONObject();
        send_token=true;

        try {
            params.put("token", token);
            params.put("user_id", user_id);


            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);


                    MyApplication.get().getRequestQueue().getCache().clear();
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf(TAG, "onErrorResponse : " + error);
                   Toast.makeText(MainActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                }
            };
            com.redcircle.Request.AqJSONObjectRequest aqJSONObjectRequest = new com.redcircle.Request.AqJSONObjectRequest(TAG, BASE_URL + "update_token", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Hata", Toast.LENGTH_SHORT).show();
        }
    }
    private void spotify_logins(){
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
                        editor.putString("loginResponse", response + "");
                        editor.apply();

                         getnewTokenJson(response.getAccessToken(), user_id);

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




    public static void setWindowFlag(MainActivity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
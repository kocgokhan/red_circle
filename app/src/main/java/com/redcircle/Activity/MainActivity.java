package com.redcircle.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.redcircle.Adapter.PostsAdapter;
import com.redcircle.Pojo.Posts;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.IsLoading;
import com.redcircle.Util.MyApplication;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import static com.redcircle.Util.StaticFields.BASE_URL;
import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "eab1006d63ce4dfaa37ade914acc567d";

    private Context context = MainActivity.this;
    private Activity activity = MainActivity.this;
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "com.redcircle://callback";
    private ImageButton post_btn,music_list;
    private String user_id,osi,newtoken;
    private boolean send_token=false;
    private int expired;
    private String TAG="MainAct";
    private Long tsLong;
    private Socket socket;
    private Boolean isConnected = true;
    private TextView myTextView;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerView;
    private ImageView no_post_image;
    private TextView no_post_text;
    private boolean posters = false;

    private ArrayList<Posts> postsArrayList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ProgressBar isLoadingPB;
    final IsLoading isLoading = new IsLoading();


    private int pageNumber = 1;
    private int maxPageCount = 0;
    private int offset = 4;
    private boolean hasMore = false;
    private boolean firstTime = true;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent match_intent = new Intent(MainActivity.this, MatchActivity.class);
                    startActivity(match_intent);
                    break;
                case R.id.navigation_notification:
                    Intent notif_intent = new Intent(MainActivity.this, NotificationActivity.class);
                    startActivity(notif_intent);
                    break;
                case R.id.navigation_profile:
                    Intent profile_intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(profile_intent);
                    break;
                case R.id.navigation_music_list:
                    Intent music_intent = new Intent(MainActivity.this, DiscoverUserActivity.class);
                    startActivity(music_intent);
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide status bar

        MyApplication app = (MyApplication) getApplicationContext();
        socket = app.getSocket();
        socket.on(Socket.EVENT_CONNECT,onConnect);
        socket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        socket.on("get_token", getToken);
        socket.on("event", event);
        socket.connect();

        View bView = getWindow().getDecorView();// hide hardware buttons
        bView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);// hide hardware buttons
        try
        {
            this.getSupportActionBar().hide();// hide status bar
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setItemIconTintList(null);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id = preferences.getString("user_id", "Error");
        osi = preferences.getString("osi", "Error");

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        recyclerView = findViewById(R.id.song_poster);
        isLoadingPB = findViewById(R.id.isLoadingPB);
        if (pageNumber < maxPageCount || postsArrayList.size()==0)  {
            initializeWidgets();
        }
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

                Intent i = new Intent(getApplicationContext(), ChatListActivity.class);
                startActivity(i);

            }
        });

        myTextView = findViewById(R.id.playing_song);
        myTextView.setSingleLine(true);
        myTextView.setMarqueeRepeatLimit(-1);
        myTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        myTextView.setSelected(true);

    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    socket.emit("register", user_id);

                }
            });
        }
    };
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "diconnected");
                    isConnected = false;
                }
            });
        }
    };
    private Emitter.Listener getToken = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(args[0].equals("get token bro")){
                        spotify_logins();
                    }
                }
            });
        }
    };

    private Emitter.Listener event = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myTextView.setVisibility(View.VISIBLE);
                    myTextView.setText(String.valueOf(args[0]));
                    Log.wtf(TAG,String.valueOf(args[0]));
                }
            });
        }
    };

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
    private void initializeWidgets() {

        isLoading.setListener(new IsLoading.OnLoadingListener() {
            @Override
            public void onChange() {
                if (isLoading.isLoading()){
                    Log.d(TAG, "onChange: is loading");
                    ViewGroup.MarginLayoutParams marginLayoutParams =
                            (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
                    marginLayoutParams.setMargins(0, 0, 0, dpToPx(100));
                    recyclerView.setLayoutParams(marginLayoutParams);
                    isLoadingPB.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG, "onChange: not loading");
                    ViewGroup.MarginLayoutParams marginLayoutParams =
                            (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
                    marginLayoutParams.setMargins(0, 0, 0, dpToPx(0));
                    isLoadingPB.setVisibility(View.GONE);
                    recyclerView.setLayoutParams(marginLayoutParams);
                }
            }
        });

            getpostlist();

    }
    private void getpostlist(){
        JSONObject params = new JSONObject();
        posters=true;
        try {

            params.put("user_id", user_id);
            params.put("is_user", false);
            params.put("page",String.valueOf(pageNumber));
            params.put("row_per_page",String.valueOf(offset));
            Response.Listener<JSONObject> listener = response -> {

                if (progressDialog.isShowing()) progressDialog.dismiss();
                if(isLoading.isLoading()) isLoading.setLoading(false);
                Log.wtf(TAG, "onResponse : " + response);

                JSONArray jsonArray = null;
                try {

                    if (response.isNull("data")){
                        hasMore = false;
                    } else {
                        hasMore = true;
                        jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject;
                        postsArrayList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            maxPageCount= Integer.parseInt(jsonObject.getString("max_page_count"));
                            Posts test = new Posts(jsonObject, false);
                            postsArrayList.add(test);

                        }

                        if (postsArrayList.get(0)==null){

                        }else{ drawCart();}
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf(TAG, "onErrorResponse : " + error);
                    Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
                }
            };
            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "posts", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
        }


    }

    private void drawCart() {
        PostsAdapter postsAdapter = new PostsAdapter(context, postsArrayList);
        recyclerView.setAdapter(postsAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)){
                    Log.d(TAG, "onScrollStateChanged: last");
                    if (hasMore){
                        if (pageNumber < maxPageCount || postsArrayList.size()==0) {
                            isLoading.setLoading(true);
                            pageNumber += 1;
                            getpostlist();
                        }
                    } else {
                        if(isLoading.isLoading()) isLoading.setLoading(false);
                    }
                }
            }
        });

        firstTime = false;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        socket.disconnect();

        socket.off(Socket.EVENT_CONNECT, onConnect);
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off("get_token", getToken);
        socket.off("event", event);
    }

}
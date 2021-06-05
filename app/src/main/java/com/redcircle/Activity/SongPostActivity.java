package com.redcircle.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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
import com.github.nkzawa.socketio.client.Socket;
import com.redcircle.Adapter.PostsAdapter;
import com.redcircle.Adapter.SongPostsAdapter;
import com.redcircle.Pojo.Posts;
import com.redcircle.Pojo.SongPost;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.IsLoading;
import com.redcircle.Util.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.redcircle.Util.StaticFields.BASE_URL;

public class SongPostActivity extends AppCompatActivity {
    private ImageButton back_views;

    private String user_id,song_uri;
    private boolean send_token=false;
    private int expired;
    private String TAG="SongPostAct";
    private Boolean isConnected = true;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView song_poster;
    private boolean posters = false;

    private ArrayList<SongPost> songPostArrayList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ProgressBar isLoadingPB;
    final IsLoading isLoading = new IsLoading();


    private int pageNumber = 1;
    private int maxPageCount = 0;
    private int offset = 4;
    private boolean hasMore = false;
    private boolean firstTime = true;
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
        setContentView(R.layout.activity_song_post);
        back_views = (ImageButton) findViewById(R.id.back_views);
        back_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            song_uri= null;
        } else {
            song_uri= extras.getString("song_uri");
        }


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id = preferences.getString("user_id", "Error");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Lütfen Bekleyin");
        progressDialog.setCancelable(false);


        song_poster = findViewById(R.id.song_poster);
        isLoadingPB = findViewById(R.id.isLoadingPB);
        if (pageNumber <= maxPageCount || songPostArrayList.size()==0)  {
            initializeWidgets();
        }

    }
    private void initializeWidgets() {

        isLoading.setListener(new IsLoading.OnLoadingListener() {
            @Override
            public void onChange() {
                if (isLoading.isLoading()){
                    Log.d(TAG, "onChange: is loading");
                    ViewGroup.MarginLayoutParams marginLayoutParams =
                            (ViewGroup.MarginLayoutParams) song_poster.getLayoutParams();
                    marginLayoutParams.setMargins(0, 0, 0, dpToPx(100));
                    song_poster.setLayoutParams(marginLayoutParams);
                    isLoadingPB.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG, "onChange: not loading");
                    ViewGroup.MarginLayoutParams marginLayoutParams =
                            (ViewGroup.MarginLayoutParams) song_poster.getLayoutParams();
                    marginLayoutParams.setMargins(0, 0, 0, dpToPx(0));
                    isLoadingPB.setVisibility(View.GONE);
                    song_poster.setLayoutParams(marginLayoutParams);
                }
            }
        });

        getpostlist(user_id,song_uri, pageNumber, offset);

    }
    private void getpostlist(String id,String song_uri, final int pageNumber, final int offset){
        JSONObject params = new JSONObject();
        posters=true;
        try {

            params.put("user_id", id);
            params.put("song_uri", song_uri);
            params.put("page",String.valueOf(pageNumber));
            params.put("row_per_page",String.valueOf(offset));
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    if(isLoading.isLoading()) isLoading.setLoading(false);
                    Log.wtf(TAG, "onResponse : " + response);
                    songPostArrayList.clear();
                    JSONArray jsonArray = null;
                    try {

                        if (response.isNull("data")){
                            hasMore = false;

                        } else {
                            hasMore = true;
                            jsonArray = response.getJSONArray("data");
                            JSONObject jsonObject;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                maxPageCount= Integer.parseInt(jsonObject.getString("max_page_count"));
                                SongPost test = new SongPost(jsonObject, false);
                                songPostArrayList.add(test);

                            }
                            if (songPostArrayList.get(0)==null){

                            }else{ drawCart();}
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf(TAG, "onErrorResponse : " + error);
                    Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
                }
            };
            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "song_posted", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
        }


    }

    private void drawCart() {
        SongPostsAdapter songPostsAdapter = new SongPostsAdapter(getApplicationContext(), songPostArrayList);
        song_poster.setAdapter(songPostsAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        song_poster.setLayoutManager(linearLayoutManager);

        song_poster.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)){
                    Log.d(TAG, "onScrollStateChanged: last");
                    if (hasMore){
                        if (pageNumber <= maxPageCount || songPostArrayList.size()==0) {
                            isLoading.setLoading(true);
                            pageNumber += 1;
                            getpostlist(user_id, song_uri,pageNumber, offset);
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
}
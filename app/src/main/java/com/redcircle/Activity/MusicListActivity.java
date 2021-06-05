package com.redcircle.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.redcircle.Adapter.ArtistSponsoredAdapter;
import com.redcircle.Adapter.VjAdapter;
import com.redcircle.Pojo.ArtistSponsored;
import com.redcircle.Pojo.ConnectListVj;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.redcircle.Util.StaticFields.BASE_URL;

public class MusicListActivity extends AppCompatActivity {
    RecyclerView recyclerView,iv_artist;
    private ImageButton back_views;
    private ImageView top_ten,top_hundred;


    // Layout Manager
    RecyclerView.LayoutManager RecyclerViewLayoutManager;

    // Linear Layout Manager
    LinearLayoutManager HorizontalLayout;

    View ChildView;

    private String user_id;
    private boolean vj= false;
    int RecyclerViewItemPosition;
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
        setContentView(R.layout.activity_music_list);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id = preferences.getString("user_id", "Error");

        recyclerView = (RecyclerView) findViewById(R.id.album_track_recy);
        iv_artist = (RecyclerView) findViewById(R.id.iv_artist);

        top_ten = (ImageView) findViewById(R.id.top_ten);
        top_hundred = (ImageView) findViewById(R.id.top_hundred);

        back_views = (ImageButton) findViewById(R.id.back_views);
        back_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        top_ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MusicListActivity.this, TopListActivity.class);
                String strName = null;
                i.putExtra("top_list","top_ten" );
                startActivity(i);
                MyApplication.get().getRequestQueue().getCache().clear();
            }
        });
        top_hundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MusicListActivity.this, TopListActivity.class);
                String strName = null;
                i.putExtra("top_list","top_hundred");
                startActivity(i);
                MyApplication.get().getRequestQueue().getCache().clear();
            }
        });


        getSponsored(user_id);
        getVjlist(user_id);
    }
    private void getVjlist(String id){
        ArrayList<ConnectListVj> connectListVjArrayList = new ArrayList<>();
        JSONObject params = new JSONObject();
        vj=true;
        try {

            params.put("user_id", id);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);
                    connectListVjArrayList.clear();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            ConnectListVj test = new ConnectListVj(jsonObject, false);
                            connectListVjArrayList.add(test);
                        }


                        if (connectListVjArrayList.get(0)==null){


                        }else{ drawCart(connectListVjArrayList);}

                        MyApplication.get().getRequestQueue().getCache().clear();
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

            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "connect_list", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
        }


    }

    public void drawCart(ArrayList<ConnectListVj> list){
        if (getApplicationContext()!=null) {
            VjAdapter postsAdapter = new VjAdapter(getApplicationContext(), ConnectListVj.getData(list));
            recyclerView.setAdapter(postsAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
        }

    }

    private void getSponsored(String id){
        ArrayList<ArtistSponsored> artistSponsoredArrayList = new ArrayList<>();
        JSONObject params = new JSONObject();
        try {

            params.put("user_id", id);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);
                    artistSponsoredArrayList.clear();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            ArtistSponsored test = new ArtistSponsored(jsonObject, false);
                            artistSponsoredArrayList.add(test);
                        }


                        if (artistSponsoredArrayList.size()==0){


                        }else{ drawSponsored(artistSponsoredArrayList);}

                        MyApplication.get().getRequestQueue().getCache().clear();
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

            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "sponsored_content", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
        }


    }

    public void drawSponsored(ArrayList<ArtistSponsored> list){
        if (getApplicationContext()!=null) {
            ArtistSponsoredAdapter artistSponsoredAdapter = new ArtistSponsoredAdapter(getApplicationContext(), ArtistSponsored.getData(list));
            iv_artist.setAdapter(artistSponsoredAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            iv_artist.setLayoutManager(linearLayoutManager);
        }

    }
}
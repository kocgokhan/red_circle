package com.redcircle.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
import com.redcircle.Adapter.ArtistAlbumAdapter;
import com.redcircle.Pojo.ArtistAlbum;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.redcircle.Util.StaticFields.BASE_URL;

public class SponsorDetailActivity extends AppCompatActivity {
    private ImageButton back_btn;
    private String user_names,user_usernames,user_image,user_id,my_user_id,content_name,content_uri,content_image;
    private TextView artist_name,content_name_iv;
    private ImageView content_image_iv;
    private RecyclerView recycler_album;
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
        setContentView(R.layout.activity_sponsor_detail);
        back_btn = (ImageButton) findViewById(R.id.back_views);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recycler_album = (RecyclerView) findViewById(R.id.recycler_album);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            user_names= null;
            user_usernames= null;
            user_image= null;
            user_id= null;
            content_name= null;
            content_uri= null;
            content_image= null;
        } else {
            user_names= extras.getString("user_name");
            user_usernames= extras.getString("user_username");
            user_image= extras.getString("user_image");
            user_id= extras.getString("user_user_id");
            content_name= extras.getString("content_name");
            content_uri= extras.getString("content_uri");
            content_image= extras.getString("content_image");
        }
        content_name_iv =(TextView)findViewById(R.id.content_name_iv);
        content_name_iv.setText(content_name);
        artist_name =(TextView)findViewById(R.id.user_username);
        artist_name.setText(user_names);
        content_image_iv = (ImageView) findViewById(R.id.content_image_iv);
        Picasso.get().load(content_image).memoryPolicy(MemoryPolicy.NO_CACHE )
                .networkPolicy(NetworkPolicy.NO_CACHE).error(R.mipmap.ic_launcher).memoryPolicy(MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_STORE).into(content_image_iv);
        Picasso.get().setLoggingEnabled(false);

        getArtistAlbum(user_id);
    }
    private void getArtistAlbum(String id){
        ArrayList<ArtistAlbum> artistAlbumArrayList = new ArrayList<>();
        JSONObject params = new JSONObject();
        try {

            params.put("user_id", id);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);
                    artistAlbumArrayList.clear();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            ArtistAlbum test = new ArtistAlbum(jsonObject, false);
                            artistAlbumArrayList.add(test);
                        }


                        if (artistAlbumArrayList.get(0)==null){


                        }else{ drawArtistAlbum(artistAlbumArrayList);}

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

            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "content_album", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
        }


    }

    public void drawArtistAlbum(ArrayList<ArtistAlbum> list){

        ArtistAlbumAdapter artistAlbumAdapter = new ArtistAlbumAdapter(getApplicationContext(), ArtistAlbum.getData(list));
        recycler_album.setAdapter(artistAlbumAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_album.setLayoutManager(linearLayoutManager);


    }
}
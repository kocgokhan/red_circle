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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.redcircle.Adapter.AlbumTrackAdapter;
import com.redcircle.Pojo.AlbumTrack;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.redcircle.Util.StaticFields.BASE_URL;

public class AlbumTrackActivity extends AppCompatActivity {
    private String album_id,type,album_name;
    private RecyclerView album_track_recy;
    private ImageButton back_btn;
    private TextView album_names;
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
        setContentView(R.layout.activity_album_track);
        back_btn = (ImageButton) findViewById(R.id.back_views);
        album_names = (TextView) findViewById(R.id.user_username);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        album_track_recy = (RecyclerView) findViewById(R.id.iv_artist);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            album_id= null;
            type= null;
            album_name= null;
        } else {
            album_id= extras.getString("album_id");
            type= extras.getString("type");
            album_name= extras.getString("album_name");
        }
        album_names.setText(album_name);
        getAlbumTrack(album_id,type);
    }
    private void getAlbumTrack(String id,String type){
        ArrayList<AlbumTrack> albumTrackArrayList = new ArrayList<>();
        JSONObject params = new JSONObject();
        try {

            params.put("album_id", id);
            params.put("type", type);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);
                    albumTrackArrayList.clear();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            AlbumTrack test = new AlbumTrack(jsonObject, false);
                            albumTrackArrayList.add(test);
                        }


                        if (albumTrackArrayList.get(0)==null){


                        }else{ drawAlbumTrack(albumTrackArrayList);}

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

            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "album_track", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
        }


    }

    public void drawAlbumTrack(ArrayList<AlbumTrack> list){

        AlbumTrackAdapter albumTrackAdapter = new AlbumTrackAdapter(getApplicationContext(), AlbumTrack.getData(list));
        album_track_recy.setAdapter(albumTrackAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        album_track_recy.setLayoutManager(linearLayoutManager);


    }
}
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
import com.redcircle.Adapter.TopListAdapter;
import com.redcircle.Pojo.TopList;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.redcircle.Util.StaticFields.BASE_URL;

public class TopListActivity extends AppCompatActivity {
    private String top_ten,top_hundred,top_list;
    private TextView top_list_text;
    private ImageButton back_views,song_post_btn,live_song_btn;
    private Boolean limit_list = false;
    private RecyclerView top_list_recyclerView;
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
        setContentView(R.layout.activity_top_list);

        top_list_recyclerView = (RecyclerView) findViewById(R.id.album_track_recy);

        back_views = (ImageButton) findViewById(R.id.back_views);
        back_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            top_list= null;
        } else {
            top_list= extras.getString("top_list");
        }


        top_list_text = (TextView) findViewById(R.id.user_username);
        if(top_list.equals("top_ten")){
            top_list_text.setText("Top 10");
            getTopList(10);
        }else{
            top_list_text.setText("Top 100");
            getTopList(100);
        }

    }
    private void getTopList(Integer limit){
        ArrayList<TopList> topListArrayList = new ArrayList<>();
        JSONObject params = new JSONObject();
        limit_list=true;
        try {

            params.put("limit", limit);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);
                    topListArrayList.clear();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            TopList test = new TopList(jsonObject, false);
                            topListArrayList.add(test);
                        }

                       if (topListArrayList.get(0)==null){


                        }else{ drawCart(topListArrayList);}

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

            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "top_played", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
        }


    }

   public void drawCart(ArrayList<TopList> list){
        if (getApplicationContext()!=null) {
            TopListAdapter topListAdapter = new TopListAdapter(getApplicationContext(), TopList.getData(list));
            top_list_recyclerView.setAdapter(topListAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            top_list_recyclerView.setLayoutManager(linearLayoutManager);
        }

    }
}
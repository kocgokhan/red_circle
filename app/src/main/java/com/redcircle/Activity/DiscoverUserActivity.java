package com.redcircle.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
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

public class DiscoverUserActivity extends AppCompatActivity {
    private String user_id;
    private ImageButton back_views;
    private RecyclerView iv_artist,iv_vj_user;
    private Socket socket;
    private ArrayList<ConnectListVj> connectListVjArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide status bar

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id = preferences.getString("user_id", "Error");

        MyApplication app = (MyApplication) getApplicationContext();
        socket = app.getSocket();

        socket.emit("listen live", user_id);
        socket.on("user_listen", user_listen);
        socket.connect();
        View bView = getWindow().getDecorView();// hide hardware buttons
        bView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);// hide hardware buttons
        try
        {
            this.getSupportActionBar().hide();// hide status bar
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_discover_user);
        back_views = (ImageButton) findViewById(R.id.back_views);
        back_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv_artist = (RecyclerView) findViewById(R.id.iv_artist);
        iv_vj_user = (RecyclerView) findViewById(R.id.iv_vj_user);

        getSponsored(user_id);
    }
    private Emitter.Listener user_listen = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connectListVjArrayList.clear();
                    String json = (String) args[0]; // your Object is variable argument so access it via index like array
                    //Log.wtf(TAG,json);
                    try {
                        JSONObject jsonResponse;
                        jsonResponse = new JSONObject(json);
                        JSONArray jsonArray = null;
                        jsonArray = jsonResponse.getJSONArray("data");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            ConnectListVj test = new ConnectListVj(jsonObject, false);
                            connectListVjArrayList.add(test);
                        }
                        if (connectListVjArrayList.get(0) == null) {

                        } else {
                            drawVjList(connectListVjArrayList);
                        }
                        MyApplication.get().getRequestQueue().getCache().clear();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
    };
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
    public void drawVjList(ArrayList<ConnectListVj> list){
            VjAdapter vjAdapter = new VjAdapter(getApplicationContext(), ConnectListVj.getData(list));
            iv_vj_user.setAdapter(vjAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            iv_vj_user.setLayoutManager(linearLayoutManager);
    }
}
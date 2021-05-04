package com.redcircle.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.redcircle.Adapter.SongAdapter;
import com.redcircle.Pojo.Songs;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.redcircle.Util.StaticFields.BASE_URL;

public class SongActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText editTextSearch;
    ArrayList<String> names;
    SongAdapter adapter;
    private String TAG="SongAct";
    private String token;
    private  boolean songs = false;
    private ImageButton back_view;

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
        setContentView(R.layout.activity_song);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString("token", "Error");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewSong);
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        recyclerView.setVisibility(View.INVISIBLE);

        back_view = (ImageButton) findViewById(R.id.back_view);

        back_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                recyclerView.setVisibility(View.VISIBLE);
                if(editable.length() > 2){
                    String loginResponse = MyApplication.get().getPreferences().getString("loginResponse", null);
                    if (loginResponse != null) {
                        getSongList(String.valueOf(editable),token);
                    }
                }

            }
        });

    }
    private void getSongList(String word, String token){
        ArrayList<Songs> songsArrayList = new ArrayList<>();
        JSONObject params = new JSONObject();
        songs=true;
        try {

            params.put("key", word);
            params.put("token", token);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);
                    songsArrayList.clear();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            Songs test = new Songs(jsonObject, false);
                            songsArrayList.add(test);
                        }
                        drawCart(songsArrayList);

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

            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "song_list", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
        }


    }

    public void drawCart(ArrayList<Songs> list){

        SongAdapter songAdapter = new SongAdapter(getApplicationContext(), Songs.getData(list));
        recyclerView.setAdapter(songAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


    }
}
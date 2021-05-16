package com.redcircle.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.redcircle.Adapter.PreviewSongUserAdapter;
import com.redcircle.Pojo.UserPreviewSong;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.redcircle.Util.StaticFields.BASE_URL;

public class ConnectUserActivity extends AppCompatActivity {
    private Socket socket;
    {
        try {
            socket = IO.socket("https://www.spotisocket.krakersoft.com:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    RecyclerView recyclerView;
    private ImageButton connect_me,unconnect_me;
    private boolean prev = false;
    private String user_names,user_usernames,user_image,user_id,my_user_id;
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
        setContentView(R.layout.activity_connect_user);

        ImageButton back_btn= (ImageButton) findViewById(R.id.back_views);
        ImageView imageView_photo = (ImageView) findViewById(R.id.imageView_photo);
        ImageView song_photo = (ImageView) findViewById(R.id.song_photo);
        TextView user_name = (TextView) findViewById(R.id.user_name);
        TextView artist = (TextView) findViewById(R.id.artist);

        TextView myTextView = findViewById(R.id.playing_songs);


        connect_me= (ImageButton) findViewById(R.id.connect_me);
        unconnect_me= (ImageButton) findViewById(R.id.unconnect_me);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        my_user_id = preferences.getString("user_id", "");

        recyclerView  = (RecyclerView) findViewById(R.id.preview_songitem);


        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            user_names= null;
            user_usernames= null;
            user_image= null;
            user_id= null;
        } else {
            user_names= extras.getString("user_name");
            user_usernames= extras.getString("user_username");
            user_image= extras.getString("user_image");
            user_id= extras.getString("user_user_id");
        }

        user_name.setText(user_names);

        Picasso.get().load(String.valueOf(Html.fromHtml(user_image))).into(imageView_photo);


        connect_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect_do(user_id,my_user_id);
            }
        });
        unconnect_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        socket.emit("host_detail", user_id);

        socket.on("current_song", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        myTextView.setVisibility(View.VISIBLE);


                        String income_data = (String) args[0];
                        String[] separated = income_data.split(" - ");


                        if(separated[4].equals(user_id)){

                            myTextView.setText(separated[0]);
                            artist.setText(separated[1]);

                            Picasso.get().load(String.valueOf(Html.fromHtml(separated[2]))).into(song_photo);

                        }



                        //Log.wtf("current_song",separated[0]);
                    }
                });
            }
        });
        socket.on("disconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        myTextView.setVisibility(View.INVISIBLE);

                        artist.setVisibility(View.INVISIBLE);
                        song_photo.setVisibility(View.INVISIBLE);




                        //Log.wtf("current_song",separated[0]);
                    }
                });
            }
        });
        get_preview_list(user_id);
    }

    private void get_preview_list(String id){
        ArrayList<UserPreviewSong> userPreviewSongArrayList = new ArrayList<>();
        JSONObject params = new JSONObject();
        prev=true;
        try {

            params.put("user_id", id);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);
                    userPreviewSongArrayList.clear();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            UserPreviewSong test = new UserPreviewSong(jsonObject, false);
                            userPreviewSongArrayList.add(test);
                        }


                        if (userPreviewSongArrayList.get(0)==null){


                        }else{ drawCart(userPreviewSongArrayList);}

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

            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "list_preview_song", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
        }


    }

    public void drawCart(ArrayList<UserPreviewSong> list){

        PreviewSongUserAdapter previewSongUserAdapter = new PreviewSongUserAdapter(getApplicationContext(), UserPreviewSong.getData(list));
        recyclerView.setAdapter(previewSongUserAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


    }

    public void connect_do(String host_id , String my_id){

        connect_me.setVisibility(View.INVISIBLE);
        unconnect_me.setVisibility(View.VISIBLE);

        JSONObject params = new JSONObject();

        try {
            params.put("host_id", host_id);
            params.put("user_id", user_id);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Log.wtf(TAG, "onResponse : " + response);


                    MyApplication.get().getRequestQueue().getCache().clear();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf(TAG, "onErrorResponse : " + error);
                    Toast.makeText(getApplication(), "Hata", Toast.LENGTH_SHORT).show();
                }
            };

            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "connect_host", params, listener, errorListener);
            aqJSONObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getApplication(), "Hata", Toast.LENGTH_SHORT).show();
        }


    }
}
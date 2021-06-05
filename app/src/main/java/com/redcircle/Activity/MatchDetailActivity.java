package com.redcircle.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static com.redcircle.Util.StaticFields.BASE_URL;

public class MatchDetailActivity extends AppCompatActivity {

    private String name_name,images,user_id,username,bio,song_image,song_name,song_artist,match_status,match_id,my_id;
    private ImageButton back_views,decline_btn,accept_btn,undecline_btn,delete_btn;
    private TextView match_username,match_user_name,match_userbio,name_song,artist_song,decline_text,accept_text,undecline_text,delete_text;
    private String TAG = "MatchDetailAct";
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
        setContentView(R.layout.activity_match_detail);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        my_id = preferences.getString("user_id", "");

        back_views = (ImageButton) findViewById(R.id.back_views);
        back_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MatchDetailActivity.this, MatchActivity.class);
                startActivity(i);
                MyApplication.get().getRequestQueue().getCache().clear();
            }
        });

        match_username = (TextView) findViewById(R.id.match_username);
        match_user_name = (TextView) findViewById(R.id.match_user_name);
        match_userbio = (TextView) findViewById(R.id.match_userbio);
        name_song = (TextView) findViewById(R.id.name_song);
        artist_song = (TextView) findViewById(R.id.artist_song);

        decline_text = (TextView) findViewById(R.id.decline_text);
        accept_text = (TextView) findViewById(R.id.accept_text);
        undecline_text = (TextView) findViewById(R.id.undecline_text);
        delete_text= (TextView) findViewById(R.id.delete_text);

        ImageView image = (ImageView) findViewById(R.id.match_userphoto);
        ImageView set_post_image = (ImageView) findViewById(R.id.back_image);

        decline_btn = (ImageButton) findViewById(R.id.decline_btn);
        accept_btn = (ImageButton) findViewById(R.id.accept_btn);
        undecline_btn = (ImageButton) findViewById(R.id.undecline_btn);
        delete_btn = (ImageButton) findViewById(R.id.delete_btn);




        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            name_name= null;
            username= null;
            images= null;
            user_id= null;
            bio= null;
            song_image= null;
            song_name= null;
            song_artist= null;
            match_status= null;
            match_id= null;
        } else {
            name_name= extras.getString("user_name");
            username= extras.getString("user_username");
            images= extras.getString("user_image");
            user_id= extras.getString("user_user_id");
            bio= extras.getString("user_bio");
            song_image= extras.getString("song_image");
            song_name= extras.getString("song_name");
            song_artist= extras.getString("song_artist");
            match_status= extras.getString("match_status");
            match_id= extras.getString("match_id");
        }

        match_username.setText("@"+username);
        match_user_name.setText(name_name);
        match_userbio.setText(bio);
        name_song.setText(song_name);
        artist_song.setText(song_artist);

        Picasso.get().load("https://spotify.krakersoft.com/upload_user_pic/"+images).into(image);
        Picasso.get().load(song_image).into(set_post_image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go_profile();
            }
        });

        switch(match_status){
            case "3":

                decline_btn.setVisibility(View.INVISIBLE);
                accept_btn.setVisibility(View.INVISIBLE);
                undecline_btn.setVisibility(View.VISIBLE);
                delete_btn.setVisibility(View.VISIBLE);

                decline_text.setVisibility(View.INVISIBLE);
                accept_text.setVisibility(View.INVISIBLE);
                undecline_text.setVisibility(View.VISIBLE);
                delete_text.setVisibility(View.VISIBLE);

                break;

            default:

                decline_btn.setVisibility(View.VISIBLE);
                accept_btn.setVisibility(View.VISIBLE);
                undecline_btn.setVisibility(View.INVISIBLE);
                delete_btn.setVisibility(View.INVISIBLE);

                decline_text.setVisibility(View.VISIBLE);
                accept_text.setVisibility(View.VISIBLE);
                undecline_text.setVisibility(View.INVISIBLE);
                delete_text.setVisibility(View.INVISIBLE);

                break;
        }

        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept(my_id,match_id);
            }
        });

        decline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decline(my_id,user_id);
            }
        });

        undecline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undecline(my_id,user_id);
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(my_id,user_id);
            }
        });

    }
    public void go_profile(){

        Intent i = new Intent(this, UserProfileActivity.class);
        String strName = null;
        i.putExtra("user_name", name_name);
        i.putExtra("user_image", images);
        i.putExtra("user_username", username);
        i.putExtra("user_user_id", user_id);
        startActivity(i);
        MyApplication.get().getRequestQueue().getCache().clear();

    }
     public void  accept(String my_id,String match_id){

            accept_btn.setVisibility(View.INVISIBLE);
            decline_btn.setVisibility(View.INVISIBLE);
            undecline_btn.setVisibility(View.INVISIBLE);
            delete_btn.setVisibility(View.INVISIBLE);

            decline_text.setVisibility(View.INVISIBLE);
            accept_text.setVisibility(View.INVISIBLE);
            undecline_text.setVisibility(View.INVISIBLE);
            delete_text.setVisibility(View.INVISIBLE);


            JSONObject params = new JSONObject();

            try {
                params.put("user_id", my_id);
                params.put("match_id", match_id);
                Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.wtf(TAG, "onResponse : " + response);

                        Intent i = new Intent(MatchDetailActivity.this, ChatActivity.class);
                        String strName = null;
                        i.putExtra("user_name", name_name);
                        i.putExtra("user_image", images);
                        i.putExtra("chatId", user_id);
                        startActivity(i);
                        finish();
                        MyApplication.get().getRequestQueue().getCache().clear();
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf(TAG, "onErrorResponse : " + error);
                        Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
                    }
                };

                AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "accept_match", params, listener, errorListener);
                aqJSONObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
            } catch (JSONException e) {
                Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
            }




        }
        public void  decline(String my_id,String user_id){

            accept_btn.setVisibility(View.INVISIBLE);
            decline_btn.setVisibility(View.INVISIBLE);
            undecline_btn.setVisibility(View.VISIBLE);
            decline_btn.setVisibility(View.VISIBLE);

            decline_text.setVisibility(View.INVISIBLE);
            accept_text.setVisibility(View.INVISIBLE);
            undecline_text.setVisibility(View.VISIBLE);
            delete_text.setVisibility(View.VISIBLE);


            JSONObject params = new JSONObject();

            try {
                params.put("my_id", my_id);
                params.put("user_id", user_id);
                Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.wtf(TAG, "onResponse : " + response);


                        Intent i = new Intent(MatchDetailActivity.this, MatchActivity.class);
                        startActivity(i);
                        finish();
                        MyApplication.get().getRequestQueue().getCache().clear();
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf(TAG, "onErrorResponse : " + error);
                        Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
                    }
                };

                AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "decline_match", params, listener, errorListener);
                aqJSONObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
            } catch (JSONException e) {
                Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
            }
        }

        public void  undecline(String my_id,String user_id){


            accept_btn.setVisibility(View.VISIBLE);
            decline_btn.setVisibility(View.VISIBLE);
            undecline_btn.setVisibility(View.INVISIBLE);
            delete_btn.setVisibility(View.INVISIBLE);

            accept_text.setVisibility(View.VISIBLE);
            decline_text.setVisibility(View.VISIBLE);
            undecline_text.setVisibility(View.INVISIBLE);
            delete_text.setVisibility(View.INVISIBLE);

            JSONObject params = new JSONObject();

            try {
                params.put("my_id", my_id);
                params.put("user_id", user_id);
                Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.wtf(TAG, "onResponse : " + response);
                        //refresgFrag(getAdapterPosition(),user_id);
                        MyApplication.get().getRequestQueue().getCache().clear();
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf(TAG, "onErrorResponse : " + error);
                        Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
                    }
                };

                AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "undecline_match", params, listener, errorListener);
                aqJSONObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
            } catch (JSONException e) {
                Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
            }
        }
        public void  delete(String my_id,String user_id){

            JSONObject params = new JSONObject();

            try {
                params.put("my_id", my_id);
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
                        Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
                    }
                };

                AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "delete_match", params, listener, errorListener);
                aqJSONObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
            } catch (JSONException e) {
                Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
            }
        }
}
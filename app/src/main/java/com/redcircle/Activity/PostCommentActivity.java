package com.redcircle.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.redcircle.Adapter.ChatListAdapter;
import com.redcircle.Adapter.PostCommentAdapter;
import com.redcircle.Pojo.ChatList;
import com.redcircle.Pojo.NotificationType;
import com.redcircle.Pojo.Notifications;
import com.redcircle.Pojo.PostComment;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.redcircle.Util.StaticFields.BASE_URL;

public class PostCommentActivity extends AppCompatActivity {
    private String TAG ="CommentAct";
    private ImageButton back_views,send_comment;
    private EditText comment_text;
    private String comment,owner_user_id,post_id,user_id;
    private RecyclerView comment_recy;
    private ImagePopup imagePopup;
    private TextView set_user_name, set_user_username,set_post_text,count_like,count_comment,song_name,song_artist;
    private ImageView productImage,song_image,set_profile_image,like_post,unlike_post,set_post_image,song_image_v,comment_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide status bar
        View bView = getWindow().getDecorView();// hide hardware buttons
        bView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        try
        {
            this.getSupportActionBar().hide();// hide status bar
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_post_comment);
        back_views = (ImageButton) findViewById(R.id.back_views);
        back_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        productImage = (ImageView) findViewById(R.id.song_image_detail);
        song_image_v = (ImageView) findViewById(R.id.song_image_v);
        set_profile_image = (ImageView) findViewById(R.id.match_userphoto);
        set_post_image = (ImageView) findViewById(R.id.back_image);
        like_post = (ImageView) findViewById(R.id.like_post);
        unlike_post = (ImageView) findViewById(R.id.unlike_post);
        comment_btn = (ImageView) findViewById(R.id.comment_btn);
        set_user_name = (TextView) findViewById(R.id.set_user_name);
        set_post_text = (TextView) findViewById(R.id.set_post_text);
        set_user_username = (TextView) findViewById(R.id.set_user_username);
        song_name = (TextView) findViewById(R.id.song_name);
        song_artist = (TextView) findViewById(R.id.song_artist);
        count_like = (TextView) findViewById(R.id.count_like);
        count_comment = (TextView) findViewById(R.id.count_comment);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        owner_user_id = preferences.getString("user_id", "Error");

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            post_id= null;
            user_id= null;
        } else {
            post_id= extras.getString("post_id");
            user_id= extras.getString("user_user_id");
            getPostDetail(post_id,owner_user_id);
            getComment(post_id);
        }

        send_comment = (ImageButton) findViewById(R.id.send_comment);
        comment_text = (EditText) findViewById(R.id.comment_text);
        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = comment_text.getText().toString().trim();

                sendComment(user_id,post_id,comment);
            }
        });
        comment_recy =(RecyclerView) findViewById(R.id.comment_recy);
    }
    public void getPostDetail(String post_id, String user_id){
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", user_id);
            params.put("post_id", post_id);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);
                    JSONArray jsonArrayPost = null;
                    try {
                        jsonArrayPost = response.getJSONArray("data");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArrayPost.length(); i++) {
                            jsonObject = jsonArrayPost.getJSONObject(i);

                            String song_images = jsonObject.getString("song_image");
                            String song_names = jsonObject.getString("song_name");
                            String song_artists = jsonObject.getString("song_artist");
                            String post_text = jsonObject.getString("post_text");
                            String post_image = jsonObject.getString("post_image");
                            String proccess_time = jsonObject.getString("proccess_time");
                            String count_likes = jsonObject.getString("count_like");
                            String count_comments = jsonObject.getString("count_comment");
                            String display_name = jsonObject.getString("display_name");
                            String username = jsonObject.getString("username");
                            String images = jsonObject.getString("images");
                            String isLike = jsonObject.getString("isLike");

                            song_name.setText(song_names);
                            song_artist.setText(song_artists);
                            set_user_name.setText(display_name);
                            set_user_username.setText(username);
                            set_post_text.setText(post_text);
                            count_like.setText(count_likes);
                            count_comment.setText(count_comments);

                            productImage.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                            productImage.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                            productImage.setAdjustViewBounds(false);
                            productImage.setScaleType(ImageView.ScaleType.FIT_XY);

                            String imgResource = "https://spotify.krakersoft.com/upload_post_pic/"+post_image;
                            String song_image =   song_images;
                            String profile_photo = "https://spotify.krakersoft.com/upload_user_pic/"+images;
                            if(isLike.equals("1")){
                                like_post.setVisibility(View.INVISIBLE);
                                unlike_post.setVisibility(View.VISIBLE);
                            }else{
                                like_post.setVisibility(View.VISIBLE);
                                unlike_post.setVisibility(View.INVISIBLE);
                            }

                            Picasso.get().load(profile_photo).into(set_profile_image);
                            Picasso.get().load(song_image).into(song_image_v);
                            Picasso.get().load(song_image).into(productImage);
                            Picasso.get().load(imgResource).into(set_post_image);

                        }

                        MyApplication.get().getRequestQueue().getCache().clear();
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                    MyApplication.get().getRequestQueue().getCache().clear();
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf(TAG, "onErrorResponse : " + error);
                    Toast.makeText(PostCommentActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                }
            };
            com.redcircle.Request.AqJSONObjectRequest aqJSONObjectRequest = new com.redcircle.Request.AqJSONObjectRequest(TAG, BASE_URL + "post_detail", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(PostCommentActivity.this, "Hata", Toast.LENGTH_SHORT).show();
        }


    }
    public void getComment(String post_id){
        ArrayList<PostComment> postCommentArrayList = new ArrayList<>();
        JSONObject params = new JSONObject();
        try {

            params.put("post_id", post_id);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);
                    postCommentArrayList.clear();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            PostComment test = new PostComment(jsonObject, false);
                            postCommentArrayList.add(test);
                        }


                        if (postCommentArrayList.get(0)==null){


                        }else{ drawCart(postCommentArrayList);}

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

            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "get_comment", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendComment(String user_id, String posts_id, String comment){
        JSONObject params = new JSONObject();
        ArrayList<PostComment> postCommentArrayList = new ArrayList<>();

        try {
            params.put("user_id", owner_user_id);
            params.put("post_id", posts_id);
            params.put("comment", comment);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);
                    comment_text.getText().clear();
                   // updateCart(postCommentArrayList);
                    getComment(posts_id);
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf(TAG, "onErrorResponse : " + error);
                    Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
                }
            };

            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "send_comment", params, listener, errorListener);
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


    public void drawCart(ArrayList<PostComment> list){

        PostCommentAdapter postCommentAdapter = new PostCommentAdapter(getApplicationContext(), PostComment.getData(list));
        comment_recy.setAdapter(postCommentAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        comment_recy.setLayoutManager(linearLayoutManager);


    }

}
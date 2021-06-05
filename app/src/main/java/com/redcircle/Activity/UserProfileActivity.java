package com.redcircle.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.tabs.TabLayout;
import com.redcircle.Adapter.FragmentLayoutAdapter;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.redcircle.Util.StaticFields.BASE_URL;

public class UserProfileActivity extends AppCompatActivity {
    private String user_names,user_usernames,user_image,user_id,my_user_id;
    private TextView user_name,user_username,followers_count,folllowing_count,like_count,followtext,unfollowtext,lock_text;
    private ImageButton follow,unfollow,back_view;
    private ImageView lock_image;
    private String TAG="UserInformationAct";
    private boolean user_inf=false;
    private FragmentLayoutAdapter adapter;
    TabLayout tabLayout;
    ViewPager viewPager;
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
        setContentView(R.layout.activity_user_profile);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        my_user_id = preferences.getString("user_id", "Error");


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

        tabLayout=(TabLayout) findViewById(R.id.tabLayout);
        viewPager=(ViewPager) findViewById(R.id.viewPager);
        lock_image=(ImageView) findViewById(R.id.lock_image);
        lock_text=(TextView) findViewById(R.id.lock_text);



        tabLayout.addTab(tabLayout.newTab().setText("Dinlediklerim"));
        tabLayout.addTab(tabLayout.newTab().setText("Gönderilerim"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new FragmentLayoutAdapter(this, this.getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        if(user_id!=null){

            get_user_follow_detail(my_user_id,user_id);

            user_name = (TextView) findViewById(R.id.user_name);
            user_username = (TextView) findViewById(R.id.user_username);
            followtext = (TextView) findViewById(R.id.followtext);
            unfollowtext = (TextView) findViewById(R.id.unfollowtext);
            followers_count = (TextView) findViewById(R.id.followers_count);
            folllowing_count = (TextView) findViewById(R.id.folllowing_count);
            like_count = (TextView) findViewById(R.id.like_count);
            follow = (ImageButton) findViewById(R.id.follow);
            unfollow = (ImageButton) findViewById(R.id.unfollow);

            user_name.setText(user_names);
            user_username.setText(user_usernames);

            ImageView image = (ImageView) findViewById(R.id.content_image_iv);

            Picasso.get().load("https://spotify.krakersoft.com/upload_user_pic/"+user_image).into(image);


        }else{
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            MyApplication.get().getRequestQueue().getCache().clear();
        }

        back_view = (ImageButton) findViewById(R.id.back_views);

        back_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_follow(my_user_id,user_id);
            }
        });
        unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_unfollow(my_user_id,user_id);
            }
        });

    }

    public void get_user_follow_detail(String id_one, String id_two){

        JSONObject params = new JSONObject();
        user_inf=true;

        try {
            params.put("my_user_id", id_one);
            params.put("user_id", id_two);


            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);

                            String profile_lock = jsonObject.getString("profile_lock");
                            String count_of_followers = jsonObject.getString("count_of_followers");
                            String count_of_following = jsonObject.getString("count_of_following");
                            String count_of_like = jsonObject.getString("count_of_like");
                            int isfollow = jsonObject.getInt("isfollow");
                            if(isfollow == 0 ){
                                follow.setVisibility(View.VISIBLE);
                                followtext.setVisibility(View.VISIBLE);
                            }else{
                                unfollow.setVisibility(View.VISIBLE);
                                unfollowtext.setVisibility(View.VISIBLE);
                            }
                            followers_count.setText(count_of_followers);
                            folllowing_count.setText(count_of_following);
                            like_count.setText(count_of_like);

                            if(profile_lock.equals("1") && isfollow ==0 ){
                                tabLayout.setVisibility(View.INVISIBLE);
                                viewPager.setVisibility(View.INVISIBLE);
                                lock_image.setVisibility(View.VISIBLE);
                                lock_text.setVisibility(View.VISIBLE);
                            }else{
                                tabLayout.setVisibility(View.VISIBLE);
                                viewPager.setVisibility(View.VISIBLE);
                                lock_image.setVisibility(View.INVISIBLE);
                                lock_text.setVisibility(View.INVISIBLE);
                            }

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
                    Toast.makeText(UserProfileActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                }
            };
            com.redcircle.Request.AqJSONObjectRequest aqJSONObjectRequest = new com.redcircle.Request.AqJSONObjectRequest(TAG, BASE_URL + "user_information", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(UserProfileActivity.this, "Hata", Toast.LENGTH_SHORT).show();
        }


    }

    public void send_follow(String id_one, String id_two){

        follow.setVisibility(View.INVISIBLE);
        unfollow.setVisibility(View.VISIBLE);
        followtext.setVisibility(View.INVISIBLE);
        unfollowtext.setVisibility(View.VISIBLE);

        JSONObject params = new JSONObject();

        try {
            params.put("my_user_id", id_one);
            params.put("user_id", id_two);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Log.wtf(TAG, "onResponse : " + response);
                    FancyToast.makeText(UserProfileActivity.this, "Takip Edildi", FancyToast.LENGTH_LONG, FancyToast.CONFUSING, R.drawable.spls, false).show();
                    MyApplication.get().getRequestQueue().getCache().clear();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf(TAG, "onErrorResponse : " + error);
                    FancyToast.makeText(UserProfileActivity.this, "Takip Edilemedi", FancyToast.LENGTH_LONG, FancyToast.ERROR, R.drawable.spls, false).show();
                    Toast.makeText(UserProfileActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                }
            };

            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "follow_user", params, listener, errorListener);
            aqJSONObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            FancyToast.makeText(UserProfileActivity.this, "Takip Edilemedi", FancyToast.LENGTH_LONG, FancyToast.ERROR, R.drawable.spls, false).show();
        }

    }
    public void send_unfollow(String id_one, String id_two){

        unfollow.setVisibility(View.INVISIBLE);
        follow.setVisibility(View.VISIBLE);
        followtext.setVisibility(View.VISIBLE);
        unfollowtext.setVisibility(View.INVISIBLE);

        JSONObject params = new JSONObject();

        try {
            params.put("my_user_id", id_one);
            params.put("user_id", id_two);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Log.wtf(TAG, "onResponse : " + response);
                    FancyToast.makeText(UserProfileActivity.this, "Takipten Çıkarıldı", FancyToast.LENGTH_LONG, FancyToast.CONFUSING, R.drawable.spls, false).show();
                    MyApplication.get().getRequestQueue().getCache().clear();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf(TAG, "onErrorResponse : " + error);
                    FancyToast.makeText(UserProfileActivity.this, "Takipten Çıkarılamadı", FancyToast.LENGTH_LONG, FancyToast.ERROR, R.drawable.spls, false).show();
                    Toast.makeText(UserProfileActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                }
            };

            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "unfollow_user", params, listener, errorListener);
            aqJSONObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            FancyToast.makeText(UserProfileActivity.this, "Takipten Çıkarılamadı", FancyToast.LENGTH_LONG, FancyToast.ERROR, R.drawable.spls, false).show();
        }

    }

}
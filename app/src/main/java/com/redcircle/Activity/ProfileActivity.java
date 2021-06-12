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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;
import com.redcircle.Adapter.FragmentLayoutAdapter;
import com.redcircle.R;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.redcircle.Util.StaticFields.BASE_URL;

public class ProfileActivity extends AppCompatActivity {
    private String TAG="ProfileAct";
    private String user_id;
    private TextView my_name,my_username,user_bio,followers_count,folllowing_count,like_count;
    private ImageButton setting_btn,back_views;
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
        setContentView(R.layout.activity_profile);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id = preferences.getString("user_id", "Error");

        my_name=(TextView) findViewById(R.id.user_name);
        my_username=(TextView) findViewById(R.id.user_username);
        user_bio=(TextView) findViewById(R.id.user_bio);
        followers_count=(TextView) findViewById(R.id.followers_count);
        folllowing_count=(TextView) findViewById(R.id.folllowing_count);
        like_count=(TextView) findViewById(R.id.like_count);

        setting_btn = (ImageButton) findViewById(R.id.setting_button);
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(i);
                MyApplication.get().getRequestQueue().getCache().clear();

            }
        });
        back_views = (ImageButton) findViewById(R.id.back_views);
        back_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getMyProfile(user_id);


        tabLayout=(TabLayout) findViewById(R.id.tabLayout);
        viewPager=(ViewPager) findViewById(R.id.viewPager);

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
    }
    public void getMyProfile(String id_one){

        JSONObject params = new JSONObject();
        try {
            params.put("user_id", id_one);


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
                            String display_name = jsonObject.getString("display_name");
                            String username = jsonObject.getString("username");
                            String images = jsonObject.getString("images");
                            String bio = jsonObject.getString("bio");
                            String profile_lock = jsonObject.getString("profile_lock");
                            String count_of_followers = jsonObject.getString("count_of_followers");
                            String count_of_following = jsonObject.getString("count_of_following");
                            String count_of_like = jsonObject.getString("count_of_like");

                            ImageView image = (ImageView) findViewById(R.id.content_image_iv);
                            Picasso.get().load("https://spotify.krakersoft.com/upload_user_pic/"+images).into(image);

                            my_name.setText(display_name);
                            my_username.setText(username);
                            user_bio.setText(bio);
                            followers_count.setText(count_of_followers);
                            folllowing_count.setText(count_of_following);
                            like_count.setText(count_of_like);
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
                    Toast.makeText(ProfileActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                }
            };
            com.redcircle.Request.AqJSONObjectRequest aqJSONObjectRequest = new com.redcircle.Request.AqJSONObjectRequest(TAG, BASE_URL + "my_information", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(ProfileActivity.this, "Hata", Toast.LENGTH_SHORT).show();
        }


    }
}
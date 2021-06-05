package com.redcircle.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.redcircle.Adapter.NotificationAdapter;
import com.redcircle.Pojo.FollowNotification;
import com.redcircle.Pojo.LikeNotification;
import com.redcircle.Pojo.Match;
import com.redcircle.Pojo.MatchNotification;
import com.redcircle.Pojo.NotificationType;
import com.redcircle.Pojo.Notifications;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;

import static android.content.ContentValues.TAG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.redcircle.Util.StaticFields.BASE_URL;

public class NotificationActivity extends AppCompatActivity {


    private String user_id;
    private Boolean notifs=false;
    private RecyclerView recyclerView;
    private ImageButton back_views;
    private ArrayList<Notifications> notificationsArrayList = new ArrayList<>();

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
        setContentView(R.layout.activity_notification);
        back_views = (ImageButton) findViewById(R.id.back_views);
        back_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id = preferences.getString("user_id", "Error");


        recyclerView = (RecyclerView) findViewById(R.id.recylerview);

        getNotification(user_id);
    }
    private void getNotification(String id){

        JSONObject params = new JSONObject();
        notifs=true;
        try {

            params.put("user_id", id);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);
                    notificationsArrayList.clear();
                    JSONArray jsonArray = null;
                    try {
                        //jsonArray = response.getJSONArray("data");
                        JSONObject data = response.getJSONObject("data");
                        JSONObject jsonObject;


                        if( data.getJSONArray("follows").length()>0){
                            jsonArray = data.getJSONArray("follows");
                             for (int i = 0; i < jsonArray.length(); i++) {
                                 jsonObject = jsonArray.getJSONObject(i);
                                 Notifications testFollow = new Notifications(NotificationType.FOLLOW,jsonObject.getString("sender_user_id"),jsonObject.getString("message"),
                                         jsonObject.getString("amIFollow"),jsonObject.getString("follow_status"),jsonObject.getString("sender_images"),
                                         jsonObject.getString("like_time"));
                                 notificationsArrayList.add(testFollow);
                             }

                         }

                        if(data.getJSONArray("likes").length()>0){
                            jsonArray = data.getJSONArray("likes");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                Notifications testLike = new Notifications(NotificationType.LIKE,jsonObject.getString("sender_user_id"),jsonObject.getString("message"),jsonObject.getString("sender_images"),
                                        jsonObject.getString("like_time"),jsonObject.getString("post_id"));
                                notificationsArrayList.add(testLike);
                            }
                        }
                        if(data.getJSONArray("matches").length()>0){
                            jsonArray = data.getJSONArray("matches");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                Notifications testMatch = new Notifications(NotificationType.MATCH,jsonObject.getString("sender_user_id"),jsonObject.getString("message"),jsonObject.getString("sender_images"),
                                        jsonObject.getString("like_time"));
                                notificationsArrayList.add(testMatch);
                            }
                        }
                        drawCartFollow();
                      // drawCart();

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
            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "my_notification", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
        }


    }

   public void drawCartFollow(){

        NotificationAdapter notificationAdapter = new NotificationAdapter(getApplicationContext(),notificationsArrayList);
        recyclerView.setAdapter(notificationAdapter);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


    }
}
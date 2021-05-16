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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.redcircle.Adapter.ChatListAdapter;
import com.redcircle.Adapter.PreviewSongUserAdapter;
import com.redcircle.Pojo.ChatList;
import com.redcircle.Pojo.UserPreviewSong;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.redcircle.Util.StaticFields.BASE_URL;

public class ChatListActivity extends AppCompatActivity {

    private String my_user_id;
    private RecyclerView recyclerView_chat;
    private ImageButton back_views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide status bar
        try
        {
            this.getSupportActionBar().hide();// hide status bar
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_chat_list);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        my_user_id = preferences.getString("user_id", "");

        recyclerView_chat  = (RecyclerView) findViewById(R.id.chat_list_recy);
        back_views  = (ImageButton) findViewById(R.id.back_views);

        back_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        get_chat_list(my_user_id);
    }
    private void get_chat_list(String id){
        ArrayList<ChatList> chatListArrayList = new ArrayList<>();
        JSONObject params = new JSONObject();
        try {

            params.put("user_id", id);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);
                    chatListArrayList.clear();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            ChatList test = new ChatList(jsonObject, false);
                            chatListArrayList.add(test);
                        }


                        if (chatListArrayList.get(0)==null){


                        }else{ drawCart(chatListArrayList);}

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

            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "chat_list", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
        }


    }

    public void drawCart(ArrayList<ChatList> list){

        ChatListAdapter chatListAdapter = new ChatListAdapter(getApplicationContext(), ChatList.getData(list));
        recyclerView_chat.setAdapter(chatListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_chat.setLayoutManager(linearLayoutManager);


    }
}
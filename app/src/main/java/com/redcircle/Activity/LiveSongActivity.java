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

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.redcircle.Adapter.LiveSongUserAdapter;
import com.redcircle.Adapter.MatchAdapter;
import com.redcircle.Pojo.LiveSongUser;
import com.redcircle.Pojo.Match;
import com.redcircle.Pojo.Posts;
import com.redcircle.R;
import com.redcircle.Util.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LiveSongActivity extends AppCompatActivity {

    private String user_id,song_uri;
    private ImageButton back_views;
    private Socket socket;
    private String TAG ="LiveSongAct";
    private RecyclerView live_recycle;
    private ArrayList<LiveSongUser> liveSongUserArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide status bar

        MyApplication app = (MyApplication) getApplicationContext();
        socket = app.getSocket();
        socket.on("live_listen", live_listen);
        socket.connect();

        View bView = getWindow().getDecorView();// hide hardware buttons
        bView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);// hide hardware buttons
        try
        {
            this.getSupportActionBar().hide();// hide status bar
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_live_song);

        live_recycle = (RecyclerView) findViewById(R.id.live_recycle);

        back_views = (ImageButton) findViewById(R.id.back_views);
        back_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                socket.off("liver");
                socket.off("live_listen", live_listen);
                MyApplication.get().getRequestQueue().getCache().clear();
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id = preferences.getString("user_id", "Error");

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            song_uri= null;
        } else {
            song_uri= extras.getString("song_uri");
        }
        socket.emit("liver", song_uri,user_id);

    }

    private Emitter.Listener live_listen = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray jsonArray = null;
                    try {
                        JSONObject obj1 = new JSONObject(args[0].toString());
                        jsonArray = obj1.getJSONArray("data");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            LiveSongUser test = new LiveSongUser(jsonObject, false);
                            liveSongUserArrayList.add(test);
                        }
                        if (liveSongUserArrayList.size()==0){

                        }
                        else{ drawCart(liveSongUserArrayList);}

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    public void drawCart(ArrayList<LiveSongUser>list){
        if (getApplicationContext()!=null) {
            LiveSongUserAdapter liveSongUserAdapter = new LiveSongUserAdapter(this, LiveSongUser.getData(list));
            live_recycle.setAdapter(liveSongUserAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            live_recycle.setLayoutManager(linearLayoutManager);
            Log.wtf("notification", String.valueOf(list));
        }
    }

}
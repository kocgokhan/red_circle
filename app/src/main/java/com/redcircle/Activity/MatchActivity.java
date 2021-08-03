package com.redcircle.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.redcircle.Adapter.MatchAdapter;
import com.redcircle.Pojo.ConnectListVj;
import com.redcircle.Pojo.Match;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.redcircle.Util.StaticFields.BASE_URL;

public class MatchActivity extends AppCompatActivity {

    private String user_id,song_uri;
    private boolean match = false;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerView;
    private ImageView no_post_image;
    private TextView no_post_text;
    private ImageButton back_views;
    private Socket socket;
    private ArrayList<Match> matchArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide status bar
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id = preferences.getString("user_id", "Error");

        MyApplication app = (MyApplication) getApplicationContext();
        socket = app.getSocket();
        socket.on("songForMatch", songForMatch);
        socket.on("have_match", have_match);
        socket.connect();

        View bView = getWindow().getDecorView();// hide hardware buttons
        bView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);// hide hardware buttons
        try
        {
            this.getSupportActionBar().hide();// hide status bar
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_match);

        back_views = (ImageButton) findViewById(R.id.back_views);
        back_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                socket.off("matcher");
                socket.off("songForMatch", songForMatch);
                socket.off("have_match", have_match);
                MyApplication.get().getRequestQueue().getCache().clear();
            }
        });
        recyclerView =(RecyclerView) findViewById(R.id.match_recycle);
    }

    private Emitter.Listener songForMatch = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.wtf(TAG,String.valueOf(args[0]));
                    socket.emit("matcher",String.valueOf(args[0]),String.valueOf(args[1]));
                }
            });
        }
    };
    private Emitter.Listener have_match = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    matchArrayList.clear();
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
                            Match test = new Match(jsonObject, false);

                            if(user_id!= test.getMatch_user_is()){
                                matchArrayList.add(test);
                            }

                        }
                        if (matchArrayList.size()==0) {

                        } else {
                            drawCart(matchArrayList);
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

    public void drawCart(ArrayList<Match>list){
        if (getApplicationContext()!=null) {
            MatchAdapter matchAdapter = new MatchAdapter(this, Match.getData(list));
            recyclerView.setAdapter(matchAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            Log.wtf("notification", String.valueOf(list));
        }
    }
}
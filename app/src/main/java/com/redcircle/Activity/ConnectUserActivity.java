package com.redcircle.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.redcircle.R;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;

public class ConnectUserActivity extends AppCompatActivity {
    private Socket socket;
    {
        try {
            socket = IO.socket("https://www.spotisocket.krakersoft.com:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


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

        ImageButton back_btn= (ImageButton) findViewById(R.id.back_view);
        ImageView imageView_photo = (ImageView) findViewById(R.id.imageView_photo);
        ImageView song_photo = (ImageView) findViewById(R.id.song_photo);
        TextView user_name = (TextView) findViewById(R.id.user_name);
        TextView artist = (TextView) findViewById(R.id.artist);

        TextView myTextView = findViewById(R.id.playing_songs);

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


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

                        myTextView.setText(separated[0]);
                        artist.setText(separated[1]);


                        Picasso.get().load(String.valueOf(Html.fromHtml(separated[2]))).into(song_photo);

                        //Log.wtf("current_song",separated[0]);
                    }
                });
            }
        });
    }
}
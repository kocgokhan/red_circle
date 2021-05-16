package com.redcircle.Activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.redcircle.Adapter.ChatActAdapter;
import com.redcircle.Pojo.Mesaj;
import com.redcircle.R;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceChats;
    private TextView chat_user_name,chat_user_typing;
    private EditText editText;
    private ImageButton buttonSend,back_view;
    private ListView listView;
    private String roomName, oneSignalId = null, webSignalID = null;
    private ImageView imageView_photo;
    private String user_names,user_image,chatId,userId;
    private boolean mTyping = false;
    private Socket socket;
    {
        try {
            socket = IO.socket("https://www.spotisocket.krakersoft.com:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
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
        setContentView(R.layout.activity_chat);

        chat_user_name = findViewById(R.id.chat_user_name);
        chat_user_typing = findViewById(R.id.chat_user_typing);
        RelativeLayout linlay = findViewById(R.id.linlay);
        imageView_photo = findViewById(R.id.imageView_photo);
        editText = findViewById(R.id.editText4);
        buttonSend = findViewById(R.id.imageButton);
        back_view = findViewById(R.id.back_views);
        listView = findViewById(R.id.listview);
        listView.setDivider(null);
        buttonSend.setOnClickListener(this);
        linlay.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            user_names= null;
            oneSignalId= null;
            user_image= null;
        } else {
            user_names= extras.getString("user_name");
            oneSignalId= extras.getString("onesignal_id");
            user_image= extras.getString("user_image");
            chatId= extras.getString("chatId");
        }
        chat_user_name.setText(user_names);

        Picasso.get().load(String.valueOf(Html.fromHtml(user_image))).into(imageView_photo);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = preferences.getString("user_id", "Error");




        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceChats = firebaseDatabase.getReference("Chats");

        final ArrayList<Mesaj> mesajList = new ArrayList<>();
        final ChatActAdapter adapter = new ChatActAdapter(this, mesajList, firebaseUser);

        //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:dd");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)");
        final String zaman = sdf.format(new Date());

        if (Integer.valueOf(chatId) < Integer.valueOf(userId))
            roomName = chatId + "-" + userId;
        else
            roomName = userId + "-" + chatId;

        //databaseReferenceChats.getRef().child(roomName).child("updateTime").setValue(zaman);

        databaseReferenceChats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mesajList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    if (ds.getKey().equals(roomName))
                        for (DataSnapshot ds2 : ds.getChildren())
                            if (!ds2.getKey().equals("updateTime"))
                                mesajList.add(ds2.getValue(Mesaj.class));

                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        back_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButton:
                String gonderen = firebaseUser.getEmail();
                final String mesaj = editText.getText().toString();
                //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:dd");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)");
                final String zaman = sdf.format(new Date());

                databaseReferenceChats.getRef().child(roomName).push().setValue(new Mesaj(gonderen, mesaj, userId + "", zaman, user_names));
                editText.setText("");

                // 'small_icon':'"+R.drawable.ic_stat_onesignal_default+"',
                // ResourcesCompat.getDrawable(getResources(), R.drawable.your_drawable, null)
                if (oneSignalId != null)
                    try {
                        OneSignal.postNotification(new JSONObject("{'contents': {'en':" + mesaj + "}, 'include_player_ids': ['" + oneSignalId + "'],'data':{'chatId':" + userId + "}}"), null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}

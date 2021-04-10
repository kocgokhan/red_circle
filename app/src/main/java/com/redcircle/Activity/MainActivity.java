package com.redcircle.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.redcircle.Fragment.DashboardFragment;
import com.redcircle.Fragment.HomeFragment;
import com.redcircle.Fragment.NotificationFragment;
import com.redcircle.Fragment.PostFragment;
import com.redcircle.Fragment.ProfileFragment;
import com.redcircle.R;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {


    private Socket socket;
    {
        try {
            socket = IO.socket("https://www.spotisocket.krakersoft.com:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    ImageButton post_btn;
    String user_id;
    private String TAG="MainAct";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    post_btn.setImageResource(R.mipmap.post_icon);
                    fragment = new HomeFragment();
                    break;
                case R.id.navigation_notification:
                    post_btn.setImageResource(R.mipmap.post_icon);
                    fragment = new NotificationFragment();
                    break;
                case R.id.navigation_dashboard:
                    post_btn.setImageResource(R.mipmap.post_icon);
                    fragment = new DashboardFragment();
                    break;
                case R.id.navigation_profile:
                    post_btn.setImageResource(R.mipmap.post_icon);
                    fragment = new ProfileFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    public boolean loadPostFragment(){
        Fragment fragment = new PostFragment();
        post_btn.setImageResource(R.mipmap.complete_btn);
        return loadFragment(fragment);
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
        setContentView(R.layout.activity_main);

        loadFragment(new HomeFragment());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setItemIconTintList(null);

        post_btn = findViewById(R.id.post_button);
        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPostFragment();
            }
        });




        TextView myTextView = findViewById(R.id.playing_song);
        myTextView.setSingleLine(true);
        myTextView.setMarqueeRepeatLimit(-1);
        myTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        myTextView.setSelected(true);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id = preferences.getString("user_id", "Error");

        socket.on("connect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        socket.emit("register", user_id);
                    }
                });
            }
        });

        socket.on("event", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        Log.wtf(TAG, String.valueOf(args));
                    }
                });
            }
        });



    }
    public static void setWindowFlag(MainActivity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
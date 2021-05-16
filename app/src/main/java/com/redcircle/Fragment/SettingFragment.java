package com.redcircle.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.firebase.ui.auth.AuthUI;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.redcircle.Activity.LoginActivity;
import com.redcircle.R;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;

public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String my_account_name,images,user_id;
    private TextView my_name;
    private ImageButton setting_btn;
    private ImageView logout_btn;

    public SettingFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private Socket socket;
    {
        try {
            socket = IO.socket("https://www.spotisocket.krakersoft.com:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        socket.connect();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        user_id = preferences.getString("user_id", "Error");
        my_account_name = preferences.getString("name", "Error");
        images = preferences.getString("images", "Error");

        my_name=(TextView) view.findViewById(R.id.profile_name);
        my_name=(TextView) view.findViewById(R.id.profile_name);
        logout_btn=(ImageView) view.findViewById(R.id.logout_button);

        my_name.setText(my_account_name);

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("loginResponse");
                editor.remove("user_id");
                editor.remove("name");
                editor.remove("images");
                editor.remove("email");
                editor.remove("username");
                editor.remove("bio");
                editor.remove("count_of_following");
                editor.remove("count_of_followers");
                editor.remove("count_of_like");
                editor.apply();
                startActivity(new Intent(getContext(), LoginActivity.class));

                socket.disconnect();
                socket.close();
                AuthUI.getInstance().signOut(getContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Çıkış yaptınız", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ImageView image = (ImageView) view.findViewById(R.id.set_profile_image);

        Picasso.get().load(String.valueOf(Html.fromHtml(images))).into(image);



        return view;
    }
}


package com.redcircle.Fragment;

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

import androidx.fragment.app.Fragment;

import com.redcircle.R;
import com.squareup.picasso.Picasso;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        user_id = preferences.getString("user_id", "Error");
        my_account_name = preferences.getString("name", "Error");
        images = preferences.getString("images", "Error");

        my_name=(TextView) view.findViewById(R.id.profile_name);

        my_name.setText(my_account_name);


        ImageView image = (ImageView) view.findViewById(R.id.profile_image);

        Picasso.get().load(String.valueOf(Html.fromHtml(images))).into(image);



        return view;
    }
}


package com.redcircle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.redcircle.Activity.MatchDetailActivity;
import com.redcircle.Activity.UserProfileActivity;
import com.redcircle.Pojo.LiveSongUser;
import com.redcircle.Pojo.Match;
import com.redcircle.R;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LiveSongUserAdapter extends RecyclerView.Adapter<LiveSongUserAdapter.MyViewHolder> {

    private ArrayList<LiveSongUser> mProductList;
    private LayoutInflater inflater;
    private String TAG = "SongAdp";
    private ImagePopup imagePopup;

    public LiveSongUserAdapter(Context context, ArrayList<LiveSongUser> products) {
        inflater = LayoutInflater.from(context);
        imagePopup = new ImagePopup(context);
        this.mProductList = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_live_song_user, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        LiveSongUser selectedProduct = mProductList.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        try {
            int size = mProductList.size();
            return size;
        } catch(NullPointerException ex) {
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView set_username, set_user_name;
        ImageView productImage;
        ConstraintLayout card;
        ImageButton view_btn;
        String user_id;

        public MyViewHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.match_userphoto);
            set_user_name = (TextView) itemView.findViewById(R.id.name);
            set_username = (TextView) itemView.findViewById(R.id.username);
            card = (ConstraintLayout) itemView.findViewById(R.id.cardSong);

            view_btn = (ImageButton) itemView.findViewById(R.id.view_btn);

            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    go_profile(v.getContext());
                }
            });
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
            user_id = preferences.getString("user_id", "Error");
            view_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    go_profile(view.getContext());
                }
            });

        }

        public void setData(LiveSongUser selectedProduct, int position) {
            String profile_photo = "https://spotify.krakersoft.com/upload_user_pic/"+selectedProduct.getImages();

            this.set_user_name.setText(selectedProduct.getDisplay_name());
            this.set_username.setText(selectedProduct.getUsername());

            Picasso.get().load(String.valueOf(Html.fromHtml(profile_photo))).into(this.productImage);
        }
        public void go_profile(Context context){

            Intent i = new Intent(context.getApplicationContext(), UserProfileActivity.class);
            String strName = null;
            i.putExtra("user_name", mProductList.get(getAdapterPosition()).getDisplay_name());
            i.putExtra("user_image", mProductList.get(getAdapterPosition()).getImages());
            i.putExtra("user_username", mProductList.get(getAdapterPosition()).getUsername());
            i.putExtra("user_user_id", mProductList.get(getAdapterPosition()).getUser_id());
            context.startActivity(i);
            MyApplication.get().getRequestQueue().getCache().clear();

        }



    }



}
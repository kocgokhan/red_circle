package com.redcircle.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.redcircle.Fragment.PostFragment;
import com.redcircle.Pojo.Posts;
import com.redcircle.Pojo.Songs;
import com.redcircle.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    private ArrayList<Posts> mProductList;
    private LayoutInflater inflater;
    private String TAG = "SongAdp";

    public PostsAdapter(Context context, ArrayList<Posts> products) {
        inflater = LayoutInflater.from(context);
        this.mProductList = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.post_list_cycle, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        Posts selectedProduct = mProductList.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView productName, set_user_name, set_user_username, productArtist,set_post_text;
        ImageView productImage,set_profile_image,like_post,set_post_image;
        ConstraintLayout card;

        public MyViewHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            set_profile_image = (ImageView) itemView.findViewById(R.id.set_profile_image);
            set_post_image = (ImageView) itemView.findViewById(R.id.set_post_image);
            like_post = (ImageView) itemView.findViewById(R.id.like_post);
            set_user_name = (TextView) itemView.findViewById(R.id.set_user_name);
            set_post_text = (TextView) itemView.findViewById(R.id.set_post_text);
            set_user_username = (TextView) itemView.findViewById(R.id.set_user_username);
            card = (ConstraintLayout) itemView.findViewById(R.id.cardSong);

            card.setOnClickListener(this);
        }

        public void setData(Posts selectedProduct, int position) {

            String imgResource = "https://spotify.krakersoft.com/upload_post_pic/" +  selectedProduct.getPost_img_url();
            String song_image =   selectedProduct.getSong_image();
            String profile_photo = selectedProduct.getPost_user_image();

            this.set_user_name.setText(selectedProduct.getPost_user_name());
            this.set_user_username.setText(selectedProduct.getPost_user_username());
            this.set_post_text.setText(selectedProduct.getPost_text());

            productImage.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            productImage.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            productImage.setAdjustViewBounds(false);
            productImage.setScaleType(ImageView.ScaleType.FIT_XY);


            Picasso.get().load(String.valueOf(Html.fromHtml(profile_photo))).into(this.set_profile_image);
            Picasso.get().load(song_image).into(this.productImage);
            Picasso.get().load(imgResource).into(this.set_post_image);
        }

        @Override
        public void onClick(View v) {

        }



    }

}
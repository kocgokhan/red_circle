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

        TextView productName, productDescription, productArtist;
        ImageView productImage;
        ConstraintLayout card;

        public MyViewHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            card = (ConstraintLayout) itemView.findViewById(R.id.cardSong);

            card.setOnClickListener(this);
        }

        public void setData(Posts selectedProduct, int position) {

            String imgResource = "https://spotify.krakersoft.com/upload_post_pic/" +  selectedProduct.getPost_img_url();
            String song_image =   selectedProduct.getImages();

            Picasso.get().load(song_image).into(this.productImage);
        }

        @Override
        public void onClick(View v) {

        }



    }

}
package com.redcircle.Adapter;

import android.content.Context;
import android.content.Intent;
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

import com.redcircle.Activity.ChatActivity;
import com.redcircle.Activity.PostActivity;
import com.redcircle.Pojo.Songs;
import com.redcircle.R;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {

    private ArrayList<Songs> mProductList;
    private LayoutInflater inflater;
    private String TAG = "SongAdp";

    public SongAdapter(Context context, ArrayList<Songs> products) {
        inflater = LayoutInflater.from(context);
        this.mProductList = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_postsearch_cycle, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        Songs selectedProduct = mProductList.get(position);
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
            productName = (TextView) itemView.findViewById(R.id.productName);
            productArtist = (TextView) itemView.findViewById(R.id.productArtist);
            productImage = (ImageView) itemView.findViewById(R.id.prev_play);
            card = (ConstraintLayout) itemView.findViewById(R.id.cardSong);

            card.setOnClickListener(this);
        }

        public void setData(Songs selectedProduct, int position) {
            this.productName.setText(selectedProduct.getName());
            this.productArtist.setText(selectedProduct.getArtists());
            Picasso.get().load(selectedProduct.getImages()).into(this.productImage);
        }

        @Override
        public void onClick(View v) {
            try {
                Intent i = new Intent(v.getContext().getApplicationContext(), PostActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("name", mProductList.get(getAdapterPosition()).getName());
                i.putExtra("artist", mProductList.get(getAdapterPosition()).getArtists());
                i.putExtra("images", String.valueOf(Html.fromHtml(mProductList.get(getAdapterPosition()).getImages())));
                i.putExtra("uri", mProductList.get(getAdapterPosition()).getUri());
                v.getContext().startActivity(i);
            } catch (Exception e) {
                Log.wtf("course_images", e);
            }
        }


    }

}
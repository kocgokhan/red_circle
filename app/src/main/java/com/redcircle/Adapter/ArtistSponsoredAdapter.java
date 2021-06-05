package com.redcircle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.redcircle.Activity.SponsorDetailActivity;
import com.redcircle.Pojo.ArtistSponsored;
import com.redcircle.R;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArtistSponsoredAdapter extends RecyclerView.Adapter<ArtistSponsoredAdapter.MyViewHolder> {

    private ArrayList<ArtistSponsored> mProductList;
    private LayoutInflater inflater;
    private String TAG = "SongAdp";
    private ImagePopup imagePopup;

    public ArtistSponsoredAdapter(Context context, ArrayList<ArtistSponsored> products) {
        inflater = LayoutInflater.from(context);
        imagePopup = new ImagePopup(context);
        this.mProductList = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_artist_sponsored, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        ArtistSponsored selectedProduct = mProductList.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView set_user_name,set_username;
        ImageView productImage;

        // Linear Layout Manager
        LinearLayoutManager HorizontalLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.content_image_iv);
            set_user_name = (TextView) itemView.findViewById(R.id.iv_name);
            set_username = (TextView) itemView.findViewById(R.id.iv_username);

            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), SponsorDetailActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("user_name", mProductList.get(getAdapterPosition()).getName());
                    i.putExtra("user_image", mProductList.get(getAdapterPosition()).getImages());
                    i.putExtra("user_username", mProductList.get(getAdapterPosition()).getUsername());
                    i.putExtra("content_name", mProductList.get(getAdapterPosition()).getContent_name());
                    i.putExtra("content_uri", mProductList.get(getAdapterPosition()).getContent_uri());
                    i.putExtra("content_image", mProductList.get(getAdapterPosition()).getContent_images());
                    i.putExtra("user_user_id", mProductList.get(getAdapterPosition()).getUser_id());
                    view.getContext().startActivity(i);
                    MyApplication.get().getRequestQueue().getCache().clear();
                }
            });
        }

        public void setData(ArtistSponsored selectedProduct, int position) {


            this.set_user_name.setText(selectedProduct.getName());
            this.set_username.setText(selectedProduct.getUsername());


            Picasso.get().load(selectedProduct.getContent_images()).memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE).error(R.mipmap.ic_launcher).memoryPolicy(MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_STORE).into(this.productImage);
            Picasso.get().setLoggingEnabled(false);

        }

    }
}
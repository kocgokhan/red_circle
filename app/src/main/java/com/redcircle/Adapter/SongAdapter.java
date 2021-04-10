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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.redcircle.Fragment.PostFragment;
import com.redcircle.Pojo.Songs;
import com.redcircle.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder>{

        ArrayList<Songs> mProductList;
        LayoutInflater inflater;
        String name,artis,image,uri;
    private String TAG="SongAdp";

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

    TextView productName, productDescription,productArtist;
    ImageView productImage;
    ConstraintLayout card;

    public MyViewHolder(View itemView) {
        super(itemView);
        productName = (TextView) itemView.findViewById(R.id.productName);
        productArtist = (TextView) itemView.findViewById(R.id.productArtist);
        productImage = (ImageView) itemView.findViewById(R.id.productImage);
        card = (ConstraintLayout) itemView.findViewById(R.id.cardSong);

        card.setOnClickListener(this);
    }

    public void setData(Songs selectedProduct, int position) {

        this.productName.setText(selectedProduct.getName());
        this.productArtist.setText(selectedProduct.getArtists());
        Picasso.get().load(selectedProduct.getImages()).into(this.productImage);

        name=selectedProduct.getName();
        artis=selectedProduct.getArtists();
        image=selectedProduct.getImages();
        uri=selectedProduct.getUri();
    }
    @Override
    public void onClick(View v) {

        try {
            cardSong(v.getContext());
        }catch (Exception e){
            Log.wtf("course_images",e);
        }
    }

    private void cardSong(Context context) {

        Fragment fragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("artist", artis);
        bundle.putString("images", String.valueOf(Html.fromHtml(image)));
        bundle.putString("uri", uri);
        fragment.setArguments(bundle);
        FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment);
        ft.commit();

    }

}
}
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
import com.redcircle.Activity.ConnectUserActivity;
import com.redcircle.Pojo.ConnectListVj;
import com.redcircle.R;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VjAdapter extends RecyclerView.Adapter<VjAdapter.MyViewHolder> {

    private ArrayList<ConnectListVj> mProductList;
    private LayoutInflater inflater;
    private String TAG = "SongAdp";
    private ImagePopup imagePopup;

    public VjAdapter(Context context, ArrayList<ConnectListVj> products) {
        inflater = LayoutInflater.from(context);
        imagePopup = new ImagePopup(context);
        this.mProductList = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.vj_list_item, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        ConnectListVj selectedProduct = mProductList.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView set_user_name;
        ImageView productImage;

        // Linear Layout Manager
        LinearLayoutManager HorizontalLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.content_image_iv);
            set_user_name = (TextView) itemView.findViewById(R.id.iv_name);

            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), ConnectUserActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("user_name", mProductList.get(getAdapterPosition()).getDisplay_name());
                    i.putExtra("user_image", mProductList.get(getAdapterPosition()).getUser_image());
                    i.putExtra("user_username", mProductList.get(getAdapterPosition()).getUser_username());
                    i.putExtra("user_user_id", mProductList.get(getAdapterPosition()).getUser_id());
                    view.getContext().startActivity(i);
                    MyApplication.get().getRequestQueue().getCache().clear();
                }
            });
        }

        public void setData(ConnectListVj selectedProduct, int position) {


            this.set_user_name.setText(selectedProduct.getDisplay_name());


            Picasso.get().load("https://spotify.krakersoft.com/upload_user_pic/"+selectedProduct.getUser_image()).memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE).error(R.mipmap.ic_launcher).memoryPolicy(MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_STORE).resize(200,250).into(this.productImage);
            Picasso.get().setLoggingEnabled(false);





        }




    }


}
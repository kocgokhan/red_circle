package com.redcircle.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.redcircle.Pojo.PostComment;
import com.redcircle.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.MyViewHolder> {

    private ArrayList<PostComment> mProductList;
    private LayoutInflater inflater;
    private String TAG = "SongAdp";
    private ImagePopup imagePopup;

    public PostCommentAdapter(Context context, ArrayList<PostComment> products) {
        inflater = LayoutInflater.from(context);
        imagePopup = new ImagePopup(context);
        this.mProductList = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_comment, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        PostComment selectedProduct = mProductList.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name_commenter, comment;
        ImageView user_image;


        public MyViewHolder(View itemView) {
            super(itemView);
            user_image = (ImageView) itemView.findViewById(R.id.song_image_post);
            name_commenter = (TextView) itemView.findViewById(R.id.name_commenter);
            comment = (TextView) itemView.findViewById(R.id.comment);

        }

        public void setData(PostComment selectedProduct, int position) {
            String profile_photo = selectedProduct.getImages();

            this.name_commenter.setText(selectedProduct.getDisplay_name());
            this.comment.setText(selectedProduct.getComment());

            Picasso.get().load(String.valueOf(Html.fromHtml("https://spotify.krakersoft.com/upload_user_pic/"+profile_photo))).into(this.user_image);
        }



    }


}
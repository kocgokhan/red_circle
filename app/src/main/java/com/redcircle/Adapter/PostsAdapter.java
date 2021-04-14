package com.redcircle.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Base64;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;
import com.redcircle.Activity.MainActivity;
import com.redcircle.Fragment.PostFragment;
import com.redcircle.Pojo.Posts;
import com.redcircle.Pojo.Songs;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.redcircle.Util.StaticFields.BASE_URL;

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

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView productName, set_user_name, set_user_username, productArtist,set_post_text,count_like;
        ImageView productImage,set_profile_image,like_post,unlike_post,set_post_image;
        ConstraintLayout card;


        public MyViewHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            set_profile_image = (ImageView) itemView.findViewById(R.id.set_profile_image);
            set_post_image = (ImageView) itemView.findViewById(R.id.set_post_image);
            like_post = (ImageView) itemView.findViewById(R.id.like_post);
            unlike_post = (ImageView) itemView.findViewById(R.id.unlike_post);
            set_user_name = (TextView) itemView.findViewById(R.id.set_user_name);
            set_post_text = (TextView) itemView.findViewById(R.id.set_post_text);
            set_user_username = (TextView) itemView.findViewById(R.id.set_user_username);
            count_like = (TextView) itemView.findViewById(R.id.count_like);
            card = (ConstraintLayout) itemView.findViewById(R.id.cardSong);


            like_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Iliked(v.getContext());
                }
            });
            unlike_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Idontliked(v.getContext());
                }
            });
        }

        public void setData(Posts selectedProduct, int position) {

            String imgResource = "https://spotify.krakersoft.com/upload_post_pic/" +  selectedProduct.getPost_img_url();
            String song_image =   selectedProduct.getSong_image();
            String profile_photo = selectedProduct.getPost_user_image();

            this.set_user_name.setText(selectedProduct.getPost_user_name());
            this.set_user_username.setText(selectedProduct.getPost_user_username());
            this.set_post_text.setText(selectedProduct.getPost_text());
            this.count_like.setText(selectedProduct.getCount_like());

            productImage.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            productImage.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            productImage.setAdjustViewBounds(false);
            productImage.setScaleType(ImageView.ScaleType.FIT_XY);

            if(selectedProduct.getIsLike().endsWith("1")){

                like_post.setVisibility(View.INVISIBLE);
                unlike_post.setVisibility(View.VISIBLE);
            }else{

                like_post.setVisibility(View.VISIBLE);
                unlike_post.setVisibility(View.INVISIBLE);

            }

            Picasso.get().load(String.valueOf(Html.fromHtml(profile_photo))).into(this.set_profile_image);
            Picasso.get().load(song_image).into(this.productImage);
            Picasso.get().load(imgResource).into(this.set_post_image);
        }


        public void  Iliked(Context context){



            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String person_id = preferences.getString("user_id", "");
            String post_id= mProductList.get(getAdapterPosition()).getPost_id();


            like_post.setVisibility(View.INVISIBLE);
            unlike_post.setVisibility(View.VISIBLE);

            JSONObject params = new JSONObject();

            try {
                params.put("user_id", person_id);
                params.put("post_id", post_id);
                Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.wtf(TAG, "onResponse : " + response);


                        MyApplication.get().getRequestQueue().getCache().clear();
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf(TAG, "onErrorResponse : " + error);
                        Toast.makeText(context, "Hata", Toast.LENGTH_SHORT).show();
                    }
                };

                AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "like_post", params, listener, errorListener);
                aqJSONObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
            } catch (JSONException e) {
                Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
                e.printStackTrace();
                Toast.makeText(context, "Hata", Toast.LENGTH_SHORT).show();
            }




        }
        public void  Idontliked(Context context){

            like_post.setVisibility(View.VISIBLE);
            unlike_post.setVisibility(View.INVISIBLE);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String person_id = preferences.getString("user_id", "");
            String post_id= mProductList.get(getAdapterPosition()).getPost_id();
        }


    }


    }
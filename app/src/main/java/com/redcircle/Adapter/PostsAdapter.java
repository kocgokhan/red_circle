package com.redcircle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.redcircle.Activity.PostCommentActivity;
import com.redcircle.Activity.UserProfileActivity;
import com.redcircle.Pojo.Posts;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.redcircle.Util.StaticFields.BASE_URL;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    private ArrayList<Posts> mProductList;
    private LayoutInflater inflater;
    private String TAG = "SongAdp";
    private ImagePopup imagePopup;


    public PostsAdapter(Context context, ArrayList<Posts> products) {
        inflater = LayoutInflater.from(context);
        imagePopup = new ImagePopup(context);
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

        TextView productName, set_user_name, set_user_username,set_post_text,count_like,count_comment,song_name,song_artist;
        ImageView productImage,song_image,set_profile_image,like_post,unlike_post,set_post_image,song_image_v,comment_btn;
        ConstraintLayout card;
        ImageButton play_song_btn;


        public MyViewHolder(View itemView) {
            super(itemView);
            //play_song_btn = (ImageButton) itemView.findViewById(R.id.play_song_btn);
            productImage = (ImageView) itemView.findViewById(R.id.song_image_post);
            song_image_v = (ImageView) itemView.findViewById(R.id.song_image_v);
            set_profile_image = (ImageView) itemView.findViewById(R.id.match_userphoto);
            set_post_image = (ImageView) itemView.findViewById(R.id.back_image);
            like_post = (ImageView) itemView.findViewById(R.id.like_post);
            unlike_post = (ImageView) itemView.findViewById(R.id.unlike_post);
            comment_btn = (ImageView) itemView.findViewById(R.id.comment_btn);
            set_user_name = (TextView) itemView.findViewById(R.id.set_user_name);
            set_post_text = (TextView) itemView.findViewById(R.id.set_post_text);
            set_user_username = (TextView) itemView.findViewById(R.id.set_user_username);
            song_name = (TextView) itemView.findViewById(R.id.song_name);
            song_artist = (TextView) itemView.findViewById(R.id.song_artist);
            count_like = (TextView) itemView.findViewById(R.id.count_like);
            count_comment = (TextView) itemView.findViewById(R.id.count_comment);
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
            comment_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goComment(v.getContext());
                }
            });
            /*play_song_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    play_song(view.getContext());
                }
            });*/

            set_profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    go_profile(view.getContext());
                }
            });


        }

        public void setData(Posts selectedProduct, int position) {

            String imgResource = "https://spotify.krakersoft.com/upload_post_pic/" +  mProductList.get(position).getPost_img_url();
            String song_image =   selectedProduct.getSong_image();
            String profile_photo = "https://spotify.krakersoft.com/upload_user_pic/"+selectedProduct.getPost_user_image();

            this.song_name.setText(selectedProduct.getSong_name());
            this.song_artist.setText(selectedProduct.getSong_artist());
            this.set_user_name.setText(selectedProduct.getPost_user_name());
            this.set_user_username.setText(selectedProduct.getPost_user_username());
            this.set_post_text.setText(selectedProduct.getPost_text());
            this.count_like.setText(selectedProduct.getCount_like());
            this.count_comment.setText(selectedProduct.getCount_comment());

            productImage.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            productImage.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            productImage.setAdjustViewBounds(false);
            productImage.setScaleType(ImageView.ScaleType.FIT_XY);




            if(selectedProduct.getIsLike().equals("1")){

                like_post.setVisibility(View.INVISIBLE);
                unlike_post.setVisibility(View.VISIBLE);
            }else{

                like_post.setVisibility(View.VISIBLE);
                unlike_post.setVisibility(View.INVISIBLE);

            }

            Picasso.get().load(profile_photo).into(this.set_profile_image);
            Picasso.get().load(song_image).into(this.song_image_v);
            Picasso.get().load(song_image).into(this.productImage);
            Picasso.get().load(imgResource).into(this.set_post_image);


            imagePopup.setBackgroundColor(Color.BLACK);
            imagePopup.setFullScreen(true);
            imagePopup.setHideCloseIcon(true);
            imagePopup.setImageOnClickClose(true);

            imagePopup.initiatePopupWithPicasso(imgResource);

            set_post_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /** Initiate Popup view **/
                    imagePopup.viewPopup();
                }
            });
        }

        public void go_profile(Context context){

            Intent i = new Intent(context.getApplicationContext(), UserProfileActivity.class);
            String strName = null;
            i.putExtra("user_name", mProductList.get(getAdapterPosition()).getPost_user_name());
            i.putExtra("user_image", mProductList.get(getAdapterPosition()).getPost_user_image());
            i.putExtra("user_username", mProductList.get(getAdapterPosition()).getPost_user_username());
            i.putExtra("user_user_id", mProductList.get(getAdapterPosition()).getPost_user_id());
            i.putExtra("user_profile_lock", mProductList.get(getAdapterPosition()).getProfile_lock());
            context.startActivity(i);
            MyApplication.get().getRequestQueue().getCache().clear();

        }
        public void goComment(Context context){

            Intent i = new Intent(context.getApplicationContext(), PostCommentActivity.class);
            String strName = null;
            i.putExtra("user_user_id", mProductList.get(getAdapterPosition()).getPost_user_id());
            i.putExtra("post_id", mProductList.get(getAdapterPosition()).getPost_id());
            context.startActivity(i);
            MyApplication.get().getRequestQueue().getCache().clear();

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

                AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "unlike_post", params, listener, errorListener);
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

        public void play_song(Context context){

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String token = preferences.getString("token", "");
            String song_uri= mProductList.get(getAdapterPosition()).getSong_uri();

            JSONObject params = new JSONObject();

            try {
                params.put("token", token);
                params.put("song_uri", song_uri);
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

                AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "play_song", params, listener, errorListener);
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


    }


}
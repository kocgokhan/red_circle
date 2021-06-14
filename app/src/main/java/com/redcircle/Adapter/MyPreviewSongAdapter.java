package com.redcircle.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.redcircle.Pojo.MyPreviewSong;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.redcircle.Util.StaticFields.BASE_URL;

public class MyPreviewSongAdapter extends RecyclerView.Adapter<MyPreviewSongAdapter.MyViewHolder> {

    private ArrayList<MyPreviewSong> mProductList;
    private LayoutInflater inflater;
    private String TAG = "SongAdp";
    private ImagePopup imagePopup;

    public MyPreviewSongAdapter(Context context, ArrayList<MyPreviewSong> products) {
        inflater = LayoutInflater.from(context);
        imagePopup = new ImagePopup(context);
        this.mProductList = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.profile_preview_song, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyPreviewSong selectedProduct = mProductList.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView prev_songname,prev_songartist;
        ImageView productImage;

        // Linear Layout Manager
        LinearLayoutManager HorizontalLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.song_image_post);
            prev_songname = (TextView) itemView.findViewById(R.id.prev_songname);
            prev_songartist = (TextView) itemView.findViewById(R.id.prev_songartist);

            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    play_song(view.getContext());
                }
            });
        }

        public void setData(MyPreviewSong selectedProduct, int position) {

            //String profile_photo = selectedProduct.getUser_image();

            this.prev_songname.setText(selectedProduct.getSong_name());
            this.prev_songartist.setText(selectedProduct.getSong_artist());


            this.productImage.setImageResource(R.mipmap.play);

            //Picasso.get().load(String.valueOf(Html.fromHtml(profile_photo))).into(this.productImage);

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
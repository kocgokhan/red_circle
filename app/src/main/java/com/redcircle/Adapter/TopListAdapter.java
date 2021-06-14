package com.redcircle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.redcircle.Activity.LiveSongActivity;
import com.redcircle.Activity.SongPostActivity;
import com.redcircle.Pojo.TopList;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.redcircle.Util.StaticFields.BASE_URL;

public class TopListAdapter extends RecyclerView.Adapter<TopListAdapter.MyViewHolder> {

    private ArrayList<TopList> mProductList;
    private LayoutInflater inflater;
    private String TAG = "SongAdp";
    private ImagePopup imagePopup;

    public TopListAdapter(Context context, ArrayList<TopList> products) {
        inflater = LayoutInflater.from(context);
        imagePopup = new ImagePopup(context);
        this.mProductList = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_top, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        TopList selectedProduct = mProductList.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView prev_songname,prev_songartist;
        ImageView productImage;
        ImageButton song_post_btn,live_song_btn;
        LinearLayoutManager HorizontalLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.song_image_post);

            prev_songname = (TextView) itemView.findViewById(R.id.prev_songname);
            prev_songartist = (TextView) itemView.findViewById(R.id.prev_songartist);

            live_song_btn = (ImageButton) itemView.findViewById(R.id.live_song_btn);
            song_post_btn = (ImageButton) itemView.findViewById(R.id.album_track_btn);

            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    play_song(view.getContext());
                }
            });
            song_post_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    song_post(view.getContext());
                }
            });
            live_song_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   live_song(view.getContext());
                }
            });
        }

        public void setData(TopList selectedProduct, int position) {

            this.prev_songname.setText(selectedProduct.getSong_name());
            this.prev_songartist.setText(selectedProduct.getSong_artist());
            this.productImage.setImageResource(R.mipmap.play);

        }
        public void song_post(Context context){
            Intent i = new Intent(context, SongPostActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("song_uri", mProductList.get(getAdapterPosition()).getSong_uri());
            context.startActivity(i);
            MyApplication.get().getRequestQueue().getCache().clear();
        }
        public void live_song(Context context){
            Intent i = new Intent(context, LiveSongActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("song_uri", mProductList.get(getAdapterPosition()).getSong_uri());
            context.startActivity(i);
            MyApplication.get().getRequestQueue().getCache().clear();
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
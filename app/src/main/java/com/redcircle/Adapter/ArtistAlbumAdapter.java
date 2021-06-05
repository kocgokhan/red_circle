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
import com.redcircle.Activity.AlbumTrackActivity;
import com.redcircle.Activity.UserProfileActivity;
import com.redcircle.Pojo.ArtistAlbum;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.redcircle.Util.StaticFields.BASE_URL;

public class ArtistAlbumAdapter extends RecyclerView.Adapter<ArtistAlbumAdapter.MyViewHolder> {

    private ArrayList<ArtistAlbum> mProductList;
    private LayoutInflater inflater;
    private String TAG = "SongAdp";
    private ImagePopup imagePopup;

    public ArtistAlbumAdapter(Context context, ArrayList<ArtistAlbum> products) {
        inflater = LayoutInflater.from(context);
        imagePopup = new ImagePopup(context);
        this.mProductList = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_artist_album, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        ArtistAlbum selectedProduct = mProductList.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView prev_songname,prev_songartist;
        LinearLayoutManager HorizontalLayout;
        ImageButton album_track_btn;

        public MyViewHolder(View itemView) {
            super(itemView);
            prev_songname = (TextView) itemView.findViewById(R.id.prev_songname);
            prev_songartist = (TextView) itemView.findViewById(R.id.prev_songartist);
            album_track_btn = (ImageButton) itemView.findViewById(R.id.album_track_btn);

            album_track_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goAlbumTrack(v.getContext());
                }
            });
        }

        public void setData(ArtistAlbum selectedProduct, int position) {
            this.prev_songname.setText(selectedProduct.getAlbum_name());
            this.prev_songartist.setText(selectedProduct.getArtist_name());
        }
        public void goAlbumTrack(Context context){

            Intent i = new Intent(context.getApplicationContext(), AlbumTrackActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("album_name", mProductList.get(getAdapterPosition()).getAlbum_name());
            i.putExtra("album_id", mProductList.get(getAdapterPosition()).getAlbum_id());
            i.putExtra("type", mProductList.get(getAdapterPosition()).getType());
            context.startActivity(i);
            MyApplication.get().getRequestQueue().getCache().clear();

        }
    }

}
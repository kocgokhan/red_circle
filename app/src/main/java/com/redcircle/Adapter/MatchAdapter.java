package com.redcircle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
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
import com.redcircle.Activity.ChatActivity;
import com.redcircle.Activity.MainActivity;
import com.redcircle.Activity.UserProfileActivity;
import com.redcircle.Pojo.Match;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.redcircle.Util.StaticFields.BASE_URL;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MyViewHolder> {

    private ArrayList<Match> mProductList;
    private LayoutInflater inflater;
    private String TAG = "SongAdp";
    private ImagePopup imagePopup;

    public MatchAdapter(Context context, ArrayList<Match> products) {
        inflater = LayoutInflater.from(context);
        imagePopup = new ImagePopup(context);
        this.mProductList = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_mach_cycle, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        Match selectedProduct = mProductList.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView follower_count, set_user_name, following_count,decline_text,accept_text,message_text;
        ImageView productImage;
        ConstraintLayout card;
        ImageButton decline_btn,accept_btn,send_message;


        public MyViewHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.photo_user);
            set_user_name = (TextView) itemView.findViewById(R.id.name);
            following_count = (TextView) itemView.findViewById(R.id.following_count);
            follower_count = (TextView) itemView.findViewById(R.id.follower_count);
            decline_text = (TextView) itemView.findViewById(R.id.decline_text);
            accept_text = (TextView) itemView.findViewById(R.id.accept_text);
            message_text = (TextView) itemView.findViewById(R.id.message_text);
            card = (ConstraintLayout) itemView.findViewById(R.id.cardSong);

            send_message = (ImageButton) itemView.findViewById(R.id.send_message);
            decline_btn = (ImageButton) itemView.findViewById(R.id.decline_btn);
            accept_btn = (ImageButton) itemView.findViewById(R.id.accept_btn);

            send_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   message(view.getContext());
                }
            });

            decline_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            accept_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    accept(view.getContext());
                }
            });

        }

        public void setData(Match selectedProduct, int position) {
            String profile_photo = selectedProduct.getPost_user_image();

            this.set_user_name.setText(selectedProduct.getPost_user_name());
            this.follower_count.setText(selectedProduct.getCount_of_followers());
            this.following_count.setText(selectedProduct.getCount_of_following());

            Picasso.get().load(String.valueOf(Html.fromHtml(profile_photo))).into(this.productImage);

            if(selectedProduct.getStatus().equals("0")){

                send_message.setVisibility(View.INVISIBLE);
                accept_btn.setVisibility(View.VISIBLE);
                decline_btn.setVisibility(View.VISIBLE);

                message_text.setVisibility(View.INVISIBLE);
                accept_btn.setVisibility(View.VISIBLE);
                accept_text.setVisibility(View.VISIBLE);


            }else{

                send_message.setVisibility(View.VISIBLE);
                accept_btn.setVisibility(View.INVISIBLE);
                decline_btn.setVisibility(View.INVISIBLE);

                message_text.setVisibility(View.VISIBLE);
                decline_text.setVisibility(View.INVISIBLE);
                accept_text.setVisibility(View.INVISIBLE);

            }

        }



        public void  accept(Context context){

            send_message.setVisibility(View.VISIBLE);
            accept_btn.setVisibility(View.INVISIBLE);
            decline_btn.setVisibility(View.INVISIBLE);

            message_text.setVisibility(View.VISIBLE);
            decline_text.setVisibility(View.INVISIBLE);
            accept_text.setVisibility(View.INVISIBLE);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String person_id = preferences.getString("user_id", "");
            String match_id= mProductList.get(getAdapterPosition()).getMatch_id();


            JSONObject params = new JSONObject();

            try {
                params.put("user_id", person_id);
                params.put("match_id", match_id);
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

                AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "accept_match", params, listener, errorListener);
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
        public void  decline(Context context){



            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String person_id = preferences.getString("user_id", "");
            String Match_id= mProductList.get(getAdapterPosition()).getMatch_id();

            JSONObject params = new JSONObject();

            try {
                params.put("user_id", person_id);
                params.put("Match_id", Match_id);
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

                AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "unlike_Match", params, listener, errorListener);
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

        public void message(Context context){

            Intent i = new Intent(context.getApplicationContext(), ChatActivity.class);
            String strName = null;
            i.putExtra("user_name", mProductList.get(getAdapterPosition()).getPost_user_name());
            i.putExtra("user_image", mProductList.get(getAdapterPosition()).getPost_user_image());
            i.putExtra("onesignal_id", mProductList.get(getAdapterPosition()).getOneSignal_id());
            i.putExtra("chatId", mProductList.get(getAdapterPosition()).getMatch_user_is());
            context.startActivity(i);
            MyApplication.get().getRequestQueue().getCache().clear();


        }


    }


}
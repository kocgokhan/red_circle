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

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.redcircle.Activity.UserProfileActivity;
import com.redcircle.Pojo.NotificationType;
import com.redcircle.Pojo.Notifications;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.redcircle.Util.StaticFields.BASE_URL;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{

    ArrayList<Notifications> mProductList;
    LayoutInflater inflater;
    private String TAG = "NotifAdp";

    public NotificationAdapter(Context context, ArrayList<Notifications> list) {
        inflater = LayoutInflater.from(context);
        this.mProductList = list;
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notification_card, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Notifications selectedProduct = mProductList.get(position);
        holder.setData(selectedProduct, position);

    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView notif_message, productDescription,notif_date,request_send_text;
        ImageView sender_image,like_song_image,like_post_image;
        ImageButton follow_accept_btn,follow_cancel_btn,request_send,follow_user_btn;
        boolean waiting=false;
        boolean send_follow=false;

        public MyViewHolder(View itemView) {
            super(itemView);
            notif_message = (TextView) itemView.findViewById(R.id.notif_message);
            notif_date = (TextView) itemView.findViewById(R.id.notif_date);
            sender_image = (ImageView) itemView.findViewById(R.id.prev_play);
            like_post_image = (ImageView) itemView.findViewById(R.id.like_post_image);
            like_song_image = (ImageView) itemView.findViewById(R.id.like_song_image);
            follow_accept_btn = (ImageButton) itemView.findViewById(R.id.follow_accept_btn);
            follow_cancel_btn = (ImageButton) itemView.findViewById(R.id.follow_cancel_btn);
            request_send = (ImageButton) itemView.findViewById(R.id.request_send);
            follow_user_btn = (ImageButton) itemView.findViewById(R.id.follow_user);

            follow_accept_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    accept_follow(v.getContext());

                }
            });
            follow_cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel_follow(v.getContext());
                }
            });
            follow_user_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    follow_user(v.getContext());
                }
            });
            sender_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    go_profile(view.getContext());
                }
            });

        }
        public void setData(Notifications selectedProduct, int position) {
            Picasso.get().load("https://spotify.krakersoft.com/upload_user_pic/"+selectedProduct.getSender_images()).into(this.sender_image);


            if(NotificationType.FOLLOW.equals(mProductList.get(position).getNotificationType())
                    && mProductList.get(position).getFollow_status().equals("2")){
                follow_accept_btn.setVisibility(View.VISIBLE);
                follow_cancel_btn.setVisibility(View.VISIBLE);

                request_send.setVisibility(View.GONE);
                like_post_image.setVisibility(View.GONE);
                like_song_image.setVisibility(View.GONE);
                follow_user_btn.setVisibility(View.GONE);

                send_follow = true;
            }
            if(NotificationType.FOLLOW.equals(mProductList.get(position).getNotificationType())
                    && mProductList.get(position).getFollow_status().equals("1")
                    && mProductList.get(position).getAmIFollow().equals("0")){

                follow_user_btn.setVisibility(View.VISIBLE);

                follow_accept_btn.setVisibility(View.GONE);
                follow_cancel_btn.setVisibility(View.GONE);

                request_send.setVisibility(View.GONE);
                like_post_image.setVisibility(View.GONE);
                like_song_image.setVisibility(View.GONE);

            }
            if(NotificationType.FOLLOW.equals(mProductList.get(position).getNotificationType())
                    && mProductList.get(position).getFollow_status().equals("1")
                    && mProductList.get(position).getAmIFollow().equals("2")){

                request_send.setVisibility(View.VISIBLE);

                follow_user_btn.setVisibility(View.GONE);
                follow_accept_btn.setVisibility(View.GONE);
                follow_cancel_btn.setVisibility(View.GONE);

                like_post_image.setVisibility(View.GONE);
                like_song_image.setVisibility(View.GONE);

                waiting=true;
            }
            if(NotificationType.FOLLOW.equals(mProductList.get(position).getNotificationType())
                    && mProductList.get(position).getFollow_status().equals("0")
                    && mProductList.get(position).getAmIFollow().equals("1")){

                request_send.setVisibility(View.GONE);

                follow_user_btn.setVisibility(View.GONE);
                follow_accept_btn.setVisibility(View.GONE);
                follow_cancel_btn.setVisibility(View.VISIBLE);

                like_post_image.setVisibility(View.GONE);
                like_song_image.setVisibility(View.GONE);

            }
            if(NotificationType.FOLLOW.equals(mProductList.get(position).getNotificationType())
                    && mProductList.get(position).getFollow_status().equals("1")
                    && mProductList.get(position).getAmIFollow().equals("1")){

                request_send.setVisibility(View.GONE);

                follow_user_btn.setVisibility(View.GONE);
                follow_accept_btn.setVisibility(View.GONE);
                follow_cancel_btn.setVisibility(View.VISIBLE);

                like_post_image.setVisibility(View.GONE);
                like_song_image.setVisibility(View.GONE);

            }






            if(NotificationType.LIKE.equals(mProductList.get(position).getNotificationType())){
                follow_accept_btn.setVisibility(View.GONE);
                follow_cancel_btn.setVisibility(View.GONE);
                request_send.setVisibility(View.GONE);
                follow_user_btn.setVisibility(View.GONE);

                like_post_image.setVisibility(View.VISIBLE);
                like_song_image.setVisibility(View.VISIBLE);

                Picasso.get().load("https://spotify.krakersoft.com/upload_post_pic/"+selectedProduct.getPost_images()).into(this.like_post_image);
                Picasso.get().load(selectedProduct.getSong_images()).into(this.like_song_image);

            }


            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");

            try {

                Date oldDate = dateFormat.parse(selectedProduct.getLike_time());
                System.out.println(oldDate);

                Date currentDate = new Date();

                long diff = currentDate.getTime() - oldDate.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;
                long week = days / 7;

                if (oldDate.before(currentDate)) {

                    Log.e("oldDate", "is previous date");
                    Log.e("Difference: ", " seconds: " + seconds + " minutes: " + minutes
                            + " hours: " + hours + " days: " + days + " week: " + week);

                    if(week==0){
                        this.notif_message.setText(selectedProduct.getMessage()+" " + days + "g");
                    }if(days==0){
                        this.notif_message.setText(selectedProduct.getMessage()+" " + hours + "s");
                    }if(hours==0){
                        this.notif_message.setText(selectedProduct.getMessage()+" " + minutes + "d");
                    }if(minutes==0){
                        this.notif_message.setText(selectedProduct.getMessage()+" " + seconds + "sn");
                    }if(week!=0){
                        this.notif_message.setText(selectedProduct.getMessage()+" " + week + "h");
                    }


                }

                // Log.e("toyBornTime", "" + toyBornTime);

            } catch (ParseException e) {

                e.printStackTrace();
            }

        }
        public void accept_follow(Context context){

            if(send_follow==true){

                follow_user_btn.setVisibility(View.VISIBLE);

                follow_accept_btn.setVisibility(View.GONE);
                follow_cancel_btn.setVisibility(View.GONE);

                request_send.setVisibility(View.GONE);
                like_post_image.setVisibility(View.GONE);
                like_song_image.setVisibility(View.GONE);
            }
            if(waiting==true){

                request_send.setVisibility(View.VISIBLE);

                follow_user_btn.setVisibility(View.GONE);
                follow_accept_btn.setVisibility(View.GONE);
                follow_cancel_btn.setVisibility(View.GONE);

                like_post_image.setVisibility(View.GONE);
                like_song_image.setVisibility(View.GONE);
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String my_id = preferences.getString("user_id", "");
            String user_id= mProductList.get(getAdapterPosition()).getSender_user_id();


            JSONObject params = new JSONObject();

            try {
                params.put("my_user_id", my_id);
                params.put("user_id", user_id);
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

                AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "accept_follow", params, listener, errorListener);
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
        public void cancel_follow(Context context){


            if(send_follow==true){

                follow_user_btn.setVisibility(View.VISIBLE);

                follow_accept_btn.setVisibility(View.GONE);
                follow_cancel_btn.setVisibility(View.GONE);

                request_send.setVisibility(View.GONE);
                like_post_image.setVisibility(View.GONE);
                like_song_image.setVisibility(View.GONE);
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String my_id = preferences.getString("user_id", "");
            String user_id= mProductList.get(getAdapterPosition()).getSender_user_id();


            JSONObject params = new JSONObject();

            try {
                params.put("my_user_id", my_id);
                params.put("user_id", user_id);
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

                AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "decline_follow", params, listener, errorListener);
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
        public void follow_user(Context context){


            if(mProductList.get(getAdapterPosition()).getProfile_lock().equals("0")){

                follow_user_btn.setVisibility(View.GONE);

                follow_accept_btn.setVisibility(View.GONE);
                follow_cancel_btn.setVisibility(View.VISIBLE);

                request_send.setVisibility(View.GONE);
                like_post_image.setVisibility(View.GONE);
                like_song_image.setVisibility(View.GONE);
            }else{

                follow_user_btn.setVisibility(View.GONE);

                follow_accept_btn.setVisibility(View.GONE);
                follow_cancel_btn.setVisibility(View.GONE);

                request_send.setVisibility(View.VISIBLE);
                like_post_image.setVisibility(View.GONE);
                like_song_image.setVisibility(View.GONE);

            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String my_id = preferences.getString("user_id", "");
            String user_id= mProductList.get(getAdapterPosition()).getSender_user_id();


            JSONObject params = new JSONObject();

            try {
                params.put("my_user_id", my_id);
                params.put("user_id", user_id);
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

                AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "follow_user", params, listener, errorListener);
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
        public void go_profile(Context context){

            Intent i = new Intent(context.getApplicationContext(), UserProfileActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("user_user_id", mProductList.get(getAdapterPosition()).getSender_user_id());
            i.putExtra("user_profile_lock", mProductList.get(getAdapterPosition()).getProfile_lock());
            context.startActivity(i);
            MyApplication.get().getRequestQueue().getCache().clear();

        }

    }
}

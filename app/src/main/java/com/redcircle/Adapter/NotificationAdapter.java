package com.redcircle.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.redcircle.Pojo.FollowNotification;
import com.redcircle.Pojo.LikeNotification;
import com.redcircle.Pojo.MatchNotification;
import com.redcircle.Pojo.NotificationType;
import com.redcircle.Pojo.Notifications;
import com.redcircle.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{

    ArrayList<Notifications> mProductList;
    LayoutInflater inflater;

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


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView notif_message, productDescription,notif_date;
        ImageView sender_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            notif_message = (TextView) itemView.findViewById(R.id.notif_message);
            notif_date = (TextView) itemView.findViewById(R.id.notif_date);
            sender_image = (ImageView) itemView.findViewById(R.id.prev_play);

        }
        public void setData(Notifications selectedProduct, int position) {
            Picasso.get().load("https://spotify.krakersoft.com/upload_user_pic/"+selectedProduct.getSender_images()).into(this.sender_image);

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            //if(NotificationType.FOLLOW.equals(selectedProduct.getNotificationType()))
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
        @Override
        public void onClick(View v) {

        }

    }
}

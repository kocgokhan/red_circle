package com.redcircle.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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

    public NotificationAdapter(Context context, ArrayList<Notifications> products) {
        inflater = LayoutInflater.from(context);
        this.mProductList = products;
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

        TextView notif_message, productDescription,productDate;
        ImageView sender_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            notif_message = (TextView) itemView.findViewById(R.id.notif_message);
            productDate = (TextView) itemView.findViewById(R.id.productDate);
            sender_image = (ImageView) itemView.findViewById(R.id.prev_play);

        }

        public void setData(Notifications selectedProduct, int position) {

            this.notif_message.setText(selectedProduct.getMessage());

            Picasso.get().load(selectedProduct.getSender_images()).into(this.sender_image);


            SimpleDateFormat originalFormat = new SimpleDateFormat("hh:mm");
            SimpleDateFormat targetFormat = new SimpleDateFormat("hh:mm");
            Date date;
            try {
                date = originalFormat.parse(selectedProduct.getSendDate());
                this.productDate.setText(targetFormat.format(date));
            } catch (ParseException ex) {
                // Handle Exception.
            }
        }
        @Override
        public void onClick(View v) {

        }
    }
}

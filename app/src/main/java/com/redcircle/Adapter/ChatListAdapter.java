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
import com.redcircle.Pojo.ChatList;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.redcircle.Util.StaticFields.BASE_URL;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private ArrayList<ChatList> mProductList;
    private LayoutInflater inflater;
    private String TAG = "SongAdp";
    private ImagePopup imagePopup;

    public ChatListAdapter(Context context, ArrayList<ChatList> products) {
        inflater = LayoutInflater.from(context);
        imagePopup = new ImagePopup(context);
        this.mProductList = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_chat_list, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatList selectedProduct = mProductList.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView follower_count, following_count,set_user_name, message_text,username;
        ImageView productImage;
        ConstraintLayout card;
        ImageButton send_message;


        public MyViewHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.photo_user);
            set_user_name = (TextView) itemView.findViewById(R.id.name);
            username = (TextView) itemView.findViewById(R.id.username);
            following_count = (TextView) itemView.findViewById(R.id.following_count);
            follower_count = (TextView) itemView.findViewById(R.id.follower_count);
            message_text = (TextView) itemView.findViewById(R.id.message_text);
            card = (ConstraintLayout) itemView.findViewById(R.id.cardSong);

            send_message = (ImageButton) itemView.findViewById(R.id.send_messages);

            send_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    message(view.getContext());
                }
            });


        }

        public void setData(ChatList selectedProduct, int position) {
            String profile_photo = selectedProduct.getPost_user_image();

            this.set_user_name.setText(selectedProduct.getPost_user_name());
            this.username.setText(selectedProduct.getUsername());
           // this.follower_count.setText(selectedProduct.getCount_of_followers());
            //this.following_count.setText(selectedProduct.getCount_of_following());

            Picasso.get().load(String.valueOf(Html.fromHtml(profile_photo))).into(this.productImage);
        }

        public void message(Context context){

            Intent i = new Intent(context.getApplicationContext(), ChatActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
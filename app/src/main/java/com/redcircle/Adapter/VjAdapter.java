package com.redcircle.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.redcircle.Activity.ConnectUserActivity;
import com.redcircle.Activity.UserProfileActivity;
import com.redcircle.Pojo.ConnectListVj;
import com.redcircle.Pojo.Posts;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.redcircle.Util.StaticFields.BASE_URL;

public class VjAdapter extends RecyclerView.Adapter<VjAdapter.MyViewHolder> {

    private ArrayList<ConnectListVj> mProductList;
    private LayoutInflater inflater;
    private String TAG = "SongAdp";
    private ImagePopup imagePopup;

    public VjAdapter(Context context, ArrayList<ConnectListVj> products) {
        inflater = LayoutInflater.from(context);
        imagePopup = new ImagePopup(context);
        this.mProductList = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.vj_list_item, parent, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        ConnectListVj selectedProduct = mProductList.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView set_user_name;
        ImageView productImage;

        // Linear Layout Manager
        LinearLayoutManager HorizontalLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.iv);
            set_user_name = (TextView) itemView.findViewById(R.id.iv_textview);

            productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), ConnectUserActivity.class);
                    String strName = null;
                    i.putExtra("user_name", mProductList.get(getAdapterPosition()).getDisplay_name());
                    i.putExtra("user_image", mProductList.get(getAdapterPosition()).getUser_image());
                    i.putExtra("user_username", mProductList.get(getAdapterPosition()).getUser_username());
                    i.putExtra("user_user_id", mProductList.get(getAdapterPosition()).getUser_id());
                    view.getContext().startActivity(i);
                    MyApplication.get().getRequestQueue().getCache().clear();
                }
            });
        }

        public void setData(ConnectListVj selectedProduct, int position) {

            String profile_photo = selectedProduct.getUser_image();

            this.set_user_name.setText(selectedProduct.getDisplay_name());

            Picasso.get().load(String.valueOf(Html.fromHtml(profile_photo))).into(this.productImage);

        }




    }


}
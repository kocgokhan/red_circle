package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConnectListVj {

    private static final String TAG = "PostsPojo ";

    private String user_id,display_name,user_image,user_username;

    public ConnectListVj(JSONObject response, boolean isLogin) {
        try {
            this.user_username = response.getString("username");
            this.display_name = response.getString("display_name");
            this.user_image = response.getString("images");
            this.user_id = response.getString("user_id");
            if (isLogin) {
                this.user_username = response.getString("username");
                this.display_name = response.getString("display_name");
                this.user_image = response.getString("images");
                this.user_id = response.getString("user_id");
            }
        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ConnectListVj() {

    }

    public static String getTAG() {
        return TAG;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_username() {
        return user_username;
    }

    public void setUser_username(String user_username) {
        this.user_username = user_username;
    }

    public static ArrayList<ConnectListVj> getData (ArrayList<ConnectListVj> ary) {
        ArrayList<ConnectListVj> productList = new ArrayList<ConnectListVj>();

        for (int i = 0; i < ary.size(); i++) {
            ConnectListVj temp = new ConnectListVj();
            temp.setUser_id(ary.get(i).user_id);
            temp.setDisplay_name(ary.get(i).display_name);
            temp.setUser_image(ary.get(i).user_image);
            temp.setUser_username(ary.get(i).user_username);
            productList.add(temp);
        }
        return productList;


    }
}

package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LiveSongUser {

    private static final String TAG = "UserPojo ";

    private String user_id,display_name ,username , images;

    public LiveSongUser(JSONObject response, boolean isLogin) {
        try {
            this.user_id = response.getString("id");
            this.display_name = response.getString("display_name");
            this.username = response.getString("username");
            this.images = response.getString("images");
            if (isLogin) {
                this.user_id = response.getString("id");
                this.display_name = response.getString("display_name");
                this.username = response.getString("username");
                this.images = response.getString("images");
            }
        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public LiveSongUser() {

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public static ArrayList<LiveSongUser> getData (ArrayList<LiveSongUser> ary) {
        ArrayList<LiveSongUser> productList = new ArrayList<LiveSongUser>();

        for (int i = 0; i < ary.size(); i++) {
            LiveSongUser temp = new LiveSongUser();
            temp.setUser_id(ary.get(i).getUser_id());
            temp.setUsername(ary.get(i).getUsername());
            temp.setImages(ary.get(i).getImages());
            temp.setDisplay_name(ary.get(i).getDisplay_name());
            productList.add(temp);
        }
        return productList;


    }
}

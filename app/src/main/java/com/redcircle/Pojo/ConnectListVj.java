package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConnectListVj {

    private static final String TAG = "PostsPojo ";

    private String user_id,display_name,user_image,user_username,
            song_images,song_names,song_artist,song_uris,count_of_following,
            count_of_followers,count_of_like;

    public ConnectListVj(JSONObject response, boolean isLogin) {
        try {
            this.user_username = response.getString("username");
            this.display_name = response.getString("display_name");
            this.user_image = response.getString("images");
            this.user_id = response.getString("id");
            this.song_uris = response.getString("song_uris");
            this.song_images = response.getString("song_images");
            this.song_names = response.getString("song_names");
            this.song_artist = response.getString("song_artist");
            if (isLogin) {
                this.user_username = response.getString("username");
                this.display_name = response.getString("display_name");
                this.user_image = response.getString("images");
                this.user_id = response.getString("id");
                this.song_uris = response.getString("song_uris");
                this.song_images = response.getString("song_images");
                this.song_names = response.getString("song_names");
                this.song_artist = response.getString("song_artist");


            }
        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ConnectListVj() {

    }

    public String getSong_images() {
        return song_images;
    }

    public void setSong_images(String song_images) {
        this.song_images = song_images;
    }

    public String getSong_names() {
        return song_names;
    }

    public void setSong_names(String song_names) {
        this.song_names = song_names;
    }

    public String getSong_uris() {
        return song_uris;
    }

    public void setSong_uris(String song_uris) {
        this.song_uris = song_uris;
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


    public String getSong_artist() {
        return song_artist;
    }

    public void setSong_artist(String song_artist) {
        this.song_artist = song_artist;
    }

    public static ArrayList<ConnectListVj> getData (ArrayList<ConnectListVj> ary) {
        ArrayList<ConnectListVj> productList = new ArrayList<ConnectListVj>();

        for (int i = 0; i < ary.size(); i++) {
            ConnectListVj temp = new ConnectListVj();
            temp.setUser_id(ary.get(i).user_id);
            temp.setDisplay_name(ary.get(i).display_name);
            temp.setUser_image(ary.get(i).user_image);
            temp.setUser_username(ary.get(i).user_username);
            temp.setSong_uris(ary.get(i).song_uris);
            temp.setSong_names(ary.get(i).song_names);
            temp.setSong_images(ary.get(i).song_images);
            temp.setSong_artist(ary.get(i).song_artist);
            productList.add(temp);
        }
        return productList;


    }
}

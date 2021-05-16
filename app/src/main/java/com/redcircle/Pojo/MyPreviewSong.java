package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyPreviewSong {

    private static final String TAG = "PostsPojo ";

    private String user_id,song_name,song_image,song_artist,song_uri;

    public MyPreviewSong(JSONObject response, boolean isLogin) {
        try {
            this.song_name = response.getString("song_name");
            this.song_image = response.getString("song_image");
            this.song_artist = response.getString("song_artist");
            this.song_uri = response.getString("song_uri");
            this.user_id = response.getString("user_id");
            if (isLogin) {
                this.song_name = response.getString("song_name");
                this.song_image = response.getString("song_image");
                this.song_artist = response.getString("song_artist");
                this.song_uri = response.getString("song_uri");
                this.user_id = response.getString("user_id");
            }
        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public MyPreviewSong() {

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

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getSong_image() {
        return song_image;
    }

    public void setSong_image(String song_image) {
        this.song_image = song_image;
    }

    public String getSong_artist() {
        return song_artist;
    }

    public void setSong_artist(String song_artist) {
        this.song_artist = song_artist;
    }

    public String getSong_uri() {
        return song_uri;
    }

    public void setSong_uri(String song_uri) {
        this.song_uri = song_uri;
    }

    public static ArrayList<MyPreviewSong> getData (ArrayList<MyPreviewSong> ary) {
        ArrayList<MyPreviewSong> productList = new ArrayList<MyPreviewSong>();

        for (int i = 0; i < ary.size(); i++) {
            MyPreviewSong temp = new MyPreviewSong();
            temp.setUser_id(ary.get(i).user_id);
            temp.setSong_artist(ary.get(i).song_artist);
            temp.setSong_image(ary.get(i).song_image);
            temp.setSong_name(ary.get(i).song_name);
            temp.setSong_uri(ary.get(i).song_uri);
            productList.add(temp);
        }
        return productList;


    }
}

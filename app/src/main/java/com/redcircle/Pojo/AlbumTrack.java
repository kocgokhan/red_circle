package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlbumTrack {

    private static final String TAG = "PostsPojo ";

    private String track_name,track_uri,album_name;

    public AlbumTrack(JSONObject response, boolean isLogin) {
        try {
            this.album_name = response.getString("album_name");
            this.track_name = response.getString("track_name");
            this.track_uri = response.getString("track_uri");
            if (isLogin) {
                this.album_name = response.getString("album_name");
                this.track_name = response.getString("track_name");
                this.track_uri = response.getString("track_uri");
            }
        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public AlbumTrack() {

    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public static String getTAG() {
        return TAG;
    }

    public String getTrack_name() {
        return track_name;
    }

    public void setTrack_name(String track_name) {
        this.track_name = track_name;
    }

    public String getTrack_uri() {
        return track_uri;
    }

    public void setTrack_uri(String track_uri) {
        this.track_uri = track_uri;
    }

    public static ArrayList<AlbumTrack> getData (ArrayList<AlbumTrack> ary) {
        ArrayList<AlbumTrack> productList = new ArrayList<AlbumTrack>();

        for (int i = 0; i < ary.size(); i++) {
            AlbumTrack temp = new AlbumTrack();
            temp.setAlbum_name(ary.get(i).album_name);
            temp.setTrack_name(ary.get(i).track_name);
            temp.setTrack_uri(ary.get(i).track_uri);
            productList.add(temp);
        }
        return productList;


    }
}

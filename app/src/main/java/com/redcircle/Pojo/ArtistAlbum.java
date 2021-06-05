package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArtistAlbum {

    private static final String TAG = "PostsPojo ";

    private String artist_name,spotify_album_id,album_name,album_images,album_uri,album_id,type;

    public ArtistAlbum(JSONObject response, boolean isLogin) {
        try {
            this.artist_name = response.getString("artist_name");
            this.album_id = response.getString("album_id");
            this.type = response.getString("type");
            this.album_name = response.getString("album_name");
            this.album_images = response.getString("album_images");
            this.album_uri = response.getString("album_uri");
            this.spotify_album_id = response.getString("spotify_album_id");
            if (isLogin) {
                this.artist_name = response.getString("artist_name");
                this.album_id = response.getString("album_id");
                this.type = response.getString("type");
                this.album_name = response.getString("album_name");
                this.album_images = response.getString("album_images");
                this.album_uri = response.getString("album_uri");
                this.spotify_album_id = response.getString("spotify_album_id");
            }
        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ArtistAlbum() {

    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public static String getTAG() {
        return TAG;
    }

    public String getSpotify_album_id() {
        return spotify_album_id;
    }

    public void setSpotify_album_id(String spotify_album_id) {
        this.spotify_album_id = spotify_album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getAlbum_images() {
        return album_images;
    }

    public void setAlbum_images(String album_images) {
        this.album_images = album_images;
    }

    public String getAlbum_uri() {
        return album_uri;
    }

    public void setAlbum_uri(String album_uri) {
        this.album_uri = album_uri;
    }

    public static ArrayList<ArtistAlbum> getData (ArrayList<ArtistAlbum> ary) {
        ArrayList<ArtistAlbum> productList = new ArrayList<ArtistAlbum>();

        for (int i = 0; i < ary.size(); i++) {
            ArtistAlbum temp = new ArtistAlbum();
            temp.setArtist_name(ary.get(i).artist_name);
            temp.setAlbum_id(ary.get(i).album_id);
            temp.setType(ary.get(i).type);
            temp.setAlbum_images(ary.get(i).album_images);
            temp.setAlbum_name(ary.get(i).album_name);
            temp.setAlbum_uri(ary.get(i).album_uri);
            temp.setSpotify_album_id(ary.get(i).spotify_album_id);
            productList.add(temp);
        }
        return productList;


    }
}

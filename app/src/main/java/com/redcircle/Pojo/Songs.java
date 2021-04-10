package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Songs {

    private static final String TAG = "UserPojo ";

    private int studentID;
    private String name, images, album_type,artists,uri;
    // private ArrayList<UserOtherImage> userOtherImageArrayList = new ArrayList<>();

    public Songs(JSONObject response, boolean isLogin) {
        try {
            this.name = response.getString("name");
            this.images = response.getString("images");
            this.artists = response.getString("artists");
            this.album_type = response.getString("album_type");
            this.uri = response.getString("uri");
            if (isLogin) {
                this.name = response.getString("name");
                this.images = response.getString("images");
                this.artists = response.getString("artists");
                this.album_type = response.getString("album_type");
                this.uri = response.getString("uri");
            }
        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
    }
    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public static String getTAG() {
        return TAG;
    }


    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getAlbum_type() {
        return album_type;
    }

    public void setAlbum_type(String album_type) {
        this.album_type = album_type;
    }



    public Songs() {

    }

    public Songs(boolean b, String name, String images,String artists, String album_type, JSONObject jsonObject) {
    }

    public static ArrayList<Songs> getData (ArrayList<Songs> ary) {
        ArrayList<Songs> productList = new ArrayList<Songs>();

        for (int i = 0; i < ary.size(); i++) {
            Songs temp = new Songs();
            temp.setImages(ary.get(i).images);
            temp.setName(ary.get(i).name);
            temp.setArtists(ary.get(i).artists);
            temp.setAlbum_type(ary.get(i).album_type);
            temp.setUri(ary.get(i).uri);
            productList.add(temp);
        }
        return productList;


    }
}

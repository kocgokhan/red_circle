package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Posts {

    private static final String TAG = "PostsPojo ";

    private int studentID;
    private String name, images,artists,uri,post_text,post_img_url;
    // private ArrayList<UserOtherImage> userOtherImageArrayList = new ArrayList<>();

    public Posts(JSONObject response, boolean isLogin) {
        try {
            this.name = response.getString("song_name");
            this.images = response.getString("song_image");
            this.artists = response.getString("song_artist");
            this.post_text = response.getString("post_text");
            this.post_img_url = response.getString("post_image");
            this.uri = response.getString("song_uri");
            if (isLogin) {
                this.name = response.getString("name");
                this.images = response.getString("image");
                this.artists = response.getString("artist");
                this.post_text = response.getString("post_text");
                this.post_img_url = response.getString("post_image");
                this.uri = response.getString("song_uri");
            }
        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Posts() {

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

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public String getPost_img_url() {
        return post_img_url;
    }

    public void setPost_img_url(String post_img_url) {
        this.post_img_url = post_img_url;
    }


    public static ArrayList<Posts> getData (ArrayList<Posts> ary) {
        ArrayList<Posts> productList = new ArrayList<Posts>();

        for (int i = 0; i < ary.size(); i++) {
            Posts temp = new Posts();
            temp.setPost_img_url(ary.get(i).post_img_url);
            temp.setPost_text(ary.get(i).post_text);
            temp.setName(ary.get(i).name);
            temp.setImages(ary.get(i).images);
            temp.setArtists(ary.get(i).artists);
            productList.add(temp);
        }
        return productList;


    }
}

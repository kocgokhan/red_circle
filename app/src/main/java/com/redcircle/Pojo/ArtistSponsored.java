package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArtistSponsored {

    private static final String TAG = "PostsPojo ";

    private String user_id,name,images,bio,username,content_name,content_uri,content_images;

    public ArtistSponsored(JSONObject response, boolean isLogin) {
        try {
            this.user_id = response.getString("artist_id");
            this.name = response.getString("name");
            this.images = response.getString("images");
            this.bio = response.getString("bio");
            this.username = response.getString("username");
            this.content_name = response.getString("content_name");
            this.content_uri = response.getString("content_uri");
            this.content_images = response.getString("content_images");
            if (isLogin) {

                this.user_id = response.getString("artist_id");
                this.name = response.getString("name");
                this.images = response.getString("images");
                this.bio = response.getString("bio");
                this.username = response.getString("username");
                this.content_name = response.getString("content_name");
                this.content_uri = response.getString("content_uri");
                this.content_images = response.getString("content_images");

            }

        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ArtistSponsored() {

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

    public String getName() {
        return name;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent_name() {
        return content_name;
    }

    public void setContent_name(String content_name) {
        this.content_name = content_name;
    }

    public String getContent_uri() {
        return content_uri;
    }

    public void setContent_uri(String content_uri) {
        this.content_uri = content_uri;
    }

    public String getContent_images() {
        return content_images;
    }

    public void setContent_images(String content_images) {
        this.content_images = content_images;
    }

    public static ArrayList<ArtistSponsored> getData (ArrayList<ArtistSponsored> ary) {
        ArrayList<ArtistSponsored> productList = new ArrayList<ArtistSponsored>();

        for (int i = 0; i < ary.size(); i++) {
            ArtistSponsored temp = new ArtistSponsored();
            temp.setUser_id(ary.get(i).user_id);
            temp.setBio(ary.get(i).bio);
            temp.setName(ary.get(i).name);
            temp.setUsername(ary.get(i).username);
            temp.setImages(ary.get(i).images);
            temp.setContent_images(ary.get(i).content_images);
            temp.setContent_name(ary.get(i).content_name);
            temp.setContent_uri(ary.get(i).content_uri);
            productList.add(temp);
        }
        return productList;


    }
}

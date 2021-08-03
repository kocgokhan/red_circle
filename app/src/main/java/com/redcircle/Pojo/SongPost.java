package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SongPost {

    private static final String TAG = "PostsPojo ";

    private String song_name, song_image,song_artist,song_uri,post_text,
            post_img_url,post_id,post_user_id,post_user_name,
            post_user_image,post_user_username,count_like,isLike,profile_lock,count_comment;

    public SongPost(JSONObject response, boolean isLogin) {
        try {
            this.song_name = response.getString("song_name");
            this.song_image = response.getString("song_image");
            this.song_artist = response.getString("song_artist");
            this.post_text = response.getString("post_text");
            this.post_img_url = response.getString("post_image");
            this.song_uri = response.getString("song_uri");
            this.post_user_name = response.getString("display_name");
            this.post_user_username = response.getString("username");
            this.post_user_image = response.getString("images");
            this.post_user_id = response.getString("user_id");
            this.post_id = response.getString("post_id");
            this.count_like = response.getString("count_like");
            this.count_comment = response.getString("count_comment");
            this.isLike = response.getString("isLike");
            this.profile_lock = response.getString("profile_lock");
            if (isLogin) {
                this.song_name = response.getString("song_name");
                this.song_image = response.getString("song_image");
                this.song_artist = response.getString("song_artist");
                this.post_text = response.getString("post_text");
                this.post_img_url = response.getString("post_image");
                this.song_uri = response.getString("song_uri");
                this.post_user_name = response.getString("display_name");
                this.post_user_username = response.getString("username");
                this.post_user_image = response.getString("images");
                this.post_user_id = response.getString("user_id");
                this.post_id = response.getString("post_id");
                this.count_like = response.getString("count_like");
                this.count_comment = response.getString("count_comment");
                this.isLike = response.getString("isLike");
                this.profile_lock = response.getString("profile_lock");
            }
        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public SongPost() {

    }

    public String getCount_comment() {
        return count_comment;
    }

    public void setCount_comment(String count_comment) {
        this.count_comment = count_comment;
    }

    public String getProfile_lock() {
        return profile_lock;
    }

    public void setProfile_lock(String profile_lock) {
        this.profile_lock = profile_lock;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public String getCount_like() {
        return count_like;
    }

    public void setCount_like(String count_like) {
        this.count_like = count_like;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_user_id() {
        return post_user_id;
    }

    public void setPost_user_id(String post_user_id) {
        this.post_user_id = post_user_id;
    }

    public String getPost_user_name() {
        return post_user_name;
    }

    public void setPost_user_name(String post_user_name) {
        this.post_user_name = post_user_name;
    }

    public String getPost_user_image() {
        return post_user_image;
    }

    public void setPost_user_image(String post_user_image) {
        this.post_user_image = post_user_image;
    }

    public String getPost_user_username() {
        return post_user_username;
    }

    public void setPost_user_username(String post_user_username) {
        this.post_user_username = post_user_username;
    }

    public static String getTAG() {
        return TAG;
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


    public static ArrayList<SongPost> getData (ArrayList<SongPost> ary) {
        ArrayList<SongPost> productList = new ArrayList<SongPost>();

        for (int i = 0; i < ary.size(); i++) {
            SongPost temp = new SongPost();
            temp.setPost_id(ary.get(i).post_id);
            temp.setPost_img_url(ary.get(i).post_img_url);
            temp.setPost_text(ary.get(i).post_text);
            temp.setSong_name(ary.get(i).song_name);
            temp.setSong_image(ary.get(i).song_image);
            temp.setSong_artist(ary.get(i).song_artist);
            temp.setSong_uri(ary.get(i).song_uri);
            temp.setPost_user_id(ary.get(i).post_user_id);
            temp.setPost_user_image(ary.get(i).post_user_image);
            temp.setPost_user_name(ary.get(i).post_user_name);
            temp.setPost_user_username(ary.get(i).post_user_username);
            temp.setCount_like(ary.get(i).count_like);
            temp.setCount_comment(ary.get(i).count_comment);
            temp.setIsLike(ary.get(i).isLike);
            temp.setProfile_lock(ary.get(i).profile_lock);
            productList.add(temp);
        }
        return productList;


    }
}

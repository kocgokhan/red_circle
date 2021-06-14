package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostComment {

    private static final String TAG = "PostsPojo ";

    private String comment,like_count,sender_user_id,proccess_date,display_name,username,images;

    public PostComment(JSONObject response, boolean isLogin) {
        try {
            this.comment = response.getString("comment");
            this.like_count = response.getString("like_count");
            this.sender_user_id = response.getString("sender_user_id");
            this.proccess_date = response.getString("proccess_date");
            this.display_name = response.getString("display_name");
            this.username = response.getString("username");
            this.images = response.getString("images");
            if (isLogin) {
                this.comment = response.getString("comment");
                this.like_count = response.getString("like_count");
                this.sender_user_id = response.getString("sender_user_id");
                this.proccess_date = response.getString("proccess_date");
                this.display_name = response.getString("display_name");
                this.username = response.getString("username");
                this.images = response.getString("images");
            }
        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public PostComment() {

    }

    public static String getTAG() {
        return TAG;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getSender_user_id() {
        return sender_user_id;
    }

    public void setSender_user_id(String sender_user_id) {
        this.sender_user_id = sender_user_id;
    }

    public String getProccess_date() {
        return proccess_date;
    }

    public void setProccess_date(String proccess_date) {
        this.proccess_date = proccess_date;
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

    public static ArrayList<PostComment> getData (ArrayList<PostComment> ary) {
        ArrayList<PostComment> productList = new ArrayList<PostComment>();

        for (int i = 0; i < ary.size(); i++) {
            PostComment temp = new PostComment();
            temp.setComment(ary.get(i).comment);
            temp.setLike_count(ary.get(i).like_count);
            temp.setSender_user_id(ary.get(i).sender_user_id);
            temp.setProccess_date(ary.get(i).proccess_date);
            temp.setDisplay_name(ary.get(i).display_name);
            temp.setImages(ary.get(i).images);
            productList.add(temp);
        }
        return productList;
    }
}

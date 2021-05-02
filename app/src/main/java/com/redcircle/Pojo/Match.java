package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Match {

    private static final String TAG = "PostsPojo ";

    private String song_name, song_image,song_artist,song_uri,post_user_name,post_user_image,match_id,isfollow,count_of_following,count_of_followers,status,oneSignal_id,match_user_is;

    public Match(JSONObject response, boolean isLogin) {
        try {
            this.song_name = response.getString("song_name");
            this.song_image = response.getString("song_image");
            this.song_artist = response.getString("song_artist");
            this.song_uri = response.getString("song_uri");
            this.post_user_name = response.getString("display_name");
            this.post_user_image = response.getString("user_image");
            this.match_id = response.getString("match_id");
            this.isfollow = response.getString("isfollow");
            this.count_of_following = response.getString("count_of_following");
            this.count_of_followers = response.getString("count_of_followers");
            this.status = response.getString("status");
            this.oneSignal_id = response.getString("osi");
            this.match_user_is = response.getString("user_two");
            if (isLogin) {
                this.song_name = response.getString("song_name");
                this.song_image = response.getString("song_image");
                this.song_artist = response.getString("song_artist");
                this.song_uri = response.getString("song_uri");
                this.post_user_name = response.getString("display_name");
                this.post_user_image = response.getString("user_image");
                this.match_id = response.getString("match_id");
                this.isfollow = response.getString("isfollow");
                this.count_of_following = response.getString("count_of_following");
                this.count_of_followers = response.getString("count_of_followers");
                this.status = response.getString("status");
                this.oneSignal_id = response.getString("osi");
                this.match_user_is = response.getString("user_two");
            }
        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Match() {

    }

    public String getOneSignal_id() {
        return oneSignal_id;
    }

    public void setOneSignal_id(String oneSignal_id) {
        this.oneSignal_id = oneSignal_id;
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

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(String isfollow) {
        this.isfollow = isfollow;
    }

    public String getCount_of_following() {
        return count_of_following;
    }

    public void setCount_of_following(String count_of_following) {
        this.count_of_following = count_of_following;
    }

    public String getCount_of_followers() {
        return count_of_followers;
    }

    public void setCount_of_followers(String count_of_followers) {
        this.count_of_followers = count_of_followers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMatch_user_is() {
        return match_user_is;
    }

    public void setMatch_user_is(String match_user_is) {
        this.match_user_is = match_user_is;
    }

    public static ArrayList<Match> getData (ArrayList<Match> ary) {
        ArrayList<Match> productList = new ArrayList<Match>();

        for (int i = 0; i < ary.size(); i++) {
            Match temp = new Match();
            temp.setMatch_id(ary.get(i).match_id);
            temp.setCount_of_followers(ary.get(i).count_of_followers);
            temp.setCount_of_following(ary.get(i).count_of_following);
            temp.setPost_user_image(ary.get(i).post_user_image);
            temp.setPost_user_name(ary.get(i).post_user_name);
            temp.setStatus(ary.get(i).status);
            temp.setOneSignal_id(ary.get(i).oneSignal_id);
            temp.setMatch_user_is(ary.get(i).match_user_is);
            productList.add(temp);
        }
        return productList;


    }
}

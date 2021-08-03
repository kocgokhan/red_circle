package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Match {

    private static final String TAG = "PostsPojo ";

    private String post_user_name,post_user_image,match_user_is,username;

    public Match(JSONObject response, boolean isLogin) {
        try {
            this.post_user_name = response.getString("display_name");
            this.username = response.getString("username");
            this.post_user_image = response.getString("images");
            this.match_user_is = response.getString("id");
            if (isLogin) {
                this.post_user_name = response.getString("display_name");
                this.username = response.getString("username");
                this.post_user_image = response.getString("images");
                this.match_user_is = response.getString("id");
            }
        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Match() {

    }

    public static String getTAG() {
        return TAG;
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

    public String getMatch_user_is() {
        return match_user_is;
    }

    public void setMatch_user_is(String match_user_is) {
        this.match_user_is = match_user_is;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static ArrayList<Match> getData (ArrayList<Match> ary) {
        ArrayList<Match> productList = new ArrayList<Match>();

        for (int i = 0; i < ary.size(); i++) {
            Match temp = new Match();
            temp.setPost_user_image(ary.get(i).post_user_image);
            temp.setPost_user_name(ary.get(i).post_user_name);
            temp.setUsername(ary.get(i).username);
            temp.setMatch_user_is(ary.get(i).match_user_is);
            productList.add(temp);
        }
        return productList;


    }
}

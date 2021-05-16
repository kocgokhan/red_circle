package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {

    private static final String TAG = "UserPojo ";

    private String user_id,display_name ,username , email ,spotify_id,
            product, country, images, token, osi, regDate,profile_lock,status,
            bio,count_of_following,count_of_like,count_of_followers;
    // private ArrayList<UserOtherImage> userOtherImageArrayList = new ArrayList<>();

    public User(JSONObject response, boolean isLogin) {
        try {
            this.user_id = response.getString("id");
            this.display_name = response.getString("display_name");
            this.username = response.getString("username");
            this.email = response.getString("email");
            this.spotify_id = response.getString("spotify_id");
            this.product = response.getString("product");
            this.country = response.getString("country");
            this.token = response.getString("token");
            this.profile_lock = response.getString("profile_lock");
            this.status = response.getString("status");
            this.images = response.getString("images");
            this.country = response.getString("country");
            this.bio = response.getString("bio");
            this.count_of_following = response.getString("count_of_following");
            this.count_of_like = response.getString("count_of_like");
            this.count_of_followers = response.getString("count_of_followers");

            if (isLogin) {
                this.user_id = response.getString("id");
                this.display_name = response.getString("display_name");
                this.username = response.getString("username");
                this.email = response.getString("email");
                this.spotify_id = response.getString("spotify_id");
                this.product = response.getString("product");
                this.country = response.getString("country");
                this.token = response.getString("token");
                this.profile_lock = response.getString("profile_lock");
                this.status = response.getString("status");
                this.images = response.getString("images");
                this.country = response.getString("country");
                this.bio = response.getString("bio");
                this.count_of_following = response.getString("count_of_following");
                this.count_of_like = response.getString("count_of_like");
                this.count_of_followers = response.getString("count_of_followers");
            }
        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCount_of_following() {
        return count_of_following;
    }

    public void setCount_of_following(String count_of_following) {
        this.count_of_following = count_of_following;
    }

    public String getCount_of_like() {
        return count_of_like;
    }

    public void setCount_of_like(String count_of_like) {
        this.count_of_like = count_of_like;
    }

    public String getCount_of_followers() {
        return count_of_followers;
    }

    public void setCount_of_followers(String count_of_followers) {
        this.count_of_followers = count_of_followers;
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

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpotify_id() {
        return spotify_id;
    }

    public void setSpotify_id(String spotify_id) {
        this.spotify_id = spotify_id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOsi() {
        return osi;
    }

    public void setOsi(String osi) {
        this.osi = osi;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getProfile_lock() {
        return profile_lock;
    }

    public void setProfile_lock(String profile_lock) {
        this.profile_lock = profile_lock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

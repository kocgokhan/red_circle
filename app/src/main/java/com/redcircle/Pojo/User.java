package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {

    private static final String TAG = "UserPojo ";

    private int studentID;
    private String user_id,display_name , email ,spotify_id, product, country, images, token, osi, regDate,profile_lock,status;
    // private ArrayList<UserOtherImage> userOtherImageArrayList = new ArrayList<>();

    public User(JSONObject response, boolean isLogin) {
        try {
            this.user_id = response.getString("id");
            this.display_name = response.getString("display_name");
            this.email = response.getString("email");
            this.spotify_id = response.getString("spotify_id");
            this.product = response.getString("product");
            this.country = response.getString("country");
            this.token = response.getString("token");
            this.profile_lock = response.getString("profile_lock");
            this.status = response.getString("status");
            this.images = response.getString("images");
            if (isLogin) {
                this.user_id = response.getString("id");
                this.display_name = response.getString("display_name");
                this.email = response.getString("email");
                this.spotify_id = response.getString("spotify_id");
                this.product = response.getString("product");
                this.country = response.getString("country");
                this.token = response.getString("token");
                this.profile_lock = response.getString("profile_lock");
                this.status = response.getString("status");
            }
        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
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

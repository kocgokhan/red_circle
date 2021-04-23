package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notifications {

    private static final String TAG = "UserPojo ";

    private String post_id,sender_user_id,receiver_user_id,notification_id;
    private String title, message, sendDate,sender_images;
    private boolean notificationData;
    // private ArrayList<UserOtherImage> userOtherImageArrayList = new ArrayList<>();

    public Notifications(JSONObject response, boolean isLogin) {
        try {
            this.sender_images = response.getString("sender_images");
            this.sendDate = response.getString("like_time");
            this.message = response.getString("message");
            this.post_id = response.getString("post_id");
            this.sender_user_id = response.getString("sender_user_id");
            this.receiver_user_id = response.getString("receiver_user_id");
            this.notification_id = response.getString("notification_id");
            if (isLogin) {
                this.sender_images = response.getString("sender_images");
                this.sendDate = response.getString("like_time");
                this.message = response.getString("message");
                this.post_id = response.getString("post_id");
                this.sender_user_id = response.getString("sender_user_id");
                this.receiver_user_id = response.getString("receiver_user_id");
                this.notification_id = response.getString("notification_id");
            }

        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Notifications() {

    }

    public static String getTAG() {
        return TAG;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getSender_user_id() {
        return sender_user_id;
    }

    public void setSender_user_id(String sender_user_id) {
        this.sender_user_id = sender_user_id;
    }

    public String getReceiver_user_id() {
        return receiver_user_id;
    }

    public void setReceiver_user_id(String receiver_user_id) {
        this.receiver_user_id = receiver_user_id;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getSender_images() {
        return sender_images;
    }

    public void setSender_images(String sender_images) {
        this.sender_images = sender_images;
    }

    public boolean isNotificationData() {
        return notificationData;
    }


    public static ArrayList<Notifications> getData (ArrayList<Notifications> ary) {
        ArrayList<Notifications> productList = new ArrayList<Notifications>();

        for (int i = 0; i < ary.size(); i++) {
            Notifications temp = new Notifications();
            temp.setPost_id(ary.get(i).post_id);
            temp.setSender_user_id(ary.get(i).sender_user_id);
            temp.setReceiver_user_id(ary.get(i).receiver_user_id);
            temp.setSender_images(ary.get(i).sender_images);
            temp.setMessage(ary.get(i).message);
            temp.setSendDate(ary.get(i).sendDate);
            productList.add(temp);
        }
        return productList;
    }
}

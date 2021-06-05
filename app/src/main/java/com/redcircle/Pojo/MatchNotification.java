package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MatchNotification {

    private static final String TAG = "UserPojo ";

    private String post_id,sender_user_id,receiver_user_id,notification_id;
    private String title, message, sendDate,sender_images;
    private boolean notificationData;
    // private ArrayList<UserOtherImage> userOtherImageArrayList = new ArrayList<>();

    public MatchNotification(JSONObject response, boolean isLogin) {
        try {
            this.sender_images = response.getString("sender_images");
            this.sendDate = response.getString("like_time");
            this.message = response.getString("message");
            this.sender_user_id = response.getString("sender_user_id");
            this.receiver_user_id = response.getString("receiver_user_id");
            this.notification_id = response.getString("match_notif_id");
            if (isLogin) {
                this.sender_images = response.getString("sender_images");
                this.sendDate = response.getString("like_time");
                this.message = response.getString("message");
                this.sender_user_id = response.getString("sender_user_id");
                this.receiver_user_id = response.getString("receiver_user_id");
                this.notification_id = response.getString("match_notif_id");
            }

        } catch (JSONException e) {
            Log.wtf(TAG, "json parse catche dustu : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public MatchNotification() {

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

    public static ArrayList<MatchNotification> getDataMatch (ArrayList<MatchNotification> ary) {
        ArrayList<MatchNotification> productList = new ArrayList<MatchNotification>();

        for (int i = 0; i < ary.size(); i++) {
            MatchNotification temp = new MatchNotification();
            productList.add(temp);
        }
        return productList;
    }

}

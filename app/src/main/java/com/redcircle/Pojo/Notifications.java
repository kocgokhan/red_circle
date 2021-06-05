package com.redcircle.Pojo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notifications {

    private static final String TAG = "NotifPojo ";
    private String sender_user_id,message,amIFollow,follow_status,sender_images,like_time,post_id;
    private NotificationType notificationType;

    public Notifications(NotificationType notificationType,String sender_user_id,String message,String amIFollow,String follow_status,String sender_images,String like_time) {
        this.notificationType= notificationType;
        this.sender_user_id = sender_user_id;
        this.message = message;
        this.amIFollow = amIFollow;
        this.follow_status = follow_status;
        this.sender_images = sender_images;
        this.like_time = like_time;
    }

    public Notifications(NotificationType notificationType,String sender_user_id,String message,String sender_images,String like_time,String post_id) {
        this.notificationType= notificationType;
        this.sender_user_id = sender_user_id;
        this.message = message;
        this.sender_images = sender_images;
        this.like_time = like_time;
        this.post_id = post_id;
    }
    public Notifications(NotificationType notificationType,String sender_user_id,String message,String sender_images,String like_time) {
        this.notificationType= notificationType;
        this.sender_user_id = sender_user_id;
        this.message = message;
        this.sender_images = sender_images;
        this.like_time = like_time;
    }

    public static String getTAG() {
        return TAG;
    }

    public String getSender_user_id() {
        return sender_user_id;
    }

    public String getMessage() {
        return message;
    }

    public String getAmIFollow() {
        return amIFollow;
    }

    public String getFollow_status() {
        return follow_status;
    }

    public String getSender_images() {
        return sender_images;
    }

    public String getLike_time() {
        return like_time;
    }

    public String getPost_id() {
        return post_id;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }
}
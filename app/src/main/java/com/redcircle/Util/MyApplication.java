package com.redcircle.Util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.onesignal.OneSignal;

import java.net.URISyntaxException;

import static com.redcircle.Util.StaticFields.CHAT_SERVER_URL;


/**
 * Created by GK
 * on 16.02.2019.
 */

public class MyApplication extends Application {

    private static MyApplication _instance;
    private RequestQueue _requestQueue;
    private SharedPreferences _preferences;
    private String ONESIGNAL_APP_ID="ea03e363-3132-4a69-a472-80b4e8ccd660";
    private static final String TAG = MyApplication.class
            .getSimpleName();
    public static MyApplication get() {
        return _instance;
    }
    public static synchronized MyApplication getInstance() {
        return _instance;
    }

    private Socket socket;
    {
        try {
            socket = IO.socket(CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.setUserId("myAppUserId");
        FirebaseAnalytics.getInstance(this);

        if (FirebaseCrashlytics.getInstance().didCrashOnPreviousExecution()) {

        }

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);





        _instance = this;
        _preferences = PreferenceManager.getDefaultSharedPreferences(this);
        _requestQueue = Volley.newRequestQueue(this);

    }

    public RequestQueue getRequestQueue() {
        if (_requestQueue == null) {
            _requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return _requestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public SharedPreferences getPreferences() {
        return _preferences;
    }

    public SharedPreferences.Editor getPreferencesEditor() {
        return _preferences.edit();
    }



}

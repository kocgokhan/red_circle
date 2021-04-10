package com.redcircle.Request;
import androidx.annotation.Nullable;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SD
 * on 25.07.2018.
 */

public class AqJSONArrayRequest extends JsonArrayRequest {

    private String TAG = "AqJSONArrayRequest ";

    public AqJSONArrayRequest(String TAG, String url, @Nullable JSONArray params, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener) {
        super(Method.POST, url, params, listener, errorListener);
        this.TAG = TAG;
        Log.wtf(TAG, "params : " + params);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> _headers = super.getHeaders();

        if (_headers == null || _headers.equals(Collections.emptyMap())) {
            _headers = new HashMap<String, String>();
        }

        _headers.put("Content-Type", "application/json");

        Log.wtf(TAG, "getHeaders : " + _headers);

        return _headers;
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        Log.wtf(TAG, "parseNetworkError volleyError : " + volleyError + " / volleyError.getMessage() : " + volleyError.getMessage());

        NetworkResponse response = volleyError.networkResponse;
        if (volleyError instanceof ServerError && response != null) {
            try {
                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                Log.wtf(TAG, "parseNetworkError res : " + res + " / response : " + response);
                JSONObject obj = new JSONObject(res);
                Log.wtf(TAG, "parseNetworkError obj : " + obj);
            } catch (UnsupportedEncodingException | JSONException e1) {
                e1.printStackTrace();
            }
        }

        volleyError.printStackTrace();
        return volleyError;
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=UTF-8";
    }

}

package com.redcircle.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import static com.redcircle.Util.StaticFields.BASE_URL;

public class FirstSettingActivity extends AppCompatActivity {
    private String user_id,name,username,bio,update_name,update_username,update_bio;
    private EditText etName,etUsername,etBio;
    private ImageButton first_update_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide status bar
        try
        {
            this.getSupportActionBar().hide();// hide status bar
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_first_setting);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id = preferences.getString("user_id", "Error");
        name = preferences.getString("name", "Error");
        username = preferences.getString("username", "Error");
        bio = preferences.getString("bio", "Error");

        etName = (EditText) findViewById(R.id.etname);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etBio = (EditText) findViewById(R.id.etBio);

        first_update_btn = (ImageButton) findViewById(R.id.first_update_btn);

        etName.setText(name);



        first_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_name= etName.getText().toString().trim();
                update_username= etUsername.getText().toString().trim();
                update_bio= etBio.getText().toString().trim();
                completeAccount(user_id,update_name,update_username,update_bio);
            }
        });
    }
    private void completeAccount(String user_ids,String update_name, String update_username, String update_bio) {
        JSONObject params = new JSONObject();
        ProgressDialog progressDialog = new ProgressDialog(FirstSettingActivity.this);
        progressDialog.setMessage("Hesap Bilgileriniz Güncelleniyor..");
        progressDialog.show();
        try {
            params.put("user_id", user_ids);
            params.put("name", update_name);
            params.put("username", update_username);
            params.put("bio", update_bio);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Log.wtf(TAG, "onResponse : " + response);
                    SharedPreferences.Editor editor = MyApplication.get().getPreferencesEditor();
                    try {


                        JSONArray jsonArray = response.getJSONArray("data");
                        JSONObject users_data = jsonArray.getJSONObject(0);

                        editor.putString("loginResponse", response + "");
                        editor.putString("user_id", users_data.getString("id"));
                        editor.putString("name", users_data.getString("display_name"));
                        editor.putString("images", users_data.getString("images"));
                        editor.putString("email", users_data.getString("email"));
                        editor.putString("username",users_data.getString("username"));
                        editor.putString("bio",users_data.getString("bio"));
                        editor.putString("count_of_following",users_data.getString("count_of_following"));
                        editor.putString("count_of_followers",users_data.getString("count_of_followers"));
                        editor.putString("count_of_like",users_data.getString("count_of_like"));

                        editor.apply();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finish();
                    startActivity(new Intent(FirstSettingActivity.this, MainActivity.class));
                    MyApplication.get().getRequestQueue().getCache().clear();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf("Update Account", "onErrorResponse : " + error);
                    Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
                }
            };

            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest("Update Account", BASE_URL + "first_update", params, listener, errorListener);
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf("Update Account", "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
        }

    }
}
package com.redcircle.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.redcircle.Pojo.User;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.redcircle.Util.StaticFields.BASE_URL;

public class PostActivity extends AppCompatActivity {

    private ImageButton gallery_btn;
    ImageView IVPreviewImages,search_content,song_image,posted_image,post_bt;
    private String songs_name,songs_artist,songs_image,songs_uri,user_id,posted_text;
    TextView song_name,song_artist;
    EditText post_descr;
    private  boolean send = false;
    private String TAG="PostAct";
    private final int GALLERY = 1;
    public static final int REQUEST_IMAGE = 100;
    private ImageButton back_view;
    RequestQueue rQueue;
    Bitmap bitmap;
    private ArrayList<User> userArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide status bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide status bar
        //View bView = getWindow().getDecorView();// hide hardware buttons
        //bView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);// hide hardware buttons
        try
        {
            this.getSupportActionBar().hide();// hide status bar
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_post);

        back_view = (ImageButton) findViewById(R.id.back_views);

        back_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        IVPreviewImages = findViewById(R.id.IVPreviewImage);


        requestMultiplePermissions();

        gallery_btn = (ImageButton) findViewById(R.id.camera_btn);
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
                intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

                // setting aspect ratio
                intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
                intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
                intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        TextView search_text = (TextView) findViewById(R.id.search_text);
        ImageView search_image = (ImageView) findViewById(R.id.search_image);
        search_content = (ImageView) findViewById(R.id.search_content);
        search_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SongActivity.class);
                startActivity(intent);
            }

        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id = preferences.getString("user_id", "Error");

        song_name=(TextView) findViewById(R.id.song_name);
        song_artist=(TextView)findViewById(R.id.song_artist);
        song_image=(ImageView) findViewById(R.id.song_image);

        try {
            Bundle bundle =  getIntent().getExtras();
            if (bundle != null) {
                songs_name = bundle.getString("name", "Error");
                songs_artist = bundle.getString("artist", "Error");
                songs_uri = bundle.getString("uri", "Error");
                songs_image = bundle.getString("images", "Error");

                search_text.setVisibility(View.INVISIBLE);
                search_image.setVisibility(View.INVISIBLE);

                song_image.setVisibility(View.VISIBLE);
                song_name.setVisibility(View.VISIBLE);
                song_artist.setVisibility(View.VISIBLE);

                song_name.setText(songs_name);
                song_artist.setText(songs_artist);
                Picasso.get().load(songs_image).into(song_image);
            }
        }catch (Exception e){
        }


        post_descr = (EditText) findViewById(R.id.post_descr);
        post_bt = (ImageView ) findViewById(R.id.post_send);
        post_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posted_text= post_descr.getText().toString().trim();
                requestJson(user_id,songs_name,songs_artist,songs_uri,songs_image,bitmap,posted_text);
            }
        });




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentURI = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server

                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), contentURI);
                        IVPreviewImages.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void requestJson(String user_id, String name_song, String artist_song, String uri_song, String image_song, @Nullable Bitmap post_image , String post_texts) {
        JSONObject params = new JSONObject();
        String encodedImage = null;
        if(post_image!=null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            post_image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        }

        final ProgressDialog loading = new ProgressDialog(PostActivity.this);
        loading.setMessage("Lütfen Bekleyin...");
        loading.show();
        loading.setCanceledOnTouchOutside(false);
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        send = true;
        try {
            params.put("user_id", user_id);
            params.put("song_name", name_song);
            params.put("song_image", image_song);
            params.put("song_artist", artist_song);
            params.put("song_uri", uri_song);
            if(encodedImage == null){
                params.put("post_image", " ");
            }else{
                params.put("post_image", encodedImage);
            }

            params.put("post_text", post_texts);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Log.wtf(TAG, "onResponse : " + response);

                    Intent i = new Intent(PostActivity.this, MainActivity.class);
                    startActivity(i);

                    MyApplication.get().getRequestQueue().getCache().clear();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf(TAG, "onErrorResponse : " + error);
                    Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
                }
            };

            AqJSONObjectRequest aqJSONObjectRequest = new AqJSONObjectRequest(TAG, BASE_URL + "send_post", params, listener, errorListener);
            aqJSONObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
        }

    }
    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext().getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext().getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
}
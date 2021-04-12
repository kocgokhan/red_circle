package com.redcircle.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.redcircle.Activity.MainActivity;
import com.redcircle.R;
import com.redcircle.Request.AqJSONObjectRequest;
import com.redcircle.Request.MySingleton;
import com.redcircle.Util.MyApplication;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.redcircle.Util.StaticFields.BASE_URL;
import static com.redcircle.Util.StaticFields.UPLOAD_URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton gallery_btn;
    ImageView IVPreviewImages,search_content,song_image,posted_image,post_bt;
    private String songs_name,songs_artist,songs_image,songs_uri,user_id,posted_text;
    TextView song_name,song_artist;
    EditText post_descr;
    private  boolean send = false;
    private String TAG="PostAct";
    private final int GALLERY = 1;
    RequestQueue rQueue;
    Bitmap bitmap;
    public PostFragment() {
    }
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @SuppressLint("WrongThread")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        IVPreviewImages = view.findViewById(R.id.IVPreviewImage);


        requestMultiplePermissions();

        gallery_btn = (ImageButton) view.findViewById(R.id.camera_btn);
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, GALLERY);
            }
        });

        TextView search_text = (TextView) view.findViewById(R.id.search_text);
        ImageView search_image = (ImageView) view.findViewById(R.id.search_image);
        search_content = (ImageView) view.findViewById(R.id.search_content);
        search_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongFragment findSongFragment = new SongFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, findSongFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        user_id = preferences.getString("user_id", "Error");

        song_name=(TextView)view.findViewById(R.id.song_name);
        song_artist=(TextView)view.findViewById(R.id.song_artist);
        song_image=(ImageView) view.findViewById(R.id.song_image);

        try {
            Bundle bundle = this.getArguments();
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


        post_descr = (EditText) view.findViewById(R.id.post_descr);
        post_bt = (ImageView )getActivity().findViewById(R.id.post_button);
        post_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               posted_text= post_descr.getText().toString().trim();
                if(bitmap!=null){
                    requestJson(user_id,songs_name,songs_artist,songs_uri,songs_image,bitmap,posted_text);
                }

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), contentURI);
                    IVPreviewImages.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void requestJson(String user_id, String name_song,String artist_song, String uri_song, String image_song,final Bitmap post_image,  String post_texts) {
        JSONObject params = new JSONObject();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        post_image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        send = true;
        try {
            params.put("user_id", user_id);
            params.put("song_name", name_song);
            params.put("song_image", image_song);
            params.put("song_artist", artist_song);
            params.put("song_uri", uri_song);
            params.put("post_image", encodedImage);
            params.put("post_text", post_texts);
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf(TAG, "onResponse : " + response);

                    Intent i = new Intent(getContext(), MainActivity.class);
                    startActivity(i);

                    MyApplication.get().getRequestQueue().getCache().clear();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf(TAG, "onErrorResponse : " + error);
                    Toast.makeText(getContext(), "Hata", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "Hata", Toast.LENGTH_SHORT).show();
        }

    }
    private void  requestMultiplePermissions(){
        Dexter.withActivity(getActivity())
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getContext().getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext().getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
}

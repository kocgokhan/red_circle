package com.redcircle.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import androidx.loader.content.CursorLoader;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.redcircle.R;
import com.redcircle.Request.VolleyMultipartRequest;
import com.redcircle.Util.StaticFields;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


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
    ImageView IVPreviewImage,search_content,song_image,posted_image,post_bt;
    private String songs_name,songs_artist,songs_image,songs_uri,user_id,posted_text;
    TextView song_name,song_artist;
    EditText post_descr;
    private  boolean send = false;
    private String TAG="PostAct";
    static final int PICK_IMAGE_REQUEST = 100;
    String filePath;
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
        IVPreviewImage = view.findViewById(R.id.IVPreviewImage);
        gallery_btn = (ImageButton) view.findViewById(R.id.camera_btn);
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBrowse();
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
                posted_text =  post_descr.getText().toString().trim();
                if (bitmap != null) {
                    requestJson(user_id,songs_name,songs_artist,songs_uri,songs_image,bitmap,posted_text);
                } else {
                    Toast.makeText(getContext(), "Image not selected!", Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }
    private void imageBrowse() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if(requestCode == PICK_IMAGE_REQUEST){
                Uri picUri = data.getData();

                 bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), picUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //displaying selected image to imageview
                IVPreviewImage.setImageBitmap(bitmap);


            }

        }

    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void requestJson(String user_id, String name_song,String artist_song, String uri_song, String image_song,final Bitmap bitmap,  String post_texts) {
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, StaticFields.UPLOAD_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("song_name", name_song);
                params.put("song_image", image_song);
                params.put("song_artist", artist_song);
                params.put("song_uri", uri_song);
                params.put("post_text", post_texts);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("post_image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Log.e(TAG,"aaaaa "+volleyMultipartRequest);
        Volley.newRequestQueue(getContext()).add(volleyMultipartRequest);
    }





    /*private void requestJson(String user_id, String name_song,String artist_song, String uri_song, String image_song,final String post_image,  String post_texts) {
        JSONObject params = new JSONObject();
        send=true;
        try {
            params.put("user_id", user_id);
            params.put("song_name", name_song);
            params.put("song_image", image_song);
            params.put("song_artist", artist_song);
            params.put("song_uri", uri_song);
            params.put("post_image", post_image);
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
            MyApplication.get().getRequestQueue().add(aqJSONObjectRequest);
        } catch (JSONException e) {
            Log.wtf(TAG, "request params catch e.getMessage() : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(getContext(), "Hata", Toast.LENGTH_SHORT).show();
        }*/
    }

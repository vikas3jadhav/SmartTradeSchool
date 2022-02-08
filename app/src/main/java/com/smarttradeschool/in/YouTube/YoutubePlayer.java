package com.smarttradeschool.in.YouTube;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.smarttradeschool.in.R;
import com.smarttradeschool.in.utils.SessionManagementUser;
import com.smarttradeschool.in.webservice.BaseURL;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;




public class YoutubePlayer extends YouTubeActivity implements
        YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    // YouTube player view
    private YouTubePlayerView youTubeView;
    String url;
    String[] Video_ID;
    Context context;
    SessionManagementUser sessionManagementUser;
    String fullname,user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.youtubeplayer);

        context = this;

        sessionManagementUser = new SessionManagementUser(context);
        HashMap<String, String> user = sessionManagementUser.getUserDetails();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");

            Log.e("url", "" + url);
            if (!url.isEmpty()) {
                if (url.contains("watch")) {
                    Video_ID = url.split("v=");
                    Log.e("Video_ID[1]", "" + Video_ID[1]);
                } else if (url.contains("youtu.be")) {
                    Video_ID = url.split("//youtu.be/");
                    Log.e("Video_ID[1]", "" + Video_ID[1]);
                } else {
                    Toast.makeText(YoutubePlayer.this, "Youtube Video link is not Valid", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(YoutubePlayer.this, "Youtube Video link is not Valid", Toast.LENGTH_LONG).show();
            }
        }

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        // Initializing video player with developer key
        youTubeView
                .initialize(
                        getString(R.string.youtube_api_key),
                        this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        // TODO Auto-generated method stub
        if (!wasRestored) {
            if (!url.isEmpty()) {
                player.loadVideo(Video_ID[1]);
            }
        }

    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        // TODO Auto-generated method stub
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void log_out_details(final String id,final String type,final String url,final String topic) {


        String base_url = BaseURL.BASE_URL;
        String halfurl = base_url + "log_out_details/format/json";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, halfurl, new com
                .android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);

                    boolean res_status = object.getBoolean("status");

                    if(res_status) {
                        finish();
                    }

                    else{
                        finish();
                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context,"Server Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", type);
                params.put("type_id", id);
                params.put("user_id", user_id);
                params.put("condition", "2");

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

}

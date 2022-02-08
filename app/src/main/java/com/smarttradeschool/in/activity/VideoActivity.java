package com.smarttradeschool.in.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.smarttradeschool.in.R;
import com.smarttradeschool.in.adapter.MessageAdapter;
import com.smarttradeschool.in.adapter.VideoViewAllAdapter;
import com.smarttradeschool.in.model.MessageModel;
import com.smarttradeschool.in.model.VideoModel;
import com.smarttradeschool.in.utils.SessionManagementUser;
import com.smarttradeschool.in.webservice.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smarttradeschool.in.utils.SessionManagementUser.KEY_FULL_NAME;
import static com.smarttradeschool.in.utils.SessionManagementUser.KEY_ID;

public class VideoActivity extends AppCompatActivity {

    Context context;
    ImageView iv_back;
    RecyclerView rv_video;
    LinearLayout layout_no_data;
    VideoViewAllAdapter adapter;
    List<VideoModel> videoList = new ArrayList<>();
    SessionManagementUser sessionManagementUser;
    String user_id,full_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        context = this;

        sessionManagementUser = new SessionManagementUser(context);
        HashMap<String, String> user = sessionManagementUser.getUserDetails();
        user_id = user.get(KEY_ID);
        full_name = user.get(KEY_FULL_NAME);

        iv_back = findViewById(R.id.iv_back);
        rv_video = findViewById(R.id.rv_video);
        layout_no_data = findViewById(R.id.layout_no_data);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        GetData();
    }

    public void GetData() {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        videoList.clear();
        String base_url = BaseURL.BASE_URL;
        String halfurl = base_url + "video_screen/format/json";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, halfurl, new com
                .android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);

                    boolean res_status = object.getBoolean("status");

                    if(res_status) {
                        JSONObject jsonObject = object.getJSONObject("data");


                        JSONArray jsonArray2 = jsonObject.getJSONArray("main_data");
                        for(int j=0; j<jsonArray2.length(); j++){
                            JSONObject jsonObject2  = jsonArray2.getJSONObject(j);
                            String id = jsonObject2.getString("id");
                            String heading = jsonObject2.getString("heading");
                            String message = jsonObject2.getString("message");
                            String video_url = jsonObject2.getString("video_url");
                            String added_date = jsonObject2.getString("added_date");

                            VideoModel videoModel = new VideoModel(id,heading,message,video_url,added_date);
                            videoList.add(videoModel);
                        }

                        if(videoList.size() == 0){
                            layout_no_data.setVisibility(View.VISIBLE);
                            rv_video.setVisibility(View.GONE);
                        }else{
                            layout_no_data.setVisibility(View.GONE);
                            rv_video.setVisibility(View.VISIBLE);
                        }



                        adapter = new VideoViewAllAdapter(context,videoList);
                        rv_video.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context,
                                LinearLayoutManager.VERTICAL, false);
                        rv_video.setLayoutManager(mLayoutManager);
                        rv_video.setItemAnimator(new DefaultItemAnimator());
                        rv_video.setAdapter(adapter);
                        adapter.notifyDataSetChanged();




                    }else{
                        Toast.makeText(context, "" + "No Data found", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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
                params.put("user_id", user_id);

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
package com.smarttradeschool.in;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smarttradeschool.in.activity.BlogActivity;
import com.smarttradeschool.in.activity.MessageActivity;
import com.smarttradeschool.in.activity.NotificationActivity;
import com.smarttradeschool.in.activity.PlanActivity;
import com.smarttradeschool.in.activity.ProfileActivity;
import com.smarttradeschool.in.activity.VideoActivity;
import com.smarttradeschool.in.adapter.BannerAdapter;
import com.smarttradeschool.in.adapter.MessageAdapter;
import com.smarttradeschool.in.adapter.VideoAdapter;
import com.smarttradeschool.in.model.BannerModel;
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
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.smarttradeschool.in.utils.SessionManagementUser.KEY_FULL_NAME;
import static com.smarttradeschool.in.utils.SessionManagementUser.KEY_ID;

public class MainActivity extends Fragment {

    Context context;
    SessionManagementUser sessionManagementUser;
    String user_id,full_name;
    TextView tv_name;
    RecyclerView recyclerview_message,recyclerview_video;
    LinearLayout layout_blogs,layout_videos;
    CardView card_view;
    ViewPager viewPager;
    List<BannerModel> BannerList = new ArrayList<>();
    BannerAdapter bannerAdapter;

    Timer timer;
    VideoAdapter videoAdapter;
    MessageAdapter messageAdapter;
    List<VideoModel> videoList = new ArrayList<>();
    List<MessageModel> messageList = new ArrayList<>();
    LinearLayout layout_no_data;
    TextView tv_see_more,tv_blog_see_all;
    ImageView img_notification;
    CircleImageView img_profile;
    Button btn_paid_plan;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.activity_main, container, false);

        context = getActivity();

        sessionManagementUser = new SessionManagementUser(context);
        HashMap<String, String> user = sessionManagementUser.getUserDetails();
        user_id = user.get(KEY_ID);
        full_name = user.get(KEY_FULL_NAME);

        tv_name = view.findViewById(R.id.tv_name);
        recyclerview_message = view.findViewById(R.id.recyclerview_message);
        recyclerview_video = view.findViewById(R.id.recyclerview_video);
        card_view = view.findViewById(R.id.card_view);
        img_notification = view.findViewById(R.id.img_notification);
        img_profile = view.findViewById(R.id.img_profile);
        tv_blog_see_all = view.findViewById(R.id.tv_blog_see_all);
        btn_paid_plan = view.findViewById(R.id.btn_paid_plan);

        viewPager = view.findViewById(R.id.viewPager);

        layout_blogs = view.findViewById(R.id.layout_blogs);
        layout_videos = view.findViewById(R.id.layout_videos);
        layout_no_data = view.findViewById(R.id.layout_no_data);
        tv_see_more = view.findViewById(R.id.tv_see_more);

        tv_name.setText("Hello , "+full_name);

        GetData();
        Get_banner();

        layout_blogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        layout_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoActivity.class);
                startActivity(intent);
            }
        });

        tv_see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                startActivity(intent);
            }
        });

        tv_blog_see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BlogActivity.class);
                startActivity(intent);
            }
        });

        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NotificationActivity.class);
                startActivity(intent);
            }
        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                startActivity(intent);
            }
        });

        btn_paid_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlanActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }

    public void GetData() {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        videoList.clear();
        messageList.clear();
        String base_url = BaseURL.BASE_URL;
        String halfurl = base_url + "dashboard/format/json";

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

                        JSONArray jsonArray1 = jsonObject.getJSONArray("videos");
                        for(int j=0; j<jsonArray1.length(); j++){
                            JSONObject jsonObject2  = jsonArray1.getJSONObject(j);
                            String id = jsonObject2.getString("id");
                            String heading = jsonObject2.getString("heading");
                            String message = jsonObject2.getString("message");
                            String video_url = jsonObject2.getString("video_url");
                            String added_date = jsonObject2.getString("added_date");

                            VideoModel videoModel = new VideoModel(id,heading,message,video_url,added_date);
                            videoList.add(videoModel);
                        }


                        JSONArray jsonArray2 = jsonObject.getJSONArray("messages");
                        for(int j=0; j<jsonArray2.length(); j++){
                            JSONObject jsonObject2  = jsonArray2.getJSONObject(j);
                            String id = jsonObject2.getString("id");
                            String heading = jsonObject2.getString("heading");
                            String message = jsonObject2.getString("description");
                            String added_date = jsonObject2.getString("added_date");

                            MessageModel messageModel = new MessageModel(id,heading,message,added_date);
                            messageList.add(messageModel);
                        }

                        if(messageList.size() == 0){
                            layout_no_data.setVisibility(View.VISIBLE);
                            tv_see_more.setVisibility(View.GONE);
                            recyclerview_message.setVisibility(View.GONE);
                        }else{
                            layout_no_data.setVisibility(View.GONE);
                            tv_see_more.setVisibility(View.VISIBLE);
                            recyclerview_message.setVisibility(View.VISIBLE);
                        }

                        if(videoList.size() == 0){
                            layout_videos.setVisibility(View.GONE);
                            recyclerview_video.setVisibility(View.GONE);
                        }else{
                            layout_videos.setVisibility(View.VISIBLE);
                            recyclerview_video.setVisibility(View.VISIBLE);
                        }

                        videoAdapter = new VideoAdapter(context,videoList);
                        recyclerview_video.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(context,
                                LinearLayoutManager.HORIZONTAL, false);
                        recyclerview_video.setLayoutManager(mLayoutManager3);
                        recyclerview_video.setItemAnimator(new DefaultItemAnimator());
                        recyclerview_video.setAdapter(videoAdapter);
                        videoAdapter.notifyDataSetChanged();

                        messageAdapter = new MessageAdapter(context,messageList);
                        recyclerview_message.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context,
                                LinearLayoutManager.VERTICAL, false);
                        recyclerview_message.setLayoutManager(mLayoutManager);
                        recyclerview_message.setItemAnimator(new DefaultItemAnimator());
                        recyclerview_message.setAdapter(messageAdapter);
                        messageAdapter.notifyDataSetChanged();




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


    public void Get_banner() {
        BannerList.clear();
        String base_url = BaseURL.BASE_URL;
        String halfurl = base_url + "dashboard/format/json";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, halfurl, new com
                .android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    boolean res_status = object.getBoolean("status");

                    if(res_status) {

                        JSONObject jsonObject = object.getJSONObject("data");

                        JSONArray jsonArray = jsonObject.getJSONArray("news");
                        for(int i=0; i<jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                            String heading = jsonObject1.getString("heading");
                            String description = jsonObject1.getString("description");
                            String image_url = jsonObject1.getString("image_url");
                            String added_date = jsonObject1.getString("added_date");

                            BannerModel bannerModel = new BannerModel(id,heading,description,image_url,added_date);
                            BannerList.add(bannerModel);

                        }

                        SetBanner();


                    }else{
                        Toast.makeText(context, "" + "No Data found", Toast.LENGTH_SHORT).show();
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

    public void SetBanner() {
        bannerAdapter = new BannerAdapter(context, BannerList);
        viewPager.setAdapter(bannerAdapter);
        if (BannerList.size() > 0) {
            layout_blogs.setVisibility(View.VISIBLE);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    viewPager.post(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % BannerList.size());
                        }
                    });
                }
            };
            timer = new Timer();
            timer.schedule(timerTask, 10000, 10000);
        }else {
            layout_blogs.setVisibility(View.GONE);
        }
    }

}
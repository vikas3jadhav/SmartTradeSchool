
package com.smarttradeschool.in.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.smarttradeschool.in.R;
import com.smarttradeschool.in.YouTube.YoutubePlayer;
import com.smarttradeschool.in.adapter.MessageAdapter;
import com.smarttradeschool.in.model.MessageModel;
import com.smarttradeschool.in.utils.SessionManagementUser;
import com.smarttradeschool.in.webservice.BaseURL;
import com.squareup.picasso.Picasso;

import static com.smarttradeschool.in.utils.SessionManagementUser.KEY_FULL_NAME;
import static com.smarttradeschool.in.utils.SessionManagementUser.KEY_ID;
import static com.squareup.picasso.Picasso.Priority.HIGH;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BlogDetailActivity extends AppCompatActivity {

    Context context;
    ImageView img_blog,ic_like,ic_dislike;
    ImageView iv_back;
    TextView tv_heading,tv_added_date,tv_description;
    Bundle bundle;
    String heading,added_date,description,image_url;
    SessionManagementUser sessionManagementUser;
    String user_id,full_name,like,dislike,news_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        context = this;

        iv_back = findViewById(R.id.iv_back);
        tv_heading = findViewById(R.id.tv_heading);
        tv_added_date = findViewById(R.id.tv_added_date);
        tv_description = findViewById(R.id.tv_description);
        img_blog = findViewById(R.id.img_blog);
        ic_like = findViewById(R.id.ic_like);
        ic_dislike = findViewById(R.id.ic_dislike);

        sessionManagementUser = new SessionManagementUser(context);
        HashMap<String, String> user = sessionManagementUser.getUserDetails();
        user_id = user.get(KEY_ID);
        full_name = user.get(KEY_FULL_NAME);

        bundle = getIntent().getExtras();
        if(bundle != null){
            news_id = bundle.getString("news_id");
            heading = bundle.getString("heading");
            added_date = bundle.getString("added_date");
            description = bundle.getString("description");
            image_url = bundle.getString("image_url");

            tv_heading.setText(heading);
            tv_added_date.setText(added_date);
            tv_description.setText(description);

            Picasso.with(context).load(image_url).priority(HIGH)
                    .resize(700, 1000).centerInside().into(img_blog);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ic_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ic_like.setColorFilter(ContextCompat.getColor(context, R.color.green));
                ic_dislike.setColorFilter(ContextCompat.getColor(context, R.color.colorIcons));
                like = "1";
                dislike = "0";
                likeDislike();
            }
        });

        ic_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ic_like.setColorFilter(ContextCompat.getColor(context, R.color.greyDark));
                ic_dislike.setColorFilter(ContextCompat.getColor(context, R.color.red));
                dislike = "1";
                like = "0";
                likeDislike();
            }
        });


    }

    public void likeDislike() {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        String base_url = BaseURL.BASE_URL;
        String halfurl = base_url + "news_like_dislike/format/json";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, halfurl, new com
                .android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);

                    boolean res_status = object.getBoolean("status");

                    if(res_status) {
                        Toast.makeText(context, "" + "Added", Toast.LENGTH_SHORT).show();
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
                params.put("news_id", news_id);
                params.put("like_cnt", like);
                params.put("dislike_cnt", dislike);

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
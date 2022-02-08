package com.smarttradeschool.in.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.smarttradeschool.in.adapter.NotificationAdapter;
import com.smarttradeschool.in.model.NotificationModel;
import com.smarttradeschool.in.utils.SessionManagementUser;
import com.smarttradeschool.in.webservice.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.smarttradeschool.in.utils.SessionManagementUser.KEY_ID;

public class NotificationActivity extends Fragment {

    Context context;
    SessionManagementUser sessionManagementUser;
    String user_id;
    LinearLayout layout_no_data;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationModel> notificationList = new ArrayList<>();
    private RecyclerView recyclerView;

    ImageView iv_back;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.activity_notification, container, false);

        context = getActivity();

        sessionManagementUser = new SessionManagementUser(context);
        HashMap<String, String> user = sessionManagementUser.getUserDetails();
        user_id = user.get(KEY_ID);

        layout_no_data = view.findViewById(R.id.layout_no_data);
        recyclerView = view.findViewById(R.id.rv_notification);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        iv_back = view.findViewById(R.id.iv_back);

        GetData(user_id);

//        iv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        return view;

    }

    public void GetData(String user_id) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String base_url = BaseURL.BASE_URL;
        String halfurl = base_url + "user_noti/format/json";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, halfurl, new com
                .android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);

                    boolean res_status = object.getBoolean("status");

                    if(res_status) {


                        JSONArray jsonArray1 = object.getJSONArray("data");
                        for(int j=0; j<jsonArray1.length(); j++){
                            JSONObject jsonObject2  = jsonArray1.getJSONObject(j);
                            String heading = jsonObject2.getString("heading");
                            String added_date = jsonObject2.getString("added_date");
                            String type = jsonObject2.getString("type");
                            String type_id = jsonObject2.getString("type_id");


                            NotificationModel homeworkModel = new NotificationModel(heading,added_date,type,type_id);
                            notificationList.add(homeworkModel);


                        }

                        if(notificationList.size() == 0){
                            layout_no_data.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }else{

                            layout_no_data.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            notificationAdapter = new NotificationAdapter(context,notificationList);
                            recyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(context,
                                    LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(mLayoutManager3);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(notificationAdapter);
                            notificationAdapter.notifyDataSetChanged();

                        }





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
package com.smarttradeschool.in.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.smarttradeschool.in.R;
import com.smarttradeschool.in.utils.SessionManagementUser;
import com.smarttradeschool.in.webservice.BaseURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MobileOtpActivity extends AppCompatActivity {

    EditText edit_mobile;
    Button btn_submit;
    Context context;
    LinearLayout layout_register;
    String mobile_no;
    TextView tv_signup;
    SessionManagementUser sessionManagementUser;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_otp);

        context = this;

        sessionManagementUser = new SessionManagementUser(MobileOtpActivity.this);
        sessionManagementUser.checkLoginUser();

        edit_mobile = findViewById(R.id.edit_mobile);
        btn_submit = findViewById(R.id.btn_submit);

        layout_register = findViewById(R.id.layout_register);
        tv_signup = findViewById(R.id.tv_signup);



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile_no = edit_mobile.getText().toString().trim();
                if (mobile_no.isEmpty()) {
                    Toast.makeText(context, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else {
                    Login();
                }
            }
        });

        layout_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void Login() {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String base_url = BaseURL.BASE_URL;
        String halfurl = base_url + "sms_login/format/json";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, halfurl, new com
                .android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);

                    boolean res_status = object.getBoolean("status");
                    String message = object.getString("message");

                    if (res_status) {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, OtpActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("mobile_no", mobile_no);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    } else {
                        dialogMessage(message);
                    //    Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile_no", mobile_no);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    public void dialogMessage(final String message){
        new AlertDialog.Builder(this)
                //.setTitle("Really Exit?")
                .setMessage(message)
//                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                    }
                }).create().show();
    }
}
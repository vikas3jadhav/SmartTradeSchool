package com.smarttradeschool.in.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.MultiSpinnerSerachWithoutSection;
import com.smarttradeschool.in.R;
import com.smarttradeschool.in.utils.CommonSpinner;
import com.smarttradeschool.in.utils.SessionManagementUser;
import com.smarttradeschool.in.webservice.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Spinner spinner_segment;
    Context context;
    CommonSpinner commonSpinner;
    EditText edit_name, edit_dob, edit_email, edit_mobile;
    CheckBox checkbox_privacy;
    Button btn_register;
    String selectedTradingSegment = "", mobile_no, user_id,name, dob, email, privacy_policy = "0";
    TextView tv_privacy_notice;
    ImageView iv_back;
    List<String> segment_list = new ArrayList<String>();
    SessionManagementUser sessionManagementUser;
    Bundle bundle;
    String userId, userDob, userTradingSegment, userFullname, userEmail, userPhoneno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;

        sessionManagementUser = new SessionManagementUser(context);

        commonSpinner = new CommonSpinner(context);

        checkbox_privacy = findViewById(R.id.checkbox_privacy);
        edit_name = findViewById(R.id.edit_name);
        edit_dob = findViewById(R.id.edit_dob);
        edit_email = findViewById(R.id.edit_email);
        edit_mobile = findViewById(R.id.edit_mobile);
        btn_register = findViewById(R.id.btn_register);
        iv_back = findViewById(R.id.iv_back);

        tv_privacy_notice = findViewById(R.id.tv_privacy_notice);

        spinner_segment = findViewById(R.id.spinner_segment);

        bundle = getIntent().getExtras();
        if(bundle != null){
            user_id = bundle.getString("user_id");
            mobile_no = bundle.getString("mobile_no");
            edit_mobile.setText(mobile_no);
        }

        segment_list.add("Select Trading Segment");
        segment_list.add("Cash");
        segment_list.add("Future");
        segment_list.add("Stock Option");
        segment_list.add("Nifty BankNifty Option");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, segment_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_segment.setAdapter(dataAdapter);


        //     commonSpinner.getTradingSegment(spinner_segment, "", "", false);

        edit_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                                Date date2 = new Date(year, monthOfYear, dayOfMonth - 1);
                                String dayOfWeek = simpledateformat.format(date2);

                                String date = checkDigit(dayOfMonth) + "/" + checkDigit(monthOfYear + 1) + "/" + year;
                                String datef = year + "-" + checkDigit(monthOfYear + 1) + "-" + checkDigit(dayOfMonth);
                                edit_dob.setText(date);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        checkbox_privacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    privacy_policy = "1";
                } else {
                    privacy_policy = "0";
                }
            }
        });

        spinner_segment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTradingSegment = spinner_segment.getSelectedItem().toString();
                if (selectedTradingSegment.equalsIgnoreCase("Select Trading Segment")) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile_no = edit_mobile.getText().toString();
                email = edit_email.getText().toString();
                dob = edit_dob.getText().toString();
                name = edit_name.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(context, "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mobile_no)) {
                    Toast.makeText(context, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(dob)) {
                    Toast.makeText(context, "Enter date of birth", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(context, "Enter email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedTradingSegment.equalsIgnoreCase("Select Trading Segment")) {
                    Toast.makeText(context, "Please select trading segment", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (privacy_policy.equalsIgnoreCase("0")) {
                    Toast.makeText(context, "Please accept the terms and conditions ", Toast.LENGTH_SHORT).show();
                    return;
                }

                Register();

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_privacy_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogview = inflater.inflate(R.layout.dialog_webview_layout, null);

                WebView webView = dialogview.findViewById(R.id.webView);

                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }
                });

                String url = "http://smarttradeschool.com/api/agree/format/html";
                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
//                    String base64version = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
//                    webView.loadData(base64version, "text/html; charset=UTF-8", "base64");
                webView.loadUrl(url);


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogview);
                builder.setPositiveButton("  OK  ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.setTitle("Terms and conditions");
                dialog.show();

            }
        });

    }

    public static String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public void Register() {

        String base_url = BaseURL.BASE_URL;
        String halfurl = base_url + "user_registeration/format/json";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, halfurl, new com
                .android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    boolean res_status = object.getBoolean("status");
                    String message = object.getString("message");

                    if (res_status) {
                        JSONArray jsonArray = object.getJSONArray("data");
                        for(int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                             userId = jsonObject.getString("id");
                             userTradingSegment = jsonObject.getString("trading_segment");
                             userDob = jsonObject.getString("dob");
                             userFullname = jsonObject.getString("full_name");
                             userEmail = jsonObject.getString("email");
                             userPhoneno = jsonObject.getString("phone_no");
                        }
                        sessionManagementUser.createLoginSession(userId, userDob, userTradingSegment, userFullname, userEmail, userPhoneno);
                        Intent intent = new Intent(context, HomeActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
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
                params.put("user_id", user_id);
                params.put("name", name);
                params.put("email", email);
                params.put("dob", dob);
                params.put("trading_segment", selectedTradingSegment);
                params.put("agree", privacy_policy);

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
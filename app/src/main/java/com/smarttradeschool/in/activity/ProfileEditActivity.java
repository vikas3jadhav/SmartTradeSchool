package com.smarttradeschool.in.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

import static com.smarttradeschool.in.utils.SessionManagementUser.KEY_ID;

public class ProfileEditActivity extends AppCompatActivity {

    Context context;
    Spinner spinner_segment;
    EditText edit_phone_no,edit_email,edit_dob,edit_full_name;
    Button btn_update;
    SessionManagementUser sessionManagementUser;
    String user_id,selectedTradingSegment;
    CommonSpinner commonSpinner;
    List<String> segment_list = new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        context = this;

        commonSpinner = new CommonSpinner(context);

        edit_phone_no = findViewById(R.id.edit_phone_no);
        edit_email = findViewById(R.id.edit_email);
        edit_dob = findViewById(R.id.edit_dob);
        edit_full_name = findViewById(R.id.edit_full_name);
        spinner_segment = findViewById(R.id.spinner_segment);
        btn_update = findViewById(R.id.btn_update);


        sessionManagementUser = new SessionManagementUser(context);
        HashMap<String, String> user = sessionManagementUser.getUserDetails();
        user_id = user.get(KEY_ID);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDetails();
            }
        });

        segment_list.add("Select Trading Segment");
        segment_list.add("Cash");
        segment_list.add("Future");
        segment_list.add("Stock Option");
        segment_list.add("Nifty BankNifty Option");

         dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, segment_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_segment.setAdapter(dataAdapter);

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
    }

    public static String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    @Override
    public void onResume() {
        super.onResume();

        GetData();

    }

    public void GetData() {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String base_url = BaseURL.BASE_URL;
        String halfurl = base_url + "user_detail/format/json";

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
                            String full_name = jsonObject2.getString("full_name");
                            String dob = jsonObject2.getString("dob");
                            String trading_segment = jsonObject2.getString("trading_segment");
                            String email = jsonObject2.getString("email");
                            String phone_no = jsonObject2.getString("phone_no");

                            if (trading_segment != null) {
                                int spinnerPosition = dataAdapter.getPosition(trading_segment);
                                spinner_segment.setSelection(spinnerPosition);
                            }

                            edit_full_name.setText(full_name);
                            edit_dob.setText(dob);
                            edit_email.setText(email);
                            edit_phone_no.setText(phone_no);

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

    public void UpdateDetails() {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String base_url = BaseURL.BASE_URL;
        String halfurl = base_url + "update_profile_app/format/json";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, halfurl, new com
                .android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);

                    boolean res_status = object.getBoolean("status");

                    if(res_status) {
                        Toast.makeText(context, "" + "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();
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
                params.put("id", user_id);
                params.put("name", edit_full_name.getText().toString().trim());
                params.put("dob", edit_dob.getText().toString().trim());
                params.put("email", edit_email.getText().toString().trim());
                params.put("trading_segment", spinner_segment.getSelectedItem().toString());
                params.put("mobile_no", edit_phone_no.getText().toString().trim());

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
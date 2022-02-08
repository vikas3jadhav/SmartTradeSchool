package com.smarttradeschool.in.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.smarttradeschool.in.R;
import com.smarttradeschool.in.utils.SessionManagementUser;
import com.smarttradeschool.in.webservice.BaseURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smarttradeschool.in.utils.SessionManagementUser.KEY_FULL_NAME;
import static com.smarttradeschool.in.utils.SessionManagementUser.KEY_ID;

public class PaidClientFormActivity extends AppCompatActivity {
    Context context;
    ImageView iv_back;
    Spinner spinner_age_group,spinner_dependents,spinner_annual_income,spinner_emergency_fund,
            spinner_source_of_income,spinner_percentage_income,spinner_assets,spinner_investment_horizon,
            spinner_investment_experience,spinner_portfolio_amount,spinner_investment_goal,spinner_proposed_investment_amount;
    Bundle bundle;
    String Select_item_name,Select_item_price;
    List<String> ageGroupList = new ArrayList<>();
    List<String> dependentsList = new ArrayList<>();
    List<String> annualIncomeList = new ArrayList<>();
    List<String> emergencyFundList = new ArrayList<>();
    List<String> sourceOfIncomeList = new ArrayList<>();
    List<String> percentageIncomeList = new ArrayList<>();
    List<String> assetsList = new ArrayList<>();
    List<String> investmentHorizonList = new ArrayList<>();
    List<String> investmentExperienceList = new ArrayList<>();
    List<String> portfolioAmountList = new ArrayList<>();
    List<String> investmentGoalList = new ArrayList<>();
    List<String> proposedInvestmentList = new ArrayList<>();
    Button btn_submit;
    CheckBox checkbox_privacy;
    String privacy_policy="0";
    TextView tv_privacy_notice;
    String age_group,dependents,annual_income,emergency_fund,source_of_income,percentage_income,assets,
    investment_horizon,investment_experience,portfolio_amount,investment_goal,proposed_investment_amount;
    SessionManagementUser sessionManagementUser;
    String user_id,full_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_client_form);

        context = this;

        sessionManagementUser = new SessionManagementUser(context);
        HashMap<String, String> user = sessionManagementUser.getUserDetails();
        user_id = user.get(KEY_ID);
        full_name = user.get(KEY_FULL_NAME);

        iv_back = findViewById(R.id.iv_back);
        spinner_age_group = findViewById(R.id.spinner_age_group);
        spinner_dependents = findViewById(R.id.spinner_dependents);
        spinner_annual_income = findViewById(R.id.spinner_annual_income);
        spinner_emergency_fund = findViewById(R.id.spinner_emergency_fund);
        spinner_source_of_income = findViewById(R.id.spinner_source_of_income);
        spinner_percentage_income = findViewById(R.id.spinner_percentage_income);
        spinner_assets = findViewById(R.id.spinner_assets);
        spinner_investment_horizon = findViewById(R.id.spinner_investment_horizon);
        spinner_investment_experience = findViewById(R.id.spinner_investment_experience);
        spinner_portfolio_amount = findViewById(R.id.spinner_portfolio_amount);
        spinner_investment_goal = findViewById(R.id.spinner_investment_goal);
        spinner_proposed_investment_amount = findViewById(R.id.spinner_proposed_investment_amount);
        checkbox_privacy = findViewById(R.id.checkbox_privacy);
        btn_submit = findViewById(R.id.btn_submit);
        tv_privacy_notice = findViewById(R.id.tv_privacy_notice);

        bundle = getIntent().getExtras();
        if(bundle != null){
            Select_item_name = bundle.getString("Select_item_name");
            Select_item_price = bundle.getString("Select_item_price");
        }

        ageGroupList.add("Select Age Group");
        ageGroupList.add("Under 35");
        ageGroupList.add("36 to 45");
        ageGroupList.add("46 to 55");
        ageGroupList.add("56 to 60");
        ageGroupList.add("60+");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, ageGroupList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_age_group.setAdapter(dataAdapter);

        //dependents
        dependentsList.add("Select");
        dependentsList.add("None");
        dependentsList.add("Between 1-3");
        dependentsList.add("4+");

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, dependentsList);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_dependents.setAdapter(dataAdapter2);

        //annual income
        annualIncomeList.add("Select");
        annualIncomeList.add("Less than 1 lac");
        annualIncomeList.add("1 to 5 lacs");
        annualIncomeList.add("5 to 10 lacs");
        annualIncomeList.add("10 to 25 lacs");
        annualIncomeList.add("More than 25 lacs");

        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, annualIncomeList);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_annual_income.setAdapter(dataAdapter3);

        //emergencyFundList

        emergencyFundList.add("Select");
        emergencyFundList.add("Do not have");
        emergencyFundList.add("Less than 1 month income");
        emergencyFundList.add("1 to 3 months income");
        emergencyFundList.add("3 to 6 months income");
        emergencyFundList.add("More than 6 months income");

        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, emergencyFundList);
        dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_emergency_fund.setAdapter(dataAdapter4);


        //sourceOfIncomeList

        sourceOfIncomeList.add("Select");
        sourceOfIncomeList.add("Salary");
        sourceOfIncomeList.add("Business");
        sourceOfIncomeList.add("Profession");
        sourceOfIncomeList.add("Rental");
        sourceOfIncomeList.add("Others");

        ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, sourceOfIncomeList);
        dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_source_of_income.setAdapter(dataAdapter5);

        //percentageIncomeList

        percentageIncomeList.add("Select");
        percentageIncomeList.add("None");
        percentageIncomeList.add("Between 0% -20%");
        percentageIncomeList.add("Between 20% - 35%");
        percentageIncomeList.add("Between 35% - 50%");
        percentageIncomeList.add("> 50%");

        ArrayAdapter<String> dataAdapter6 = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, percentageIncomeList);
        dataAdapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_percentage_income.setAdapter(dataAdapter6);

        //assetsList

        assetsList.add("Select");
        assetsList.add("2 Wheeler, 4 Wheeler, Commercial Vehicle");
        assetsList.add("House : Own / rented");
        assetsList.add("Others");

        ArrayAdapter<String> dataAdapter7 = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, assetsList);
        dataAdapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_assets.setAdapter(dataAdapter7);

        //investmentHorizonList

        investmentHorizonList.add("Select");
        investmentHorizonList.add("Up to two years");
        investmentHorizonList.add("Two and three years");
        investmentHorizonList.add("Three and five years");
        investmentHorizonList.add("Five years and Ten years");
        investmentHorizonList.add("Ten years and more");

        ArrayAdapter<String> dataAdapter8 = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, investmentHorizonList);
        dataAdapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_investment_horizon.setAdapter(dataAdapter8);

        //investmentExperienceList
        investmentExperienceList.add("Select");
        investmentExperienceList.add("Less than 3 years");
        investmentExperienceList.add("3 to 5 years");
        investmentExperienceList.add("More than 5 years");

        ArrayAdapter<String> dataAdapter9 = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, investmentExperienceList);
        dataAdapter9.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_investment_experience.setAdapter(dataAdapter9);

        //portfolioAmountList
        portfolioAmountList.add("Select");
        portfolioAmountList.add("Less than 1 lac");
        portfolioAmountList.add("1 to 2 lacs");
        portfolioAmountList.add("2 to 5 lacs");
        portfolioAmountList.add("5 to 10 lacs");
        portfolioAmountList.add("More than 10 lacs");

        ArrayAdapter<String> dataAdapter10 = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, portfolioAmountList);
        dataAdapter10.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_portfolio_amount.setAdapter(dataAdapter10);

        //investmentGoalList
        investmentGoalList.add("Select");
        investmentGoalList.add("Capital Appreciation");
        investmentGoalList.add("Regular Income ");
        investmentGoalList.add("Capital Appreciation and Regular Income");

        ArrayAdapter<String> dataAdapter11 = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, investmentGoalList);
        dataAdapter11.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_investment_goal.setAdapter(dataAdapter11);

        //proposedInvestmentList
        proposedInvestmentList.add("Select");
        proposedInvestmentList.add("Less than 1 lac");
        proposedInvestmentList.add("1 to 2 lacs");
        proposedInvestmentList.add("2 to 5 lacs");
        proposedInvestmentList.add("5 to 10 lacs");
        proposedInvestmentList.add("10 to 25 lacs");
        proposedInvestmentList.add("More than 25 lacs");

        ArrayAdapter<String> dataAdapter12 = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, proposedInvestmentList);
        dataAdapter12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_proposed_investment_amount.setAdapter(dataAdapter12);

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

                String url = "http://smarttradeschool.com/api/paynow_agree/format/html";
                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webView.loadUrl(url);
//                String base64version = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
//                webView.loadData(base64version, "text/html; charset=UTF-8", "base64");


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


        spinner_age_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                age_group = spinner_age_group.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_dependents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dependents = spinner_dependents.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_annual_income.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                annual_income = spinner_annual_income.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_emergency_fund.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                emergency_fund = spinner_emergency_fund.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_source_of_income.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                source_of_income = spinner_source_of_income.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_percentage_income.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                percentage_income = spinner_percentage_income.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_assets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                assets = spinner_assets.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_investment_horizon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                investment_horizon = spinner_investment_horizon.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_investment_experience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                investment_experience = spinner_investment_experience.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_portfolio_amount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                portfolio_amount = spinner_portfolio_amount.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_investment_goal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                investment_goal = spinner_investment_goal.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_proposed_investment_amount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                proposed_investment_amount = spinner_proposed_investment_amount.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spinner_age_group.getSelectedItem().toString().equalsIgnoreCase("Select Age group")){
                    Toast.makeText(context,"Select Age Group",Toast.LENGTH_SHORT).show();
                }else if(spinner_dependents.getSelectedItem().toString().equalsIgnoreCase("Select")){
                    Toast.makeText(context,"Select Dependents",Toast.LENGTH_SHORT).show();
                }else if(spinner_annual_income.getSelectedItem().toString().equalsIgnoreCase("Select")){
                    Toast.makeText(context,"Select Annual Income",Toast.LENGTH_SHORT).show();
                }else if(spinner_emergency_fund.getSelectedItem().toString().equalsIgnoreCase("Select")){
                    Toast.makeText(context,"Select Emergency Fund",Toast.LENGTH_SHORT).show();
                }else if(spinner_source_of_income.getSelectedItem().toString().equalsIgnoreCase("Select")){
                    Toast.makeText(context,"Select Source Of Income",Toast.LENGTH_SHORT).show();
                }else if(spinner_percentage_income.getSelectedItem().toString().equalsIgnoreCase("Select")){
                    Toast.makeText(context,"Select Income Percentage",Toast.LENGTH_SHORT).show();
                }else if(spinner_assets.getSelectedItem().toString().equalsIgnoreCase("Select")){
                    Toast.makeText(context,"Select Assets",Toast.LENGTH_SHORT).show();
                }else if(spinner_investment_horizon.getSelectedItem().toString().equalsIgnoreCase("Select")){
                    Toast.makeText(context,"Select Investment Horizon",Toast.LENGTH_SHORT).show();
                }else if(spinner_investment_experience.getSelectedItem().toString().equalsIgnoreCase("Select")){
                    Toast.makeText(context,"Select Investment Experience",Toast.LENGTH_SHORT).show();
                }else if(spinner_portfolio_amount.getSelectedItem().toString().equalsIgnoreCase("Select")){
                    Toast.makeText(context,"Select Portfolio Amount",Toast.LENGTH_SHORT).show();
                }else if(spinner_investment_goal.getSelectedItem().toString().equalsIgnoreCase("Select")){
                    Toast.makeText(context,"Select Investment Goal",Toast.LENGTH_SHORT).show();
                }else if(spinner_proposed_investment_amount.getSelectedItem().toString().equalsIgnoreCase("Select")){
                    Toast.makeText(context,"Select Poposed Investment Amount",Toast.LENGTH_SHORT).show();
                }else{
                    paidForm();
                }
            }
        });

        checkbox_privacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    privacy_policy = "1";
                }else {
                    privacy_policy = "0";
                }
            }
        });

    }

    public void paidForm() {

        String base_url = BaseURL.BASE_URL;
        String halfurl = base_url + "user_paid_form/format/json";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, halfurl, new com
                .android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    boolean res_status = object.getBoolean("status");
                    String message = object.getString("message");

                    if(res_status) {
                        dialogMessage(message);
                    }else{
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
                params.put("age_group", age_group);
                params.put("dependents", dependents);
                params.put("annual_income", annual_income);
                params.put("source_of_income", source_of_income);
                params.put("percentage_income", percentage_income);
                params.put("emergency_fund", emergency_fund);
                params.put("assets", assets);
                params.put("investment_horizon", investment_horizon);
                params.put("investment_experience", investment_experience);
                params.put("portfolio_amount", portfolio_amount);
                params.put("investment_goal", investment_goal);
                params.put("proposed_investment_amount", proposed_investment_amount);
                params.put("agree", privacy_policy);
                params.put("plan", Select_item_name);

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
                        Intent intent = new Intent(context,HomeActivity.class);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                    }
                }).create().show();
    }

}
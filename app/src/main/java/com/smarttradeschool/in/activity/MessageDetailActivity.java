package com.smarttradeschool.in.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarttradeschool.in.R;

import java.util.List;

public class MessageDetailActivity extends AppCompatActivity {

    Context context;
    ImageView iv_back;
    TextView tv_heading,tv_added_date,tv_description;
    Bundle bundle;
    String heading,added_date,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        context = this;

        iv_back = findViewById(R.id.iv_back);
        tv_heading = findViewById(R.id.tv_heading);
        tv_added_date = findViewById(R.id.tv_added_date);
        tv_description = findViewById(R.id.tv_description);

        bundle = getIntent().getExtras();
        if(bundle != null){
            heading = bundle.getString("heading");
            added_date = bundle.getString("added_date");
            description = bundle.getString("description");

            tv_heading.setText(heading);
            tv_added_date.setText(added_date);
            tv_description.setText(description);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
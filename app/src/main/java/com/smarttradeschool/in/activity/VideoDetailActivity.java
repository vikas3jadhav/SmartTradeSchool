package com.smarttradeschool.in.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarttradeschool.in.R;
import com.smarttradeschool.in.YouTube.YoutubePlayer;
import com.squareup.picasso.Picasso;

import static com.squareup.picasso.Picasso.Priority.HIGH;

public class VideoDetailActivity extends AppCompatActivity {

    Context context;
    ImageView img_video;
    ImageView iv_back;
    TextView tv_heading,tv_added_date,tv_description;
    Bundle bundle;
    String heading,added_date,description,video_url,thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        context = this;

        iv_back = findViewById(R.id.iv_back);
        tv_heading = findViewById(R.id.tv_heading);
        tv_added_date = findViewById(R.id.tv_added_date);
        tv_description = findViewById(R.id.tv_description);
        img_video = findViewById(R.id.img_video);

        bundle = getIntent().getExtras();
        if(bundle != null){
            heading = bundle.getString("heading");
            added_date = bundle.getString("added_date");
            description = bundle.getString("description");
            video_url = bundle.getString("video_url");
            thumbnail = bundle.getString("thumbnail");

            tv_heading.setText(heading);
            tv_added_date.setText(added_date);
            tv_description.setText(description);

            Picasso.with(context).load(thumbnail).priority(HIGH)
                    .resize(300, 200).centerInside().into(img_video);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        img_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, YoutubePlayer.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", video_url);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }
}
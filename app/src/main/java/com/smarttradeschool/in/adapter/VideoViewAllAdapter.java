package com.smarttradeschool.in.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smarttradeschool.in.R;
import com.smarttradeschool.in.activity.VideoDetailActivity;
import com.smarttradeschool.in.model.VideoModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.squareup.picasso.Picasso.Priority.HIGH;

public class VideoViewAllAdapter extends RecyclerView.Adapter<VideoViewAllAdapter.ViewHolder>{
    Context context;
    List<VideoModel> videoList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_topic,tv_added_date;
        ImageView iv_noti, iv_sketch;

        public ViewHolder(View view) {
            super(view);
            iv_noti = (ImageView) view.findViewById(R.id.iv_noti);
            tv_topic = (TextView) view.findViewById(R.id.tv_topic);
            tv_added_date = view.findViewById(R.id.tv_added_date);
            iv_sketch = view.findViewById(R.id.iv_sketch);
        }
    }

    public VideoViewAllAdapter(Context context,List<VideoModel> videoList){
        this.context = context;
        this.videoList = videoList;
    }


    @Override
    public VideoViewAllAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_video_all_single_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( VideoViewAllAdapter.ViewHolder holder, int position) {
        final String topic = getNonNullStringCustom(videoList.get(position).getHeading(),"");
        final String url = getNonNullStringCustom(videoList.get(position).getVideo_url(),"");

        final String id = getNonNullStringCustom(videoList.get(position).getId(),"");
        final String description = getNonNullStringCustom(videoList.get(position).getMessage(),"");
        final String added_date = getNonNullStringCustom(videoList.get(position).getAdded_date(),"");

        holder.tv_topic.setText(topic);
        holder.tv_added_date.setText("Added Date : "+added_date);

        String youtube_code = "";

        if (url.contains("&ab_channel")) {
            youtube_code = url;

            youtube_code = youtube_code.substring(youtube_code.indexOf("?v=") + 0);
            youtube_code = youtube_code.substring(0, youtube_code.indexOf("&ab_channel"));

            if (youtube_code.contains("?v=")) {
                youtube_code = youtube_code.replace("?v=", "");
            }
        } else if (url.contains("https://www.youtube.com/watch?v=")) {

            youtube_code = url.replace("https://www.youtube.com/watch?v=", "");

        } else if (url.contains("https://youtube.com/watch?v=")) {
            youtube_code = url.replace("https://youtube.com/watch?v=", "");
        } else if (url.contains("https://youtu.be/")) {
            youtube_code = url.replace("https://youtu.be/", "");
        }

        String thumbnail = "http://img.youtube.com/vi/"+youtube_code+"/0.jpg";

        Picasso.with(context).load(thumbnail).priority(HIGH)
                .resize(300, 130).centerInside().into(holder.iv_sketch);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("heading",topic);
                bundle.putString("added_date",added_date);
                bundle.putString("description",description);
                bundle.putString("video_url",url);
                bundle.putString("thumbnail", thumbnail);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static boolean isNotNull(String data) {
        return !TextUtils.isEmpty(data) && data != null && !data.isEmpty() && data != "" && !data.equals("") && !data.equals("null");
    }

    public static String getNonNullStringCustom(String s, String yourCustomMessage){
        String na = isNotNull(s) ? s : yourCustomMessage;
        return na;
    }
}

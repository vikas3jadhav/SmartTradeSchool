package com.smarttradeschool.in.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smarttradeschool.in.R;
import com.smarttradeschool.in.activity.BlogDetailActivity;
import com.smarttradeschool.in.activity.VideoDetailActivity;
import com.smarttradeschool.in.model.BannerModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.squareup.picasso.Picasso.Priority.HIGH;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder>{

    Context context;
    List<BannerModel> bannerList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_thumb;
        TextView tv_topic;


        public ViewHolder(View itemView) {

            super(itemView);

            iv_thumb=itemView.findViewById(R.id.iv_thumb);
            tv_topic = itemView.findViewById(R.id.tv_topic);


        }
    }

    public BlogAdapter(Context context,List<BannerModel> bannerList){
        this.context = context;
        this.bannerList = bannerList;
    }

    @Override
    public BlogAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_blog_single_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( BlogAdapter.ViewHolder holder, int position) {

        String id = bannerList.get(position).getId();
        String heading = bannerList.get(position).getHeading();
        String description = bannerList.get(position).getDescription();
        String image_url = bannerList.get(position).getImage_url();
        String added_date = bannerList.get(position).getAdded_date();

        holder.tv_topic.setText(heading);

//        Picasso.with(context).load(bannerList.get(position).getImage_url()).priority(HIGH)
//                .fit().centerCrop().into(holder.iv_thumb);

        Picasso.with(context).load(image_url).priority(HIGH)
                .resize(700, 1000).centerInside().into(holder.iv_thumb);


   //     Glide.with(context).load(image_url).placeholder(R.drawable.no_image).into(holder.iv_thumb);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BlogDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("news_id",id);
                bundle.putString("heading",heading);
                bundle.putString("added_date",added_date);
                bundle.putString("description",description);
                bundle.putString("image_url",image_url);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }
}

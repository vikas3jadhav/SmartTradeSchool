package com.smarttradeschool.in.adapter;

import androidx.recyclerview.widget.RecyclerView;

public class BlogCommentAdapter extends RecyclerView.Adapter<BlogCommentAdapter.ViewHolder>{

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
}


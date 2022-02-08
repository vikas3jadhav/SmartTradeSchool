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


import com.smarttradeschool.in.R;
import com.smarttradeschool.in.model.NotificationModel;

import java.util.ArrayList;


/**
 * Created by wolfsoft4 on 28/12/18.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    Context context;
    private ArrayList<NotificationModel> notificationList;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tv_msg.setText(notificationList.get(position).getHeading());
        holder.tv_added_date.setText(notificationList.get(position).getAdded_date());

        String type = notificationList.get(position).getType();
        String type_id = notificationList.get(position).getType_id();



        if(type.equalsIgnoreCase("Message")){
            holder.iv_person.setImageResource(R.drawable.ic_noti_1);
        }else  if(type.equalsIgnoreCase("Video")){
            holder.iv_person.setImageResource(R.drawable.ic_noti_3);
        }else  if(type.equalsIgnoreCase("News")){
            holder.iv_person.setImageResource(R.drawable.ic_noti_4);
        }else{
            holder.iv_person.setImageResource(R.drawable.ic_noti_1);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(type.equalsIgnoreCase("Zoom")){
//
//                }else {
//                    Intent intent = new Intent(context, NotificationDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("type", type);
//                    bundle.putString("type_id", type_id);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_person;
        TextView tv_msg,tv_added_date;

        public ViewHolder(View itemView) {

            super(itemView);

            iv_person=itemView.findViewById(R.id.iv_noti);
            tv_msg=itemView.findViewById(R.id.tv_msg);
            tv_added_date = itemView.findViewById(R.id.tv_added_date);


        }
    }
}

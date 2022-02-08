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
import com.smarttradeschool.in.activity.MessageActivity;
import com.smarttradeschool.in.activity.MessageDetailActivity;
import com.smarttradeschool.in.activity.VideoDetailActivity;
import com.smarttradeschool.in.model.MessageModel;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    Context context;
    List<MessageModel> messageList;

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

    public MessageAdapter(Context context,List<MessageModel> messageList){
        this.context = context;
        this.messageList = messageList;
    }


    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_single_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder holder, int position) {

        String heading = messageList.get(position).getHeading();
        String description = messageList.get(position).getDescription();
        String added_date = messageList.get(position).getAdded_date();

        holder.tv_msg.setText(messageList.get(position).getHeading());
        holder.tv_added_date.setText(messageList.get(position).getAdded_date());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("heading",heading);
                bundle.putString("added_date",added_date);
                bundle.putString("description",description);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}

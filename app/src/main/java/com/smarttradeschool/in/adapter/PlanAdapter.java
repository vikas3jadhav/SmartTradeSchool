package com.smarttradeschool.in.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.smarttradeschool.in.R;
import com.smarttradeschool.in.activity.PaidClientFormActivity;
import com.smarttradeschool.in.model.PlanModel;
import com.smarttradeschool.in.utils.Watchinter;

import java.util.ArrayList;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    Context context;
    List<PlanModel> planList = new ArrayList<>();
    Watchinter watchinter;
    int row_index = 100;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_sub_name, txt_sub_price, tv_non_refundable;
        LinearLayout card_view;
        LinearLayout ly_annual;
        Button btn_plan;


        public ViewHolder(View view) {
            super(view);
            txt_sub_name = (TextView) view.findViewById(R.id.txt_sub_name);
            txt_sub_price = (TextView) view.findViewById(R.id.txt_sub_price);
            card_view = (LinearLayout) view.findViewById(R.id.card_view);
            ly_annual = (LinearLayout) view.findViewById(R.id.ly_annual);
            tv_non_refundable = view.findViewById(R.id.tv_non_refundable);
            btn_plan = view.findViewById(R.id.btn_plan);

        }
    }

    public PlanAdapter(Context context, List<PlanModel> planList, Watchinter watchinter) {
        this.context = context;
        this.planList = planList;
        this.watchinter = watchinter;
    }


    @Override
    public PlanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_plan_single_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlanAdapter.ViewHolder holder, int position) {
        String plan_name = planList.get(position).getName();
        String days = planList.get(position).getDays();
        String price = planList.get(position).getPrice();

        holder.txt_sub_name.setText(plan_name);
        holder.txt_sub_price.setText("₹ " + price);

//        if (row_index == position) {
//            holder.ly_annual.setBackground(context.getResources().getDrawable(R.drawable.round_bor_yellow));
//            holder.ly_annual.setBackgroundColor(Color.parseColor("#ffbb00"));
//        } else {
//            holder.ly_annual.setBackground(context.getResources().getDrawable(R.drawable.round_bor_gray));
//            holder.ly_annual.setBackgroundColor(Color.parseColor("#ffffff"));
//        }

        if (position % 7 == 0) {
            holder.card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.flatPurple));
            holder.txt_sub_name.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.txt_sub_price.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.tv_non_refundable.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.btn_plan.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.btn_plan.setBackgroundResource(R.drawable.rect_white);
        } else if (position % 7 == 1) {
            holder.card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.lineRed));
            holder.txt_sub_name.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.txt_sub_price.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tv_non_refundable.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.btn_plan.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.btn_plan.setBackgroundResource(R.drawable.rect_black);
        } else if (position % 7 == 2) {
            holder.card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
            holder.txt_sub_name.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.txt_sub_price.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.tv_non_refundable.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.btn_plan.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.btn_plan.setBackgroundResource(R.drawable.rect_white);
        } else if (position % 7 == 3) {
            holder.card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_screen3));
            holder.txt_sub_name.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.txt_sub_price.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tv_non_refundable.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.btn_plan.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.btn_plan.setBackgroundResource(R.drawable.rect_black);
        } else if (position % 7 == 4) {
            holder.card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.flatPink));
            holder.txt_sub_name.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.txt_sub_price.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.tv_non_refundable.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.btn_plan.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.btn_plan.setBackgroundResource(R.drawable.rect_white);
        } else if (position % 7 == 5) {
            holder.card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
            holder.txt_sub_name.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.txt_sub_price.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tv_non_refundable.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.btn_plan.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.btn_plan.setBackgroundResource(R.drawable.rect_black);
        } else if (position % 7 == 6) {
            holder.card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.greyDark));
            holder.txt_sub_name.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.txt_sub_price.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.tv_non_refundable.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.btn_plan.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.btn_plan.setBackgroundResource(R.drawable.rect_white);
        }

//        if (position % 2 == 1) {
//            holder.card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
//            holder.txt_sub_name.setTextColor(ContextCompat.getColor(context, R.color.white));
//            holder.txt_sub_price.setTextColor(ContextCompat.getColor(context, R.color.white));
//            holder.tv_non_refundable.setTextColor(ContextCompat.getColor(context, R.color.white));
//        } else {
//            holder.card_view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
//            holder.txt_sub_name.setTextColor(ContextCompat.getColor(context, R.color.black));
//            holder.txt_sub_price.setTextColor(ContextCompat.getColor(context, R.color.black));
//            holder.tv_non_refundable.setTextColor(ContextCompat.getColor(context, R.color.black));
//        }

//        holder.card_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                row_index = position;
//                notifyDataSetChanged();
//                if (position % 2 == 1) {
//                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
//                }
//
//                watchinter.remove_item("" + position);
//
//            }
//        });

        holder.btn_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PaidClientFormActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Select_item_name",plan_name);
                bundle.putString("Select_item_price",price);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return planList.size();
    }
}

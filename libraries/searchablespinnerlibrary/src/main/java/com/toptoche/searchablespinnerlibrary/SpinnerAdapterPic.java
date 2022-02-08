package com.toptoche.searchablespinnerlibrary;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SpinnerAdapterPic extends ArrayAdapter<String> {

    private Context ctx;
    private List<String> contentArray;
    private List<String> imageArray;

    public SpinnerAdapterPic(Context context, int resource, List<String> objects, List<String> imageArray) {
        super(context,  R.layout.spinner_value_layout, R.id.spinnerTextView, objects);
        this.ctx = context;
        this.contentArray = objects;
        this.imageArray = imageArray;
    }

    public SpinnerAdapterPic(Context context, int spinner_value_layout, List items) {
        super(context,  R.layout.spinner_value_layout, R.id.spinnerTextView, items);
        this.ctx = context;
        this.contentArray = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_value_layout, null);

        }
        TextView textView = (TextView) convertView.findViewById(R.id.spinnerTextView);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.spinnerImages);

        if(position==0) {
            textView.setText(contentArray.get(position));
            imageView.setImageResource(R.drawable.userdefault);
        }else {
            ModelClass  modelClass = new ModelClass(ctx);
            textView.setText(contentArray.get(position));

            imageView.setImageURI(Uri.parse(modelClass.getImgurl()));

        }
        return convertView;

    }

}

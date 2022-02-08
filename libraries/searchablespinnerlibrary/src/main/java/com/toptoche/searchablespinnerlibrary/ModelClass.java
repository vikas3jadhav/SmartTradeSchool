package com.toptoche.searchablespinnerlibrary;

import android.content.Context;


public class ModelClass {
    private String imgurl;
    Context context;
    ModelClass(Context context){
        this.context=context;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}

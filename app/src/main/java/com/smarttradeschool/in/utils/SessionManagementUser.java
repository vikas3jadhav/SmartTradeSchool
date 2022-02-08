package com.smarttradeschool.in.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import com.smarttradeschool.in.MainActivity;
import com.smarttradeschool.in.activity.HomeActivity;
import com.smarttradeschool.in.activity.MobileOtpActivity;

import java.util.HashMap;

public class SessionManagementUser {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "smart_trade_school";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "false";

    public static final String KEY_ID = "id";
    public static final String KEY_DOB = "dob";
    public static final String KEY_TRADING_SEGMENT = "trading_segment";
    public static final String KEY_FULL_NAME = "full_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE_NO = "phone_no";

    // Constructor
    public SessionManagementUser(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String id, String dob,String trading_segment,
                                   String full_name, String email,String phone_no){
        // Storing login value as TRUE

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_ID, id);
        // Storing name in pref
        editor.putString(KEY_DOB, dob);
        editor.putString(KEY_TRADING_SEGMENT, trading_segment);
        editor.putString(KEY_FULL_NAME, full_name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE_NO, phone_no);

        editor.commit();
    }

    public void checkLoginUser() {
        // CheckActivity login status
        if (this.isLoggedIn()) {
            // user is not logged in redirect him to LoginActivity Activity
            Intent i = new Intent(_context, HomeActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring LoginActivity Activity
            _context.startActivity(i);
        }

    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_DOB, pref.getString(KEY_DOB, null));
        user.put(KEY_TRADING_SEGMENT, pref.getString(KEY_TRADING_SEGMENT,null));
        user.put(KEY_FULL_NAME, pref.getString(KEY_FULL_NAME,null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL,null));
        user.put(KEY_PHONE_NO, pref.getString(KEY_PHONE_NO,null));
        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, MobileOtpActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring LoginActivity Activity
        _context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }




}

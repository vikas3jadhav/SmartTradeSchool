package com.smarttradeschool.in.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smarttradeschool.in.Fragment.BlogFragment;
import com.smarttradeschool.in.Fragment.MessageFragment;
import com.smarttradeschool.in.Fragment.VideoFragment;
import com.smarttradeschool.in.MainActivity;
import com.smarttradeschool.in.R;
import com.smarttradeschool.in.utils.BottomNavigationViewHelper;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadFragment(new MainActivity());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setItemHorizontalTranslationEnabled(false);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {

            case R.id.bottomNavigationAlarmMenuId:
                fragment = new MainActivity();
                break;

            case R.id.bottomNavigationClockMenuId:
                fragment = new MessageFragment();
                break;

            case R.id.bottomNavigationDocumentMenuId:
                fragment = new VideoFragment();
                break;

            case R.id.bottomNavigationTimerMenuId:
                fragment = new BlogFragment();
                break;

            case R.id.bottomNavigationZoomMenuId:
                fragment = new NotificationActivity();
                break;

//            case R.id.bottomNavigationStopWatchMenuId:
//                fragment = new Profile();
//                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLoad, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                //.setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HomeActivity.super.onBackPressed();
                        //   quit();
                    }
                }).create().show();
    }


}
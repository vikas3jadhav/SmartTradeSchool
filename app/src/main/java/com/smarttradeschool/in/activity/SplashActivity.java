
package com.smarttradeschool.in.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.smarttradeschool.in.R;

public class SplashActivity extends AppCompatActivity {

    Animation bounce;
    ImageView img_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        bounce = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);

     //   bounce.setAnimationListener(this);

        img_logo = findViewById(R.id.img_logo);

        img_logo.startAnimation(bounce);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent i = new Intent(SplashActivity.this, MobileOtpActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}
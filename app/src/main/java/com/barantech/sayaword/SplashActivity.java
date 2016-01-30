package com.barantech.sayaword;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.View;
import android.view.Window;

public class SplashActivity extends AppCompatActivity {

    View mIvLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mIvLogo = findViewById(R.id.iv_logo);
        mIvLogo.animate()
                .alpha(1f)
                .setDuration(1000);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(SplashActivity.this, mIvLogo, "robot");
                startActivity(new Intent(SplashActivity.this, WizardActivity.class), options.toBundle());

            }
        }, 3*1000);

    }
}

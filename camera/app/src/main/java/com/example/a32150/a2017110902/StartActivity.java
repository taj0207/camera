package com.example.a32150.a2017110902;

import android.content.Context;
import android.content.Intent;
import android.icu.text.AlphabeticIndex;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    private int nowPicPos = 0;
    private int[] imgRes = {R.drawable.car};
    public ImageView iv;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        context = this;

        iv=(ImageView)findViewById(R.id.imgCar);
        //fadeOutAndHideImage(iv);
        iv.setVisibility(View.VISIBLE);
        fadeInAndShowImage(iv);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent it = new Intent(StartActivity.this, MainActivity.class);
                startActivity(it);
            }
        },3000);
        //scale(iv);
    }



    private void fadeOutAndHideImage(final ImageView img){
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(5000);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                //nowPicPos %= 2;
                img.setImageResource(imgRes[0]);
                //nowPicPos++;
                fadeInAndShowImage(img);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });
        img.startAnimation(fadeOut);
    }

    public void scale(final ImageView img) {
        Animation am = new ScaleAnimation(0.0f, 2.0f, 0.0f, 2.0f);
        am.setDuration(2000);
        am.setRepeatCount(0);
        img.startAnimation(am);
    }

    private void fadeInAndShowImage(final ImageView img){
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(1000);
        fadeIn.setRepeatCount(0);
//        fadeIn.setAnimationListener(new Animation.AnimationListener()
//        {
//            public void onAnimationEnd(Animation animation)
//            {
//                fadeOutAndHideImage(img);
//            }
//            public void onAnimationRepeat(Animation animation) {}
//            public void onAnimationStart(Animation animation) {}
//        });
        Animation scale = new ScaleAnimation(0.0f, 2.0f, 0.0f, 2.0f);
        scale.setDuration(2000);
        scale.setRepeatCount(0);
        scale.setFillAfter(true);

        Animation trans = new TranslateAnimation(60,60, 180, 180);

        AnimationSet as = new AnimationSet(false);
        as.addAnimation(fadeIn);
        as.addAnimation(scale);
        as.addAnimation(trans);

        img.startAnimation(as);
        //img.clearAnimation();
    }

    public void onClick(View v) {
        Intent it = new Intent(StartActivity.this, MainActivity.class);
        startActivity(it);
    }
}
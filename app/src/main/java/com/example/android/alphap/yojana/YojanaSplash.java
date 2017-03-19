package com.example.android.alphap.yojana;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.android.alphap.R;

/**
 * Created by yojanasharma on 3/15/17.
 */

public class YojanaSplash extends Activity {

    ImageView potatoIV, cloudParallaxIV, clouds, taterPopLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yojana_splash);

        clouds = (ImageView) findViewById(R.id.moving_clouds);
        potatoIV = (ImageView) findViewById(R.id.image_potato);
        cloudParallaxIV = (ImageView) findViewById(R.id.parallax_clouds);
        taterPopLogo = (ImageView) findViewById(R.id.tater_pop_logo);
        taterPopLogo.setVisibility(View.INVISIBLE);


//        ObjectAnimator moveAnim = ObjectAnimator.ofFloat(potatoIV, "Y", 1500);
//        moveAnim.setDuration(2000);
//        moveAnim.setInterpolator(new BounceInterpolator());
//        moveAnim.start();


        TranslateAnimation slowAnimation = new TranslateAnimation(0f, 0f,
                -500f, 800f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        slowAnimation.setDuration(5000);  // animation duration
        slowAnimation.setFillAfter(true);


        TranslateAnimation animation = new TranslateAnimation(0f, 0f,
                -1000f, 800f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(1500);  // animation duration
        animation.setRepeatCount(2);  // animation repeat count
//        animation.setRepeatMode(2);   // repeat animation (left to right, right to left )
        animation.setFillAfter(true);
        animation.setRepeatMode(Animation.RESTART);

//        clouds.startAnimation(animation);

        Animation.AnimationListener name = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation taterTop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_offscreen_top);
                potatoIV.startAnimation(taterTop);
                potatoIV.setVisibility(View.GONE);

//                Animation tPopLogo = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
//                taterPopLogo.startAnimation(tPopLogo);
//                taterPopLogo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        animation.setAnimationListener(name);
        clouds.startAnimation(animation);
        cloudParallaxIV.startAnimation(slowAnimation);


        taterPopLogo.postDelayed(new Runnable() {
            public void run() {
                fadeIn(taterPopLogo);
            }
        }, 6100);

    }

    void fadeIn(ImageView img) {

        Animation fadeIn = new AlphaAnimation(0, 1);  // the 1, 0 here notifies that we want the opacity to go from opaque (1) to transparent (0)
//        fadeIn.setInterpolator(new AccelerateInterpolator());
//        fadeIn.setStartOffset(500); // Start fading out after 500 milli seconds
        fadeIn.setDuration(2000); // Fadeout duration should be 1000 milli seconds

        img.startAnimation(fadeIn);
        img.setVisibility(View.VISIBLE);

    }

}
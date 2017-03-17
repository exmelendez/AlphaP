package com.example.android.alphap.yojana;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.android.alphap.R;

/**
 * Created by yojanasharma on 3/15/17.
 */

public class Yojana_Splash extends Activity {

    ImageView space_imageview;
    ImageView potato_imageview;
    ImageView plate_imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_yojana);

        space_imageview = (ImageView) findViewById(R.id.space_image);
        potato_imageview = (ImageView) findViewById(R.id.image_potato);
        plate_imageview = (ImageView) findViewById(R.id.image_plate);


        ObjectAnimator moveAnim = ObjectAnimator.ofFloat(potato_imageview, "Y", 1500);
        moveAnim.setDuration(2000);
        moveAnim.setInterpolator(new BounceInterpolator());
        moveAnim.start();


        TranslateAnimation animation = new TranslateAnimation(0.10f, 200f,
                0.10f, 0.10f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(10000);  // animation duration
        animation.setRepeatCount(5);  // animation repeat count
        animation.setRepeatMode(2);   // repeat animation (left to right, right to left )
        animation.setFillAfter(true);
        animation.setRepeatMode(Animation.RESTART);

        space_imageview.startAnimation(animation);



    }


}
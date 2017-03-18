package com.example.android.alphap.yojana;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.example.android.alphap.R;

/**
 * Created by yojanasharma on 3/15/17.
 */

public class Yojana_Splash extends Activity {

    ImageView space_imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashlayout);

        space_imageview = (ImageView) findViewById(R.id.iv_potato);

        ObjectAnimator moveAnim = ObjectAnimator.ofFloat(space_imageview, "Y", 1000);
        moveAnim.setDuration(2000);
        moveAnim.setInterpolator(new BounceInterpolator());
        moveAnim.start();


    }


}
package com.example.android.alphap;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

public class Splash extends Activity {

    ImageView potatoIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashlayout);

        potatoIV = (ImageView) findViewById(R.id.iv_potato);

        ObjectAnimator moveAnim = ObjectAnimator.ofFloat(potatoIV, "Y", 1000);
        moveAnim.setDuration(2000);
        moveAnim.setInterpolator(new BounceInterpolator());
        moveAnim.start();

        finish();



    }



}
package com.example.android.alphap;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.example.android.alphap.bluetoothmode.StartScreenActivity;

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

        moveAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(Splash.this, StartScreenActivity.class);
                        startActivity(intent);

                    }
                }, 3000);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        moveAnim.start();
    }

}
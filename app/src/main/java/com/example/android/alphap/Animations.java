package com.example.android.alphap;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by ridita on 3/4/17.
 */

public class Animations {

    private final Context context;

    public Animations(Context context){
        this.context = context;
    }

    public void slideInFromTop(View view){
        Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_top);
        view.startAnimation(fadeIn);
    }

}

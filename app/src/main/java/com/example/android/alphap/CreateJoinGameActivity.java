package com.example.android.alphap;

import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.alphap.eddie.MainActivityRVAdapter;

import java.util.ArrayList;

/**
 * Created by yojanasharma on 2/26/17.
 */

public class CreateJoinGameActivity extends AppCompatActivity {
    private ArrayList mainRvImages;
    private ArrayList mainRvTvAList;
    private TextView appName;
    private ImageView aboutUsIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen2);

        /* Setting Font Types */
        appName = (TextView) findViewById(R.id.product_name);
        Typeface satisfy_font = Typeface.createFromAsset(getAssets(), "fonts/Satisfy-Regular.ttf");
        appName.setTypeface(satisfy_font);

        initViews();

        final ImageView animImageView = (ImageView) findViewById(R.id.header_view2);
        animImageView.setBackgroundResource(R.drawable.sun_header_anim);
        animImageView.setImageAlpha(5);
        animImageView.post(new Runnable() {
            @Override
            public void run() {
                AnimationDrawable frameAnimation =
                        (AnimationDrawable) animImageView.getBackground();
                frameAnimation.start();
            }
        });

        aboutUsIcon = (ImageView) findViewById(R.id.fab);

    }


    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_rv);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        mainRvImages = new ArrayList<>();
        mainRvImages.add(R.drawable.tractor_clip_art_470px);
        mainRvImages.add(R.drawable.barn_clipart_470px);

        mainRvTvAList = new ArrayList<>();
        mainRvTvAList.add("Create");
        mainRvTvAList.add("Join");


        RecyclerView.Adapter adapter = new MainActivityRVAdapter(mainRvImages, mainRvTvAList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);

                    if (position == 0) {
                        Toast.makeText(getApplicationContext(), "Create Activity", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Join Activity", Toast.LENGTH_SHORT).show();

                    }
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    public void AboutUs(View view) {

        Toast.makeText(this, "About Us Fragment", Toast.LENGTH_SHORT).show();

    }
}
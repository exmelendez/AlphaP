package com.example.android.alphap.swipefunction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.android.alphap.R;
import com.example.android.alphap.bluetoothmode.JoinedPlayerActivity;


public class GamePlay extends Activity {

    ProgressBar progressBar;
    MyCountDownTimer myCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        myCountDownTimer = new MyCountDownTimer(15000, 1000);
        myCountDownTimer.start();


        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);

    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            int progress = (int) (millisUntilFinished/1000);

            progressBar.setProgress(progress);
        }

        @Override
        public void onFinish() {

            Intent someScreen = new Intent(GamePlay.this, JoinedPlayerActivity.class);
            startActivity(someScreen);
        }
    }

    private class ImagePagerAdapter extends PagerAdapter {

        Boolean potatoPresent = true;
        private int[] potatoNotPresentArr = new int[1];
        private int[] potatoPresentArr = new int[2];


        public int[] createArray() {

            if (potatoPresent) {
                potatoPresentArr[0] = R.drawable.potato_alone;
                potatoPresentArr[1] = R.drawable.blank_space;
                return potatoPresentArr;
            } else {

                potatoNotPresentArr[0] = R.drawable.blank_space;
                return potatoNotPresentArr;

            }

        }

        @Override
        public int getCount() {
            return createArray().length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getApplicationContext());
            int padding = getResources().getDimensionPixelSize(R.dimen.padding_medium);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageResource(createArray()[position]);
            container.addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

}

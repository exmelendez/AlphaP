package com.example.android.alphap.eddie;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.example.android.alphap.CreateJoinGameActivity;
import com.example.android.alphap.R;

public class Timer extends AppCompatActivity {
    ProgressBar progressBar;
    MyCountDownTimer myCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        myCountDownTimer = new MyCountDownTimer(20000, 1000);
        myCountDownTimer.start();

    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            int progress = (int) (millisUntilFinished/1000);
            if(progress <= 10 && progress > 5){
                progressBar.setBackgroundColor(Color.BLUE);
            }else if(progress <= 5){
                progressBar.setBackgroundColor(Color.RED);
            }

            progressBar.setProgress(progress);
        }

        @Override
        public void onFinish() {

            Intent someScreen = new Intent(Timer.this, CreateJoinGameActivity.class);
            startActivity(someScreen);
        }
    }

}

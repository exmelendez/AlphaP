package com.example.android.alphap.gamemode;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.alphap.R;

import java.util.Timer;
import java.util.TimerTask;

public class HotPotato extends AppCompatActivity {


    HostGameActivity hostOfGame;
    JoinedPlayerActivity JoinedPlayer;

    TextView shockClock;
    ImageView potatoDude;

    private int frameHight;
    private int potatoSize;


    //Position & checks
    private int potatoY;

    private boolean action_flag = false;
    private boolean start_flag = false;

    private Timer timer = new Timer();

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_potato);

        shockClock = (TextView) findViewById(R.id.time_clock_tv);
        potatoDude = (ImageView) findViewById(R.id.game_potato);

//        potatoY = 500;

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                shockClock.setText("Time: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                shockClock.setText("done!");
            }
        }.start();


    }

    public void changePosition() {
        //Will move potato

        //when toching
        if (action_flag == true) {
            potatoY -= 20;
        }
        //not tocuhing
        potatoY += 20;

        if(potatoY < 0) potatoY = 0;

        if (potatoY > frameHight - potatoSize) potatoY = frameHight -potatoSize;

        potatoDude.setY(potatoY);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (start_flag == false) {

            start_flag = true;

            FrameLayout frame = (FrameLayout)findViewById(R.id.frame_layout);
            frameHight = frame.getHeight();

            potatoY = (int) potatoDude.getY();
            potatoSize = potatoDude.getHeight();


            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePosition();
                        }
                    });
                }
            }, 0, 20);

        } else {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                action_flag = true;
            }

            else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                action_flag = false;

            }
        }
        return true;
    }
}
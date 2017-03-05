package com.example.android.alphap.gamemode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.android.alphap.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by asiagibson on 3/5/17.
 */

public class GamePlayActivity extends Activity {

    public static final int UPDATE_PERIOD = 10;
    public TextView gameClock;
    public int countDownDone;
    public Runnable game_main_thread;
    public boolean finished;
    GameView gamePlayView;
    Context context;
    public Runnable runnable;
    public TimerTask timer_task;
    public ViewGroup layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context = this;
        finished = false;
        countDownDone = 100;

        gameClock = (TextView) findViewById(R.id.game_timer);
        gamePlayView = new GameView(this, 1000);

        run();

        timer_task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(game_main_thread);
            }
        };

        layout = (ViewGroup) findViewById(R.id.game_play_main_layout);
        layout.addView(gamePlayView);

        update_game();

    }

    private void update_game() {
        Timer t = new Timer();
        t.scheduleAtFixedRate(timer_task, 0, UPDATE_PERIOD);
    }



    public void run() {

        if (!finished) {
            gamePlayView.update();

            countDownDone--;
            if (countDownDone < 0) {
                finished = true;
                gamePlayView.get_game_play().stop_players();
                if (BtConnectDevices.connection_socket != null) {
                    BtConnectDevices.connection_socket.cancel();
                }
                Intent startSceenIntent = new Intent(context,
                        StartScreenActivity.class);
                startActivity(startSceenIntent);
                if (BtConnectDevices.connection_socket != null) {
                    BtConnectDevices.connection_socket.cancel();
                }
                finish();
                return;
            }

        }

    }
}


//        timer_task=new TimerTask(){
//@Override
//public void run(){
//        runOnUiThread(game_main_thread);
//        }
//        };
//
//        layout=(ViewGroup)findViewById(R.id.game_play_main_layout);
//        layout.addView(gamePlayView);
//
//        update_game();
//
//        }
//
//private void update_game(){
//        Timer t=new Timer();
//        t.scheduleAtFixedRate(timer_task,0,UPDATE_PERIOD);
//        }
//        }
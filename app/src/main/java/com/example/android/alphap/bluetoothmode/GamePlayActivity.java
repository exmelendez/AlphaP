package com.example.android.alphap.bluetoothmode;

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
    TextView score1;
    TextView score2;
    public int countDownDone;
    public Runnable gameMainThread;
    public boolean finished;
    GameView gamePlayView;
    Context context;
    public Runnable runnable;
    public TimerTask timerTask;
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

        score1 = (TextView) findViewById(R.id.score_one);
        score2 = (TextView) findViewById(R.id.score_two);

        gameClock = (TextView) findViewById(R.id.game_timer);
        gamePlayView = new GameView(this, 1000);


        gameMainThread = new Runnable() {

            @Override
            public void run() {
                if (!finished) {
                    gamePlayView.update();

                    if (gamePlayView.getGamePlay().getGameState() == Game.PLAYING) {
                        score1.setText(gamePlayView.getGamePlay()
                                .getPlayer1Score() + "");
                        score2.setText(gamePlayView.getGamePlay()
                                .getPlayer2Score() + "");
                        gameClock.setText(gamePlayView.getGamePlay()
                                .getCurrentTime());

                        countDownDone--;
                        if (countDownDone < 0) {
                            finished = true;
                            gamePlayView.getGamePlay().stop_players();
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

                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(gameMainThread);
                        }
                    };

                    layout = (ViewGroup) findViewById(R.id.game_play_main_layout);
                    layout.addView(gamePlayView);

                    update_game();

                }
            }

            private void update_game() {
                Timer t = new Timer();
                t.scheduleAtFixedRate(timerTask, 0, UPDATE_PERIOD);
            }


        };
    }
}

package com.example.android.alphap.bluetoothmode;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by asiagibson on 3/4/17.
 */

public class Game {

    public static final int NUMBER_POTATOES = 5;
    private final Context context;
    final int gameSeed;
    final Random gameRandom;



    private int gameState;
    private final int screen_height;
    public static final int PLAYING = 3;
    private final int player1Score;
    private final int player2Score;
    int screen_width;
     ArrayList<Potato> potatoArrayList;

    String currentTime;
    boolean timeOut;
    int minutes;
    int seconds;
    int clockTicks;
    boolean isPassed;

    MediaPlayer ticksPlayer;
    AssetFileDescriptor ticksAfd;

    public Game(int gameSeed, int screen_width, int screen_height,
                Context context) {
        this.context = context;

        player1Score = 0;
        player2Score = 0;
        minutes = 1;
        seconds = 0;
        timeOut = false;

        tick();

        ticksPlayer = new MediaPlayer();
        this.gameSeed = gameSeed;
        gameRandom = new Random(gameSeed);
        this.screen_width = screen_width;
        this.screen_height = screen_height;

    }


    private void tick() {
        currentTime = "0" + minutes + ":";
        if (seconds < 10) {
            currentTime += "0" + seconds;
        } else {
            currentTime += seconds;
        }

        seconds--;
        if (seconds < 0) {
            seconds = 59;
            minutes--;
            if (minutes < 0) {
                timeOut = true;
            }
        }

        if (minutes == 0 && seconds == 9) {
            play_ticks();
        }
    }


    public void play_ticks() {
        try {
            ticksPlayer.reset();
            ticksAfd = context.getAssets().openFd("clock_ticks.mp3");
            ticksPlayer.setDataSource(ticksAfd.getFileDescriptor(),
                    ticksAfd.getStartOffset(), ticksAfd.getLength());
            ticksPlayer.prepare();
            ticksPlayer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        clockTicks++;
        if (clockTicks >= 1000 / GamePlayActivity.UPDATE_PERIOD) {
            clockTicks = 0;
            tick();
        }
    }

    public ArrayList<Potato> appearingPotatoes() {
        return potatoArrayList;
    }

    public int getGameState() {
        return gameState;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void potatoPassed() {
        isPassed = true;
        showPotato();
    }

    private void showPotato() {

    }


    public void stop_players() {
        ticksPlayer.release();

    }

}

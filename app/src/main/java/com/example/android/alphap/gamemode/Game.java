package com.example.android.alphap.gamemode;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

/**
 * Created by asiagibson on 3/4/17.
 */

public class Game {
     public static final int NUMBER_POTATOES = 7;
     private final Context context;

     private String current_time;
     private boolean time_out;
     int minutes;
     int seconds;
     int clock_ticks;

     MediaPlayer ticks_player;
     AssetFileDescriptor ticks_afd;

     public Game(int game_seed, int screen_width, int screen_height,
                 Context context) {
          this.context = context;

          minutes = 3;
          seconds = 0;
          time_out = false;

          tick();

          ticks_player = new MediaPlayer();

     }


     private void tick()
     {
          current_time = "0" + minutes + ":";
          if (seconds < 10)
          {
               current_time += "0" + seconds;
          } else
          {
               current_time += seconds;
          }

          seconds--;
          if (seconds < 0)
          {
               seconds = 59;
               minutes--;
               if (minutes < 0)
               {
                    time_out = true;
               }
          }

          if (minutes == 0 && seconds == 9)
          {
               play_ticks();
          }
     }


     public void play_ticks()
     {
          try
          {
               ticks_player.reset();
               ticks_afd = context.getAssets().openFd("clock_ticks.mp3");
               ticks_player.setDataSource(ticks_afd.getFileDescriptor(),
                       ticks_afd.getStartOffset(), ticks_afd.getLength());
               ticks_player.prepare();
               ticks_player.start();

          } catch (Exception e)
          {
               e.printStackTrace();
          }
     }

     public void update() {
          clock_ticks++;
          if (clock_ticks >= 1000 / GamePlayActivity.UPDATE_PERIOD)
          {
               clock_ticks = 0;
               tick();
          }
     }
     public void stop_players()
     {
          ticks_player.release();

     }

}

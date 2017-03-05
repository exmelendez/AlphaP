package com.example.android.alphap.gamemode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.View;

/**
 * Created by asiagibson on 3/5/17.
 */

public class GameView extends View {

    Point screen_size;

    Context context;
    Game game_play;

    public GameView(Context context, int imgPotato) {
        super(context);
        this.context = context;

        screen_size = new Point();
        game_play = new Game(imgPotato, screen_size.x, screen_size.y, context);

    }

    public void update() {
        game_play.update();
        invalidate();
    }
    public Game get_game_play()
    {
        return game_play;
    }
}

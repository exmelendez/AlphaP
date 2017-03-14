package com.example.android.alphap.bluetoothmode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

import com.example.android.alphap.R;

import java.util.Iterator;

/**
 * Created by asiagibson on 3/5/17.
 */

public class GameView extends View {

    Point screen_size;
    Bitmap[] potatoes;

    Context context;
    Game game_play;
    Paint paintPotato;
    Bitmap potato;

    public GameView(Context context, int game_seed) {
        super(context);
        this.context = context;

        potatoes = new Bitmap[Game.NUMBER_POTATOES];
        potatoes[Potato.POTATO_DUDE] = BitmapFactory.decodeResource(getResources(),
                R.drawable.tater_todd);
        potatoes[Potato.POTATO_HARY] = BitmapFactory.decodeResource(getResources(),
                R.drawable.harry_potato);
        potatoes[Potato.POTATO_TITO] = BitmapFactory.decodeResource(getResources(),
                R.drawable.tito_tater);
        potatoes[Potato.POTATO_THELMA] = BitmapFactory.decodeResource(
                getResources(), R.drawable.thelma_tater);
        potatoes[Potato.POTATO_TONEE] = BitmapFactory.decodeResource(
                getResources(), R.drawable.tonee_tater);

        screen_size = new Point();
        game_play = new Game(game_seed, screen_size.x, screen_size.y, context);

        paintPotato = new Paint();
        potato = BitmapFactory.decodeResource(getResources(),
                R.drawable.potatobuddy);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPotatoes(canvas);

    }

    private void drawPotatoes(Canvas canvas) {

        Iterator<Potato> potatoIterator = game_play.appearingPotatoes().iterator();
        while (potatoIterator.hasNext())
        {
            Potato potato1 = potatoIterator.next();
            canvas.drawBitmap(potatoes[potato1.get_type()], potato1.get_x(),
                    potato1.get_y(), null);
        }
    }


    public void update() {
        game_play.update();
        invalidate();
    }

    public Game getGamePlay() {
        return game_play;
    }
}

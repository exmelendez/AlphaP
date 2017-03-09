package com.example.android.alphap.gamemode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.android.alphap.R;

/**
 * Created by asiagibson on 3/4/17.
 */

public class StartScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public void toNewGame(View view)
    {
        Intent newGameIntent = new Intent(this, HostGameActivity.class);
        startActivity(newGameIntent);
    }

    public void toJoinGame(View view)
    {
        Intent join_game_intent = new Intent(this, JoinedPlayerActivity.class);
        startActivity(join_game_intent);
    }

    public void exit(View view)
    {
        finish();
        System.exit(0);
    }

    @Override
    public void onBackPressed()
    {
        finish();
        System.exit(0);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        finish();
    }



}

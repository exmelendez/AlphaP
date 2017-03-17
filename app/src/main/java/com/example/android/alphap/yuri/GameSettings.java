package com.example.android.alphap.yuri;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.android.alphap.R;

import java.util.Timer;

public class GameSettings extends AppCompatActivity {
    private String name;
    private int players;
    private boolean timeVisibility;
    Button startGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

        startGame = (Button) findViewById(R.id.strt_btn);

        startGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent startGame = new Intent(GameSettings.this, Timer.class);
                startActivity(startGame);
            }
        });
    }

    public GameSettings(String name, int players, boolean timeVisibility) {
        this.name = name;
        this.players = players;
        this.timeVisibility = timeVisibility;
    }

    public String getName() {
        return name;
    }

    public int getPlayers() {
        return players;
    }

    public boolean isTimeVisibility() {
        return timeVisibility;
    }
}



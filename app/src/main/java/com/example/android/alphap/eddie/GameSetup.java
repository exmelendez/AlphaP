package com.example.android.alphap.eddie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.android.alphap.R;

public class GameSetup extends AppCompatActivity {

    Button startGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        startGame = (Button) findViewById(R.id.strt_btn);

        startGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent startGame = new Intent(GameSetup.this, Timer.class);
                startActivity(startGame);
            }
        });
    }

}

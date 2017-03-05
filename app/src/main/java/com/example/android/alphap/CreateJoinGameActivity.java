package com.example.android.alphap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by yojanasharma on 2/26/17.
 */

public class CreateJoinGameActivity extends AppCompatActivity {

    Button createBtn, joinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_join_game_layout);

        createBtn = (Button) findViewById(R.id.create_button);
        joinBtn = (Button) findViewById(R.id.join_button);
    }

    void createGameNav(View view){

        Intent navToCreateGame = new Intent(this, CreateGame.class);
        startActivity(navToCreateGame);

    }
}
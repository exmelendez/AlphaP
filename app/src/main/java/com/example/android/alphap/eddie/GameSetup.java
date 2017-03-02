package com.example.android.alphap.eddie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.alphap.R;

public class GameSetup extends AppCompatActivity {

    EditText hostName;
    Boolean swipeMode, shuffleMode;
    int numOfPlayers;
    Button startLobby;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);

//        startLobby = (Button) findViewById();
    }


    void moveToLobby() {

        if (hostName.length() > 0 && swipeMode) {

//            Toast toast = Toast.makeText(startLobby.getContext(), "Some msg", duration).show();
        }

        if (hostName.length() > 0 && shuffleMode) {

//            Toast toast = Toast.makeText(startLobby.getContext(), "Some msg", duration).show();

        }

    }

}

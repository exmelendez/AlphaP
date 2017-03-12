package com.example.android.alphap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.android.alphap.gamemode.JoinedPlayerActivity;

public class MainActivity extends AppCompatActivity {


//    public void firstbutton(View view) {
//        Intent intent = new Intent(this, JoinedPlayerActivity.class);
//        startActivity(intent);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
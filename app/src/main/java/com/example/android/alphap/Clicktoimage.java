package com.example.android.alphap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ToggleButton;

/**
 * Created by yojanasharma on 3/4/17.
 */

public class Clicktoimage extends AppCompatActivity {

    ToggleButton on;
    ImageView img;
    private static final String TAG = "Clicktoimage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visible_invisible_button);

        Log.d(TAG, "onCreate: image");
        img = (ImageView) findViewById(R.id.imageView);

        on = (ToggleButton) findViewById(R.id.toggleButton1);

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (on.isChecked()) {
                        img.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "onClick: invisible");
                } else {
                    img.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onClick: visible");
                }

            }
        });
    }
}
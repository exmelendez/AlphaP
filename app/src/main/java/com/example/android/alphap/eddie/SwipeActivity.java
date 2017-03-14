package com.example.android.alphap.eddie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.alphap.R;

public class SwipeActivity extends AppCompatActivity {

    private ViewGroup parent;
    private ImageView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        cardView = (ImageView) findViewById(R.id.tater_logo);
        parent = (ViewGroup) findViewById(R.id.card_parent_layout);
        SwipeCallback callback = createSwipeCallback();
        SwipeListener listener = new SwipeListener(cardView, callback, parent, parent.getPaddingLeft(), parent.getPaddingTop(), 15f, 0f);
        cardView.setOnTouchListener(listener);
    }

    public SwipeCallback createSwipeCallback() {
        return new SwipeCallback() {
            @Override
            public void cardSwipedLeft(View card) {
                Toast.makeText(SwipeActivity.this, "LEFT!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cardSwipedRight(View card) {
                Toast.makeText(SwipeActivity.this, "RIGHT!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cardOffScreen(View card) {

            }

            @Override
            public boolean isDragEnabled() {
                return true;
            }
        };
    }
}

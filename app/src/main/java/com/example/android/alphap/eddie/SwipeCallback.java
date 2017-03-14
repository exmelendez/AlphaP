package com.example.android.alphap.eddie;

import android.view.View;

public interface SwipeCallback {

    void cardSwipedLeft(View card);

    void cardSwipedRight(View card);

    void cardOffScreen(View card);

    boolean isDragEnabled();

}

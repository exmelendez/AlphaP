package com.example.android.alphap.gamemode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.android.alphap.R;

/**
 * Created by asiagibson on 3/5/17.
 */

public class HostGameActivity extends AppCompatActivity {


    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_host_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mContext = this;
        BtConnectDevices.establish_connection(mContext);
        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                while (true)
                {
                    if (BtConnectDevices.is_ready && BtConnectDevices.connection_socket.read_next() == -2)
                    {
                        Intent intent = new Intent(mContext,
                            HotPotato.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        }).start();
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        finish();
    }



    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        BtConnectDevices.connection_socket.cancel();
        Intent launch_screen_intent = new Intent(this, StartScreenActivity.class);
        startActivity(launch_screen_intent);
        finish();
    }

}

package com.example.android.alphap.gamemode;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.alphap.R;

import java.util.Set;

/**
 * Created by asiagibson on 3/4/17.
 */

public class LobbyActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = -1;
    ListView player_devices;
    ArrayAdapter<String> arrayAdapter;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket transferSocket;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lobby);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bluetoothAdapter = BtConnectDevices.mBluetoothAdapter;
        context = this;

        player_devices = (ListView) findViewById(R.id.player_device);
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        player_devices.setAdapter(arrayAdapter);
        player_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View v, int arg2,
                                    long arg3) {
                String info = ((TextView) v).getText().toString();
                String address = info.substring(info.length() - 17);

                ConnectThread connect_thread = new ConnectThread(
                        bluetoothAdapter.getRemoteDevice(address));
                connect_thread.run();

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (true) {
                            if (BtConnectDevices.connection) {
                                BtConnectDevices.connection_socket = new ConnectionSocket(
                                        BtConnectDevices.transferSocket);
                                BtConnectDevices.connection_socket.write((byte) -2);

                                Intent game_intent = new Intent(context,
                                        HotPotato.class);
                                startActivity(game_intent);
                                break;
                            }
                        }
                    }
                }).start();
            }
        });
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        enable_bluetooth();
        registerReceiver(ActionFoundReceiver, new IntentFilter(
                BluetoothDevice.ACTION_FOUND));
        scan();
    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent launch_screen_intent = new Intent(this, StartScreenActivity.class);
        startActivity(launch_screen_intent);
        finish();
    }

    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                arrayAdapter.add(device.getName() + "\n" + device.getAddress());
                arrayAdapter.notifyDataSetChanged();
            }

        }
    };

    private void enable_bluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
        } else {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(ActionFoundReceiver);
    }

    public void scan() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter
                .getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                arrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
        bluetoothAdapter.startDiscovery();
        arrayAdapter.clear();
    }
}

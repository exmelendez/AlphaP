package com.example.android.alphap.bluetoothmode;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {

    private final BluetoothSocket mmSocket;

    public ConnectThread(BluetoothDevice device) {

        BluetoothSocket tmp = null;


        try {

            UUID uuid = UUID.fromString("0000110E-0000-1000-8000-00805F9B34FB");
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
        }
        mmSocket = tmp;
    }

    public void run() {

        try {

            mmSocket.connect();
            BtConnectDevices.transferSocket = mmSocket;
            BtConnectDevices.connection = true;
        } catch (IOException connectException) {
            try {
                mmSocket.close();
            } catch (IOException closeException) {
            }
            return;
        }
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }
}

package com.example.android.alphap.gamemode;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by asiagibson on 3/4/17.
 */

public class ConnectionSocket {
    public static final int ERROR_RETURN = -16;
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    public ConnectionSocket(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public int read_next() {
        byte[] buffer = new byte[4];
        try {
            mmInStream.read(buffer);
            Log.e("msg", buffer[0] + "");
            return buffer[0];
        } catch (IOException e) {
            return ERROR_RETURN;
        }
    }

    public void write(byte value) {
        try {
            mmOutStream.write(value);
        } catch (IOException e) {
        }
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (Exception e) {
        }
    }
}

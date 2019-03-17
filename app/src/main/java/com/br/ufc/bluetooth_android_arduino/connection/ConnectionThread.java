package com.br.ufc.bluetooth_android_arduino.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by naelio on 02/02/2018.
 */

public class ConnectionThread extends Thread {

    InputStream input = null;
    OutputStream output = null;
    private boolean server;
    private boolean running = false;
    private String btDevAddress = null;
    private String myUUID = "00001101-0000-1000-8000-00805F9B34FB";
    private BluetoothSocket btSocket = null;
    private BluetoothServerSocket btServerSocket = null;

    // act as a server
    public ConnectionThread() {
        this.server = true;
    }

    // act as a customer
    public ConnectionThread(String btDevAddress) {
        this.server = false;
        this.btDevAddress = btDevAddress;
    }

    public void run() {
        this.running = true;
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (this.server) {// if you are a server
            try {
                //USADO PARA ESTABELECER CONEXÃO, FICARÁ EM MODO DE ESPERA ATÉ A CONEXÃO SER REALIZADA
                btServerSocket = btAdapter.listenUsingRfcommWithServiceRecord("Super Bluetooth", UUID.fromString(myUUID));
                btSocket = btServerSocket.accept();

                // if connection OK, free socket
                if (btSocket != null) {
                    btServerSocket.close();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                toMainActivity("---N".getBytes());
            }


        } else {// if you are a customer
            try {
                BluetoothDevice btDevice = btAdapter.getRemoteDevice(btDevAddress);
                btSocket = btDevice.createRfcommSocketToServiceRecord(UUID.fromString(myUUID));

                btAdapter.cancelDiscovery();

                if (btSocket != null){
                    btSocket.connect();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                toMainActivity("---N".getBytes());
            }
        }

        // receive
        if (btSocket != null) {
            try {

                // reference to input and output of data
                input = btSocket.getInputStream();
                output = btSocket.getOutputStream();

                byte[] buffer = new byte[1024]; // save msg in bytes
                int bytes; // qtd at bytes


                // 1 => FICAR EM ESTADO DE ESPERA ATE RECEBER ALGO
                // 2 => ARMAZENAR A MENSAGEM NO BUFFER
                // 3 => ENVIA A MENSAGEM RECEBIDA EM BYTES
                // 4 => FICARA EM ESTADO TRUE ATE A VARIAVEL 'running' ASSUMIR FALSE
                while (running) {
                    bytes = input.read(buffer);
                    toMainActivity(Arrays.copyOfRange(buffer, 0, bytes));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                toMainActivity("---N".getBytes());
            }
        }
    }// final run

    private void toMainActivity(byte[] data) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putByteArray("data", data);
        message.setData(bundle);
    }

    public void write(byte[] data) { // send data

        if (output != null) {
            try {
                output.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            toMainActivity("---N".getBytes());
        }
    }


    public void cancel() { // close connection
        try {
            running = false;
            if (btSocket != null) btSocket.close();
            if (btServerSocket != null) btServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = false;
    }

}


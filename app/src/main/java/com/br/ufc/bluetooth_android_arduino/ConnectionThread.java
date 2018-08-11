package com.br.ufc.bluetooth_android_arduino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by naelio on 02/02/2018.
 */

public class ConnectionThread extends Thread {


    private boolean server;
    private boolean running = false;

    private String btDevAddress = null;
    private String myUUID = "00001101-0000-1000-8000-00805F9B34FB";

    private BluetoothSocket btSocket = null;
    private BluetoothServerSocket btServerSocket = null;

    // ENTRADA E SAIDA DE DADOS
    InputStream input = null;
    OutputStream output = null;

    // ATUAR COMO SERVIDOR
    public ConnectionThread() {
        this.server = true;
    }

    // ATUAR COMO CLIENTE
    public ConnectionThread(String btDevAddress) {
        this.server = false;
        this.btDevAddress = btDevAddress;
    }

    public void run(){
        this.running = true;
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        // SE FOR ATUAR COMO SERVIDOR
        if(this.server){
            try{
                //USADO PARA ESTABELECER CONEXÃO, FICARÁ EM MODO DE ESPERA ATÉ A CONEXÃO SER REALIZADA
                btServerSocket = btAdapter.listenUsingRfcommWithServiceRecord("Super Bluetooth", UUID.fromString(myUUID));
                btSocket = btServerSocket.accept();

                // SE A CONEXÃO ESTABELECIDA LIBERA O SOCKET
                if(btSocket != null)
                    btServerSocket.close();

            }catch (IOException ex){
                ex.printStackTrace();
                toMainActivity("---N".getBytes());
            }

        // SE FOR ATUAR COMO CLIENTE
        }
        else{
            try {
                BluetoothDevice btDevice = btAdapter.getRemoteDevice(btDevAddress);
                btSocket = btDevice.createRfcommSocketToServiceRecord(UUID.fromString(myUUID));

                btAdapter.cancelDiscovery();

                if (btSocket != null)
                    btSocket.connect();



            }catch (IOException ex){
                ex.printStackTrace();
                toMainActivity("---N".getBytes());
            }
        }

        // RECEBER
        if(btSocket != null){

            // AVISANDO QUE A CONEXÃO FOI ESTABELECIDA A ACTIVITY PRINCIPAL
            toMainActivity("---S".getBytes());
            try {

                // REFERENCIAS PARA ENTRADA E SAIDA DE DADOS
                input = btSocket.getInputStream();
                output = btSocket.getOutputStream();

                byte[] buffer = new byte[1024]; // GUARDAR A MENSAGEM EM BYTES
                int bytes; // REPRESENTAR O NUMERO DE BYTES LIDOS


                // 1 => FICAR EM ESTADO DE ESPERA ATE RECEBER ALGO
                // 2 => ARMAZENAR A MENSAGEM NO BUFFER
                // 3 => ENVIA A MENSAGEM RECEBIDA EM BYTES
                // 4 => FICARA EM ESTADO TRUE ATE A VARIAVEL 'running' ASSUMIR FALSE
                while(running) {
                    bytes = input.read(buffer);
                    toMainActivity(Arrays.copyOfRange(buffer, 0, bytes));
                }
            }catch (IOException ex){
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
//        MainActivity.handler.sendMessage(message);
    }

    /**
     * Escrever Dados
     */
    public void write(byte[] data) {

        if(output != null) {
            try {
                output.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            toMainActivity("---N".getBytes());
        }
    }

    // METODO PARA ENCERRAR A CONEXÃO
    public void cancel() {

        try {
            running = false;
            btServerSocket.close();
            btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = false;
    }

}


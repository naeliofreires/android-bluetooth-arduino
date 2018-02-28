package com.br.ufc.bluetooth_android_arduino;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /***********************************************/
    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;
    /***********************************************/

    /**
     * Show The Message
     */
    private static TextView statusMessage;

    /**
     * Buttons
     */
    private Button buttonAparelhosPareados;
    private Button buttonProcurarDispositivos;
    private Button buttonHabilitarVisibilidade;
    private Button buttonEnviarMensagem;
    private Button buttonOpenDialog;

    /**
     * ConnectionThread
     */
    private ConnectionThread connect;

    /**
     * AlertDialog
     */
    private AlertDialog.Builder dialogForConnectionBluetooth;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Alert Dialog
         */
        this.buttonOpenDialog = findViewById(R.id.buttonOpenDialog);

        this.buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogForConnectionBluetooth = new AlertDialog.Builder(MainActivity.this);
                dialogForConnectionBluetooth.setTitle("Conecte-se ao bluetooth...");
                dialogForConnectionBluetooth.setCancelable(false);
                dialogForConnectionBluetooth.setIcon(R.drawable.ic_launcher_background);

                dialogForConnectionBluetooth.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"É necessário realizar uma conexão...",Toast.LENGTH_SHORT).show();
                    }
                });

                dialogForConnectionBluetooth.create();
                dialogForConnectionBluetooth.show();
            }
        });

        /**
         *
         */
        this.statusMessage = findViewById(R.id.statusMensagem);

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        //  Verificando se o Bluetooth existe e esta funcionando no aparelho
        if (btAdapter == null) {
            statusMessage.setText("Que pena! Hardware Bluetooth não está funcionando.");
        } else {
            statusMessage.setText("Ótimo! Hardware Bluetooth está funcionando.");
        }

        //  ativando o  bluetooth com a permissão do usuário
        if(!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH);
            statusMessage.setText("Solicitando ativação do Bluetooth...");
        } else {
            statusMessage.setText("Bluetooth esta ativado!");
        }


        /**
         * Action of List Paired Devices
         */
        this.buttonAparelhosPareados = findViewById(R.id.buttonDispositivosPareados);

        this.buttonAparelhosPareados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPairedDevices();
            }
        });

        /**
         * Action of Seacher News Devices
         */
        this.buttonProcurarDispositivos = findViewById(R.id.buttonProcurarDispositivos);

        this.buttonProcurarDispositivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discoverDevices();
            }
        });

        /**
         * Enable Visibility Of Bluetooth
         */
        this.buttonHabilitarVisibilidade = findViewById(R.id.buttonHabilitarVisibilidade);

        this.buttonHabilitarVisibilidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableVisibility();
            }
        });

        /**
         * Send Message
         */
        this.buttonEnviarMensagem = findViewById(R.id.buttonEnviar);

        this.buttonEnviarMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //requestCode: funciona como um identificador sobre qual Activity está retornando um resultado.
        //resultCode: traz a informação sobre a decisão do usuário.

        if(requestCode == ENABLE_BLUETOOTH) {
            if(resultCode == RESULT_OK) {
                statusMessage.setText("Bluetooth ativado :D");
            }
            else {
                statusMessage.setText("Bluetooth não ativado :(");
            }
        }
        else if(requestCode == SELECT_PAIRED_DEVICE || requestCode == SELECT_DISCOVERED_DEVICE) {
            if(resultCode == RESULT_OK) {
                statusMessage.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n"
                        + data.getStringExtra("btDevAddress"));
                try {
                    connect = new ConnectionThread(data.getStringExtra("btDevAddress"));
                    connect.start();
                    //Toast.makeText(this,"Conexão Realizada com Sucesso!", Toast.LENGTH_LONG).show();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            else {
                statusMessage.setText("Nenhum dispositivo selecionado :(");
            }
        }
    }

    /**
     * View Of Paired Devices
     */
    private void openPairedDevices(){
        Intent searchPairedDevicesIntent = new Intent(MainActivity.this, PairedDevicesActivity.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
    }

    /**
     * View Of Seacher Of Devices
     */
    private void discoverDevices() {
        Intent searchPairedDevicesIntent = new Intent(this, ProcurarDispositivosActivity.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_DISCOVERED_DEVICE);
    }

    /**
     *  Enable Visibility
     */
    public void enableVisibility() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
        startActivity(discoverableIntent);
    }

    /**
     * Send Message
     */
    public void sendMessage() {
        EditText messageBox = findViewById(R.id.editText);
        String messageBoxString = messageBox.getText().toString();
        byte[] data =  messageBoxString.getBytes();
        connect.write(data);

        messageBox.setText(""); // limpando a caixa de texto
    }

    /**
     *  Handler
     */
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            byte[] data = bundle.getByteArray("data");
            String dataString= new String(data);

            if(dataString.equals("---N"))
                statusMessage.setText("Ocorreu um erro durante a conexão D:");
            else if(dataString.equals("---S"))
                statusMessage.setText("Conectado :D");

        }
    };


}

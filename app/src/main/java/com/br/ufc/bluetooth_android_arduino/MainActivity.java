package com.br.ufc.bluetooth_android_arduino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /***********************************************/
    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;
    /***********************************************/

    /**
     * Buttons
     */
    private Button buttonAparelhosPareados;
    private Button buttonProcurarDispositivos;
    private Button buttonHabilitarVisibilidade;
    private Button buttonOpenDialog;

    /**
     * ConnectionThread
     */
    private ConnectionThread connect;

    /**
     * AlertDialog
     */
    private AlertDialog.Builder dialogForConnectionBluetooth;
    private ArrayAdapter<String> lista_dispositivos;

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
        this.lista_dispositivos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        this.buttonOpenDialog = findViewById(R.id.buttonOpenDialog);

        this.buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configuratioDialog();
            }
        });

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        //  Verificando se o Bluetooth existe e esta funcionando no aparelho
        if (btAdapter == null) {
            Toast.makeText(getApplicationContext(),"O bluetooth não esta funcioando!",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),"Otimo, bluetooth funcionado!",Toast.LENGTH_SHORT).show();
        }

        //  ativando o  bluetooth com a permissão do usuário
        if(!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH);
            Toast.makeText(getApplicationContext(),"Solicitação do bluetooth!",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),"Bluetooth esta ativado!",Toast.LENGTH_SHORT).show();
        }

        /**
         * Action of List Paired Devices
         */
        this.buttonAparelhosPareados = findViewById(R.id.buttonDispositivosPareados);

        this.buttonAparelhosPareados.setOnClickListener((v)->{ openPairedDevices(); });


        /**
         * Action of Seacher News Devices
         */
        this.buttonProcurarDispositivos = findViewById(R.id.buttonProcurarDispositivos);

        this.buttonProcurarDispositivos.setOnClickListener((v)-> { discoverDevices(); });

        /**
         * Enable Visibility Of Bluetooth
         */
        this.buttonHabilitarVisibilidade = findViewById(R.id.buttonHabilitarVisibilidade);

        this.buttonHabilitarVisibilidade.setOnClickListener((v)-> { enableVisibility(); });
    }

    void configuratioDialog(){

        dialogForConnectionBluetooth = new AlertDialog.Builder(MainActivity.this);
        dialogForConnectionBluetooth.setTitle("Conecte-se ao bluetooth...");
        dialogForConnectionBluetooth.setCancelable(false);
        dialogForConnectionBluetooth.setIcon(R.drawable.ic_launcher_background);

        /**
         * Descobrimento dos Dispositivos
         */
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        dialogForConnectionBluetooth.setAdapter(lista_dispositivos, (dialog, which) -> {
            String item = lista_dispositivos.getItem(which);
            String devName = item.substring(0, item.indexOf("\n"));
            String devAddress = item.substring(item.indexOf("\n")+1, item.length());

            AlertDialog.Builder builderInner = new AlertDialog.Builder(MainActivity.this);

            builderInner.setTitle("Você selecionou este dispositivo...");
            builderInner.setMessage("Nome: " + devName + "\nAddress:" + devAddress );

            builderInner.setPositiveButton("Conectar", (dialog1, which1) -> {
                Intent intentControleRemoto = new Intent(MainActivity.this, ControlleRemotoActivity.class);
                intentControleRemoto.putExtra("devAddress", devAddress);
                startActivity(intentControleRemoto);
            });

            builderInner.show();
        });

        dialogForConnectionBluetooth.setNeutralButton("Cancelar",
                (dialog, which) ->
                        Toast.makeText(getApplicationContext(),"É necessário realizar uma conexão...",Toast.LENGTH_SHORT).show());

        dialogForConnectionBluetooth.create();
        dialogForConnectionBluetooth.show();
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
                Toast.makeText(getApplicationContext(),"Bluetooth ativado!",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Bluetooth não ativado!",Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == SELECT_PAIRED_DEVICE || requestCode == SELECT_DISCOVERED_DEVICE) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),"Você selecionou " + data.getStringExtra("btDevName") + "\n"
                        + data.getStringExtra("btDevAddress"),Toast.LENGTH_SHORT).show();
                try {
                    connect = new ConnectionThread(data.getStringExtra("btDevAddress"));
                    connect.start();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Nenhum dispositivo selecionado",Toast.LENGTH_SHORT).show();
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

    /*  Define um receptor para o evento de descoberta de dispositivo.
      */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        /*  Este método é executado sempre que um novo dispositivo for descoberto.
         */
        public void onReceive(Context context, Intent intent) {

            /*  Obtem o Intent que gerou a ação.
                Verifica se a ação corresponde à descoberta de um novo dispositivo.
                Obtem um objeto que representa o dispositivo Bluetooth descoberto.
                Exibe seu nome e endereço na lista.
             */
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                lista_dispositivos.add(device.getName() + "\n" + device.getAddress());
            }
        }
    };

    public ConnectionThread getConnect() {
        return connect;
    }

    /**
     *  Handler
     */
//    public static Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//
//            Bundle bundle = msg.getData();
//            byte[] data = bundle.getByteArray("data");
//            String dataString= new String(data);
//
//            if(dataString.equals("---N"))
//                statusMessage.setText("Ocorreu um erro durante a conexão D:");
//            else if(dataString.equals("---S"))
//                statusMessage.setText("Conectado :D");
//
//        }
//    };

}

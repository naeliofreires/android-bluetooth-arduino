package com.br.ufc.bluetooth_android_arduino;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //  valores serão utilizados durante o processo de habilitação do bluetooth.
    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;

    //  exibir mensagem ao usuario
    static TextView statusMessage;

    //
    Button buttonAparelhosPareados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.statusMessage = findViewById(R.id.statusMensagem);

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        //  verificando se o bluetooth existe e esta funcionando no aparelho
        if (btAdapter == null) {
            statusMessage.setText("Que pena! Hardware Bluetooth não está funcionando :(");
        } else {
            statusMessage.setText("Ótimo! Hardware Bluetooth está funcionando :)");
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
         * AÇÃO DE LISTAR DISPOSTIVOS PAREADOS
         * **/

        this.buttonAparelhosPareados = findViewById(R.id.buttonDispositivosPareados);

        this.buttonAparelhosPareados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchPairedDevicesIntent = new Intent(MainActivity.this, AparelhosPareadosActivity.class);
                startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
            }
        });
    }

    // feedback para o informar que o bluetooth foi ativado ou não
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
        else if(requestCode == SELECT_PAIRED_DEVICE) {
            if(resultCode == RESULT_OK) {
                statusMessage.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n"
                        + data.getStringExtra("btDevAddress"));
            }
            else {
                statusMessage.setText("Nenhum dispositivo selecionado :(");
            }
        }
    }


}

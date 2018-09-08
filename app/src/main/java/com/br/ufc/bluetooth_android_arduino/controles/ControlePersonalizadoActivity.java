package com.br.ufc.bluetooth_android_arduino.controles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.br.ufc.bluetooth_android_arduino.ConnectionThread;
import com.br.ufc.bluetooth_android_arduino.R;

/**
 * Controller de Baixo Nível - Comandos Personalizados
 */
public class ControlePersonalizadoActivity extends AppCompatActivity {

    private ConnectionThread connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle_personalizado);

        conexao_bluetooth();
    }

    private void conexao_bluetooth() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String endereco = bundle.getString("devAddress");
            connect = new ConnectionThread(endereco);
            connect.start();
        }
    }

}

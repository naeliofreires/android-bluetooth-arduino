package com.br.ufc.bluetooth_android_arduino.activitys.controles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.br.ufc.bluetooth_android_arduino.activitys.ConnectionThread;
import com.br.ufc.bluetooth_android_arduino.R;

import java.util.ArrayList;

/**
 * Controller de Alto Nível - Comandos já pré-determinado
 */
public class ControlleRemotoActivity extends AppCompatActivity {

    private Button btnCima;
    private Button btnTras;

    private Button btnDireita;
    private Button btnEsquerda;

    private Button btnUp;
    private Button btnDown;

    private Button btnWave;
    private Button btnShutdown;
    private Button btnExecutar;

    private ConnectionThread connect;
    private ArrayList<String> commands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlle_remoto);

        this.btnExecutar = findViewById(R.id.buttonExecutar);

        this.btnCima = findViewById(R.id.buttonCima);
        this.btnTras = findViewById(R.id.buttonTras);
        this.btnDireita = findViewById(R.id.buttonDireita);
        this.btnEsquerda = findViewById(R.id.buttonEsquerda);

        this.btnUp = findViewById(R.id.buttonUp);
        this.btnDown = findViewById(R.id.buttonDown);

        this.btnWave = findViewById(R.id.buttonTchau);
        this.btnShutdown = findViewById(R.id.buttonDesligar);

        this.commands = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String address = bundle.getString("devAddress");
            connect = new ConnectionThread(address);
            connect.start();
        }

        // forward_();
        this.btnCima.setOnClickListener(v -> this.commands.add("w"));

        // back_();
        this.btnTras.setOnClickListener(v -> this.commands.add("s"));

        // turn_right_();
        this.btnDireita.setOnClickListener(v -> this.commands.add("d"));

        // turn_left_();
        this.btnEsquerda.setOnClickListener(v -> this.commands.add("a"));

        //stand_();
        this.btnUp.setOnClickListener(v -> this.commands.add("e"));

        // sit_();
        this.btnDown.setOnClickListener(v -> this.commands.add("q"));

        // wave_();
        this.btnWave.setOnClickListener(v -> this.commands.add("b"));

        // shutdown();
        this.btnShutdown.setOnClickListener(v -> this.commands.add("x"));

        this.btnExecutar.setOnClickListener(v -> {
            sendMessage();
            this.clearCommands();
        });
    }

    public void sendMessage() {
        for (String srt : this.commands) {
            byte[] data = srt.getBytes();
            this.connect.write(data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connect.cancel();
        Toast.makeText(this,"Bluetooth desconectado!",Toast.LENGTH_LONG).show();
    }

    public void clearCommands() {
        this.commands = new ArrayList<>();
    }
}
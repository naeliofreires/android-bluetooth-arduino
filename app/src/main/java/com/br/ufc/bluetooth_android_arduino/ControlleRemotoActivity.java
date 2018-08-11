package com.br.ufc.bluetooth_android_arduino;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.util.ArrayList;

public class ControlleRemotoActivity extends AppCompatActivity {

    private Button buttonCima;
    private Button buttonTras;

    private Button buttonDireita;
    private Button buttonEsquerda;

    private Button buttonUp;
    private Button buttonDown;

    private Button buttonAcenar;
    private Button buttonDesligar;
    private Button buttonExecutar;

    private ConnectionThread connect;
    private ArrayList<String> comandos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlle_remoto);

        this.buttonExecutar = findViewById(R.id.buttonExecutar);

        this.buttonCima = findViewById(R.id.buttonCima);
        this.buttonTras = findViewById(R.id.buttonTras);
        this.buttonDireita = findViewById(R.id.buttonDireita);
        this.buttonEsquerda = findViewById(R.id.buttonEsquerda);

        this.buttonUp = findViewById(R.id.buttonUp);
        this.buttonDown = findViewById(R.id.buttonDown);

        this.buttonAcenar = findViewById(R.id.buttonTchau);
        this.buttonDesligar = findViewById(R.id.buttonDesligar);

        this.comandos = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String nome = bundle.getString("devAddress");
            connect = new ConnectionThread(nome);
            connect.start();
        }

        // foward_();
        this.buttonCima.setOnClickListener((v) -> this.comandos.add("w"));
        // back_();
        this.buttonTras.setOnClickListener((v) -> this.comandos.add("s"));
        // turn_right_();
        this.buttonDireita.setOnClickListener((v) -> this.comandos.add("d"));
        // turn_left_();
        this.buttonEsquerda.setOnClickListener((v) -> this.comandos.add("a"));

        //stand_();
        this.buttonUp.setOnClickListener((v) -> this.comandos.add("e"));
        // sit_();
        this.buttonDown.setOnClickListener((v) -> this.comandos.add("q"));
        // wave_();
        this.buttonAcenar.setOnClickListener((v) -> this.comandos.add("b"));
        // shutdown();
        this.buttonDesligar.setOnClickListener((v) -> this.comandos.add("x"));

        this.buttonExecutar.setOnClickListener((v) -> {
            sendMessage();
            this.comandos = new ArrayList<>();
        });
    }

    public void sendMessage() {
        for (String srt : this.comandos) {
            byte[] data = srt.getBytes();
            this.connect.write(data);
        }
    }
}

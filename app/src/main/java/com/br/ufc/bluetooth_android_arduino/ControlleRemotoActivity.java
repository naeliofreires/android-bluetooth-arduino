package com.br.ufc.bluetooth_android_arduino;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.util.ArrayList;

public class ControlleRemotoActivity extends AppCompatActivity {

    private Button buttonCima, buttonTras, buttonEsquerda, buttonDireita, buttonExecutar;

    private ArrayList<String> comandos;
    private ConnectionThread connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlle_remoto);

        this.buttonCima = findViewById(R.id.buttonCima);
        this.buttonTras = findViewById(R.id.buttonTras);
        this.buttonDireita = findViewById(R.id.buttonDireita);
        this.buttonEsquerda = findViewById(R.id.buttonEsquerda);
        this.buttonExecutar = findViewById(R.id.buttonExecutar);

        this.comandos = new ArrayList<String>();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String nome = bundle.getString("devAddress");
            connect = new ConnectionThread(nome);
            connect.start();
        }

        this.buttonCima.setOnClickListener((v)->{
            this.comandos.add("C");
        });

        this.buttonTras.setOnClickListener((v)->{
            this.comandos.add("T");
        });

        this.buttonDireita.setOnClickListener((v)->{
            this.comandos.add("D");
        });

        this.buttonEsquerda.setOnClickListener((v)-> this.comandos.add("E"));

        this.buttonExecutar.setOnClickListener((v)-> sendMessage());
    }

    public void sendMessage() {
        for(String srt : this.comandos){
            byte[] data =  srt.getBytes();
            this.connect.write(data);
        }
    }
}

package com.br.ufc.bluetooth_android_arduino.controles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.br.ufc.bluetooth_android_arduino.ConnectionThread;
import com.br.ufc.bluetooth_android_arduino.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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

    private Button btnAcenar;
    private Button btnDesligar;
    private Button btnExecutar;

    private Timer timer;
    private ConnectionThread connect;
    private ArrayList<String> comandos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlle_remoto);

        timer = new Timer();
        this.btnExecutar = findViewById(R.id.buttonExecutar);

        this.btnCima = findViewById(R.id.buttonCima);
        this.btnTras = findViewById(R.id.buttonTras);
        this.btnDireita = findViewById(R.id.buttonDireita);
        this.btnEsquerda = findViewById(R.id.buttonEsquerda);

        this.btnUp = findViewById(R.id.buttonUp);
        this.btnDown = findViewById(R.id.buttonDown);

        this.btnAcenar = findViewById(R.id.buttonTchau);
        this.btnDesligar = findViewById(R.id.buttonDesligar);

        this.comandos = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String endereco = bundle.getString("devAddress");
            //TERMINATED
            connect = new ConnectionThread(endereco);
            connect.start();
        }

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                boolean b = connect.isAlive();
                Log.d("situação bluetooth", String.valueOf(b));
            }
        }, 1000, 1000);

        // foward_();
        this.btnCima.setOnClickListener((v) -> this.comandos.add("w"));
        // back_();
        this.btnTras.setOnClickListener((v) -> this.comandos.add("s"));
        // turn_right_();
        this.btnDireita.setOnClickListener((v) -> this.comandos.add("d"));
        // turn_left_();
        this.btnEsquerda.setOnClickListener((v) -> this.comandos.add("a"));

        //stand_();
        this.btnUp.setOnClickListener((v) -> this.comandos.add("e"));
        // sit_();
        this.btnDown.setOnClickListener((v) -> this.comandos.add("q"));
        // wave_();
        this.btnAcenar.setOnClickListener((v) -> this.comandos.add("b"));
        // shutdown();
        this.btnDesligar.setOnClickListener((v) -> this.comandos.add("x"));

        this.btnExecutar.setOnClickListener((v) -> {
            sendMessage();
            this.limpar_comandos();
        });
    }

    public void sendMessage() {
        for (String srt : this.comandos) {
            byte[] data = srt.getBytes();
            this.connect.write(data);
        }
    }

    public void limpar_comandos() {
        this.comandos = new ArrayList<>();
    }
}

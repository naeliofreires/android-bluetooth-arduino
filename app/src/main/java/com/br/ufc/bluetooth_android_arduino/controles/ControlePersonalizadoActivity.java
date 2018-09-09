package com.br.ufc.bluetooth_android_arduino.controles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.br.ufc.bluetooth_android_arduino.ConnectionThread;
import com.br.ufc.bluetooth_android_arduino.R;
import com.br.ufc.bluetooth_android_arduino.constants.Constants;

/**
 * Controller de Baixo Nível - Comandos Personalizados
 */
public class ControlePersonalizadoActivity extends AppCompatActivity {

    private ConnectionThread connect;

    private SeekBar seekBar;
    private String progressoSeekBar;

    private TextView txtViewGrauMovimento;
    private EditText edTxtComandos;

    private Button buttonAdd;
    private Button btnServo1, btnServo2,
            btnServo3, btnServo4,
            btnServo5, btnServo6,
            btnServo7, btnServo8;

    private String sequencia_comandos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle_personalizado);

        if (conexao_bluetooth()) {
            instancias();

            actionSeekBar();
            actionButtonAdd();

            action_serv1();
        } else
            Toast.makeText(this, "Conexão Falhou", Toast.LENGTH_LONG).show();
    }

    private boolean conexao_bluetooth() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String endereco = bundle.getString("devAddress");
            connect = new ConnectionThread(endereco);
            connect.start();
            return true;
        }
        return false;
    }

    private void instancias() {

        sequencia_comandos = Constants.VAZIA;
        seekBar = findViewById(R.id.seekBarGraus);
        edTxtComandos = findViewById(R.id.edTxtComandos);
        txtViewGrauMovimento = findViewById(R.id.txtViewGrauMovimento);

        buttonAdd = findViewById(R.id.buttonAdd);

        btnServo1 = findViewById(R.id.btnServe1);
        btnServo2 = findViewById(R.id.btnServe2);
        btnServo3 = findViewById(R.id.btnServe3);
        btnServo4 = findViewById(R.id.btnServe4);
        btnServo5 = findViewById(R.id.btnServe5);
        btnServo6 = findViewById(R.id.btnServe6);
        btnServo7 = findViewById(R.id.btnServe7);
        btnServo8 = findViewById(R.id.btnServe8);
    }

    private void atualizar_edTxtComandos() {
        edTxtComandos.setText(sequencia_comandos);
    }

    private void actionButtonAdd() {
        buttonAdd.setOnClickListener(v -> {
            if (!sequencia_comandos.isEmpty())
                sequencia_comandos += progressoSeekBar;
            seekBar.setProgress(0);
            atualizar_edTxtComandos();
        });
    }

    private void actionSeekBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtViewGrauMovimento.setText("Progresso: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(getApplicationContext(), "SeekBar pressionado!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressoSeekBar = String.valueOf(seekBar.getProgress());
            }
        });
    }

    private void action_serv1() {
        btnServo1.setOnClickListener(v -> {

            if (sequencia_comandos.isEmpty())
                sequencia_comandos += "1:";
            else
                sequencia_comandos += "&1:";

            atualizar_edTxtComandos();
        });
    }

}

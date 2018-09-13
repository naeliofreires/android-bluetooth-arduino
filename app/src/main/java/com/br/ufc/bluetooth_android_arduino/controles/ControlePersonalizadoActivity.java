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

    private static final String RESET_COMANDOS = "1:90&2:90&3:90&4:90&5:90&6:90&7:90&8:90";
    private ConnectionThread connect;

    private SeekBar seekBar;
    private String progressoSeekBar;

    private TextView txtViewGrauMovimento;
    private EditText edTxtComandos;

    private Button btnAdd, btnExecutar, btnReset;
    private Button btnServo1, btnServo2,
            btnServo3, btnServo4,
            btnServo5, btnServo6,
            btnServo7, btnServo8;

    private String sequenciaComandos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle_personalizado);

        if (conexaoBluetooth()) {
            instancias();

            actionReset();
            actionSeekBar();
            actionButtonAdd();

            actionServ1();
            actionServ2();
            actionServ3();
            actionServ4();
            actionServ5();
            actionServ6();
            actionServ7();
            actionServ8();

            btnExecutar();
        } else
            Toast.makeText(this, "Conexão Falhou", Toast.LENGTH_LONG).show();
    }

    private boolean conexaoBluetooth() {
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

        seekBar = findViewById(R.id.seekBarGraus);
        edTxtComandos = findViewById(R.id.edTxtComandos);
        edTxtComandos.setEnabled(false);
        txtViewGrauMovimento = findViewById(R.id.txtViewGrauMovimento);
        btnAdd = findViewById(R.id.buttonAdd);

        btnServo1 = findViewById(R.id.btnServe1);
        btnServo2 = findViewById(R.id.btnServe2);
        btnServo3 = findViewById(R.id.btnServe3);
        btnServo4 = findViewById(R.id.btnServe4);
        btnServo5 = findViewById(R.id.btnServe5);
        btnServo6 = findViewById(R.id.btnServe6);
        btnServo7 = findViewById(R.id.btnServe7);
        btnServo8 = findViewById(R.id.btnServe8);

        btnReset = findViewById(R.id.btnReset); // button de resetar os servos
        btnExecutar = findViewById(R.id.buttonExecutar); // button de executar comandos

        limparSequenciaComandos();
    }

    private void atualizarEdTxtComandos() {
        edTxtComandos.setText(sequenciaComandos);
    }

    private void actionButtonAdd() {
        btnAdd.setOnClickListener(v -> {
            if (!sequenciaComandos.isEmpty())
                sequenciaComandos += progressoSeekBar;
            seekBar.setProgress(0);
            atualizarEdTxtComandos();
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

    private void actionServ1() {
        btnServo1.setOnClickListener(v -> {

            if (sequenciaComandos.isEmpty())
                sequenciaComandos += "1:";
            else
                sequenciaComandos += "&1:";

            atualizarEdTxtComandos();
        });
    }

    private void actionServ2() {
        btnServo2.setOnClickListener(v -> {

            if (sequenciaComandos.isEmpty())
                sequenciaComandos += "2:";
            else
                sequenciaComandos += "&2:";

            atualizarEdTxtComandos();
        });
    }

    private void actionServ3() {
        btnServo3.setOnClickListener(v -> {

            if (sequenciaComandos.isEmpty())
                sequenciaComandos += "3:";
            else
                sequenciaComandos += "&3:";

            atualizarEdTxtComandos();
        });
    }

    private void actionServ4() {
        btnServo4.setOnClickListener(v -> {

            if (sequenciaComandos.isEmpty())
                sequenciaComandos += "4:";
            else
                sequenciaComandos += "&4:";

            atualizarEdTxtComandos();
        });
    }

    private void actionServ5() {
        btnServo5.setOnClickListener(v -> {

            if (sequenciaComandos.isEmpty())
                sequenciaComandos += "5:";
            else
                sequenciaComandos += "&5:";

            atualizarEdTxtComandos();
        });
    }

    private void actionServ6() {
        btnServo6.setOnClickListener(v -> {

            if (sequenciaComandos.isEmpty())
                sequenciaComandos += "6:";
            else
                sequenciaComandos += "&7:";

            atualizarEdTxtComandos();
        });
    }

    private void actionServ7() {
        btnServo7.setOnClickListener(v -> {

            if (sequenciaComandos.isEmpty())
                sequenciaComandos += "7:";
            else
                sequenciaComandos += "&7:";

            atualizarEdTxtComandos();
        });
    }

    private void actionServ8() {
        btnServo8.setOnClickListener(v -> {

            if (sequenciaComandos.isEmpty())
                sequenciaComandos += "8:";
            else
                sequenciaComandos += "&8:";

            atualizarEdTxtComandos();
        });
    }

    private void actionReset() {
        btnReset.setOnClickListener(v -> sendMessage(RESET_COMANDOS));
    }

    private void btnExecutar() {
        btnExecutar.setOnClickListener(v -> sendMessage(sequenciaComandos));
    }

    public void sendMessage(String msg) {
        this.connect.write(msg.getBytes());
    }

    public void limparSequenciaComandos() {
        sequenciaComandos = Constants.VAZIA;
    }

}

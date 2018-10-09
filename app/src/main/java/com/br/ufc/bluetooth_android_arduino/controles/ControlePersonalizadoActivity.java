package com.br.ufc.bluetooth_android_arduino.controles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    private static final String RESET_COMMANDS = "1:90&2:90&3:90&4:90&5:90&6:90&7:90&8:90";

    private ConnectionThread connect;

    private SeekBar seekBar;
    private String progressSeekBar;

    private TextView txtViewGrauMovimento;
    private EditText editTxtCommands;

    private Button btnAdd, btnRun, btnReset;
    private Button btnServo1, btnServo2,
            btnServo3, btnServo4,
            btnServo5, btnServo6,
            btnServo7, btnServo8;

    private String sequencesCommands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle_personalizado);

        if (connectionBluetooth()) {
            instances();

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

            btnRun();
        } else
            Toast.makeText(this, "connection fail", Toast.LENGTH_LONG).show();
    }

    private boolean connectionBluetooth() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String address = bundle.getString("devAddress");
            connect = new ConnectionThread(address);
            connect.start();
            return true;
        }
        return false;
    }

    private void instances() {

        seekBar = findViewById(R.id.seekBarGraus);
        editTxtCommands = findViewById(R.id.edTxtComandos);
        editTxtCommands.setEnabled(false);
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

        btnReset = findViewById(R.id.btnReset); // button to reset the servs
        btnRun = findViewById(R.id.buttonExecutar); // button de executar comandos

        clearSequenceCommands();
    }

    private void updateEdTxtCommands() {
        editTxtCommands.setText(sequencesCommands);
    }

    private void actionButtonAdd() {
        btnAdd.setOnClickListener(v -> {
            if (!sequencesCommands.isEmpty())
                sequencesCommands += progressSeekBar;
            seekBar.setProgress(0);
            updateEdTxtCommands();
        });
    }

    private void actionSeekBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtViewGrauMovimento.setText("Progress: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // When SeekBar is held down
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressSeekBar = String.valueOf(seekBar.getProgress());
            }
        });
    }

    private void actionServ1() {
        btnServo1.setOnClickListener(v -> {

            if (sequencesCommands.isEmpty())
                sequencesCommands += "1:";
            else
                sequencesCommands += "&1:";

            updateEdTxtCommands();
        });
    }

    private void actionServ2() {
        btnServo2.setOnClickListener(v -> {

            if (sequencesCommands.isEmpty())
                sequencesCommands += "2:";
            else
                sequencesCommands += "&2:";

            updateEdTxtCommands();
        });
    }

    private void actionServ3() {
        btnServo3.setOnClickListener(v -> {

            if (sequencesCommands.isEmpty())
                sequencesCommands += "3:";
            else
                sequencesCommands += "&3:";

            updateEdTxtCommands();
        });
    }

    private void actionServ4() {
        btnServo4.setOnClickListener(v -> {

            if (sequencesCommands.isEmpty())
                sequencesCommands += "4:";
            else
                sequencesCommands += "&4:";

            updateEdTxtCommands();
        });
    }

    private void actionServ5() {
        btnServo5.setOnClickListener(v -> {

            if (sequencesCommands.isEmpty())
                sequencesCommands += "5:";
            else
                sequencesCommands += "&5:";

            updateEdTxtCommands();
        });
    }

    private void actionServ6() {
        btnServo6.setOnClickListener(v -> {

            if (sequencesCommands.isEmpty())
                sequencesCommands += "6:";
            else
                sequencesCommands += "&7:";

            updateEdTxtCommands();
        });
    }

    private void actionServ7() {
        btnServo7.setOnClickListener(v -> {

            if (sequencesCommands.isEmpty())
                sequencesCommands += "7:";
            else
                sequencesCommands += "&7:";

            updateEdTxtCommands();
        });
    }

    private void actionServ8() {
        btnServo8.setOnClickListener(v -> {

            if (sequencesCommands.isEmpty())
                sequencesCommands += "8:";
            else
                sequencesCommands += "&8:";

            updateEdTxtCommands();
        });
    }

    private void actionReset() {
        btnReset.setOnClickListener(v -> sendMessage(RESET_COMMANDS));
    }

    private void btnRun() {
        btnRun.setOnClickListener(v -> sendMessage(sequencesCommands));
    }

    public void sendMessage(String msg) {
        byte[] data = msg.getBytes();
        this.connect.write(data);

        this.clearSequenceCommands();
    }

    public void clearSequenceCommands() {
        sequencesCommands = Constants.EMPTY;
        this.updateEdTxtCommands();
    }

}

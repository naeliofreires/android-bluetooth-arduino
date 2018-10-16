package com.br.ufc.bluetooth_android_arduino.activitys.controles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.br.ufc.bluetooth_android_arduino.R;
import com.br.ufc.bluetooth_android_arduino.connection.ConnectionThread;
import com.br.ufc.bluetooth_android_arduino.constants.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Remote Control - Low Level
 */
public class CustomControlActivity extends AppCompatActivity {

    private static final String RESET_COMMANDS = "1:90&2:90&3:90&4:90&5:90&6:90&7:90&8:90";

    @BindView(R.id.seekBarGraus)
    SeekBar seekBar;
    String progressSeekBar;

    @BindView(R.id.txtViewRateMovimento)
    TextView txtViewRateMovement;

    @BindView(R.id.edTxtComandos)
    EditText editTxtCommands;

    @BindView(R.id.btnDelete)
    Button btnDelete;

    private ConnectionThread connect;
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

        ButterKnife.bind(this);

        if (connectionBluetooth()) {

            this.sequencesCommands = Constants.LOW_LEVEL;
            config();

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

            delete();
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

    public void config() {

        editTxtCommands.setEnabled(false);
        btnAdd = findViewById(R.id.buttonAdd);

        btnServo1 = findViewById(R.id.btnServe1);
        btnServo2 = findViewById(R.id.btnServe2);
        btnServo3 = findViewById(R.id.btnServe3);
        btnServo4 = findViewById(R.id.btnServe4);
        btnServo5 = findViewById(R.id.btnServe5);
        btnServo6 = findViewById(R.id.btnServe6);
        btnServo7 = findViewById(R.id.btnServe7);
        btnServo8 = findViewById(R.id.btnServe8);

        btnReset = findViewById(R.id.btnReset);
        btnRun = findViewById(R.id.btnRun);

        clearSequenceCommands();
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
                txtViewRateMovement.setText("Progress: " + progress);
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
        this.sequencesCommands = Constants.LOW_LEVEL;
        this.updateEdTxtCommands();
    }

    public void updateEdTxtCommands() {
        editTxtCommands.setText(sequencesCommands);
    }

    @OnClick(R.id.btnDelete)
    public void delete() {
        if (this.sequencesCommands.length() > 0)
            this.sequencesCommands = this.sequencesCommands.substring(0, this.sequencesCommands.length() - 1);
        this.updateEdTxtCommands();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connect.cancel();
        Toast.makeText(this, "bluetooth disconnected", Toast.LENGTH_LONG).show();
    }
}

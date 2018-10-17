package com.br.ufc.bluetooth_android_arduino.activitys.controles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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


    @BindView(R.id.buttonAdd)
    Button btnAdd;
    @BindView(R.id.btnRun)
    Button btnRun;
    @BindView(R.id.btnReset)
    Button btnReset;

    @BindView(R.id.btnServe1)
    Button btnServo1;
    @BindView(R.id.btnServe2)
    Button btnServo2;
    @BindView(R.id.btnServe3)
    Button btnServo3;
    @BindView(R.id.btnServe4)
    Button btnServo4;
    @BindView(R.id.btnServe5)
    Button btnServo5;
    @BindView(R.id.btnServe6)
    Button btnServo6;
    @BindView(R.id.btnServe7)
    Button btnServo7;
    @BindView(R.id.btnServe8)
    Button btnServo8;


    private ConnectionThread connect;
    private String sequencesCommands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle_personalizado);

        ButterKnife.bind(this);

        if (connectionBluetooth()) {

            editTxtCommands.setEnabled(false);
            this.sequencesCommands = Constants.LOW_LEVEL;

            actionSeekBar();
            clearSequenceCommands();

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

    @OnClick(R.id.buttonAdd)
    public void actionAdd() {
        btnAdd.setOnClickListener(v -> {
            if (!sequencesCommands.isEmpty())
                sequencesCommands += progressSeekBar;
            seekBar.setProgress(0);
            updateCommands();
        });
    }

    public void actionSeekBar() {
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

    @OnClick(R.id.btnServe1)
    public void actionServ1() {
        if (sequencesCommands.length() == 1)
            sequencesCommands += "1:";
        else
            sequencesCommands += "&1:";
        updateCommands();
    }

    @OnClick(R.id.btnServe2)
    public void actionServ2() {
        if (sequencesCommands.length() == 1)
            sequencesCommands += "2:";
        else
            sequencesCommands += "&2:";
        updateCommands();
    }

    @OnClick(R.id.btnServe3)
    public void actionServ3() {
        if (sequencesCommands.length() == 1)
            sequencesCommands += "3:";
        else
            sequencesCommands += "&3:";
        updateCommands();
    }

    @OnClick(R.id.btnServe4)
    public void actionServ4() {
        if (sequencesCommands.length() == 1)
            sequencesCommands += "4:";
        else
            sequencesCommands += "&4:";
        updateCommands();
    }

    @OnClick(R.id.btnServe5)
    public void actionServ5() {
        if (sequencesCommands.length() == 1)
            sequencesCommands += "5:";
        else
            sequencesCommands += "&5:";
        updateCommands();
    }

    @OnClick(R.id.btnServe6)
    public void actionServ6() {
        if (sequencesCommands.length() == 1)
            sequencesCommands += "6:";
        else
            sequencesCommands += "&7:";
        updateCommands();
    }

    @OnClick(R.id.btnServe7)
    public void actionServ7() {
        if (sequencesCommands.length() == 1)
            sequencesCommands += "7:";
        else
            sequencesCommands += "&7:";
        updateCommands();
    }

    @OnClick(R.id.btnServe8)
    public void actionServ8() {
        if (sequencesCommands.length() == 1)
            sequencesCommands += "8:";
        else
            sequencesCommands += "&8:";
        updateCommands();
    }

    @OnClick(R.id.btnReset)
    public void actionReset() {
        sendMessage(RESET_COMMANDS);
    }

    @OnClick(R.id.btnRun)
    public void actionRun() {
        sendMessage(sequencesCommands);
    }

    public void sendMessage(String msg) {
        byte[] data = msg.getBytes();
        this.connect.write(data);

        this.clearSequenceCommands();
    }

    public void clearSequenceCommands() {
        sequencesCommands = Constants.EMPTY;
        this.sequencesCommands = Constants.LOW_LEVEL;
        this.updateCommands();
    }

    public void updateCommands() {
        editTxtCommands.setText(sequencesCommands);
    }

    @OnClick(R.id.btnDelete)
    public void delete() {
        if (this.sequencesCommands.length() > 1)
            this.sequencesCommands = this.sequencesCommands.substring(0, this.sequencesCommands.length() - 1);
        this.updateCommands();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connect.cancel();
        Toast.makeText(this, "bluetooth disconnected", Toast.LENGTH_LONG).show();
    }
}

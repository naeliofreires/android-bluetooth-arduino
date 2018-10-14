package com.br.ufc.bluetooth_android_arduino.activitys.controles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.br.ufc.bluetooth_android_arduino.R;
import com.br.ufc.bluetooth_android_arduino.connection.ConnectionThread;
import com.br.ufc.bluetooth_android_arduino.constants.Constants;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Controller de Alto Nível - Comandos já pré-definidos
 */
public class RemoteControlActivity extends AppCompatActivity {

    @BindView(R.id.btnFront)
    Button btnFront;
    @BindView(R.id.btnBack)
    Button btnBack;
    @BindView(R.id.btnRight)
    Button btnRight;
    @BindView(R.id.btnLeft)
    Button btnLeft;
    @BindView(R.id.buttonUp)
    Button btnUp;
    @BindView(R.id.buttonDown)
    Button btnDown;

    @BindView(R.id.btnWave)
    Button btnWave;
    @BindView(R.id.btnShutdown)
    Button btnShutdown;
    @BindView(R.id.btnRun)
    Button btnRun;

    private ConnectionThread connect;
    private ArrayList<String> commands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlle_remoto);

        this.commands = new ArrayList<>();
        this.commands.add(Constants.HIGH_LEVEL);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String address = bundle.getString("devAddress");
            connect = new ConnectionThread(address);
            connect.start();
        }

        // forward_();
        this.btnFront.setOnClickListener(v -> this.commands.add("w"));

        // back_();
        this.btnBack.setOnClickListener(v -> this.commands.add("s"));

        // turn_right_();
        this.btnRight.setOnClickListener(v -> this.commands.add("d"));

        // turn_left_();
        this.btnLeft.setOnClickListener(v -> this.commands.add("a"));

        //stand_();
        this.btnUp.setOnClickListener(v -> this.commands.add("e"));

        // sit_();
        this.btnDown.setOnClickListener(v -> this.commands.add("q"));

        // wave_();
        this.btnWave.setOnClickListener(v -> this.commands.add("b"));

        // shutdown();
        this.btnShutdown.setOnClickListener(v -> this.commands.add("x"));

        this.btnRun.setOnClickListener(v -> {
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
        Toast.makeText(this, "Bluetooth desconectado!", Toast.LENGTH_LONG).show();
    }

    public void clearCommands() {
        this.commands = new ArrayList<>();
    }
}

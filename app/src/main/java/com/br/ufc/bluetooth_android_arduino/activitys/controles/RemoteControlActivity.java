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
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Remote Control - High Level
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

        ButterKnife.bind(this);
        this.commands = new ArrayList<>();
        this.commands.add(Constants.HIGH_LEVEL);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String address = bundle.getString("devAddress");
            connect = new ConnectionThread(address);
            connect.start();
        }
    }

    @OnClick(R.id.btnFront)
    public void front() { // forward_()
        this.commands.add("w");
    }

    @OnClick(R.id.btnBack)
    public void back() {// back_();
        this.commands.add("s");
    }

    @OnClick(R.id.btnRight)
    public void right() {// turn_right_();
        this.commands.add("d");
    }

    @OnClick(R.id.btnLeft)
    public void left() {// turn_left_();
        this.commands.add("a");
    }

    @OnClick(R.id.buttonUp)
    public void up() {// stand_();
        this.commands.add("e");
    }

    @OnClick(R.id.buttonDown)
    public void down() {// sit();
        this.commands.add("q");
    }

    @OnClick(R.id.btnWave)
    public void wave() { // wave();
        this.commands.add("b");
    }

    @OnClick(R.id.btnShutdown)
    public void shutdown() { // shutdown();
        this.commands.add("x");
    }

    @OnClick(R.id.btnRun)
    public void run() {
        sendMessage();
        this.clearCommands();
    }

    public void sendMessage() {
        String commands = "";
        for (String str : this.commands)
            commands = commands + str;

        byte[] data = commands.getBytes();
        this.connect.write(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connect.cancel();
        Toast.makeText(this, "Bluetooth desconectado!", Toast.LENGTH_LONG).show();
    }

    public void clearCommands() {
        this.commands = new ArrayList<>();
        this.commands.add(Constants.HIGH_LEVEL);
    }
}

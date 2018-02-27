package com.br.ufc.bluetooth_android_arduino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PairedDevicesActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aparelhos_pareados);

        this.listView = findViewById(R.id.listViewAparelhos);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        List<String> aparelhos = new ArrayList<String>();
        for(BluetoothDevice bt : pairedDevices)
            aparelhos.add(bt.getName() + "\n" + bt.getAddress());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                aparelhos
        );

        this.listView.setAdapter(adapter);

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String item = listView.getItemAtPosition(position).toString();

                String devName = item.substring(0, item.indexOf("\n"));
                String devAddress = item.substring(item.indexOf("\n")+1, item.length());

                Intent returnIntent = new Intent();
                returnIntent.putExtra("btDevName", devName);
                returnIntent.putExtra("btDevAddress", devAddress);
                returnIntent.putExtra("selecionado", "selecionado");
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}

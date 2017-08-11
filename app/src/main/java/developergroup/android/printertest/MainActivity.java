package developergroup.android.printertest;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.BluetoothChat.R;

import developergroup.android.printertest.cardreader.BluetoothSPP;
import developergroup.android.printertest.cardreader.BluetoothService;
import developergroup.android.printertest.cardreader.BluetoothState;
import developergroup.android.printertest.cardreader.Constants;
import developergroup.android.printertest.cardreader.DeviceList;

public class MainActivity extends AppCompatActivity implements BluetoothSPP.OnDataReceivedListener {
    BluetoothSPP bt = null;
    EditText edittext_command;
    TextView textview_response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview_response = (TextView) findViewById(R.id.textview_response);
        edittext_command = (EditText) findViewById(R.id.edittext_command);
        bt = new BluetoothSPP(this);
        bt.setupService();
        bt.startService(BluetoothState.DEVICE_OTHER);
        bt.setOnDataReceivedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);

        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            } else {
                // Do something if user doesn't choose any device (Pressed back)
            }
        }
    }

    public void connectDeviceCallback(View view) {
        bt.connect("98:D3:31:80:80:D3");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bt!=null)
        bt.stopService();
    }

    public void sendCommandCallback(View v) {
        Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                bt.send("$1V",true);
                bt.send("$1S0",true);
               bt.send("$1T00",true);


            }
        });

        thread.start();

    }

    @Override
    public void onDataReceived(byte[] data, String message) {
        Log.d("DEBUG", "onDataReceived: " + data + message);

    }
}

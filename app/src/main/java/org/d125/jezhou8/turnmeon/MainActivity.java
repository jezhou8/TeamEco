package org.d125.jezhou8.turnmeon;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.DateFormat;
import android.location.Location;
import android.nfc.Tag;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.widget.Toast;

import java.util.Set;


public class MainActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter;

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(bluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){

            }else if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("MainActivity", device.getName());
            }
        }
    };

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_layout);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void onClick(View view){
        if(view.getId() == R.id.returnToHome) {
            setContentView(R.layout.startup_layout);
            enableBT();
        }else {
            setContentView(R.layout.main_layout);
            enableBT();
        }
    }

    public void enableBT(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
        bluetoothAdapter.startDiscovery();
    }
    public void printDevices(View view){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        for(BluetoothDevice bt : pairedDevices)
            Log.d("MainActivity", bt.getName());
    }
    public void sendNotification(View view) {

//Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle("Reduce Energy Consumption")
                        .setContentText("Don't forget to turn off unnecessary power draw!")
                        .setVibrate(new long[] {1000,1000})
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);


// Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());

//When you issue multiple notifications about the same type of event, it’s best practice for your app to try to update an existing notification with this new information, rather than immediately creating a new notification. If you want to update this notification at a later date, you need to assign it an ID. You can then use this ID whenever you issue a subsequent notification. If the previous notification is still visible, the system will update this existing notification, rather than create a new one. In this example, the notification’s ID is 001//

    }


}

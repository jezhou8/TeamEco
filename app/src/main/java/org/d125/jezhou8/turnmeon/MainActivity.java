package org.d125.jezhou8.turnmeon;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    BluetoothAdapter bluetoothAdapter;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastUpdate = 0;
    private float last_x , last_y, last_z;
    private static final int SHAKE_THRESHHOLD = 1000;
    Vibrator v ;
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
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onSensorChanged(SensorEvent sensorEvent){
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = sensorEvent.values[0];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if( (curTime - lastUpdate) > 100 ){

                long dtime = curTime - lastUpdate;
                lastUpdate = curTime;

                float speed = Math.abs(x + z + - last_x - last_z)/dtime * 10000;

                //Log.d("MainActivity", Float.toString(speed));

                if (speed > SHAKE_THRESHHOLD) {
                    Log.d("MainActivity", Float.toString(speed));
                    Toast toast = Toast.makeText(getApplicationContext(), "Leaving home? Don't forget to save power", Toast.LENGTH_SHORT);
                    v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(500);
                    toast.show();
                }
                last_x = x;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void onClick(View view){
        if(view.getId() == R.id.returnToHome) {
            setContentView(R.layout.startup_layout);
        }else {
            setContentView(R.layout.main_layout);
        }
    }

    public void printDevices(View view){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "0");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
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
        private BluetoothAdapter mBluetoothAdapter;
        private boolean mScanning;
        private Handler mHandler;

        // Stops scanning after 10 seconds.
        private static final long SCAN_PERIOD = 10000;
        private void scanLeDevice(final boolean enable) {
            if (enable) {
                // Stops scanning after a pre-defined scan period.
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    }
                }, SCAN_PERIOD);

                mScanning = true;
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }
    }
}

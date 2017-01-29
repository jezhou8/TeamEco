package org.d125.jezhou8.turnmeon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.content.Context;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_layout);
    }

    public void onClick(View view){
        if(view.getId() == R.id.returnToHome)
            setContentView(R.layout.startup_layout);
        else
            setContentView(R.layout.main_layout);
    }

    public void sendNotification(View view) {

//Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("Reduce Energy Consumption")
                        .setContentText("Don't forget to turn off unnecessary power draw!");


// Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());

//When you issue multiple notifications about the same type of event, it’s best practice for your app to try to update an existing notification with this new information, rather than immediately creating a new notification. If you want to update this notification at a later date, you need to assign it an ID. You can then use this ID whenever you issue a subsequent notification. If the previous notification is still visible, the system will update this existing notification, rather than create a new one. In this example, the notification’s ID is 001//

    }


}

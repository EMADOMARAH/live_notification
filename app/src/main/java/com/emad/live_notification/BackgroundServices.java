package com.emad.live_notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.installations.Utils;

import java.security.Provider;

public class BackgroundServices extends Service {


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference =  database.getReference("User");
    private static Uri sound;
    private final  long[] pattern = {100,300,300,300};

    private NotificationManager manager;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, " MyService Created ", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sound  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        reference.addChildEventListener (new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // String name = editText.getText().toString();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("n","n" , NotificationManager.IMPORTANCE_DEFAULT);
                    manager = getSystemService(NotificationManager.class);
                    manager.createNotificationChannel(channel);

                    ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                            .createNotificationChannel(channel);

                }

                Notification builder =new Notification.Builder(getApplicationContext() , "n" )
                        .setContentTitle("Notification")
                        .setContentText(snapshot.getValue().toString())
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_custom_notif_icon)
                        .setLights(0xff0000ff, 300, 1000) // blue color
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setSound(sound)
                        .setVibrate(pattern)
                        .build();



                startForeground(1,builder);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return START_STICKY;



    }




}

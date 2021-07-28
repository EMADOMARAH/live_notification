package com.emad.live_notification;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ExampleJobService extends JobService {
    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Query reference =  database.getReference("User").orderByValue();


    private NotificationManager manager;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        doBackgroundWork(params);

        return true;
    }

    private void doBackgroundWork(final JobParameters params) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent fullScreenIntent = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                        fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                List<String> myNames = new ArrayList<>();

                for (int i = 0; i < 1000; i++) {
                    Log.d(TAG, "run: " + i);
//                    reference.addChildEventListener (new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                            // String name = editText.getText().toString();
//
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                NotificationChannel channel = new NotificationChannel("n","n" , NotificationManager.IMPORTANCE_DEFAULT);
//                                manager = getSystemService(NotificationManager.class);
//                                manager.createNotificationChannel(channel);
//
//                                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
//                                        .createNotificationChannel(channel);
//
//                            }
//
//                            Notification builder =new Notification.Builder(getApplicationContext() , "n" )
//                                    .setContentTitle("Notification")
//                                    .setContentText(snapshot.getValue().toString())
//                                    .setAutoCancel(true)
//                                    .setSmallIcon(R.drawable.ic_custom_notif_icon)
//                                    .setLights(0xff0000ff, 300, 1000) // blue color
//                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
////                                    .setSound(sound)
////                                    .setVibrate(pattern)
//                                    .build();
//                            Log.d(TAG,snapshot.getValue().toString());
//
//
//
//                           startForeground(1,builder);
//
//                        }
//
//                        @Override
//                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds : snapshot.getChildren()) {

                                String name = ds.child("name").getValue().toString();
                                if (snapshot.getChildrenCount() != (long) myNames.size()){
                                    myNames.add(name);
                                }
                                Log.d(TAG, name.toString());
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel channel = new NotificationChannel("n","n" , NotificationManager.IMPORTANCE_DEFAULT);
                                manager = getSystemService(NotificationManager.class);
                                manager.createNotificationChannel(channel);

                                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                                        .createNotificationChannel(channel);

                            }

                         //   String q = database.getReference("User");

                            Notification builder =new Notification.Builder(getApplicationContext() , "n" )
                                    .setContentTitle("Notification")
                                    .setContentText(String.valueOf(myNames.size()))
                                    .setAutoCancel(true)
                                    .setAllowSystemGeneratedContextualActions(true)
                                    .setSmallIcon(R.drawable.ic_custom_notif_icon)
                                    .setLights(0xff0000ff, 300, 1000) // blue color
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                                    .setSound(sound)
//                                    .setVibrate(pattern)
                                    .build();
                            Log.d(TAG,String.valueOf(String.valueOf(myNames.size())));
                            

                            startForeground(1,builder);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });

//                    reference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                NotificationChannel channel = new NotificationChannel("n","n" , NotificationManager.IMPORTANCE_DEFAULT);
//                                manager = getSystemService(NotificationManager.class);
//                                manager.createNotificationChannel(channel);
//
//                                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
//                                        .createNotificationChannel(channel);
//
//                            }
//
//                            Notification builder =new Notification.Builder(getApplicationContext() , "n" )
//                                    .setContentTitle("Notification")
//                                    .setContentText(snapshot.getChildren().toString())
//                                    .setAutoCancel(true)
//                                    .setSmallIcon(R.drawable.ic_custom_notif_icon)
//                                    .setLights(0xff0000ff, 300, 1000) // blue color
//                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
////                                    .setSound(sound)
////                                    .setVibrate(pattern)
//                                    .build();
//                            Log.d(TAG,snapshot.getValue().toString());
//
//
//
//                            startForeground(1,builder);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });

                    if (jobCancelled) {
                        return;
                    }

                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Log.d(TAG, "Job finished");
                jobFinished(params, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
}

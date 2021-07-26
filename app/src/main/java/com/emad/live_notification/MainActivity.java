package com.emad.live_notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    int id = 0;

    EditText editText ;
    Button button , stopServiceButton;


    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        member = new Member();
        button = findViewById(R.id.save_btn);
        editText = findViewById(R.id.name_edit_txt);
        stopServiceButton = findViewById(R.id.stop_service_btn);

        reference =  database.getReference("User");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    id = (int)snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("Error", "Failed to read value.", error.toException());
            }
        });
         Intent serviceIntent = new Intent(this , BackgroundServices.class);
         startService(serviceIntent);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

               // String name = editText.getText().toString();

                //notification(snapshot.getValue().toString());


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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                if (name.isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                }else {
                    member.setName (editText.getText().toString());
                    reference.child(String.valueOf(id+1)).setValue(member);
                }
            }
        });
        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(getApplicationContext() , BackgroundServices.class);
                stopService(serviceIntent);
            }
        });


    }

//    private void notification(String data){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationChannel channel = new NotificationChannel("n","n" , NotificationManager.IMPORTANCE_DEFAULT);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//
//        }
//
//        Notification builder = new Notification.Builder(this , "n")
//                .setContentText("Simple Text")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setAutoCancel(true)
//                .setContentText(data.toString())
//                .build();
//
////        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
////        managerCompat.notify(999, builder.build());
//
//    }
}
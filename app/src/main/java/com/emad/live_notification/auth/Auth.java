package com.emad.live_notification.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.emad.live_notification.MainActivity;
import com.emad.live_notification.R;
import com.emad.live_notification.auth.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class Auth extends AppCompatActivity {



    private FirebaseAuth mAuth;


    TextInputEditText email_txt , password_txt;
    String email,password;
    String deviceToken;

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //there is current user signed in
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        email_txt = findViewById(R.id.email_field);
        password_txt = findViewById(R.id.password_field);



   

    }

    public void SignIn(View view) {
        email = email_txt.getText().toString();
        password  = password_txt.getText().toString();


        mAuth.signInWithEmailAndPassword(email , password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //redirect the user to the login screen
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                            startActivity(intent);
                        }
                    }
                });



        Log.d("Auth" , email + "  " + password);
    }

    public void SignUp(View view) {
        email = email_txt.getText().toString();
        password  = password_txt.getText().toString();


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        deviceToken = task.getResult();

                    }
                });

        mAuth.createUserWithEmailAndPassword(email , password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()){

                   User user = new User();
                   user.setName(email.substring(0, email.indexOf("@")));
                   user.setDevice_token(deviceToken);
                   user.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                   FirebaseDatabase.getInstance().getReference()
                           .child("users")
                           .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                           .setValue(user)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   FirebaseAuth.getInstance().signOut();

                                   //redirect the user to the home screen
                                   Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                   finish();
                                   startActivity(intent);
                               }
                           });


               }else {
                   Log.d("Auth", task.getException().toString());
                   Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
               }

            }
        });

        Log.d("Auth" , email + "  " + password);
    }

}
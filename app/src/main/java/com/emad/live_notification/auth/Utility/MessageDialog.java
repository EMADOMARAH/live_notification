package com.emad.live_notification.auth.Utility;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.emad.live_notification.R;
import com.emad.live_notification.auth.ApiInterface;
import com.emad.live_notification.auth.Models.Message;
import com.emad.live_notification.auth.Models.Request.Notification;
import com.emad.live_notification.auth.Models.Request.Post;
import com.emad.live_notification.auth.Models.Responce.Response;
import com.emad.live_notification.auth.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessageDialog extends DialogFragment {

    private static final String TAG = "MessageDialog";
    //reference for user data
    DatabaseReference reference;
    //reference for messages data
    DatabaseReference msgReference;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //device token who will receive notification
    String device_token;

    Notification notification;
    ApiInterface apiInterface;
    Retrofit retrofit;

    //create a new bundle and set the arguments to avoid a null pointer
    public MessageDialog(){
        super();
        setArguments(new Bundle());
    }


    //widgets
    EditText mMessage;
    Message message;

    //vars
    private String mUserId , deviceToken;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started.");
        reference =  database.getReference("users");
        msgReference= database.getReference();

        //get user id that i clicked on
        mUserId = getArguments().getString("intent_user_id");

        //make new object from retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/fcm/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //get the device token who will receive notification depends on the id
        reference.child(mUserId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    device_token= task.getResult().getValue(User.class).getDevice_token();
                }

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_message, container, false);
        mMessage = (EditText) view.findViewById(R.id.message);

        message = new Message();
        Button send = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                if(!mMessage.getText().toString().equals("")){

                    //create the new message

                    message.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    message.setMessage(mMessage.getText().toString());
                    message.setTimestamp(getTimestamp());
                    //make the notification title and body the will be sent
                    notification = new Notification("Api Notification",mMessage.getText().toString());



                    //insert the new message

                    sendMessage();

                    Toast.makeText(getActivity(), "message sent", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "enter a message", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    private void sendMessage() {

        apiInterface = retrofit.create(ApiInterface.class);

        Post post = new Post(notification , device_token);
        //save the message to firebase
        msgReference
                .child("messages")
                .child(mUserId)
                .child(reference.push().getKey())
                .setValue(message);

        //make the api call that lunch the notification
        Call<Response> sendNotify = apiInterface.sendNotification(post);
        sendNotify.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                System.out.println(response.toString());
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });




    }

    /**
     * Return the current timestamp in the form of a string
     * @return
     */
    private String getTimestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
        return sdf.format(new Date());
    }


}

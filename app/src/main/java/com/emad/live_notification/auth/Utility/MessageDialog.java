package com.emad.live_notification.auth.Utility;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.emad.live_notification.R;
import com.emad.live_notification.auth.Models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MessageDialog extends DialogFragment {

    private static final String TAG = "MessageDialog";

    //create a new bundle and set the arguments to avoid a null pointer
    public MessageDialog(){
        super();
        setArguments(new Bundle());
    }


    //widgets
    EditText mMessage;

    //vars
    private String mUserId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started.");
        mUserId = getArguments().getString("intent_user_id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_message, container, false);
        mMessage = (EditText) view.findViewById(R.id.message);

        Button send = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: sending a new message");

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                if(!mMessage.getText().toString().equals("")){

                    //create the new message
                    Message message = new Message();
                    message.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    message.setMessage(mMessage.getText().toString());
                    message.setTimestamp(getTimestamp());

                    //insert the new message
                    reference
                            .child("messages")
                            .child(mUserId)
                            .child(reference.push().getKey())
                            .setValue(message);
                    Toast.makeText(getActivity(), "message sent", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "enter a message", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
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

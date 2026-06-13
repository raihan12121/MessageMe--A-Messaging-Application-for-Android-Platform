package com.example.messageme.firebase;

import android.annotation.SuppressLint;


import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;



@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MessagingService extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String token) {

        super.onNewToken(token);



    }
    @Override
    public void onMessageReceived(@NonNull  RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

    }
}

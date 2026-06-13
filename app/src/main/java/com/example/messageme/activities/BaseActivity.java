package com.example.messageme.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.messageme.utilities.Constants;
import com.example.messageme.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaseActivity extends AppCompatActivity {
    private DocumentReference documentReference;


    @Override
    protected  void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);
        if (userId != null && !userId.trim().isEmpty()) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
                    .document(userId);
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        if (documentReference != null) {
            documentReference.update(Constants.KEY_AVAILABILITY, 0);
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        if (documentReference != null) {
            documentReference.update(Constants.KEY_AVAILABILITY, 1);
        }
    }

}

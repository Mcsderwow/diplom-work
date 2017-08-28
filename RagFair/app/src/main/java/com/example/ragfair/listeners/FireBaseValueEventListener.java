package com.example.ragfair.listeners;


import android.widget.Toast;

import com.example.ragfair.MyApplication;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public abstract class FireBaseValueEventListener implements ValueEventListener {


    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(MyApplication.getAppContext(),"network error",Toast.LENGTH_SHORT).show();
    }
}

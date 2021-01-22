package com.victormenezes.whatsapp.config;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ConfigFirebase {
    private static DatabaseReference reference;

    public static DatabaseReference getFirebase(){

        if(reference == null){
            reference = FirebaseDatabase.getInstance().getReference();
        }

        return reference;
    }
}

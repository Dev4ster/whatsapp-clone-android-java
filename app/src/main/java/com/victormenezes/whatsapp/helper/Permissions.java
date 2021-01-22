package com.victormenezes.whatsapp.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

    public static boolean validatePermissions(int requestCode, Activity activity, String[] permissions){
        List<String> listaPermissions = new ArrayList<>();

        if(Build.VERSION.SDK_INT >= 23){
            for(String permission : permissions){
        boolean validatepermission = ContextCompat.checkSelfPermission(activity,permission) == PackageManager.PERMISSION_GRANTED;
        if(!validatepermission) listaPermissions.add(permission);
    }
}

        if(listaPermissions.isEmpty()) return true;

        String[] newPermissions = new String[listaPermissions.size()];
        listaPermissions.toArray(newPermissions);

        ActivityCompat.requestPermissions(activity, newPermissions, requestCode);
        return true;
    }
}

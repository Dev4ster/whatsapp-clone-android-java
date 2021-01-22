package com.victormenezes.whatsapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

    public static boolean validaPermissoes(int requestCode, Activity activity, String[] permissoes){
        List<String> listaPermissoes = new ArrayList<>();

        if(Build.VERSION.SDK_INT >= 23){
            for(String permissao : permissoes){
                boolean validaPermissao = ContextCompat.checkSelfPermission(activity,permissao) == PackageManager.PERMISSION_GRANTED;
                if(!validaPermissao) listaPermissoes.add(permissao);
            }
        }

        if(listaPermissoes.isEmpty()) return true;

        String[] novasPermissoes = new String[listaPermissoes.size()];
        listaPermissoes.toArray(novasPermissoes);

        ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);
        return true;
    }
}

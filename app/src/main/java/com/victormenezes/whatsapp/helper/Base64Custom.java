package com.victormenezes.whatsapp.helper;

import android.util.Base64;

public class Base64Custom {
    public static String crypt(String data){
        return Base64.encodeToString(data.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decrypt(String base64Data){
        return new String(Base64.decode(base64Data, Base64.DEFAULT));
    }
}

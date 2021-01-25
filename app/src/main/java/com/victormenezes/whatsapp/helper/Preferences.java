package com.victormenezes.whatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferences {
    private Context context;
    private SharedPreferences sharedPreferences;
    private final String PREFERENCES_NAME = "APP_WHATS";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    public static final   String CHAVE_NOME = "nome";
    public  static final  String CHAVE_ID = "id";
    public  static final   String CHAVE_EMAIL = "email";


    public Preferences(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, MODE);
        editor = sharedPreferences.edit();
    }

    public void saveUser(String nome, String email, String id){
        editor.putString(CHAVE_NOME,nome);
        editor.putString(CHAVE_EMAIL,email);
        editor.putString(CHAVE_ID,id);
        editor.commit();

    }

    public void wipeUser(){
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String>  getUser(){
        HashMap<String, String> userData = new HashMap<>();
        userData.put(CHAVE_NOME, sharedPreferences.getString(CHAVE_NOME,CHAVE_NOME));
        userData.put(CHAVE_EMAIL, sharedPreferences.getString(CHAVE_EMAIL,CHAVE_EMAIL));
        userData.put(CHAVE_ID, sharedPreferences.getString(CHAVE_ID,CHAVE_ID));
        return userData;
    }

}

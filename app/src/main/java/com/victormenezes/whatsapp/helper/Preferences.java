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
    public  static final  String CHAVE_TELEFONE = "telefone";
    public  static final   String CHAVE_TOKEN = "token";


    public Preferences(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, MODE);
        editor = sharedPreferences.edit();
    }

    public void saveUser(String nome, String telefone, String token){
        editor.putString(CHAVE_NOME,nome);
        editor.putString(CHAVE_TELEFONE,telefone);
        editor.putString(CHAVE_TOKEN,token);
        editor.commit();

    }

    public HashMap<String, String>  getUser(){
        HashMap<String, String> userData = new HashMap<>();
        userData.put(CHAVE_NOME, sharedPreferences.getString(CHAVE_NOME,CHAVE_NOME));
        userData.put(CHAVE_TELEFONE, sharedPreferences.getString(CHAVE_TELEFONE,CHAVE_TELEFONE));
        userData.put(CHAVE_TOKEN, sharedPreferences.getString(CHAVE_TOKEN,CHAVE_TOKEN));
        return userData;
    }

}

package com.victormenezes.whatsapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferences {
    private Context contexto;
    private SharedPreferences sharedPreferences;
    private final String PREFERENCES_NAME = "APP_WHATS";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    public static final   String CHAVE_NOME = "nome";
    public  static final  String CHAVE_TELEFONE = "telefone";
    public  static final   String CHAVE_TOKEN = "token";


    public Preferences(Context contexto){
        this.contexto = contexto;
        sharedPreferences = contexto.getSharedPreferences(PREFERENCES_NAME, MODE);
        editor = sharedPreferences.edit();
    }

    public void salvarUsuario(String nome, String telefone, String token){
        editor.putString(CHAVE_NOME,nome);
        editor.putString(CHAVE_TELEFONE,telefone);
        editor.putString(CHAVE_TOKEN,token);
        editor.commit();

    }

    public HashMap<String, String>  getDadosUsuario(){
        HashMap<String, String> dados = new HashMap<>();
        dados.put(CHAVE_NOME, sharedPreferences.getString(CHAVE_NOME,CHAVE_NOME));
        dados.put(CHAVE_TELEFONE, sharedPreferences.getString(CHAVE_TELEFONE,CHAVE_TELEFONE));
        dados.put(CHAVE_TOKEN, sharedPreferences.getString(CHAVE_TOKEN,CHAVE_TOKEN));
        return dados;
    }

}

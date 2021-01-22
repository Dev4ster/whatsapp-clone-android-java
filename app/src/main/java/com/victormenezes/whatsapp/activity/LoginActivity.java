package com.victormenezes.whatsapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.victormenezes.whatsapp.R;
import com.victormenezes.whatsapp.helper.Permissions;
import com.victormenezes.whatsapp.helper.Preferences;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText editPhone;
    private EditText editDDD;
    private EditText editTelReg;
    private EditText editName;
    private Button buttonCadastrar;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.SEND_SMS
    };


    private String[] masks = {
            "NNNNN-NNNN",
            "NN",
            "+NN"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Permissions.validaPermissoes(1,this, permissoesNecessarias);
        editPhone = findViewById(R.id.editPhone);
        editDDD = findViewById(R.id.editDDD);
        editTelReg = findViewById(R.id.editTelReg);
        editName = findViewById(R.id.editName);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);


        setInputMask(editPhone, masks[0], null);
        setInputMask(editDDD, masks[1], editPhone);
        setInputMask(editTelReg, masks[2], editDDD);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editName.getText().toString();
                String userTelephone = editPhone.getText().toString();
                String userDDD = editDDD.getText().toString();
                String userRegTel = editTelReg.getText().toString();

                String telefoneCompleto = userRegTel + userDDD + userTelephone;
                String telephoneFormatted = telefoneCompleto
                        .replace("+", "")
                        .replace("-", "");

                // Generate token
                Random random = new Random();
                int numeroRandom = random.nextInt(9999 - 1000) + 1000;
                String token = String.valueOf(numeroRandom);

                // Save token

                Preferences preferencias = new Preferences(getApplicationContext());
                preferencias.salvarUsuario(userName, telephoneFormatted, token);

                // Send sms

                telephoneFormatted = "5554";

               boolean sent = enviarSms("+" + telephoneFormatted, String.format("Olá seu token é:%s", token));

               if(sent){
                   Intent intent = new Intent(LoginActivity.this, ValidatorActivity.class);
                   startActivity(intent);
                   finish();

               }else {
                   Toast.makeText(LoginActivity.this, "Problema ao enviar sms", Toast.LENGTH_LONG).show();
               }
            }
        });
    }

    public void setInputMask(EditText field, String mask, @Nullable final EditText nextField) {
        SimpleMaskFormatter SMF = new SimpleMaskFormatter(mask);
        MaskTextWatcher watcher = new MaskTextWatcher(field, SMF);
        field.addTextChangedListener(new MaskTextWatcher(field, SMF){
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                super.onTextChanged(charSequence, i, i1, i2);
                if(this.formatter.getMask().length() == this.currentText.length()){

                    if(nextField != null){
                        if(!nextField.hasFocus()){
                            nextField.requestFocus();
                        }
                    }
                }
            }
        });

    }

    private boolean enviarSms(String telefone, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, msg, null, null);
            return true;
        } catch (Exception e) {
            Log.e("WARN", "ERRO SEND");
            e.printStackTrace();
            return false;
        }

    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("WARN", "ERRO ACEITE");
        for(int resultado : grantResults){
            if(resultado == PackageManager.PERMISSION_DENIED){
                alertPermission();
            }
        }
    }

    public void alertPermission(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("para ultilizar esse app é necessário aceitar as permissões");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}

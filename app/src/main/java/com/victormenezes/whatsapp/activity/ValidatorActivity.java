package com.victormenezes.whatsapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.victormenezes.whatsapp.R;
import com.victormenezes.whatsapp.helper.Preferences;

import java.util.HashMap;

public class ValidatorActivity extends AppCompatActivity {
    private Button buttonValidate;
    private EditText editCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validator);
        editCode = findViewById(R.id.editCode);
        buttonValidate = findViewById(R.id.buttonValidate);

        SimpleMaskFormatter SMF = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher codigoWatcher = new MaskTextWatcher(editCode, SMF);
        editCode.addTextChangedListener(codigoWatcher);

        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences preferences = new Preferences(ValidatorActivity.this);
                HashMap<String, String> userData = preferences.getUser();
                String token = userData.get(Preferences.CHAVE_TOKEN);
                String tokenDigitado = editCode.getText().toString();

                if(tokenDigitado.equals(token)){
                    Toast.makeText(ValidatorActivity.this, "Token correto", Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(ValidatorActivity.this, "Token incorreto", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

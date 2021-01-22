package com.victormenezes.whatsapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.victormenezes.whatsapp.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText editRegisterName;
    private EditText editRegisterEmail;
    private EditText editRegisterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editRegisterName = findViewById(R.id.editRegisterName);
        editRegisterEmail = findViewById(R.id.editRegisterEmail);
        editRegisterPassword = findViewById(R.id.editLoginPassword);
    }
}

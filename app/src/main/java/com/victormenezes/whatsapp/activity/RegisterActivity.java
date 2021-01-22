package com.victormenezes.whatsapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.victormenezes.whatsapp.R;
import com.victormenezes.whatsapp.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText editRegisterName;
    private EditText editRegisterEmail;
    private EditText editRegisterPassword;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editRegisterName = findViewById(R.id.editRegisterName);
        editRegisterEmail = findViewById(R.id.editRegisterEmail);
        editRegisterPassword = findViewById(R.id.editLoginPassword);
    }

    public void register(View view){
        String name, email, password;
        name = editRegisterName.getText().toString();
        email = editRegisterEmail.getText().toString();
        password = editRegisterPassword.getText().toString();
        user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);


    }
}

package com.victormenezes.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.victormenezes.whatsapp.R;
import com.victormenezes.whatsapp.config.ConfigFirebase;
import com.victormenezes.whatsapp.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText editRegisterName;
    private EditText editRegisterEmail;
    private EditText editRegisterPassword;
    private User user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editRegisterName = findViewById(R.id.editRegisterName);
        editRegisterEmail = findViewById(R.id.editRegisterEmail);
        editRegisterPassword = findViewById(R.id.editRegisterPassword);
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
        RegisterFirebase();

    }

    private void RegisterFirebase(){
        auth = ConfigFirebase.getFirebaseAuthentication();
        auth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG)
                            .show();

                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao cadastrar usuário", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }).addOnFailureListener(RegisterActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Erro ao se conectar com servidor", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
}

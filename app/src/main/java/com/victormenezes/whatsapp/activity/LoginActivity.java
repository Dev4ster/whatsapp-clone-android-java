package com.victormenezes.whatsapp.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.victormenezes.whatsapp.R;
import com.victormenezes.whatsapp.config.ConfigFirebase;
import com.victormenezes.whatsapp.helper.Base64Custom;
import com.victormenezes.whatsapp.helper.Preferences;
import com.victormenezes.whatsapp.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText editLoginEmail;
    private EditText editLoginPassword;
    private User user;
    private FirebaseAuth auth;
    private Preferences userPreferences;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editLoginEmail = findViewById(R.id.editLoginEmail);
        editLoginPassword = findViewById(R.id.editLoginPassword);
        userPreferences = new Preferences(this);
        databaseReference = ConfigFirebase.getFirebase();
        checkUserLogin();
    }

    public void Login(View view){
        String email = editLoginEmail.getText().toString();
        String password = editLoginPassword.getText().toString();
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        validateLogin();

    }

    private void validateLogin(){
        auth = ConfigFirebase.getFirebaseAuthentication();
        auth.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login", Toast.LENGTH_LONG).show();
                    openMainActivity();
                    final String userID = Base64Custom.crypt( user.getEmail());
                    databaseReference = databaseReference.child("usuarios").child(userID);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User recoverUser = snapshot.getValue(User.class);
                            userPreferences.saveUser(
                                    recoverUser.getName(),
                                    user.getEmail(),
                                    userID
                            );
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else {
                    Toast.makeText(LoginActivity.this, "Erro ao fazer login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkUserLogin(){
        auth = ConfigFirebase.getFirebaseAuthentication();
        if(auth.getCurrentUser() != null){
            openMainActivity();
        }
    }

    private void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public  void createAccount(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);

    }

}

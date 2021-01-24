package com.victormenezes.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.victormenezes.whatsapp.R;
import com.victormenezes.whatsapp.adapter.TabAdapter;
import com.victormenezes.whatsapp.config.ConfigFirebase;
import com.victormenezes.whatsapp.helper.Base64Custom;
import com.victormenezes.whatsapp.helper.SlidingTabLayout;
import com.victormenezes.whatsapp.model.Contact;
import com.victormenezes.whatsapp.model.User;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth auth;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private DatabaseReference databaseReference;
    private String contactUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        slidingTabLayout = findViewById(R.id.stl_tabs);
        viewPager = findViewById(R.id.viewPager);
        auth = ConfigFirebase.getFirebaseAuthentication();
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));
        toolbar.setTitle("Meu APP");
        setSupportActionBar(toolbar);

        // config adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), 1);
        viewPager.setAdapter( tabAdapter );
        slidingTabLayout.setViewPager(viewPager);

        databaseReference = ConfigFirebase.getFirebase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);

        return true;

    }

    private void logOutUser(){
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_config:
                return true;
            case R.id.action_logout:
                logOutUser();
                return true;
            case R.id.action_add_contact:
                openCreateContact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openCreateContact(){
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setTitle("Novo contato");
        alBuilder.setMessage("E-mail do usuário");
        alBuilder.setCancelable(false);

        final EditText editText = new EditText(this);
        alBuilder.setView(editText);

        //buttons
        alBuilder.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = editText.getText().toString();

                if(email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Preencha o e-mail", Toast.LENGTH_LONG).show();
                }else {

                    // check user exist in firebase
                    contactUser = Base64Custom.crypt(email);
                    databaseReference = databaseReference.child("usuarios").child(contactUser);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue() != null){

                                // get user query
                                Contact contactUserData = snapshot.getValue(Contact.class);
                                contactUserData.setId(contactUser);
                                //check if user logged is contact

                                if(contactUserData.getEmail().equals(auth.getCurrentUser().getEmail())) {
                                    Toast.makeText(getApplicationContext(), "Você não pode se auto-adiconar", Toast.LENGTH_LONG).show();

                                } else {
                                    // create child contact
                                    String emailLogged = auth.getCurrentUser().getEmail();
                                    emailLogged = Base64Custom.crypt(emailLogged);
                                    databaseReference = ConfigFirebase.getFirebase();
                                    databaseReference = databaseReference
                                            .child("contatos")
                                            .child(emailLogged)
                                            .child(contactUser);

                                    databaseReference.setValue(contactUserData);
                                }

                            }else {
                                Toast.makeText(getApplicationContext(), "Usuário não encontrado", Toast.LENGTH_LONG).show();
                                Log.i("INFO", String.format("snapshot: %", snapshot.exists()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        alBuilder.setNegativeButton("Cancelar", null);
        alBuilder.show();

}
}

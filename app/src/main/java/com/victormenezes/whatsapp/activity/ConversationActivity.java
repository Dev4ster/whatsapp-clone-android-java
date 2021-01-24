package com.victormenezes.whatsapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.victormenezes.whatsapp.R;
import com.victormenezes.whatsapp.config.ConfigFirebase;
import com.victormenezes.whatsapp.helper.Base64Custom;
import com.victormenezes.whatsapp.model.Message;

public class ConversationActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String contactName;
    private String contactEmail;

    private ListView listMessages;
    private EditText editMessage;
    private DatabaseReference database;

    private String idUserCurrent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        listMessages = findViewById(R.id.list_messages);
        editMessage = findViewById(R.id.editMessage);

        // recovery parameters intent
        idUserCurrent = ConfigFirebase.getFirebaseAuthentication().getCurrentUser().getEmail();
        idUserCurrent = Base64Custom.crypt(idUserCurrent);
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            contactName = extras.getString("name");
            contactEmail = extras.getString("email");
        }


        toolbar = findViewById(R.id.toolbar_conversation);
        toolbar.setTitle(contactName);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void send(View view){
        String textMessage = editMessage.getText().toString();

        if(!textMessage.isEmpty()){
            Message message = new Message();
            message.setIdUser(idUserCurrent);
            message.setMessage(textMessage);
            String contactUserId = Base64Custom.crypt(contactEmail);
            saveMessage(idUserCurrent, contactUserId, message);

        }
    }

    private boolean saveMessage(String currentUserId, String contactUserId, Message message){
        try {
            database = ConfigFirebase.getFirebase();
            database = database.child("mensagens")
                    .child(currentUserId)
                    .child(contactUserId);
            database.push().setValue(message);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}

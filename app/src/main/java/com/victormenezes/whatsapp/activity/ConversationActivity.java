package com.victormenezes.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.victormenezes.whatsapp.R;
import com.victormenezes.whatsapp.adapter.MessageAdapter;
import com.victormenezes.whatsapp.config.ConfigFirebase;
import com.victormenezes.whatsapp.helper.Base64Custom;
import com.victormenezes.whatsapp.helper.Preferences;
import com.victormenezes.whatsapp.model.Conversation;
import com.victormenezes.whatsapp.model.Message;

import java.util.ArrayList;
import java.util.HashMap;

public class ConversationActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String contactName;
    private String contactEmail;

    private ListView listMessages;
    private EditText editMessage;
    private DatabaseReference database;

    private String idUserCurrent;

    private ArrayList<Message> messages;
    private MessageAdapter adapter;
    private String contactUserId;

    private ValueEventListener messagesListener;

    private  DatabaseReference messagesFirebase;

    private Preferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        listMessages = findViewById(R.id.list_messages);
        editMessage = findViewById(R.id.editMessage);
        preferences = new Preferences(this);

        // recovery parameters intent
        idUserCurrent = ConfigFirebase.getFirebaseAuthentication().getCurrentUser().getEmail();
        idUserCurrent = Base64Custom.crypt(idUserCurrent);
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            contactName = extras.getString("name");
            contactEmail = extras.getString("email");
            contactUserId =  Base64Custom.crypt(contactEmail);
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

        // mont listview and adapter
        messages = new ArrayList<>();

        adapter = new MessageAdapter(
                this,
                messages
        );

        listMessages.setAdapter( adapter );
        listMessages.setDivider(null);

        // get messages firebase

         messagesFirebase = ConfigFirebase.getFirebase()
                .child("mensagens")
                .child(idUserCurrent)
                .child(contactUserId);

        // create listener

        messagesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    Message message = data.getValue(Message.class);
                    messages.add(message);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        // add listener
        messagesFirebase.addValueEventListener(messagesListener);
    }

    public void send(View view){
        String textMessage = editMessage.getText().toString();

        if(!textMessage.isEmpty()){
            Message message = new Message();
            message.setIdUser(idUserCurrent);
            message.setMessage(textMessage);

            boolean savedMessageCurrentUser
                    =  saveMessage(idUserCurrent, contactUserId, message);

            if(!savedMessageCurrentUser) {
                Toast.makeText(getApplicationContext(),
                        "Erro ao salvar mensagem",
                        Toast.LENGTH_SHORT).show();
            }else {
                boolean savedMessageContactUser =  saveMessage(contactUserId, idUserCurrent, message);

                if(!savedMessageContactUser){
                    Toast.makeText(getApplicationContext(),
                            "Erro ao salvar mensagem ao seu contato",
                            Toast.LENGTH_SHORT).show();
                }
            }
            editMessage.setText("");

            Conversation conversation = new Conversation();
            conversation.setUserId(contactUserId);
            conversation.setName(contactName);
            conversation.setMessage(textMessage);
            conversation.setLastUserId(idUserCurrent);

            boolean saveConversationCurrentUser = saveConversation(idUserCurrent, contactUserId, conversation);

            if(!saveConversationCurrentUser){
                Toast.makeText(getApplicationContext(),
                        "Erro ao salvar conversa",
                        Toast.LENGTH_SHORT).show();
            }else {
                HashMap<String, String> userData = preferences.getUser();
                conversation = new Conversation();
                conversation.setUserId(idUserCurrent);
                conversation.setName(userData.get("nome"));
                conversation.setMessage(textMessage);
                conversation.setLastUserId(idUserCurrent);
                boolean saveConversationContactUser = saveConversation(contactUserId , idUserCurrent , conversation);

                if(!saveConversationContactUser){
                    Toast.makeText(getApplicationContext(),
                            "Erro ao salvar conversa do contato",
                            Toast.LENGTH_SHORT).show();
                }
            }

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

    private boolean saveConversation(String currentUserId, String contactUserId, Conversation conversation){
        try {
            database = ConfigFirebase.getFirebase();
            database = database.child("conversas")
                    .child(currentUserId)
                    .child(contactUserId);
            database.setValue(conversation);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        messagesFirebase.removeEventListener(messagesListener);
        super.onStop();
    }
}

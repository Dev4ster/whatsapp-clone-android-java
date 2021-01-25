package com.victormenezes.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.victormenezes.whatsapp.R;
import com.victormenezes.whatsapp.activity.ConversationActivity;
import com.victormenezes.whatsapp.adapter.ConversationAdapter;
import com.victormenezes.whatsapp.config.ConfigFirebase;
import com.victormenezes.whatsapp.helper.Base64Custom;
import com.victormenezes.whatsapp.helper.Preferences;
import com.victormenezes.whatsapp.model.Contact;
import com.victormenezes.whatsapp.model.Conversation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationsFragment extends Fragment {

    private ListView listConversations;
    private ArrayList<Conversation> conversations;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private ConversationAdapter conversationAdapter;
    private Preferences preferences;
    private HashMap<String, String> userData;
    private String currentUserId;
    public ConversationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversations, container, false);
        listConversations = view.findViewById(R.id.list_conversations);
        preferences = new Preferences(getActivity());
        userData = preferences.getUser();
        currentUserId = userData.get(Preferences.CHAVE_ID);
        databaseReference = ConfigFirebase.getFirebase().child("conversas").child(currentUserId);
        conversations = new ArrayList<>();
        conversationAdapter = new ConversationAdapter(
                getActivity(),
                conversations
        );

        listConversations.setAdapter(conversationAdapter);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                conversations.clear();
                for(DataSnapshot c : snapshot.getChildren()){
                    Conversation conversation = c.getValue(Conversation.class);
                    conversations.add(conversation);
                }
                conversationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        listConversations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), ConversationActivity.class);
                Conversation conversation = conversations.get(position);
                intent.putExtra("name", conversation.getName());
                intent.putExtra("email", Base64Custom.decrypt(conversation.getUserId()));
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListener);
    }
}

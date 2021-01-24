package com.victormenezes.whatsapp.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.victormenezes.whatsapp.R;
import com.victormenezes.whatsapp.adapter.ContactAdapter;
import com.victormenezes.whatsapp.config.ConfigFirebase;
import com.victormenezes.whatsapp.helper.Base64Custom;
import com.victormenezes.whatsapp.model.Contact;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    private ListView listContacts;
    private ContactAdapter adapter;
    private ArrayList<Contact> contacts;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ValueEventListener valueEventListenerContacts;


    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListenerContacts);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListenerContacts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        contacts =  new ArrayList<>();
        listContacts = view.findViewById(R.id.list_contacts);
        adapter = new ContactAdapter(getActivity(), contacts);

        listContacts.setAdapter( adapter );

        auth = ConfigFirebase.getFirebaseAuthentication();
        String currentUserUid = Base64Custom.crypt(auth.getCurrentUser().getEmail());

        databaseReference = ConfigFirebase
                .getFirebase()
                .child("contatos")
                .child(currentUserUid);

        valueEventListenerContacts = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // clean list
                contacts.clear();

                for(DataSnapshot data: snapshot.getChildren()){
                    Contact contact = data.getValue(Contact.class);
                    contacts.add(contact);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        return view;
    }

}

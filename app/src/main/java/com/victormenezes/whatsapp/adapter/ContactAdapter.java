package com.victormenezes.whatsapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.imageview.ShapeableImageView;
import com.victormenezes.whatsapp.R;
import com.victormenezes.whatsapp.model.Contact;

import java.net.URL;
import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> contacts;
    private Context context;


    public ContactAdapter(@NonNull Context context, @NonNull ArrayList<Contact> objects) {
        super(context, 0, objects);
        this.contacts = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        // check list is empity
        if(contacts != null){
            // init mont view object
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // mont view
            view = inflater.inflate(R.layout.list_contact, parent, false);
            Contact contact = contacts.get(position);
            TextView contactName = view.findViewById(R.id.tv_name);
            TextView contactEmail = view.findViewById(R.id.tv_email);
            ShapeableImageView contactAvatar = view.findViewById(R.id.avatar);
            contactEmail.setText(contact.getEmail());
            setAvatarFromUrl(contact.getName(), contactAvatar);
            contactName.setText(contact.getName());
        }

        return view;
    }

    private void setAvatarFromUrl(String userName, ShapeableImageView image){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL newurl = new URL(String.format("https://ui-avatars.com/api/?name=%s", userName));
            Bitmap bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            image.setImageBitmap(bitmap);
        }

        catch (Exception e){
            Toast.makeText(context, "Error getImage", Toast.LENGTH_LONG).show();
            Log.e("Error", "erro image");
            e.printStackTrace();
        }

    }
}

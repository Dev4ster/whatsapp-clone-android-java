package com.victormenezes.whatsapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.victormenezes.whatsapp.helper.Preferences;
import com.victormenezes.whatsapp.model.Conversation;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ConversationAdapter extends ArrayAdapter<Conversation> {

    private Context context;
    private ArrayList<Conversation> conversations;
    private Preferences preferences;
    private HashMap<String,String> userdata;
    private String currentUserId;

    public ConversationAdapter(@NonNull Context context, @NonNull ArrayList<Conversation> objects) {
        super(context, 0, objects);
        this.context = context;
        this.conversations = objects;
        preferences = new Preferences(context);
        userdata = preferences.getUser();
        currentUserId = userdata.get(Preferences.CHAVE_ID);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(conversations != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(
                    R.layout.conversation_item,
                    parent,
                    false
            );

            ImageView check = view.findViewById(R.id.check);
            ShapeableImageView avatar = view.findViewById(R.id.avatar_CV);

            check.setVisibility(View.INVISIBLE);

            TextView name = view.findViewById(R.id.conversation_name);
            TextView message = view.findViewById(R.id.conversation_last_message);
            Conversation conversation = conversations.get(position);
            name.setText(conversation.getName());
            message.setText(conversation.getMessage());

            if(currentUserId.equals(conversation.getLastUserId())){
                check.setVisibility(View.VISIBLE);
            }

            setAvatarFromUrl(conversation.getName(), avatar);
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

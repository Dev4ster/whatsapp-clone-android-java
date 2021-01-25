package com.victormenezes.whatsapp.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MarginLayoutParamsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.victormenezes.whatsapp.R;
import com.victormenezes.whatsapp.config.ConfigFirebase;
import com.victormenezes.whatsapp.helper.Base64Custom;
import com.victormenezes.whatsapp.model.Message;

import java.util.ArrayList;


public class MessageAdapter extends ArrayAdapter<Message> {

    private ArrayList<Message> messages;
    private Context context;
    private FirebaseAuth auth;


    public MessageAdapter(@NonNull Context context, @NonNull ArrayList<Message> objects) {
        super(context, 0, objects);
        this.context = context;
        this.messages = objects;
        auth = ConfigFirebase.getFirebaseAuthentication();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        WindowManager window  = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        window.getDefaultDisplay().getMetrics(dm);


        if(messages != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.message_item, parent, false);
            Message message = messages.get(position);
            TextView messageText = view.findViewById(R.id.text_message);
            messageText.setText(message.getMessage());
            String userCurrentId = auth.getCurrentUser().getEmail();
            userCurrentId = Base64Custom.crypt(userCurrentId);
            LinearLayout item = view.findViewById(R.id.item_message);

            if(userCurrentId.equals(message.getIdUser())){
                messageText.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                messageText.setTextColor(context.getResources().getColor(R.color.colorWhite));
                item.setGravity(Gravity.RIGHT);
                messageText.setMaxWidth(dm.widthPixels - ((dm.widthPixels * 10) / 100));
            }
        }

        return  view;
    }
}

package com.abinav.creation.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abinav.creation.Chat;
import com.abinav.creation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


    int MSG_LEFT = 0; //MSG to the left of the Screen
    int MSG_RIGHT = 1;    //MSG to the right of the Screen

    private Context myContext;
    private List<Chat> myChat; //MSGs list

    FirebaseUser firebaseUser;

    //Constructor
    public ChatAdapter(Context myContext, List<Chat> myChat) {
        this.myChat = myChat;
        this.myContext = myContext;
    }


    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Positioning the required messages of receiver and sender
        if (viewType == MSG_RIGHT) {
            View view = LayoutInflater.from(myContext).inflate(R.layout.chat_item_right, parent, false);
            return new ChatAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(myContext).inflate(R.layout.chat_item_left, parent, false);
            return new ChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat = myChat.get(position);
        //show_message declared in ViewHolder class
        holder.show_message.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        //Size of List
        return myChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView show_message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //show_message ID from chat_item_left and right xml layout
            show_message = itemView.findViewById(R.id.showMessage);
        }
    }

    @Override
    public int getItemViewType(int position) {

        //Current user of the app
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //if you are the sender, the item view type will be set to MSG_Right and otherwise!
        //This view type will be used in OnCreateViewHolder method to inflate the messages
        if (myChat.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_RIGHT;
        } else {
            return MSG_LEFT;
        }

    }
}

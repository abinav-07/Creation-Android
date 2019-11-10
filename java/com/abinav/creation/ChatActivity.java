package com.abinav.creation;

import android.content.Intent;
import android.os.Bundle;

import com.abinav.creation.Adapter.ChatAdapter;
import com.abinav.creation.Notifications.APIService;
import com.abinav.creation.Notifications.Client;
import com.abinav.creation.Notifications.Data;
import com.abinav.creation.Notifications.MyResponse;
import com.abinav.creation.Notifications.Sender;
import com.abinav.creation.Notifications.Token;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends AppCompatActivity {

    //creating new Intents
    Intent intent;

    //Firebase Variables
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    //Layout Variables
    ImageButton msg_send;
    EditText msg_text;
    TextView chatText;

    //ChatAdapter class Variables
    ChatAdapter chatAdapter;
    List<Chat> myChat;

    //RecyclerView Variables
    RecyclerView recyclerView;

    //Clicked User Id from Map Activity
    String clickedUserId;

    APIService apiService;

    //Boolean for Notification
    Boolean notify=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Finding Views
        msg_send=findViewById(R.id.msg_send);
        msg_text=findViewById(R.id.msg_field);
        chatText=findViewById(R.id.chatText);
        recyclerView=findViewById(R.id.recycler_view);

        //Size not Changeable
        recyclerView.setHasFixedSize(true);

        //LinearlayoutManage class instantiation
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);//Starts the layout from the bottom of the Device
        recyclerView.setLayoutManager(linearLayoutManager);

        //create a instance of intent to retrieve the clicked UserId
        //from our MapsActivity and send the message
        //to the selected user
        intent=getIntent();
         clickedUserId=intent.getStringExtra("clickedUserId");

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        //Send onClickListener
        msg_send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                notify=true;
                String msg=msg_text.getText().toString();
                if(!msg.equals("")){

                    //SendMessage Method
                    //sendMessage(sender, receiver, msg to be send)
                    sendMessage(firebaseUser.getUid(),clickedUserId,msg);
                }else{
                    Toast.makeText(ChatActivity.this, "Empty Message", Toast.LENGTH_SHORT).show();
                }
                //clearing out the text field on send
                msg_text.setText("");


                chatText.setVisibility(View.GONE);
            }
        });

        reference=FirebaseDatabase.getInstance().getReference("Users").child(clickedUserId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Displays the new messages to the user
                //readMessage(CurrentUserId, Message Sent UserId)
                readMessage(firebaseUser.getUid(),clickedUserId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Calling the updateToken method
        updateToken(FirebaseInstanceId.getInstance().getToken());

    }

    //sets the token value for the user
    private void updateToken(String token){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        databaseReference.child(firebaseUser.getUid()).setValue(token1);
    }

    //Method for Sending Messages
    public void sendMessage(final String sender, final String receiver, String message){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        //creating new child to push the messages
        reference.child("Chats").push().setValue(hashMap);

        final String msg=message;

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(notify) {
                    //SendNotification Method
                    //Sends Notification on any new Message Received
                    sendNotification(receiver, msg);
                }
                notify=false;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Send Notification Method
    //Sends Notification to the reciever on Message Received
    //Notification is sent based on Token
    //Checks for token of the receiver and sends Notfication on match
    //This method is called everytime new message is sent
    private void sendNotification(String receiver, final String message){
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Token token=snapshot.getValue(Token.class);

                    //Displayed at the action bar of the receiver which includes title and body
                    Data data=new Data(firebaseUser.getUid(),R.mipmap.ic_launcher,"User"+":"+message,"New Message",
                                clickedUserId);

                    Sender sender=new Sender(data,token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call,Response<MyResponse> response) {
//                                    if(response.code()==200){
//                                        if(response.body().success!=1){
//                                            Toast.makeText(ChatActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
                                //    Toast.makeText(ChatActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Read Message method
    //This method reads any New Messages between the users and displays the message
    public void readMessage(final String myFirebaseId, final String userFirebaseId){
        //new ArrayList for Chat class
        myChat=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myChat.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Chat chat=dataSnapshot1.getValue(Chat.class);
                    //Checking the correct Users to handle Message Misplacement
                    if(chat.getReceiver().equals(myFirebaseId)&&chat.getSender().equals(userFirebaseId)||
                            chat.getReceiver().equals(userFirebaseId)&&chat.getSender().equals(myFirebaseId)){
                        myChat.add(chat);
                    }
                    //New instance of ChatAdapter class to display chat messages
                    chatAdapter=new ChatAdapter(ChatActivity.this,myChat);
                    recyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        ChatActivity.super.onBackPressed();
    }

}

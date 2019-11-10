package com.abinav.creation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.abinav.creation.Notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //Declaring Variables
    EditText username, email, password;
    Button register_button, login_button;
    TextView logintoRegister, registertoLogin;
    TextView heading;

    //Firebase Variables
    FirebaseAuth auth;
    DatabaseReference ref;
    String userid;
    FirebaseUser firebaseUser;


    Intent intent;
    String latitude;
    String longitude;

    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //finding views from our Layout
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        register_button = findViewById(R.id.register_button);
        login_button = findViewById(R.id.login_button);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        logintoRegister = findViewById(R.id.logintoRegister);
        registertoLogin = findViewById(R.id.registertoLogin);
        heading = findViewById(R.id.heading);
        username.setTranslationX(-2000f);

        //getting the firebase authentication for users
        auth = FirebaseAuth.getInstance();
        intent = getIntent();
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        // Hiding the status bar.
        final View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //On Change Listener for status bar
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                //checking if the status bar was clicked on
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            decorView.setSystemUiVisibility(uiOptions);
                        }
                    }, 2000);   //hides the status bar after 2 seconds.
                }
            }
        });

        //Clicking on not registered button
        logintoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.animate().translationXBy(2000f);
                username.animate().alpha(1).setDuration(1000);
                register_button.animate().alpha(1).setDuration(1000);
                login_button.animate().alpha(0).setDuration(1000);
                heading.setText("Register Your Account");
                logintoRegister.animate().alpha(0).setDuration(1000);
                registertoLogin.animate().alpha(1).setDuration(1000);


            }
        });

        //Clicking on already Registered button
        registertoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.animate().alpha(0).setDuration(1000);
                username.animate().translationXBy(-2000f);
                register_button.animate().alpha(0).setDuration(1000);
                login_button.animate().alpha(1).setDuration(1000);
                heading.setText("Login to your account");
                logintoRegister.animate().alpha(1).setDuration(1000);
                registertoLogin.animate().alpha(0).setDuration(1000);


            }
        });

        //OnClick Listener for register button
        register_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                final String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(RegisterActivity.this, "Fill the data", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {

                    //Creating a snack bar to inform user on password length
                    Snackbar snackbar = Snackbar
                            .make(relativeLayout, "Password must be more than 6 letters", Snackbar.LENGTH_LONG)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    password.setText("");
                                }
                            });

                    snackbar.show();
                } else {
                    //calling registerUser method
                    registerUser(txt_username, txt_email, txt_password);
                    Toast.makeText(RegisterActivity.this, "Please Wait!", Toast.LENGTH_SHORT).show();
                }

                //Getting the Keyboard input and hiding it as necessary
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);

            }
        });

        //OnClick Listener for login button
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(RegisterActivity.this, "Fill Email and Password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "WAIT!", Toast.LENGTH_LONG).show();
                    //Using firebase auth to LoginIn Registered User
                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //firebase Set up

                                        firebaseUser = auth.getCurrentUser();
                                        assert firebaseUser != null;
                                        userid = firebaseUser.getUid();
                                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        //getting the secret connected status reference of firebase
                                        DatabaseReference connectedRef = database.getReference(".info/connected");

                                        //Creating a new database reference in the name of the user
                                        //which gets created on clicking the start button
                                        final DatabaseReference myConnectionsRef = database.getReference("Users/" + userid);

                                        //setting an Event listener to check the online offline status of user
                                        connectedRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                boolean connected = dataSnapshot.getValue(Boolean.class);

                                                if (connected) {

                                                    //setting the value to online and offline on app close
                                                    DatabaseReference con = myConnectionsRef.child("status");
                                                    con.setValue("Online");

                                                    //setting offline on disconnection from the app
                                                    con.onDisconnect().setValue("Offline");

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        //Opening MapsActivity
                                        Intent intent = new Intent(RegisterActivity.this, MapsActivity.class);
                                        startActivity(intent);

                                    } else {
                                        if (isNetworkAvailable()) {
                                            Toast.makeText(RegisterActivity.this, "Failed! Wrong Password or Email", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Network is Required!", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                }
                            });
                }

            }
        });


    }

    //Register New User to the Firebase using Auth
    public void registerUser(final String username, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            userid = firebaseUser.getUid();

                            ref = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("latitude", latitude);
                            hashMap.put("longitude", longitude);
                            hashMap.put("status", "Offline");


                            //firebase Set up
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            //getting the secret connected status reference of firebase
                            DatabaseReference connectedRef = database.getReference(".info/connected");

                            //Creating a new database reference with the help of UserId
                            //which gets created on clicking the start button
                            final DatabaseReference myConnectionsRef = database.getReference("Users/" + userid);

                            //setting an Event listener to check the online offline status of user
                            connectedRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    boolean connected = dataSnapshot.getValue(Boolean.class);

                                    if (connected) {

                                        //setting the value to online and offline on app close
                                        DatabaseReference con = myConnectionsRef.child("status");
                                        con.setValue("Online");

                                        //setting offline on disconnection from the app
                                        con.onDisconnect().setValue("Offline");

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            //Opening Maps Activity
                            ref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this, MapsActivity.class);
                                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "You cannot Register with this email", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                            Toast.makeText(RegisterActivity.this, "User Registered.", Toast.LENGTH_LONG).show();


                        } else {
                            if (isNetworkAvailable()) {
                                Toast.makeText(RegisterActivity.this, "User already Registered", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Network is Required!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }


    //checking network connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}

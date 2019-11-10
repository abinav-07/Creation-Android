package com.abinav.creation;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.abinav.creation.Notifications.Token;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    //Marker for online users
    Marker onlineUserMarker;
    //instance of databaseRefrence
    DatabaseReference ref;
    //Latitude and Longitude of Active Users
    LatLng activeUsers;
    //Creating a new instance of Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser firebaseUser;
    Button reload_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        reload_button=findViewById(R.id.reload_button);

        //OnClickListener for reloadButton
        //Displays new Users
        reload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        //Creating a snack bar to inform user what to do in map activity
        Snackbar snackbar = Snackbar
                .make(mapFragment.getView(), "TAP ON A MARKER IF ANY AVAILABLE TO CHAT WITH THAT USER. NO MARKERS MEANS NO USERS ONLINE", 10000);
        snackbar.show();

        //setting onClick events to the markers
        mMap.setOnMarkerClickListener(this);


        //Creating a child in our Firebase
        ref = database.getReference().child("Users");


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                //for loop for all the users in our firebase
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (dataSnapshot.child("status") != null) {

                        //getting the values of latitude and longitude of our users except for our current user
                        if (dataSnapshot.child("status").getValue().equals("Online") && !dataSnapshot.child("id").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                            activeUsers = new LatLng(
                                    Double.parseDouble(dataSnapshot.child("latitude").getValue(String.class)),
                                    Double.parseDouble(dataSnapshot.child("longitude").getValue(String.class))
                            );

                            //adding new green markers in map to display online or active users
                            onlineUserMarker = mMap.addMarker(new MarkerOptions()
                                    .position(activeUsers)
                                    .title(dataSnapshot.child("id").getValue(String.class))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            );


                        }
//
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //on click event for markers to open new intent and to chat with the clicked users
    //sends the clicked users to the next activity
    public boolean onMarkerClick(final Marker marker) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to chat?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MapsActivity.this, ChatActivity.class);
                        intent.putExtra("clickedUserId", marker.getTitle());
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();


        return true;
    }


    @Override
    public void onBackPressed() {
        MapsActivity.super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

    }
}





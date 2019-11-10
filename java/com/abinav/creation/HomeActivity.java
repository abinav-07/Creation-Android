package com.abinav.creation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.abinav.creation.Notifications.Token;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        NavigationView.OnNavigationItemSelectedListener {

    //Layout Views
    TextView title, title2, dot1, dot2, dot3, dot4, dot5, dot6;
    Button startCreation;


    //Animations
    Animation scaledown, scaleup, transition, secondtransition;

    //Drawer views
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;


    //Firebase Variables
    FirebaseUser firebaseUser;


    //For getting location of the user
    //Using google play service
    private static final int MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION = 111;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String latitude, longitude;


    //Requesting location on Permission Granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Requesting access for Fine Location from the user
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, // Activity
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION);
        }

        setContentView(R.layout.activity_home_page);

        //Finding Views
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        startCreation = findViewById(R.id.startbutton);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        //sets the Listener for navigationView
        navigationView.setNavigationItemSelectedListener(this);


        //setting the hamburger icon for Navigation bar
        //toggling the Navigation bar
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerToggle.syncState();
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(drawerToggle);


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


        //finding views by their id
        title = (TextView) findViewById(R.id.title);
        title2 = (TextView) findViewById(R.id.title2);
        dot1 = (TextView) findViewById(R.id.dot1);
        dot2 = (TextView) findViewById(R.id.dot2);
        dot3 = (TextView) findViewById(R.id.dot3);
        dot4 = (TextView) findViewById(R.id.dot4);
        dot5 = (TextView) findViewById(R.id.dot5);
        dot6 = (TextView) findViewById(R.id.dot6);


        //for finding animations under anim folder
        transition = AnimationUtils.loadAnimation(this, R.anim.transition);
        secondtransition = AnimationUtils.loadAnimation(this, R.anim.secondtransition);
        scaledown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleup = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        //for setting the animation to the texts
        if (savedInstanceState == null) {
            title.setTranslationX(-2000f);
            title2.setTranslationY(2000f);
            title.animate().translationXBy(2000f).setDuration(3000);
            title2.animate().translationYBy(-2000f).setDuration(4000);
            startCreation.animate().alpha(1).setDuration(4000).setStartDelay(3000);
        }


        //flickering effect to the textviews
        title2.startAnimation(scaleup);
        dot1.startAnimation(scaleup);
        dot2.startAnimation(scaleup);
        dot3.startAnimation(scaleup);
        dot4.startAnimation(scaleup);
        dot5.startAnimation(scaleup);
        dot6.startAnimation(scaleup);


        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        buildGoogleApiClient();


        //on Start button click, Go to Main Activity
        startCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        //changing the online, offline status on app close and open
        //works only if the user was signed in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {


            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myConnectionsRef = database.getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status");
            myConnectionsRef.setValue("Online");

            //setting offline on disconnection from the app
            myConnectionsRef.onDisconnect().setValue("Offline");
        }


    }


    //Method using Google Api to get Location Services
    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

    }


    //checking the back pressed for navigation bar
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //exits the app
            super.onBackPressed();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }


    //Stops getting the location
    @Override
    protected void onPause() {
        super.onPause();

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    //If Google Location Api connection is Successful
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        //last known location of the user
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        //Checking if the permission was granted by the user for getting location
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (location == null) {
                //getting the location from the google service if location is null
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            } else {
                //If everything went fine lets get latitude and longitude
                latitude = Double.toString(location.getLatitude());
                longitude = Double.toString(location.getLongitude());
                Log.i("Latitude", latitude);

            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    //OnLocation Changed
    @Override
    public void onLocationChanged(Location location) {
        latitude = Double.toString(location.getLatitude());
        longitude = Double.toString(location.getLongitude());
    }


    //click actions on navigation bar items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();


        if (id == R.id.soloCreation) {//clicked on Solo Creation

            //create solo
            //no sign in required
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.multiplayerCreation) {//Clicked on Chat with Other Users

            //checks if there is a user signed in
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {

                //if there is a user signed in, takes the user to map
                startActivity(new Intent(this, MapsActivity.class));
            } else {

                //else to Registration Or Login Activity
                Intent intent = new Intent(this, RegisterActivity.class);
                //Carries the location of the user to the next Activity
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }

        } else if (id == R.id.showHoroscope) {//Clicked on Horoscope Activity
            startActivity(new Intent(this, Horoscope_activity.class));
        } else if (id == R.id.signout) {

            //if there is a user Signed in, SignOut the user
            //and set the user status to Offline
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                //sign-out the current user
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myConnectionsRef = database.getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status");


                myConnectionsRef.setValue("Offline");
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "User Signed Out", Toast.LENGTH_LONG).show();


            } else {
                Toast.makeText(this, "Not Signed In to SignOut!", Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.exit) {//Clicked on Exit
            //Closes the APP
            ActivityCompat.finishAffinity(this);

            finishAffinity();
            System.exit(0);
        }
        //Closes the drawer on back pressed
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

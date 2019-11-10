package com.abinav.creation;

import android.Manifest;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;

import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //Declaring Variables
    RelativeLayout dropView;
    RelativeLayout headLayout;
    TextView draganddroptext;
    LinearLayout linearLayout;
    HorizontalScrollView horizontalScrollView;

    Button save_button, play_button;
    ImageView down_arrow;

    //Permisiion for getting access to phone Directories
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checking for External Writing Permission Granted by the User
        //If not granted, asking for a permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }

        //finding images and layouts
        headLayout = (RelativeLayout) findViewById(R.id.headLayout);
        dropView = (RelativeLayout) findViewById(R.id.dropview);
        draganddroptext = (TextView) findViewById(R.id.text);
        linearLayout = (LinearLayout) findViewById(R.id.toplinear);
        save_button = (Button) findViewById(R.id.save_button);
        play_button = (Button) findViewById(R.id.play_button);
        horizontalScrollView = findViewById(R.id.horizontalView);

        ImageView happy_stickman = (ImageView) findViewById(R.id.happy_stickman);
        ImageView elephant = (ImageView) findViewById(R.id.elephant);
        ImageView car_push = (ImageView) findViewById(R.id.car_push);
        ImageView stickman_legclap = (ImageView) findViewById(R.id.stickman_legclap);
        ImageView omg_sticker = (ImageView) findViewById(R.id.omg_sticker);
        ImageView house = (ImageView) findViewById(R.id.house);
        ImageView stickman_truck = (ImageView) findViewById(R.id.stickman_truck);
        ImageView stickman_bodybuilding = (ImageView) findViewById(R.id.stickman_bodybuilding);
        ImageView up_arrow = (ImageView) findViewById(R.id.up_arrow);
        down_arrow = (ImageView) findViewById(R.id.down_arrow);
        ImageView right_arrow = (ImageView) findViewById(R.id.right_arrow);
        ImageView left_arrow = (ImageView) findViewById(R.id.left_arrow);
        ImageView dead_stickman = (ImageView) findViewById(R.id.dead_stickman);
        ImageView stickman_love = (ImageView) findViewById(R.id.stickman_love);
        ImageView stickman_flower = (ImageView) findViewById(R.id.stickman_flower);
        ImageView stick_girl = (ImageView) findViewById(R.id.stick_girl);
        ImageView carry_her = (ImageView) findViewById(R.id.carry_her);
        ImageView party_man = (ImageView) findViewById(R.id.party_man);
        ImageView running_man = (ImageView) findViewById(R.id.running_man);

        //setting click and drag listeners on Images
        happy_stickman.setOnLongClickListener(longClickListener);
        elephant.setOnLongClickListener(longClickListener);
        car_push.setOnLongClickListener(longClickListener);
        stickman_legclap.setOnLongClickListener(longClickListener);
        omg_sticker.setOnLongClickListener(longClickListener);
        house.setOnLongClickListener(longClickListener);
        stickman_truck.setOnLongClickListener(longClickListener);
        stickman_bodybuilding.setOnLongClickListener(longClickListener);
        up_arrow.setOnLongClickListener(longClickListener);
        down_arrow.setOnLongClickListener(longClickListener);
        left_arrow.setOnLongClickListener(longClickListener);
        right_arrow.setOnLongClickListener(longClickListener);
        dead_stickman.setOnLongClickListener(longClickListener);
        stickman_love.setOnLongClickListener(longClickListener);
        stickman_flower.setOnLongClickListener(longClickListener);
        stick_girl.setOnLongClickListener(longClickListener);
        carry_her.setOnLongClickListener(longClickListener);
        party_man.setOnLongClickListener(longClickListener);
        running_man.setOnLongClickListener(longClickListener);

        //animations
        play_button.setTranslationX(3000f);

        //setting onClickListener for save_button and play_button
        save_button.setOnClickListener(saveClickListener);
        play_button.setOnClickListener(playClickListener);

        //setting the drag listener for horizontal scroll view and relative layout
        dropView.setOnDragListener(MyDragListener);
        linearLayout.setOnDragListener(HorizontalViewDragListener);
//        horizontalScrollView.setOnDragListener(HorizontalViewDragListener);

        //codes for hiding the status bar and action bar
        // Hiding the status bar.
        final View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //On Change Listener for status bar i.e if the user pulls down the status bar
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

        //hide the action bar
        // getSupportActionBar().hide();


        //Alert Dialog for important information
        new AlertDialog.Builder(this)
                .setTitle("Instructions")
                .setCancelable(false)
                .setMessage("-Hold to an image that you want for One Second and drag the image wherever you want!" +
                        "\n" + "-You can Save the current screen and add other image afterwards.Save more than 5 images to make the creation beautiful.Click play to display your creation." +
                        "\n" + "-If you don't want the image, hold the image and drag the image to top left and drop it!" + "\n" +
                        "-Be Creative and Create A Scenery!")
                .setNeutralButton("Ok", null)
                .show();

    }

    //Listener for Long press on Images
    //Gets the clipdata of the image and builds a DragShadow
    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(data, myShadowBuilder, v, 1);
            } else {
                v.startDrag(data, myShadowBuilder, v, 0);
            }
            return true;
        }
    };

    //Drag listener for the images to Relative Layout
    View.OnDragListener MyDragListener = new View.OnDragListener() {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean onDrag(View v, DragEvent event) {

            View view = (View) event.getLocalState();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    draganddroptext.setVisibility(View.GONE);

                    break;

                case DragEvent.ACTION_DRAG_EXITED:

                    view.setX(0);
                    view.setY(0);
                    break;

                case DragEvent.ACTION_DROP:

                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
//
//                    view.setX(event.getX() - (view.getWidth()/2));
//                    view.setY(event.getY() - (view.getHeight()/2));


                    view.setVisibility(View.VISIBLE);

                    //adds the new image to the relative layout
                    dropView.addView(view);

                    //animates the images to the dropped places
                    view.animate()
                            .x(event.getX() - (view.getWidth() / 2))
                            .y(event.getY() - (view.getHeight() / 2))
                            .setDuration(200)
                            .start();


                    break;
            }

            return true;

        }
    };

    //Drag listener for the images to be dragged in the horizontal view
    View.OnDragListener HorizontalViewDragListener = new View.OnDragListener() {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean onDrag(View v, DragEvent event) {

            View view = (View) event.getLocalState();


            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:

                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    view.setX(event.getX());
                    view.setY(event.getY());
                    break;

                case DragEvent.ACTION_DROP:
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);


                    //     view.setX(view.getX()- (view.getWidth()/2 ));
                    //    view.setY(view.getY()- (view.getHeight()/2 ));

//                    view.setX(event.getX());
//                    view.setY(event.getY());


                    linearLayout.addView(view);


                    break;

                default:
                    break;
            }

            return true;

        }
    };


    //User clicks the save button and screenshot is taken of the app
    public View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //play button available
            if (play_button.getTranslationX() == 3000f) {
                play_button.animate().translationXBy(-3000f);
            }


            //names of the images are according to the date of being saved
            Date now = new Date();
            android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

            //Creating a new folder and file on Save click
            File folder = new File(Environment.getExternalStorageDirectory().toString() + "/saveclick");
            if (!folder.isDirectory()) {//Creates a new Directory only if not available before
                folder.mkdir();
            }
            //Path to new Image
            String myFolder = Environment.getExternalStorageDirectory().toString() + "/saveclick" + "/" + now + ".jpg";

            try {

                //getting the view or screenshot of the app
                View v1 = getWindow().getDecorView().getRootView();
                v1.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                v1.setDrawingCacheEnabled(false);

                //Creating the image in the folder
                File imgFile = new File(myFolder);
                FileOutputStream outputStream = new FileOutputStream(imgFile);
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                outputStream.flush();
                outputStream.close();
                Toast.makeText(MainActivity.this, "Added to the roll", Toast.LENGTH_SHORT).show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    //Click Listener for Play button
    //Opens New Activity
    public View.OnClickListener playClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, VideoPlay.class);
            startActivity(intent);
        }
    };

    //creating an alert for the user
    //On 'Ok' click, the images will be deleted from the path provided
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Going Back will delete the saved created images!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //deleting the saved images on back press
                        File file = new File(Environment.getExternalStorageDirectory().toString() + "/saveclick");
                        if (file.isDirectory()) {
                            String[] children = file.list();
                            for (int i = 0; i < children.length; i++) {
                                new File(file, children[i]).delete();
                            }
                        }

                        MainActivity.super.onBackPressed();

                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}


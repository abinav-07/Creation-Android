package com.abinav.creation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.abinav.creation.Adapter.ImageAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class VideoPlay extends AppCompatActivity {

    //Declaring Variables
    ArrayList<String> files = new ArrayList<String>();//list of file path
    File[] fileList;//list of Images
    public int numberofPages = 0;
    public int currentPage;
    ViewPager pager;
    Button backButton, replayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Calling Method getImagesFromFolder
        getImagesFromFolder();

        //Finding Views
        pager = findViewById(R.id.pager);
        backButton = findViewById(R.id.backButton);
        replayButton = findViewById(R.id.replayButton);

        //OnClickListeners
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVideo();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlay.this.finish();
            }
        });

        //Adapter
        pager.setAdapter(new ImageAdapter(VideoPlay.this, files));

        //Automatic Start ImageSlide On Activity Load
        startVideo();
    }

    //getting the images from the folder
    public void getImagesFromFolder() {
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/saveclick");
        if (file.isDirectory()) {
            fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                files.add(fileList[i].getAbsolutePath());
                Log.i("listVideos", "file" + fileList[0]);
            }
        }
    }


    public void startVideo() {
        currentPage = 0;
        numberofPages = fileList.length;

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                pager.setCurrentItem(currentPage++, true);
            }
        };
        //Timer to call Runnable every One Second
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (currentPage < numberofPages) {
                    handler.post(runnable);
                } else {
                    timer.cancel();
                }
            }
        }, 600, 1000);
    }
}

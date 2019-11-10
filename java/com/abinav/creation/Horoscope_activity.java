package com.abinav.creation;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class Horoscope_activity extends AppCompatActivity implements View.OnClickListener {

    //ImageButton Variables
    ImageButton aquarius, aries, cancer, capricorn, gemini, leo, libra, pisces, sagittarius, scorpio, taurus, virgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horoscope_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Finding Views and setting the images for the views
        //Also Setting Animations As Well As providing onClickListeners for each ImageButton
        RelativeLayout mainLayout = findViewById(R.id.mainLayout);
        aquarius = (ImageButton) findViewById(R.id.aquarius);
        aquarius.setImageResource(R.drawable.aquarius);
        aquarius.setOnClickListener(this);
        aquarius.setTranslationX(-3000f);
        aquarius.animate().translationXBy(3000f).setDuration(2400);


        aries = findViewById(R.id.aries);
        aries.setImageResource(R.drawable.aries);
        aries.setOnClickListener(this);
        aries.setTranslationX(-3000f);
        aries.animate().translationXBy(3000f).setDuration(400);

        cancer = findViewById(R.id.cancer);
        cancer.setImageResource(R.drawable.cancer);
        cancer.setOnClickListener(this);
        cancer.setTranslationX(3000f);
        cancer.animate().translationXBy(-3000f).setDuration(800);


        capricorn = findViewById(R.id.capricorn);
        capricorn.setImageResource(R.drawable.capricorn);
        capricorn.setOnClickListener(this);
        capricorn.setTranslationX(3000f);
        capricorn.animate().translationXBy(-3000f).setDuration(2000);


        gemini = findViewById(R.id.gemini);
        gemini.setImageResource(R.drawable.gemini);
        gemini.setOnClickListener(this);
        gemini.setTranslationX(-3000f);
        gemini.animate().translationXBy(3000f).setDuration(800);


        leo = findViewById(R.id.leo);
        leo.setImageResource(R.drawable.leo);
        leo.setOnClickListener(this);
        leo.setTranslationX(-3000f);
        leo.animate().translationXBy(3000f).setDuration(1200);


        libra = findViewById(R.id.libra);
        libra.setImageResource(R.drawable.libra);
        libra.setOnClickListener(this);
        libra.setTranslationX(-3000f);
        libra.animate().translationXBy(3000f).setDuration(1600);


        pisces = findViewById(R.id.pisces);
        pisces.setImageResource(R.drawable.pisces);
        pisces.setOnClickListener(this);
        pisces.setTranslationX(3000f);
        pisces.animate().translationXBy(-3000f).setDuration(2400);

        sagittarius = findViewById(R.id.sagittarius);
        sagittarius.setImageResource(R.drawable.sagittarius);
        sagittarius.setOnClickListener(this);
        sagittarius.setTranslationX(-3000f);
        sagittarius.animate().translationXBy(3000f).setDuration(2000);


        scorpio = findViewById(R.id.scorpio);
        scorpio.setImageResource(R.drawable.scorpio);
        scorpio.setOnClickListener(this);
        scorpio.setTranslationX(3000f);
        scorpio.animate().translationXBy(-3000f).setDuration(1600);


        taurus = findViewById(R.id.taurus);
        taurus.setImageResource(R.drawable.taurus);
        taurus.setOnClickListener(this);
        taurus.setTranslationX(3000f);
        taurus.animate().translationXBy(-3000f).setDuration(400);


        virgo = findViewById(R.id.virgo);
        virgo.setImageResource(R.drawable.virgo);
        virgo.setOnClickListener(this);
        virgo.setTranslationX(3000f);
        virgo.animate().translationXBy(-3000f).setDuration(1200);


        //Creating a snack bar to inform user where to click
        Snackbar snackbar = Snackbar
                .make(mainLayout, "Click on the image to know the horoscope ", 5000);
        snackbar.show();

    }

    //OnClick Listener for ImageButtons
    //Gets the tags of the ImageButtons and sends the Tag to ShowHoroscope Activity
    public void onClick(View view) {
        String horoscope = view.getTag().toString();
        Intent intent = new Intent(this, ShowHoroscope.class);
        intent.putExtra("horoscope", horoscope);
        startActivity(intent);
    }

}

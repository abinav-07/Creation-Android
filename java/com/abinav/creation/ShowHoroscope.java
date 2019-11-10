package com.abinav.creation;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShowHoroscope extends AppCompatActivity {

    //Declaring Variables
    Intent intent;
    String horoscopefromClicked;
    TextView horoscopetext;
    TextView headingText;
    ImageView roundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showhoroscope);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Finding Views
        horoscopetext = findViewById(R.id.horoscopetext);
        headingText = findViewById(R.id.text2);
        roundImage = findViewById(R.id.roundImage);

        //Animations
        roundImage.setTranslationY(3000f);
        headingText.setTranslationX(-2000f);
        roundImage.animate().translationY(-880f).setDuration(2000);
        headingText.animate().translationXBy(2000f).setDuration(3000);
        horoscopetext.animate().alpha(1).setDuration(2000).setStartDelay(3000);

        //Get Intent from Horoscope_Activity class
        intent = getIntent();
        horoscopefromClicked = intent.getStringExtra("horoscope");

        headingText.setText(horoscopefromClicked + "'s Horoscope:");

        //Checking Network
        //If Network is Available, the app uses the API to display the required Horoscope
        if (isNetworkAvailable()) {
            DownloadHoroscope downloadHoroscope = new DownloadHoroscope();
            downloadHoroscope.execute("http://horoscope-api.herokuapp.com/horoscope/today/" + horoscopefromClicked);
        }

    }

    //checking network connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    //Asynchronous Task to Download the Result From the API in the Background Thread
    public class DownloadHoroscope extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            //Empty String
            String result = "";

            //Url that we pass as Parameter
            URL url;

            HttpURLConnection urlConnection = null;

            try {
                //Takes the First Url from the array
                url = new URL(urls[0]);

                //Opens a connection to the Url
                urlConnection = (HttpURLConnection) url.openConnection();

                //Reads the data from the url
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                //Checks until the end of data
                while (data != -1) {

                    char current = (char) data;

                    //Concats to our Empty String Result
                    result += current;

                    //Continues Reading
                    data = reader.read();

                }

                Log.i("Horoscoperesult", result);
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        //After Reading from the Api using UrlConnection
        //The result is in JSON Format
        //onPostExecute picks out the required Object value pair from the JSON format
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                String horoscopeMessage = "";

                JSONObject jsonObject = new JSONObject(result);

                String horoscopeMain = jsonObject.getString("horoscope");

                if (horoscopeMain != "") {
                    horoscopeMessage += horoscopeMain;
                }

                Log.i("HoroscopeMessage", horoscopeMessage);

                if (horoscopeMessage != "") {
                    //Displays Today's Horoscope to the User
                    horoscopetext.setText(horoscopeMessage);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

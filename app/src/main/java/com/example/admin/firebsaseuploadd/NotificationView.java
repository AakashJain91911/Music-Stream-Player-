package com.example.admin.firebsaseuploadd;

/**
 * Created by Admin on 19/12/2017.
 */

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class NotificationView extends Activity {
    String title;
    String text;
    TextView txttitle;
    TextView txttext;
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_bar_expanded);

        // Create Notification Manager
        // NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Dismiss Notification
        //  notificationmanager.cancel(0);

        // Retrive the data
        Intent i = getIntent();

        title = i.getStringExtra("title");
        text = i.getStringExtra("text");

        // Locate the TextView
        txttitle = (TextView) findViewById(R.id.status_bar_track_name);
        txttext = (TextView) findViewById(R.id.status_bar_artist_name);


             // Set the data into TextView
        txttitle.setText(title);
        txttext.setText(text);






    }


    }
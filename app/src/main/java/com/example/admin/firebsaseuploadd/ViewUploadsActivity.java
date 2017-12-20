package com.example.admin.firebsaseuploadd;

/**
 * Created by Admin on 15/12/2017.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ViewUploadsActivity extends AppCompatActivity  implements MediaPlayer.OnBufferingUpdateListener,View.OnTouchListener
,MediaPlayer.OnCompletionListener{
       //the listview
    ListView listView;

    //media player
    //MediaPlayer mp = new MediaPlayer();
    private SeekBar seekBarProgress;
    private int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();
    CustomArrayAdapter adapter;
    //database reference to get uploads data
    DatabaseReference mDatabaseReference;

    //list to store uploads data
    List<Upload> uploadList = new ArrayList<>();
    private TextView mSelectedTrackTitle,selected_track_ar;
    private ImageView mSelectedTrackImage;
    private MediaPlayer mMediaPlayer =new MediaPlayer();
    private ImageView mPlayerControl;
    Toolbar toolbar2;


    TextView length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_upload);
        listView = (ListView) findViewById(R.id.listView);
        //status = (TextView) findViewById(R.id.status);

        seekBarProgress = (SeekBar)findViewById(R.id.length);

        listView.setStackFromBottom(true);
        //adding a clicklistener on listview

//media player
       mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

       mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                togglePlayPause();
            }
        });
      mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayerControl.setImageResource(R.drawable.ic_play);
            }
        });
        mSelectedTrackTitle = (TextView)findViewById(R.id.selected_track_title);
        mSelectedTrackImage = (ImageView)findViewById(R.id.selected_track_image);
        mSelectedTrackTitle.setSelected(true);
        mPlayerControl = (ImageView)findViewById(R.id.player_control);
        selected_track_ar=(TextView)findViewById(R.id.selected_track_ar);




     mPlayerControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPause();
            }
        });
        get();
                //getting the database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploadList.add(upload);
                }

                   adapter = new CustomArrayAdapter(getApplicationContext(), (ArrayList<Upload>) uploadList);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

hide();
    }
   @Override
    protected void onDestroy() {
        super.onDestroy();

       Intent svc=new Intent(this, MySongServices.class);
       startService(svc);
           /* if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {

                mMediaPlayer.stop();
                            }
           mMediaPlayer.release();
          mMediaPlayer = null;
        }*/
    }

    void hide()
    {

        mSelectedTrackImage.setVisibility(View.INVISIBLE);
        seekBarProgress.setVisibility(View.INVISIBLE);
        mSelectedTrackTitle.setVisibility(View.INVISIBLE);
        selected_track_ar.setVisibility(View.INVISIBLE);
        mPlayerControl   .setVisibility(View.INVISIBLE);
    }
    void show()
    {
        mSelectedTrackImage.setVisibility(View.VISIBLE);
        seekBarProgress.setVisibility(View.VISIBLE);
        mSelectedTrackTitle.setVisibility(View.VISIBLE);
        selected_track_ar.setVisibility(View.VISIBLE);
        mPlayerControl   .setVisibility(View.VISIBLE);

    }



    private void togglePlayPause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPlayerControl.setImageResource(R.drawable.ic_play);
        } else if(!mMediaPlayer.isPlaying()){
            mMediaPlayer.start();
            mPlayerControl.setImageResource(R.drawable.ic_pause);
            mediaFileLengthInMilliseconds = mMediaPlayer.getDuration();
            primarySeekBarProgressUpdater();
          /*  Intent serviceIntent = new Intent(getApplicationContext(), NotificationService.class);
            serviceIntent.setAction(NConstants.ACTION.STARTFOREGROUND_ACTION);
            startService(serviceIntent);*/

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;

            }
        });
        return super.onCreateOptionsMenu(menu);
    }






   public void get()
    {
        seekBarProgress.setMax(99); // It means 100% .0-99
        seekBarProgress.setOnTouchListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the upload
                Upload upload = uploadList.get(i);
                mSelectedTrackTitle.setText(upload.getName());
                selected_track_ar.setText(upload.getAr());
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                    seekBarProgress.setProgress(0);
                }

                try {
                     mMediaPlayer.setDataSource(upload.getUrl());
                    mMediaPlayer.prepare();
                    show();

                } catch (IOException e) {
                    e.printStackTrace();
                }


                //Notification
                RemoteViews remoteViews = new RemoteViews(getPackageName(),
                        R.layout.status_bar);

                // Set Notification Title
                String strtitle = upload.getName();
                // Set Notification Text
                String strtext =upload.getAr();

                // Open NotificationView Class on Notification Click
                Intent intent = new Intent(getApplicationContext(), NotificationView.class);
                // Send data to NotificationView Class
               intent.putExtra("title", strtitle);
               intent.putExtra("text", strtext);
                // Open NotificationView.java Activity
                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                        // Set Icon
                        .setSmallIcon(R.drawable.ic_play)
                        // Set Ticker Message
                        .setTicker("Music App")
                        // Dismiss Notification
                        .setAutoCancel(false)
                        // Set PendingIntent into Notification
                        .setContentIntent(pIntent)
                        // Set RemoteViews into Notification
                        .setContent(remoteViews);

                // Locate and set the Image into customnotificationtext.xml ImageViews
                remoteViews.setImageViewResource(R.id.status_bar_album_art,R.drawable.images);
                remoteViews.setImageViewResource(R.id.status_bar_play,R.drawable.ic_pause);

                // Locate and set the Text into customnotificationtext.xml TextViews
                remoteViews.setTextViewText(R.id.status_bar_track_name,upload.getName());
                remoteViews.setTextViewText(R.id.status_bar_artist_name,upload.getAr());

                // Create Notification Manager
                NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Build Notification with Notification Manager
                notificationmanager.notify(0, builder.build());
                            }
        });



    }

    private void primarySeekBarProgressUpdater() {
       try {
           seekBarProgress.setProgress((int) (((float) mMediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100));
           if (mMediaPlayer.isPlaying()) {
               Runnable notification = new Runnable() {
                   public void run() {
                       primarySeekBarProgressUpdater();
                   }
               };
               handler.postDelayed(notification, 1000);
           }
       }
       catch (NullPointerException e)
       {
           e.printStackTrace();
       }
   }




    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId() == R.id.length){
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
           if(mMediaPlayer.isPlaying()){
                SeekBar sb = (SeekBar)v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
               mMediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;


    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

        seekBarProgress.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
       mMediaPlayer.reset();
   }

}



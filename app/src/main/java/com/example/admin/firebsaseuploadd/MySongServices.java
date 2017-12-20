package com.example.admin.firebsaseuploadd;

/**
 * Created by Admin on 19/12/2017.
 */

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;


public class MySongServices extends Service

{
    private static final String TAG = null;
        MediaPlayer player;
        public IBinder onBind(Intent arg0) {

            return null;
        }
        @Override
        public void onCreate() {
            super.onCreate();
              }
        @SuppressLint("WrongConstant")
        public int onStartCommand(Intent intent, int flags, int startId) {

            return 1;
        }

        public void onStart(Intent intent, int startId) {
            // TO DO
        }
        public IBinder onUnBind(Intent arg0) {
            // TO DO Auto-generated method
            return null;
        }

        public void onStop() {

        }
        public void onPause() {

        }
        @Override
        public void onDestroy() {
            NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Dismiss Notification
            notificationmanager.cancel(4);
            player.stop();
            player.release();

        }

        @Override
        public void onLowMemory() {

        }
}
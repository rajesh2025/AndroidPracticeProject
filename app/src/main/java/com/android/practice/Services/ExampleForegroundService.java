package com.android.practice.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.practice.Models.DeviceSongs;
import com.android.practice.R;
import com.android.practice.UI.Mp3SongsActivity;
import com.android.practice.Utilities.Constants;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;

import java.util.ArrayList;

/**
 * Created by user on 09-03-2018.
 */

public class ExampleForegroundService extends Service {
    private static final String LOG_TAG = "ForegroundService";
    public static boolean IS_SERVICE_RUNNING = false;
    private ExoPlayer player;
    private boolean play_pause = false;

    private ServiceBinder binder = new ServiceBinder();
    private ArrayList<DeviceSongs> songs;


    public class ServiceBinder extends Binder {

        public ExampleForegroundService getServiceInstance() {
            return ExampleForegroundService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songs = new ArrayList<>();
        player = new ExoPlayer.Builder(this).build();

    }

    public void playSong(String currSong,String action) {
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.valueOf(currSong));

// check first is songs already playing
        //play a song
        MediaSource mediaSource = buildMediaSource(trackUri);
        player.prepare(mediaSource, true, false);
        player.setPlayWhenReady(true);
        intentAction(action);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ProgressiveMediaSource.Factory(new DefaultHttpDataSource.Factory())
                .createMediaSource(MediaItem.fromUri(uri));
        // This is the MediaSource representing the media to be played.
    }


    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {
        if (intent.getAction() != null) {
            intentAction(intent.getAction());
        }
        return START_STICKY;
    }

    public  void intentAction(String action){
        switch (action) {
            case Constants.Action.START_FOREGROUND_ACTION:
                Log.i(LOG_TAG, "");
                showNotifications();
                break;
            case Constants.Action.PREV_ACTION:
                Log.i(LOG_TAG, "previous action");

                break;
            case Constants.Action.PLAY_ACTION:
                Log.i(LOG_TAG, "play action");
                playNPauseAction();
                break;
            case Constants.Action.NEXT_ACTION:
                Log.i(LOG_TAG, "next action");

                break;
            case Constants.Action.END_FOREGROUND_ACTION:
                stopForeground(true);
                stopSelf();
                break;
        }

    }

    public void playNPauseAction(){
        if(player!= null){
            player.setPlayWhenReady(!play_pause);
        }

    }
    public void setList(ArrayList<DeviceSongs> theSongs) {
        songs = theSongs;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "service onDestroy");

    }

    private PendingIntent setUpIntent(Class<?> cls, int intentType, String action, int flags) {
        Intent notificationIntent = null;

        notificationIntent = new Intent(this, cls);
        if (intentType == 0) {
            notificationIntent.setFlags(flags);
        }
        notificationIntent.setAction(action);
        return PendingIntent.getActivity(this, 0, notificationIntent, 0);
    }


    private void showNotifications() {

        PendingIntent pendingIntent = setUpIntent(Mp3SongsActivity.class, 0,
                Constants.Action.MAIN_ACTION, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent prevPending = setUpIntent(ExampleForegroundService.class, -1,
                Constants.Action.PREV_ACTION, 0);

        PendingIntent playPending = setUpIntent(ExampleForegroundService.class, -1,
                Constants.Action.PLAY_ACTION, 0);

        PendingIntent nextPending = setUpIntent(ExampleForegroundService.class, -1,
                Constants.Action.PLAY_ACTION, 0);


        Notification notification = notificationBuilder(pendingIntent, prevPending, playPending, nextPending);
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
    }


    private Notification notificationBuilder(PendingIntent contentIntent,
                                             PendingIntent prevPending, PendingIntent playPending,
                                             PendingIntent nextPending) {
        String channel_name = "foreground_service";
        String channel_id = "my_channel_01";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (mNotificationManager != null) {
                NotificationChannel mChannel = mNotificationManager.getNotificationChannel(channel_id);
                if (mChannel == null) {
                    int importance = NotificationManager.IMPORTANCE_LOW;
                    mChannel = new NotificationChannel(channel_id, channel_name, importance);
                    mChannel.setDescription("channel for music app");
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mNotificationManager.createNotificationChannel(mChannel);
                }
            }
        }

        NotificationCompat.Builder oreoNotifBuilder = new NotificationCompat.Builder(this, channel_id);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Drawable drawable = getApplicationContext().getResources().getDrawable(R.drawable.my_car);
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
            Bitmap bitmap = bitmapDrawable.getBitmap();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); //use the compression format of your need
//            InputStream is = new ByteArrayInputStream(stream.toByteArray());
//            try {
//                WallpaperManager.getInstance(this).setStream(is, null, true, WallpaperManager.FLAG_LOCK);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        return oreoNotifBuilder.setContentTitle("Practice Music Player").
                setTicker("Practice Music Player").
                setContentText("My Song").
                setSmallIcon(R.drawable.ic_stat_name).
//                setLargeIcon(bitmap).
                setContentIntent(contentIntent)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_previous, "Previous", prevPending).
                        addAction(android.R.drawable.ic_media_play, "Play", playPending)
                .addAction(android.R.drawable.ic_media_next, "Next", nextPending).build();

    }
}

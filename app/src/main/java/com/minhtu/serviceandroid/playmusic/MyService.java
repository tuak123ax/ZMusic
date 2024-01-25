package com.minhtu.serviceandroid.playmusic;

import static com.minhtu.serviceandroid.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import com.minhtu.serviceandroid.R;
import com.minhtu.serviceandroid.constants.Constant;
import com.minhtu.serviceandroid.song.Song;

public class MyService extends Service {
    public static ExoPlayer exoPlayer;
    public static final int PAUSE = 1;
    public static final int RESUME = 2;
    public static final int STOP = 3;
    public static final int NEXT = 4;
    public static final int PREVIOUS = 5;
    private int musicStatus = STOP;

    public static boolean isPlaying = false;

    private Song currentSong;

    private void handleMusicStatus(int status, int position) {
        switch(status) {
            case PAUSE:
                {
                handlePauseStatus(position);
                break;}
            case RESUME:
                {
                handleResumeStatus(position);
                break;}
            case STOP:
                {
                handleStopStatus();
                break;}
        }
    }

    private void handleStopStatus() {
        isPlaying = false;
        stopSelf();
    }

    private void handleResumeStatus(int position) {
        if(exoPlayer != null && musicStatus != RESUME) {
            musicStatus = RESUME;
            exoPlayer.prepare();
            exoPlayer.setPlayWhenReady(true);
            isPlaying = true;
            sendSongNotification(currentSong, position);
        }
    }

    private void handlePauseStatus(int position) {
        if(exoPlayer != null && exoPlayer.isPlaying()) {
            musicStatus = PAUSE;
            exoPlayer.pause();
            isPlaying = false;
            sendSongNotification(currentSong, position);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle data_intent = intent.getExtras();
        int position = -1;
        if(data_intent != null) {
            Song song = (Song) data_intent.get("Song");
            position = data_intent.getInt("position");
            if(song != null) {
                currentSong = song;
                startMusic(song);
                sendSongNotification(song, position);
            }
        }

        int music_status = intent.getIntExtra("status_music", 0);
        handleMusicStatus(music_status, position);
        return START_NOT_STICKY;
    }

    private void startMusic(Song song) {
        if(exoPlayer == null) {
            exoPlayer = new ExoPlayer.Builder(getApplicationContext()).build();
            MediaItem item = MediaItem.fromUri(song.getResource());
            exoPlayer.setMediaItem(item);
            exoPlayer.prepare();
        }
        exoPlayer.setPlayWhenReady(true);
        isPlaying = true;
        musicStatus = RESUME;
    }

    private void sendSongNotification(Song song, int position) {
//        Intent intent = addSongDataToIntent(song, getApplicationContext(), position);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ALLOW_UNSAFE_IMPLICIT_INTENT | PendingIntent.FLAG_IMMUTABLE);

//        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
//        remoteViews.setTextViewText(R.id.name, song.getTitle());
//        if(song.getSinger() != null){
//            remoteViews.setTextViewText(R.id.singer, song.getSinger());
//        } else {
//            remoteViews.setTextViewText(R.id.singer, "");
//        }
//        if(song.getType().equals(Constant.LOCAL_TYPE)){
//            Bitmap image = Utils.getImageFromURI(Uri.parse(song.getResource()), getApplicationContext());
//            remoteViews.setImageViewBitmap(R.id.image, image);
//        } else{
//            Bitmap image = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.song_picture);
//            remoteViews.setImageViewBitmap(R.id.image, image);
//        }
//        remoteViews.setImageViewResource(R.id.playorpause, R.drawable.baseline_pause_circle_outline_24);
//
//        if(isPlaying) {
//            remoteViews.setOnClickPendingIntent(R.id.playorpause, getPendingIntent(getApplicationContext(), PAUSE));
//            remoteViews.setImageViewResource(R.id.playorpause, R.drawable.baseline_pause_circle_outline_24);
//        } else {
//            remoteViews.setOnClickPendingIntent(R.id.playorpause, getPendingIntent(getApplicationContext(), RESUME));
//            remoteViews.setImageViewResource(R.id.playorpause, R.drawable.baseline_play_circle_outline_24);
//        }
//        remoteViews.setOnClickPendingIntent(R.id.close, getPendingIntent(getApplicationContext(), STOP));

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.song_picture);
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(getApplicationContext(), "noti_session");
//        MediaMetadataCompat.Builder mediaMetadata = new MediaMetadataCompat.Builder();
//        mediaMetadata.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, Long.parseLong(song.getDuration()));
//        mediaSessionCompat.setMetadata(mediaMetadata.build());
//        PlaybackStateCompat.Builder mPlaybackState = new PlaybackStateCompat.Builder();
//        mPlaybackState.setState(getPlaybackState(), song.getCurrentProgress(), 1.0f,  SystemClock.elapsedRealtime())
//                .setActions( PlaybackStateCompat.ACTION_PLAY |
//                        PlaybackStateCompat.ACTION_PAUSE |
//                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
//                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
//                .build();
//        mediaSessionCompat.setPlaybackState(mPlaybackState.build());
//        MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {
//            @Override
//            public void onPlay() {
//                super.onPlay();
//                Log.e("Service", "onPlay");
//                sendBroadcastToReceiver(MyService.RESUME);
//            }
//
//            @Override
//            public void onPause() {
//                super.onPause();
//                Log.e("Service", "onPause");
//                sendBroadcastToReceiver(MyService.PAUSE);
//            }
//
//            @Override
//            public void onSkipToNext() {
//                super.onSkipToNext();
//                Log.e("Service", "onSkipToNext");
//                sendBroadcastToActivity(MyService.NEXT);
//            }
//
//            @Override
//            public void onSkipToPrevious() {
//                super.onSkipToPrevious();
//                Log.e("Service", "onSkipToPrevious");
//                sendBroadcastToActivity(MyService.PREVIOUS);
//            }
//        };
//        mediaSessionCompat.setCallback(callback);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.music_icon)
//                .setContentIntent(pendingIntent)
                .setSound(null)
                .setPriority(Notification.PRIORITY_MAX)
                .setLargeIcon(bitmap)
                .setContentTitle(song.getTitle());

        if(song.getSinger() != null){
            notification.setContentText(song.getSinger());
        } else{
            notification.setContentText("");
        }
        if(song.getType().equals(Constant.LOCAL_TYPE)){
            notification.addAction(R.drawable.rewind_button, "Previous", getPendingIntent(getApplicationContext(), PREVIOUS));
        }
        if(isPlaying){
            notification.addAction(R.drawable.pause_button, "Pause", getPendingIntent(getApplicationContext(), PAUSE));
        } else {
            notification.addAction(R.drawable.play_button, "Play", getPendingIntent(getApplicationContext(), RESUME));
        }
        if(song.getType().equals(Constant.LOCAL_TYPE)){
            notification.addAction(R.drawable.forward_button, "Next", getPendingIntent(getApplicationContext(), NEXT));
        }
        androidx.media.app.NotificationCompat.MediaStyle style = new androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(mediaSessionCompat.getSessionToken())
                .setShowCancelButton(true);
        if(song.getType().equals(Constant.LOCAL_TYPE)){
            style.setShowActionsInCompactView(1);
        } else{
            style.setShowActionsInCompactView(0);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            notification.setStyle(style);
        }
        startForeground(1, notification.build());
    }

    private PendingIntent getPendingIntent(Context applicationContext, int status) {
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("music_status", status);
        return PendingIntent.getBroadcast(applicationContext, status, intent, PendingIntent.FLAG_IMMUTABLE| PendingIntent.FLAG_ALLOW_UNSAFE_IMPLICIT_INTENT);
    }

//    private int getPlaybackState() {
//        switch (musicStatus){
//            case PAUSE: return PlaybackStateCompat.STATE_PAUSED;
//            case RESUME: return PlaybackStateCompat.STATE_PLAYING;
//            case STOP: return PlaybackStateCompat.STATE_STOPPED;
//            default: return PlaybackStateCompat.STATE_NONE;
//        }
//    }

//    private void sendBroadcastToReceiver(int status) {
//        Intent intent = new Intent(this, MyReceiver.class);
//        intent.putExtra("music_status", status);
//        sendBroadcast(intent);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
            musicStatus = STOP;
        }
    }

//    private void sendStatusToActivity(int status){
//        Intent intent = new Intent("send_status_to_activity");
//        Bundle bundle = new Bundle();
//        bundle.putInt("status", status);
//        intent.putExtras(bundle);
//        sendBroadcast(intent);
//    }
//
//    private Intent addSongDataToIntent(Song song, Context context, int position) {
//        Intent songIntent = new Intent(context, PlayMusicActivity.class);
//        Bundle bundleData = new Bundle();
//        bundleData.putSerializable("Song", song);
//        bundleData.putInt("position", position);
//        bundleData.putBoolean("fromService", true);
//        bundleData.putBoolean("isPlaying", isPlaying);
//        songIntent.putExtras(bundleData);
//        return songIntent;
//    }
//    private void sendBroadcastToActivity(int status_music){
//        Intent action_intent = new Intent("send_action_to_activity");
//        action_intent.putExtra("action", status_music);
//        sendBroadcast(action_intent);
//    }
}

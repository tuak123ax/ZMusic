package com.minhtu.serviceandroid.song.remind;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.minhtu.serviceandroid.MyApplication;
import com.minhtu.serviceandroid.R;

public class RemindService extends IntentService {

    public RemindService() {
        super(RemindService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.music_icon)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Hello my friend, a new day has come. Let's listen to music.");
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(999, notification.build());
    }
}

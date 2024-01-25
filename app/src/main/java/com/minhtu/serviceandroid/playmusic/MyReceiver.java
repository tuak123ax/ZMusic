package com.minhtu.serviceandroid.playmusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int status_music = intent.getIntExtra("music_status",0);
        boolean fromActivity = intent.getBooleanExtra("fromActivity", false);
        Intent music_intent = new Intent(context, MyService.class);
        music_intent.putExtra("status_music", status_music);
        if(status_music == MyService.NEXT || status_music == MyService.PREVIOUS){
            Intent actionIntent = new Intent("send_action_to_activity");
            actionIntent.putExtra("action",status_music);
            context.sendBroadcast(actionIntent);
        } else{
            context.startService(music_intent);
            if(!fromActivity){
            Intent statusIntent = new Intent("send_status_to_activity");
            Bundle bundle = new Bundle();
            bundle.putInt("status", status_music);
            statusIntent.putExtras(bundle);
            context.sendBroadcast(statusIntent);
            }
        }
    }
}

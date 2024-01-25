package com.minhtu.serviceandroid.playmusic;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyThread extends Thread implements Runnable{

    private boolean isStop;
    private ProgressBar progressBar;
    private Activity activity;
    private TextView startTime;

    public MyThread(Activity activity, ProgressBar progressBar, TextView startTime) {
        isStop = false;
        this.activity = activity;
        this.progressBar = progressBar;
        this.startTime = startTime;
    }

    @Override
    public void run() {
        while(progressBar.getProgress() < progressBar.getMax()){
            if(isStop) break;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(progressBar.getProgress() + 1);
                    long minutes = progressBar.getProgress()  / 60;
                    int seconds = (int) (progressBar.getProgress() - (minutes*60));
                    String songDuration = "";
                    if(minutes < 10){
                        songDuration = "0"+minutes + ":";
                    } else{
                        songDuration = minutes + ":";
                    }
                    if(seconds < 10)
                    {
                        songDuration += "0" + seconds;
                    } else {
                        songDuration += seconds;
                    }
                    startTime.setText(songDuration);
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }
}

package com.minhtu.serviceandroid.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import com.minhtu.serviceandroid.R;

public class Utils {
    public static Bitmap getImageFromURI(Uri uri, Context context){
        Bitmap result;
        try{
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            byte[]rawArt;
            Bitmap art = null;
            BitmapFactory.Options bfo = new BitmapFactory.Options();

            mmr.setDataSource(context, uri);
            rawArt = mmr.getEmbeddedPicture();

            if(null != rawArt){
                art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
            }

            if(null != art) {
                result = art;
            } else {
                result = BitmapFactory.decodeResource(context.getResources(), R.drawable.song_picture);
            }
        }catch (Exception exception){
            exception.printStackTrace();
            result = BitmapFactory.decodeResource(context.getResources(), R.drawable.song_picture);
        }
        return result;
    }

    public static String convertTime(String songDuration){
        long duration = Long.parseLong(songDuration);
        long minutes = (duration / 1000)  / 60;
        int seconds = (int) ((duration - (minutes*60*1000)) / 1000);
        String result = "";
        if(minutes < 10){
            result = "0"+minutes + ":";
        } else{
            result = minutes + ":";
        }
        if(seconds < 10)
        {
            result += "0" + seconds;
        } else {
            result += seconds;
        }
        return result;
    }
}

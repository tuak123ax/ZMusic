package com.minhtu.serviceandroid.song;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.minhtu.serviceandroid.constants.Constant;
import com.minhtu.serviceandroid.R;
import com.minhtu.serviceandroid.playmusic.PlayMusicActivity;
import com.minhtu.serviceandroid.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Song> listSong;

    public SongAdapter(Context context, ArrayList<Song> listSong) {
        this.context = context;
        this.listSong = listSong;
    }

    @NonNull
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.ViewHolder holder, int position) {
        Song song = listSong.get(position);
        holder.name.setText(song.getTitle());
        if(song.getSinger() == null || song.getSinger().isEmpty()){
            holder.singer.setText("");
        } else {
            holder.singer.setText(song.getSinger());
        }

        if(song.getType().equals(Constant.LOCAL_TYPE)){
            Bitmap image = Utils.getImageFromURI(Uri.parse(song.getResource()), context);
            holder.image.setImageBitmap(image);
        } else{
            if(song.getImage().isEmpty()){
                Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.drawable.song_picture);
                holder.image.setImageBitmap(image);
            } else{
                Picasso.get().load(song.getImage()).into(holder.image);
            }
        }
        
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSongData(song, context, position);
            }
        });
        
        holder.singer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSongData(song, context, position);
            }
        });
        
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSongData(song, context, position);
            }
        });
        
        holder.three_dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Information");
                if(song.getType().equals(Constant.LOCAL_TYPE)){
                    dialog.setMessage("Name: "+ song.getTitle()+"\n"
                            + "Singer: " + song.getSinger() + "\n"
                            + "Duration: "+ Utils.convertTime(song.getDuration())+"\n"
                            + "Location: "+ song.getResource());
                } else {
                    dialog.setMessage("Name: "+ song.getTitle()+"\n"
                            + "Image: " + song.getImage()+ "\n"
                            + "Location: "+ song.getResource());
                }

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
    }

    private void sendSongData(Song song, Context context, int position) {
        Intent songIntent = new Intent(context, PlayMusicActivity.class);
        Bundle bundleData = new Bundle();
        bundleData.putSerializable("Song", song);
        bundleData.putInt("position", position);
        songIntent.putExtras(bundleData);
        context.startActivity(songIntent);
    }

    @Override
    public int getItemCount() {
        return listSong.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView singer;
        ImageView three_dots;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image= itemView.findViewById(R.id.songImage);
            name= itemView.findViewById(R.id.songName);
            singer = itemView.findViewById(R.id.songSinger);
            three_dots = itemView.findViewById(R.id.three_dots);
        }
    }
}

package com.minhtu.serviceandroid.playmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.minhtu.serviceandroid.constants.Constant;
import com.minhtu.serviceandroid.R;
import com.minhtu.serviceandroid.databinding.ActivityPlayMusicBinding;
import com.minhtu.serviceandroid.song.Song;
import com.minhtu.serviceandroid.song.localsong.LocalSongFragment;
import com.minhtu.serviceandroid.utils.Utils;
import com.squareup.picasso.Picasso;

public class PlayMusicActivity extends AppCompatActivity {

    ActivityPlayMusicBinding binding;
    private int currentSongPosition = -1;
    private Song currentSong;
    private MyThread currentThread;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundleData = intent.getExtras();
            if(bundleData != null) {
                int status_music = bundleData.getInt("status");
                if(status_music == MyService.RESUME){
                    setAnimationForDisk();
                    binding.playButton.setImageResource(R.drawable.pause_button);
                    if(currentSong.getType().equals(Constant.LOCAL_TYPE)){
                        MyThread thread = new MyThread(PlayMusicActivity.this, binding.progressHorizontal, binding.startTime);
                        currentThread = thread;
                        thread.start();
                    }
                } else {
                    if(currentSong.getType().equals(Constant.LOCAL_TYPE)){
                        currentThread.setStop(true);
                    }
                    binding.disk.clearAnimation();
                    binding.playButton.setImageResource(R.drawable.play_button);
                }
            }
        }
    };

    BroadcastReceiver actionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int action = intent.getIntExtra("action", 0);
            if(action == MyService.NEXT){
                currentThread.setStop(true);
                binding.progressHorizontal.setProgress(0);
                int newPosition = currentSongPosition + 1;
                if(newPosition < LocalSongFragment.listLocalSong.size()){
                    Song song = LocalSongFragment.listLocalSong.get(newPosition);
                    stopMusic();
                    setAnimationForDisk();
                    setSongInfo(song);
                    startMusic(song);
                    binding.playButton.setImageResource(R.drawable.pause_button);
                    currentSongPosition = newPosition;
                    currentSong = song;
                } else {
                    Toast.makeText(getApplicationContext(), "This is the last song in list.", Toast.LENGTH_SHORT).show();
                }
            } else{
                currentThread.setStop(true);
                binding.progressHorizontal.setProgress(0);
                int newPosition = currentSongPosition - 1;
                if(newPosition >= 0)
                {
                    Song song = LocalSongFragment.listLocalSong.get(newPosition);
                    stopMusic();
                    setAnimationForDisk();
                    setSongInfo(song);
                    startMusic(song);
                    binding.playButton.setImageResource(R.drawable.pause_button);
                    currentSongPosition = newPosition;
                    currentSong = song;
                } else {
                    Toast.makeText(getApplicationContext(), "This is the first song in list.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle songData = getIntent().getExtras();
        if(songData != null){
            Song song = (Song) songData.getSerializable("Song");
            currentSong = song;
            currentSongPosition = songData.getInt("position");
            boolean isFromService = songData.getBoolean("fromService");
            if(!isFromService){
                stopMusic();
            }
            setAnimationForDisk();
            if(song != null) {
                setSongInfo(song);
                startMusic(song);
            }
        }

        registerReceiver(receiver, new IntentFilter("send_status_to_activity"));
        registerReceiver(actionReceiver, new IntentFilter("send_action_to_activity"));

        binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyService.exoPlayer != null){
                    if(MyService.isPlaying) {
                        if(currentSong.getType().equals(Constant.LOCAL_TYPE)){
                            currentThread.setStop(true);
                            Song song = LocalSongFragment.listLocalSong.get(currentSongPosition);
                            song.setCurrentProgress(binding.progressHorizontal.getProgress());
                        }
                        Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
                        intent.putExtra("music_status", MyService.PAUSE);
                        intent.putExtra("fromActivity", true);
                        sendBroadcast(intent);
                        binding.disk.clearAnimation();
                        binding.playButton.setImageResource(R.drawable.play_button);
                    } else {
                        setAnimationForDisk();
                        Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
                        intent.putExtra("music_status", MyService.RESUME);
                        intent.putExtra("fromActivity", true);
                        sendBroadcast(intent);

                        binding.playButton.setImageResource(R.drawable.pause_button);
                        if(currentSong.getType().equals(Constant.LOCAL_TYPE)){
                            Song song = LocalSongFragment.listLocalSong.get(currentSongPosition);
                            if(song.getType().equals(Constant.LOCAL_TYPE)){
                                MyThread thread = new MyThread(PlayMusicActivity.this, binding.progressHorizontal, binding.startTime);
                                currentThread = thread;
                                thread.start();
                            }
                        }
                    }
                } else {
                    if(currentSongPosition != -1){
                        Song song = LocalSongFragment.listLocalSong.get(currentSongPosition);
                        setAnimationForDisk();
                        setSongInfo(song);
                        binding.playButton.setImageResource(R.drawable.pause_button);
                        startMusic(song);
                    }
                }
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentThread.setStop(true);
                binding.progressHorizontal.setProgress(0);
                int newPosition = currentSongPosition + 1;
                if(newPosition < LocalSongFragment.listLocalSong.size()){
                    Song song = LocalSongFragment.listLocalSong.get(newPosition);
                    stopMusic();
                    setAnimationForDisk();
                    setSongInfo(song);
                    startMusic(song);
                    binding.playButton.setImageResource(R.drawable.pause_button);
                    currentSongPosition = newPosition;
                    currentSong = song;
                } else {
                    Toast.makeText(getApplicationContext(), "This is the last song in list.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentThread.setStop(true);
                binding.progressHorizontal.setProgress(0);
                int newPosition = currentSongPosition - 1;
                if(newPosition >= 0)
                {
                    Song song = LocalSongFragment.listLocalSong.get(newPosition);
                    stopMusic();
                    setAnimationForDisk();
                    setSongInfo(song);
                    startMusic(song);
                    binding.playButton.setImageResource(R.drawable.pause_button);
                    currentSongPosition = newPosition;
                    currentSong = song;
                } else {
                    Toast.makeText(getApplicationContext(), "This is the first song in list.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAnimationForDisk() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(5000);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setRepeatMode(Animation.INFINITE);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        binding.disk.setAnimation(rotateAnimation);
    }

    private void setSongInfo(Song song){
        if(song.getType().equals(Constant.LOCAL_TYPE)){
            Bitmap image = Utils.getImageFromURI(Uri.parse(song.getResource()), getApplicationContext());
            binding.disk.setImageBitmap(image);

            binding.endTime.setText(Utils.convertTime(song.getDuration()));
            binding.progressHorizontal.setMax((int) (Long.parseLong(song.getDuration())/1000));
        } else{
            if(song.getImage().isEmpty()){
                Bitmap image = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.song_picture);
                binding.disk.setImageBitmap(image);
            } else{
                Picasso.get().load(song.getImage()).into(binding.disk);
            }
            binding.startTime.setVisibility(View.GONE);
            binding.endTime.setVisibility(View.GONE);
            binding.nextButton.setVisibility(View.GONE);
            binding.previousButton.setVisibility(View.GONE);
            binding.progressHorizontal.setVisibility(View.GONE);
        }
        binding.songTitle.setText(song.getTitle());
        if(song.getSinger() != null)
        {
            binding.songSinger.setText(song.getSinger());
        } else {
            binding.songSinger.setText("");
        }
    }
    private void stopMusic() {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    private void startMusic(Song song) {
        song.setCurrentProgress(0);
        Intent intent = new Intent(this, MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Song", song);
        bundle.putInt("position", currentSongPosition);
        intent.putExtras(bundle);
        startService(intent);

        if(song.getType().equals(Constant.LOCAL_TYPE)){
            MyThread thread = new MyThread(this, binding.progressHorizontal, binding.startTime);
            currentThread = thread;
            thread.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(actionReceiver);
    }
}
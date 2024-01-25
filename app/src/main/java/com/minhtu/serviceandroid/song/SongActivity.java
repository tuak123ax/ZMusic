package com.minhtu.serviceandroid.song;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;


import com.google.android.material.navigation.NavigationBarView;
import com.minhtu.serviceandroid.R;
import com.minhtu.serviceandroid.databinding.ActivitySongBinding;
import com.minhtu.serviceandroid.song.remind.RemindWorker;

import java.util.concurrent.TimeUnit;

public class SongActivity extends AppCompatActivity {
    ActivitySongBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this.getLifecycle());
        binding.viewPager.setAdapter(viewPagerAdapter);

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                switch(position){
                    case 0:
                        binding.bottomNavigation.getMenu().findItem(R.id.search_song_tab).setChecked(true);
                        break;
                    case 1:
                        binding.bottomNavigation.getMenu().findItem(R.id.local_song_tab).setChecked(true);
                        break;
                }
            }
        });

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.search_song_tab){
                    binding.viewPager.setCurrentItem(0);
                } else {
                    binding.viewPager.setCurrentItem(1);
                }
                return false;
            }
        });
        requestPermission(getApplicationContext());
        remindUser(getApplicationContext());
    }
    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if(isGranted){
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("Explain");
                    dialog.setMessage("Please grant permission for our app to work normally.");
                    dialog.setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }
            });
    private void requestPermission(Context context){
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {

        } else {
            requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    private void remindUser(Context context){
        WorkRequest workRequest = new PeriodicWorkRequest.Builder(RemindWorker.class, 24, TimeUnit.HOURS)
                .addTag("remind_user")
                .build();
        WorkManager workManager = WorkManager.getInstance(context);
        workManager.cancelAllWorkByTag("remind_user");
        workManager.enqueue(workRequest);
    }
}

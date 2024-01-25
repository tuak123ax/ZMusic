package com.minhtu.serviceandroid.song.localsong;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minhtu.serviceandroid.constants.Constant;
import com.minhtu.serviceandroid.databinding.FragmentLocalSongBinding;
import com.minhtu.serviceandroid.song.Song;
import com.minhtu.serviceandroid.song.SongAdapter;

import java.io.File;
import java.util.ArrayList;

public class LocalSongFragment extends Fragment {

    public static ArrayList<Song> listLocalSong;
    SongAdapter songAdapter;
    FragmentLocalSongBinding binding;
    public LocalSongFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listLocalSong = new ArrayList<>();
        songAdapter = new SongAdapter(getContext(), listLocalSong);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLocalSongBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            requestPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO);
        } else {
            requestPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        binding.localSongRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.localSongRecyclerView.setAdapter(songAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void getAllMusicInStorage(Context context){
        binding.loadingView.setVisibility(View.VISIBLE);
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null,
                null);

        if(cursor != null && cursor.moveToFirst()){
            do{
                String title = cursor.getString(0);
                String singer = cursor.getString(1);
                String duration = cursor.getString(2);
                String resource = cursor.getString(3);
                Song song = new Song(title, singer,resource, duration, Constant.LOCAL_TYPE);
                File file = new File(song.getResource());
                if(file.exists()){
                    listLocalSong.add(song);
                }
            }
            while(cursor.moveToNext());
            songAdapter.notifyDataSetChanged();
            cursor.close();
            if(listLocalSong.size() == 0) {
                binding.noSongTextView.setVisibility(View.VISIBLE);
            }
            binding.loadingView.setVisibility(View.GONE);
        }
    }
    private ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if(isGranted){
                    getAllMusicInStorage(requireContext());
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
                    dialog.setTitle("Explain");
                    dialog.setMessage("Please grant permission for our app to get songs on this device.");
                    dialog.setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                    binding.noSongTextView.setVisibility(View.VISIBLE);
                    binding.loadingView.setVisibility(View.GONE);
                }
            });
    private void requestPermission(Context context, String permission){
        if (ContextCompat.checkSelfPermission(
                context, permission) ==
                PackageManager.PERMISSION_GRANTED) {
            getAllMusicInStorage(requireContext());
        } else {
            permissionLauncher.launch(
                    permission);
        }
    }
}
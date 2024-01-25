package com.minhtu.serviceandroid.song.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.minhtu.serviceandroid.constants.Constant;
import com.minhtu.serviceandroid.databinding.FragmentSearchBinding;
import com.minhtu.serviceandroid.song.Song;
import com.minhtu.serviceandroid.song.SongAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;
    ArrayList<Song> onlineSongList;
    SongAdapter songAdapter;
    public SearchFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onlineSongList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        songAdapter = new SongAdapter(requireContext(), onlineSongList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.searchRecyclerView.setLayoutManager(linearLayoutManager);
        binding.searchRecyclerView.setAdapter(songAdapter);
        setEvent();
    }

    private void setEvent() {
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = binding.searchEdtText.getText().toString();
                if(input.isEmpty()) {
                    Toast.makeText(requireContext(), "Please input your song or artist!", Toast.LENGTH_SHORT).show();
                } else {
                    binding.searchLoadingView.setVisibility(View.VISIBLE);
                    onlineSongList.clear();
                    RetrofitHelper.getInstance(Constant.URL).create(MusicApi.class).searchMusicInfo(
                            input,
                            Constant.SEARCH_ENGINE
                            )
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if(response.body() != null){
                                        try {
                                            JSONObject responseObject = new JSONObject(response.body());
                                            int status = responseObject.optInt("status");
                                            if(status == 200){
                                                JSONArray trackMatches = responseObject.optJSONArray("response");
                                                if(trackMatches != null){
                                                    for(int i = 0; i < trackMatches.length(); i++){
                                                        JSONObject item = trackMatches.getJSONObject(i);
                                                        String id = item.optString("id");
                                                        String title = item.optString("title");
                                                        String img = item.optString("img");
                                                        String url = Constant.URL + "fetch?id=" + id;
                                                        Song song = new Song(title, url, img, Constant.ONLINE_TYPE);
                                                        onlineSongList.add(song);
                                                    }
                                                    songAdapter.notifyDataSetChanged();
                                                    if(onlineSongList.size() == 0){
                                                        binding.noOnlineSongTextView.setVisibility(View.VISIBLE);
                                                        binding.searchRecyclerView.setVisibility(View.GONE);
                                                    } else {
                                                        binding.noOnlineSongTextView.setVisibility(View.GONE);
                                                        binding.searchRecyclerView.setVisibility(View.VISIBLE);
                                                    }
                                                    binding.searchLoadingView.setVisibility(View.GONE);
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            binding.searchLoadingView.setVisibility(View.GONE);
                                            Toast.makeText(requireContext(), "Search Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    binding.searchLoadingView.setVisibility(View.GONE);
                                    Toast.makeText(requireContext(), "Search Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}
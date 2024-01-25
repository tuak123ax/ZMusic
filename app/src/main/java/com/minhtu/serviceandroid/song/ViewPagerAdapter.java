package com.minhtu.serviceandroid.song;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.minhtu.serviceandroid.song.localsong.LocalSongFragment;
import com.minhtu.serviceandroid.song.search.SearchFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
     switch (position){
         case 0: return new SearchFragment();
         default: return new LocalSongFragment();
     }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

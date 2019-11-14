package com.example.freespotify;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class PageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    public PageAdapter(FragmentManager fm, List<Fragment> fragments){
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position){
        return fragments.get(position);
    }

    @Override
    public int getCount(){
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Songs";
            case 1:
                return "Playlists";
            case 2:
                return "Settings";

        }
        return null;
    }
}

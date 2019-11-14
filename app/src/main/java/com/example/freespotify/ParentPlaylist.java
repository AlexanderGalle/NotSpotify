package com.example.freespotify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class ParentPlaylist extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.fragement_parent_playlist, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PlaylistsFragment playlistsFragment = new PlaylistsFragment();

        getFrameLayout(playlistsFragment);
    }

    public void getFrameLayout(Fragment newFragment)
    {
        FragmentTransaction childFragMan = getChildFragmentManager().beginTransaction();
        childFragMan.replace(R.id.playlistContainer,newFragment);
        childFragMan.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        childFragMan.commit();
    }


}

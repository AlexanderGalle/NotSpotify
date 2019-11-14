package com.example.freespotify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CurrentPlaylistFragment extends Fragment {

    private RecyclerView gRecyclerView;
    private ShareViewModel gViewModel;
    private ArrayList<String> gNames = new ArrayList<>();
    private ArrayList<String> gArtist = new ArrayList<>();
    private ArrayList<String> gLink = new ArrayList<>();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.fragment_current_playlist, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            TextView playlistName;
            ImageButton close;

            gRecyclerView = getView().findViewById(R.id.recyclerView);
            playlistName = getView().findViewById(R.id.playlistTitle);
            close = getView().findViewById(R.id.close);

        if(getActivity() != null) {
            gViewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);
            gViewModel.getUserPlaylists().getValue().get(gViewModel.getPlaylistPosition().getValue()).getPlaylistName();
            playlistName.setText(gViewModel.getUserPlaylists().getValue().get(gViewModel.getPlaylistPosition().getValue()).getPlaylistName());

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   ParentPlaylist parent = (ParentPlaylist)getParentFragment();

                   PlaylistsFragment playlistsFragment = new PlaylistsFragment();

                   parent.getFrameLayout(playlistsFragment);

                }
            });
            initialiseValues();
            initRecyclerView();
        }


    }

    private void initRecyclerView(){
        List<String> playlistNames = new ArrayList<>();

        for (int i =0;i<gViewModel.getUserPlaylists().getValue().size();i++)
        {
            playlistNames.add(gViewModel.getUserPlaylists().getValue().get(i).getPlaylistName());
        }
        Adapter adapter = new Adapter(gNames, gArtist, gLink, getContext(), getActivity(), gViewModel);
        gRecyclerView.setAdapter(adapter);
        gRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    private void initialiseValues(){
        for (int i = 0;i<gViewModel.getUserPlaylists().getValue().get(gViewModel.getPlaylistPosition().getValue()).getSongs().size();i++)
        {
            gNames.add(gViewModel.getUserPlaylists().getValue().get(gViewModel.getPlaylistPosition().getValue()).getSongs().get(i).getName());
            gArtist.add(gViewModel.getUserPlaylists().getValue().get(gViewModel.getPlaylistPosition().getValue()).getSongs().get(i).getArtist());
            gLink.add(gViewModel.getUserPlaylists().getValue().get(gViewModel.getPlaylistPosition().getValue()).getSongs().get(i).getLink());
        }

    }


}
package com.example.freespotify;

import android.animation.StateListAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CurrentPlaylistFragment extends Fragment {

    private RecyclerView recyclerView;
    private ShareViewModel viewModel;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mArtist = new ArrayList<>();
    private ArrayList<String> mLink = new ArrayList<>();
    private TextView playlistName;
    private ImageButton close;
    private List<String> playlistNames;


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
            recyclerView = getView().findViewById(R.id.recyclerView);
            playlistName = getView().findViewById(R.id.playlistTitle);
            close = getView().findViewById(R.id.close);

        if(getActivity() != null) {
            viewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);
            viewModel.getUserPlaylists().getValue().get(viewModel.getPlaylistPosition().getValue()).getPlaylistName();
            playlistName.setText(viewModel.getUserPlaylists().getValue().get(viewModel.getPlaylistPosition().getValue()).getPlaylistName());

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
        playlistNames = new ArrayList<>();

        for (int i =0;i<viewModel.getUserPlaylists().getValue().size();i++)
        {
            playlistNames.add(viewModel.getUserPlaylists().getValue().get(i).getPlaylistName());
        }
        Adapter adapter = new Adapter(mNames, mArtist, mLink, getContext(), getActivity(), viewModel,playlistNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    private void initialiseValues(){
        for (int i = 0;i<viewModel.getUserPlaylists().getValue().get(viewModel.getPlaylistPosition().getValue()).getSongs().size();i++)
        {
            mNames.add(viewModel.getUserPlaylists().getValue().get(viewModel.getPlaylistPosition().getValue()).getSongs().get(i).getName());
            mArtist.add(viewModel.getUserPlaylists().getValue().get(viewModel.getPlaylistPosition().getValue()).getSongs().get(i).getArtist());
            mLink.add(viewModel.getUserPlaylists().getValue().get(viewModel.getPlaylistPosition().getValue()).getSongs().get(i).getLink());
        }

    }


}
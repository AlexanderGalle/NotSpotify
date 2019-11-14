package com.example.freespotify;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class AddToPlaylistFragment extends Fragment {

    private ShareViewModel gViewModel;
    private RecyclerView gRecyclerView;
    private String gLayout;

    public AddToPlaylistFragment(String layout)
    {
        gLayout = layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.fragment_add_to_playlist, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gRecyclerView = getView().findViewById(R.id.recyclerView);

        ImageButton close;
        close = getView().findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ParentPlaylist parent = (ParentPlaylist)getParentFragment();

                PlaylistsFragment playlistsFragment = new PlaylistsFragment();

                parent.getFrameLayout(playlistsFragment);

            }
        });

        initRecyclerView();

    }

    private void initRecyclerView(){
        gViewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);
        if(gLayout == "Add") {
            AddAdapter adapter = new AddAdapter(getContext(), gViewModel, this);
            gRecyclerView.setAdapter(adapter);
            gRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else if(gLayout == "Remove") {
            RemoveAdapter adapter = new RemoveAdapter(getContext(),gViewModel,this);
            gRecyclerView.setAdapter(adapter);
            gRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

    }
}

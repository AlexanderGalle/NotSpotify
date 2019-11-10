package com.example.freespotify;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistsFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<String> playlistSongNames;
    private List<Playlist> playlists;
    private RecyclerView recyclerView;
    private ProgressBar loading;
    private ShareViewModel viewModel;
    private ImageButton createPlaylist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.fragment_playlists, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = getView().findViewById(R.id.recyclerView);
        loading = getView().findViewById(R.id.loading);
        ReadPlaylists readPlaylists = new ReadPlaylists();
        readPlaylists.execute();
        createPlaylist = getView().findViewById(R.id.add);

        createPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreatePlaylistFragment createPlaylistFragment = new CreatePlaylistFragment();

                ParentPlaylist parent = (ParentPlaylist)getParentFragment();

                parent.getFrameLayout(createPlaylistFragment);

            }
        });


    }

    class ReadPlaylists extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... integers) {


            try {
                if(playlists == null) {
                    playlists = new ArrayList<>();

                    db = FirebaseFirestore.getInstance();
                    mAuth = FirebaseAuth.getInstance();


                    db.collection(mAuth.getCurrentUser().getDisplayName()+"Playlist").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                playlistSongNames = new ArrayList<>();

                                if(documentSnapshot.getData().size()>1) {
                                    for (int i = 1; i < documentSnapshot.getData().size(); i++) {
                                        playlistSongNames.add(documentSnapshot.getString("song" + i));

                                    }
                                }
                                final Playlist playlist = new Playlist(documentSnapshot.getString("name"),playlistSongNames);


                                db.collection("songs").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (int i=0;i<playlist.getSongNames().size();i++) {
                                            for (QueryDocumentSnapshot documentSnapshot2 : queryDocumentSnapshots) {

                                                if (playlist.getSongNames().get(i).equals(documentSnapshot2.getString("name"))) {
                                                    Song song = new Song(documentSnapshot2.getString("name"), documentSnapshot2.getString("artist")
                                                            , documentSnapshot2.getString("link"), documentSnapshot2.getString("time"));

                                                    playlist.addSong(song);


                                                }

                                            }
                                        }


                                    }
                                });
                                playlists.add(playlist);

                            }
                        }
                    });

                    Thread.sleep(2000);

                }
                publishProgress();


            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

           if(playlists.size() <= 0)
           {
               loading.setVisibility(View.INVISIBLE);
           }

             if(playlists != null) {
                initRecyclerView();
            }
            loading.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);


        }

        @Override
        protected void onPreExecute() {
            recyclerView.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);

        }

    }

    private void initRecyclerView(){
        if(getActivity() != null) {
            viewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);
            viewModel.setUserPlaylists(playlists);
            PlaylistAdapter adapter = new PlaylistAdapter(viewModel.getUserPlaylists().getValue(), getContext(), getActivity(), viewModel, this,createPlaylist);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }





}

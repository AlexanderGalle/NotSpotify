package com.example.freespotify;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;


public class PlaylistsFragment extends Fragment {

    private FirebaseFirestore gDb;
    private FirebaseAuth gAuth;
    private List<String> gPlaylistSongNames;
    private List<Playlist> gPlaylists;
    private RecyclerView gRecyclerView;
    private ProgressBar gLoading;
    private ShareViewModel gViewModel;
    private ImageButton gCreatePlaylist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.fragment_playlists, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gRecyclerView = getView().findViewById(R.id.recyclerView);
        gLoading = getView().findViewById(R.id.loading);
        ReadPlaylists readPlaylists = new ReadPlaylists();
        readPlaylists.execute();
        gCreatePlaylist = getView().findViewById(R.id.add);

        gCreatePlaylist.setOnClickListener(new View.OnClickListener() {
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
                if(gPlaylists == null) {
                    gPlaylists = new ArrayList<>();

                    gDb = FirebaseFirestore.getInstance();
                    gAuth = FirebaseAuth.getInstance();


                    gDb.collection(gAuth.getCurrentUser().getDisplayName()+"Playlist").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                gPlaylistSongNames = new ArrayList<>();

                                if(documentSnapshot.getData().size()>1) {
                                    for (int i = 1; i < documentSnapshot.getData().size(); i++) {
                                        gPlaylistSongNames.add(documentSnapshot.getString("song" + i));

                                    }
                                }
                                final Playlist playlist = new Playlist(documentSnapshot.getString("name"),gPlaylistSongNames);


                                gDb.collection("songs").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (int i=0;i<playlist.getSongNames().size();i++) {
                                            for (QueryDocumentSnapshot documentSnapshot2 : queryDocumentSnapshots) {

                                                if (playlist.getSongNames().get(i).equals(documentSnapshot2.getString("name"))) {
                                                    Song song = new Song(documentSnapshot2.getString("name"), documentSnapshot2.getString("artist")
                                                            , documentSnapshot2.getString("link"), documentSnapshot2.getString("time"));

                                                    playlist.getSongs().add(song);


                                                }

                                            }
                                        }


                                    }
                                });
                                gPlaylists.add(playlist);

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

           if(gPlaylists.size() <= 0)
           {
               gLoading.setVisibility(View.INVISIBLE);
           }

             if(gPlaylists != null) {
                initRecyclerView();
            }
            gLoading.setVisibility(View.GONE);
            gRecyclerView.setVisibility(View.VISIBLE);


        }

        @Override
        protected void onPreExecute() {
            gRecyclerView.setVisibility(View.GONE);
            gLoading.setVisibility(View.VISIBLE);

        }

    }

    private void initRecyclerView(){
        if(getActivity() != null) {
            gViewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);
            gViewModel.setUserPlaylists(gPlaylists);
            PlaylistAdapter adapter = new PlaylistAdapter(gViewModel.getUserPlaylists().getValue(), getContext(), getActivity(), gViewModel, this);
            gRecyclerView.setAdapter(adapter);
            gRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }





}

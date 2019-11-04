package com.example.freespotify;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SharedMemory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProviders;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SongsFragment extends Fragment {
    private FirebaseFirestore db;
    private List<Song> songs;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mArtist = new ArrayList<>();
    private ArrayList<String> mLink = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressBar loading;
    private ShareViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.fragment_songs, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loading = getView().findViewById(R.id.loading);
        recyclerView = getView().findViewById(R.id.recyclerView);
        ReadSongs readSongs = new ReadSongs();
        readSongs.execute();


    }

    class ReadSongs extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... integers) {


            try {
                if(songs == null) {
                    songs = new ArrayList<>();

                    db = FirebaseFirestore.getInstance();
                    db.collection("songs").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Song song = new Song(documentSnapshot.getString("name"), documentSnapshot.getString("artist")
                                        , documentSnapshot.getString("link"), documentSnapshot.getString("time"));
                                songs.add(song);
                            }
                        }
                    });

                    Thread.sleep(2000);

                    initialiseValues();
                }
                    publishProgress();


            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            if(songs != null) {
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
        viewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);
        Adapter adapter = new Adapter(mNames,mArtist,mLink,getContext(),getActivity(),viewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initialiseValues(){
        for (int i = 0;i<songs.size();i++)
        {
            mNames.add(songs.get(i).getName());
            mArtist.add(songs.get(i).getArtist());
            mLink.add(songs.get(i).getLink());
        }

    }


}

package com.example.freespotify;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class SongsFragment extends Fragment {
    private FirebaseFirestore gDb;
    private FirebaseAuth gAuth;
    private List<Song> gSongs;
    private ArrayList<String> gNames = new ArrayList<>();
    private ArrayList<String> gArtist = new ArrayList<>();
    private ArrayList<String> gLink = new ArrayList<>();
    private RecyclerView gRecyclerView;
    private ProgressBar gLoading;
    private ShareViewModel gViewModel;
    private List<String> gPlaylistNames = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.fragment_songs, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gLoading = getView().findViewById(R.id.loading);
        gRecyclerView = getView().findViewById(R.id.recyclerView);
        gAuth = FirebaseAuth.getInstance();


        ReadSongs readSongs = new ReadSongs();
        readSongs.execute();


    }

    class ReadSongs extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... integers) {


            try {
                if(gSongs == null) {
                    gSongs = new ArrayList<>();

                    gDb = FirebaseFirestore.getInstance();
                    gDb.collection("songs").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Song song = new Song(documentSnapshot.getString("name"), documentSnapshot.getString("artist")
                                        , documentSnapshot.getString("link"), documentSnapshot.getString("time"));
                                gSongs.add(song);
                            }
                        }
                    });

                    gDb.collection(gAuth.getCurrentUser().getDisplayName()+"Playlist").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                gPlaylistNames.add(documentSnapshot.getString("name"));

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

            if(gSongs != null) {
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
        gViewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);
        Adapter adapter = new Adapter(gNames, gArtist, gLink,getContext(),getActivity(), gViewModel);
        gRecyclerView.setAdapter(adapter);
        gRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initialiseValues(){
        for (int i = 0; i< gSongs.size(); i++)
        {
            gNames.add(gSongs.get(i).getName());
            gArtist.add(gSongs.get(i).getArtist());
            gLink.add(gSongs.get(i).getLink());
        }

    }

    public void onResume()
    {
        super.onResume();
        ShareViewModel viewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);
        if(viewModel.getUserPlaylists().getValue()!=null )
        {
            if(gPlaylistNames.size() != viewModel.getUserPlaylists().getValue().size()) {
                gPlaylistNames.clear();
                for (int i = 0;i<viewModel.getUserPlaylists().getValue().size();i++) {
                    gPlaylistNames.add(viewModel.getUserPlaylists().getValue().get(i).getPlaylistName());
                }
                FragmentTransaction refresh = getFragmentManager().beginTransaction();
                refresh.detach(this);
                refresh.attach(this);
                refresh.commit();
            }


        }


    }


}

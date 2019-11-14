package com.example.freespotify;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class EditPlaylistFragment extends Fragment {

    private EditText gPlaylistName;
    private FirebaseAuth gAuth;
    private FirebaseFirestore gDb;
    private ShareViewModel gViewModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.fragment_edit_playlist, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gAuth = FirebaseAuth.getInstance();
        gDb = FirebaseFirestore.getInstance();
        ImageButton close;
        Button submit;

        if(getActivity() != null) {
            gViewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);


            gPlaylistName = getView().findViewById(R.id.NewPlaylistName);

            gPlaylistName.setText(gViewModel.getUserPlaylists().getValue().get(gViewModel.getPlaylistPosition().getValue()).getPlaylistName());

            submit = getView().findViewById(R.id.confirmPlaylist);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(getContext(), "Updating Playlist", Toast.LENGTH_SHORT).show();

                    UpdatePlaylist updatePlaylist = new UpdatePlaylist();
                    updatePlaylist.execute();
                }
            });

            close = getView().findViewById(R.id.close);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PlaylistsFragment playlistsFragment = new PlaylistsFragment();

                    ParentPlaylist parent = (ParentPlaylist) getParentFragment();

                    parent.getFrameLayout(playlistsFragment);
                }
            });





        }

    }



    class UpdatePlaylist extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... integers) {


            try {
                gDb.collection(gAuth.getCurrentUser().getDisplayName() + "Playlist").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.getString("name").equals(gViewModel.getUserPlaylists().getValue().get(gViewModel.getPlaylistPosition().getValue()).getPlaylistName())) {
                                gDb.collection(gAuth.getCurrentUser().getDisplayName() + "Playlist")
                                        .document(documentSnapshot.getId())
                                        .update("name",gPlaylistName.getText().toString());
                                gViewModel.getUserPlaylists().getValue().get(gViewModel.getPlaylistPosition().getValue()).setPlaylistName(gPlaylistName.getText().toString());
                                break;

                            }
                        }

                    }
                });

                Thread.sleep(1000);
                publishProgress();


            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            Toast.makeText(getContext(), "Playlist Updated", Toast.LENGTH_SHORT).show();
            PlaylistsFragment playlistsFragment = new PlaylistsFragment();

            ParentPlaylist parent = (ParentPlaylist) getParentFragment();

            parent.getFrameLayout(playlistsFragment);

        }
    }
}






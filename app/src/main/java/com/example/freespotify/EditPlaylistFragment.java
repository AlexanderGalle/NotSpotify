package com.example.freespotify;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class EditPlaylistFragment extends Fragment {

    private EditText playlistName;
    private Button submit;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ShareViewModel viewModel;
    private ImageButton close;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.fragment_edit_playlist, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if(getActivity() != null) {
            viewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);


            playlistName = getView().findViewById(R.id.NewPlaylistName);

            playlistName.setText(viewModel.getUserPlaylists().getValue().get(viewModel.getPlaylistPosition().getValue()).getPlaylistName());

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

        class UpdatePlaylist extends AsyncTask<Integer, Integer, String> {
            @Override
            protected String doInBackground(Integer... integers) {


                try {
                    db.collection(mAuth.getCurrentUser().getDisplayName() + "Playlist").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.getString("name").equals(viewModel.getUserPlaylists().getValue().get(viewModel.getPlaylistPosition().getValue()).getPlaylistName())) {
                                    db.collection(mAuth.getCurrentUser().getDisplayName() + "Playlist")
                                            .document(documentSnapshot.getId())
                                            .update("name",playlistName.getText().toString());
                                    viewModel.getUserPlaylists().getValue().get(viewModel.getPlaylistPosition().getValue()).setPlaylistName(playlistName.getText().toString());
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



    class UpdatePlaylist extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... integers) {


            try {
                db.collection(mAuth.getCurrentUser().getDisplayName() + "Playlist").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.getString("name").equals(viewModel.getUserPlaylists().getValue().get(viewModel.getPlaylistPosition().getValue()).getPlaylistName())) {
                                db.collection(mAuth.getCurrentUser().getDisplayName() + "Playlist")
                                        .document(documentSnapshot.getId())
                                        .update("name",playlistName.getText().toString());
                                viewModel.getUserPlaylists().getValue().get(viewModel.getPlaylistPosition().getValue()).setPlaylistName(playlistName.getText().toString());
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






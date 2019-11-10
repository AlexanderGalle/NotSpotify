package com.example.freespotify;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreatePlaylistFragment extends Fragment {

    private EditText playlistName;
    private Button submit;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ShareViewModel viewModel;
    private List<String> songNames;
    private ImageButton close;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.fragment_create_playlist, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        playlistName = getView().findViewById(R.id.NewPlaylistName);
        submit = getView().findViewById(R.id.confirmPlaylist);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (getActivity()!= null) {

            viewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);
            close = getView().findViewById(R.id.close);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (!playlistName.getText().toString().isEmpty() && playlistName.getText().toString().length()<19) {
                        submit.setBackgroundColor(Color.parseColor("#F70000"));
                        submit.setTextColor(Color.parseColor("#000000"));

                        Map<String, Object> newPlaylist = new HashMap<>();
                        newPlaylist.put("name",playlistName.getText().toString());

                        // Add a new document with a generated ID
                        db.collection(user.getDisplayName() + "Playlist")
                                .add(newPlaylist)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        songNames = new ArrayList<>();

                                        Playlist playlist = new Playlist(playlistName.getText().toString(),songNames);
                                        viewModel.getUserPlaylists().getValue().add(playlist);

                                    }
                                });

                        Toast.makeText(getContext(), "Playlist Created", Toast.LENGTH_LONG).show();

                        ParentPlaylist parent = (ParentPlaylist)getParentFragment();

                        PlaylistsFragment playlistsFragment = new PlaylistsFragment();

                        parent.getFrameLayout(playlistsFragment);


                    } else if (playlistName.getText().toString().length()>=19)
                    {
                        Toast toast = Toast.makeText(getContext(), "Playlist Name is too long", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 100);
                        toast.show();
                    }
                    else{
                        Toast toast = Toast.makeText(getContext(), "Make sure playlist name is entered", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 100);
                        toast.show();
                    }
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParentPlaylist parent = (ParentPlaylist)getParentFragment();

                    PlaylistsFragment playlistsFragment = new PlaylistsFragment();

                    parent.getFrameLayout(playlistsFragment);

                }
            });
        }


    }


}

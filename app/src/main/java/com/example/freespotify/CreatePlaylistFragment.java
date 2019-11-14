package com.example.freespotify;


import android.graphics.Color;
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

    private EditText gPlaylistName;
    private Button gSubmit;
    private FirebaseAuth gAuth;
    private FirebaseFirestore gDb;
    private ShareViewModel gViewModel;
    private List<String> gSongNames;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.fragment_create_playlist, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gPlaylistName = getView().findViewById(R.id.NewPlaylistName);
        gSubmit = getView().findViewById(R.id.confirmPlaylist);

        gAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = gAuth.getCurrentUser();
        gDb = FirebaseFirestore.getInstance();
        ImageButton close;

        if (getActivity()!= null) {

            gViewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);
            close = getView().findViewById(R.id.close);

            gSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (!gPlaylistName.getText().toString().isEmpty() && gPlaylistName.getText().toString().length()<19) {
                        gSubmit.setBackgroundColor(Color.parseColor("#F70000"));
                        gSubmit.setTextColor(Color.parseColor("#000000"));

                        Map<String, Object> newPlaylist = new HashMap<>();
                        newPlaylist.put("name",gPlaylistName.getText().toString());

                        // Add a new document with a generated ID
                        gDb.collection(user.getDisplayName() + "Playlist")
                                .add(newPlaylist)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        gSongNames = new ArrayList<>();

                                        Playlist playlist = new Playlist(gPlaylistName.getText().toString(),gSongNames);
                                        gViewModel.getUserPlaylists().getValue().add(playlist);

                                    }
                                });

                        Toast.makeText(getContext(), "Playlist Created", Toast.LENGTH_LONG).show();

                        ParentPlaylist parent = (ParentPlaylist)getParentFragment();

                        PlaylistsFragment playlistsFragment = new PlaylistsFragment();

                        parent.getFrameLayout(playlistsFragment);


                    } else if (gPlaylistName.getText().toString().length()>=19)
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

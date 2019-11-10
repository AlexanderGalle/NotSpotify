package com.example.freespotify;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoveAdapter extends RecyclerView.Adapter<RemoveAdapter.ViewHolder>{

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ShareViewModel sViewModel;
    private Context sContext;
    private Activity sActivity;
    private String selectPlaylist;
    private AddToPlaylistFragment sFragment;
    private TextView title;

    public RemoveAdapter(Context context, Activity activity,ShareViewModel viewModel,AddToPlaylistFragment fragment)
    {
        sContext = context;
        sActivity = activity;
        sViewModel = viewModel;
        sFragment = fragment;

    }


    @Override
    public RemoveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_additem,parent,false);
        RemoveAdapter.ViewHolder holder = new RemoveAdapter.ViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(final RemoveAdapter.ViewHolder holder, final int position)
    {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        title = sFragment.getView().findViewById(R.id.newPlaylistTitle);

        title.setText("Remove From");

        holder.name.setText(sViewModel.getUserPlaylists().getValue().get(position).getPlaylistName());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectPlaylist = sViewModel.getUserPlaylists().getValue().get(position).getPlaylistName();

                if(sViewModel.getUserPlaylists().getValue().get(position).getSongNames().contains(sViewModel.getSelectedSong().getValue())) {
                    db.collection(mAuth.getCurrentUser().getDisplayName() + "Playlist").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.getString("name").equals(selectPlaylist)) {
                                    int j =1;
                                    while(j<documentSnapshot.getData().size()) {

                                        if (sViewModel.getSelectedSong().getValue().equals(documentSnapshot.getString("song" + j))) {

                                            sViewModel.getUserPlaylists().getValue().get(position).getSongNames()
                                                    .remove(documentSnapshot.getString("song" + j));
                                            break;
                                        }
                                        j++;
                                    }

                                    Map<String, Object> deleteSong = new HashMap<>();
                                    deleteSong.put("name", selectPlaylist);
                                    for (int k=0;k<sViewModel.getUserPlaylists().getValue().get(position).getSongNames().size();k++)
                                    {

                                        deleteSong.put("song"+(k+1),sViewModel.getUserPlaylists().getValue().get(position).getSongNames().get(k));
                                    }

                                    db.collection(mAuth.getCurrentUser().getDisplayName() + "Playlist").document(documentSnapshot.getId()).set(deleteSong);


                                    Toast toast = Toast.makeText(sContext, "Song Removed from " + selectPlaylist, Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 100);
                                    toast.show();

                                    ParentPlaylist parent = (ParentPlaylist)sFragment.getParentFragment();

                                    PlaylistsFragment playlistsFragment = new PlaylistsFragment();

                                    parent.getFrameLayout(playlistsFragment);


                                }

                            }

                        }
                    });


                }
                else {
                    Toast toast = Toast.makeText(sContext, "This is song is not in " + selectPlaylist, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 150);
                    toast.show();
                }




            }
        });

    }

    @Override
    public int getItemCount()
    {
        return sViewModel.getUserPlaylists().getValue().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            layout = itemView.findViewById(R.id.addSong);


        }


    }




}

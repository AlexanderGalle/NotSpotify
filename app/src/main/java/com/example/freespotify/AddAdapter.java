package com.example.freespotify;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class AddAdapter extends RecyclerView.Adapter<AddAdapter.ViewHolder>{

    private FirebaseAuth gAuth;
    private FirebaseFirestore gDb;
    private ShareViewModel gViewModel;
    private Context gContext;
    private String gSelectPlaylist;
    private AddToPlaylistFragment gFragment;


    public AddAdapter(Context context,ShareViewModel viewModel,AddToPlaylistFragment fragment)
    {
        gContext = context;
        gViewModel = viewModel;
        gFragment = fragment;

    }


    @Override
    public AddAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_additem,parent,false);
        AddAdapter.ViewHolder holder = new AddAdapter.ViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(final AddAdapter.ViewHolder holder, final int position)
    {
        gAuth = FirebaseAuth.getInstance();
        gDb = FirebaseFirestore.getInstance();
        TextView title;

        title = gFragment.getView().findViewById(R.id.newPlaylistTitle);

        title.setText("Add to");

        holder.name.setText(gViewModel.getUserPlaylists().getValue().get(position).getPlaylistName());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gSelectPlaylist = gViewModel.getUserPlaylists().getValue().get(position).getPlaylistName();

                if(!gViewModel.getUserPlaylists().getValue().get(position).getSongNames().contains(gViewModel.getSelectedSong().getValue())) {
                    gDb.collection(gAuth.getCurrentUser().getDisplayName() + "Playlist").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.getString("name").equals(gSelectPlaylist)) {

                                    Map<String, Object> newSong = new HashMap<>();
                                    newSong.put("song" + (documentSnapshot.getData().size()), gViewModel.getSelectedSong().getValue());

                                    gDb.collection(gAuth.getCurrentUser().getDisplayName() + "Playlist").document(documentSnapshot.getId()).update(newSong);
                                    gViewModel.getUserPlaylists().getValue().get(position).getSongNames().add(gViewModel.getSelectedSong().getValue());

                                    Toast toast = Toast.makeText(gContext, "Song added to " + gSelectPlaylist, Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 100);
                                    toast.show();

                                    ParentPlaylist parent = (ParentPlaylist)gFragment.getParentFragment();

                                    PlaylistsFragment playlistsFragment = new PlaylistsFragment();

                                    parent.getFrameLayout(playlistsFragment);


                                }

                            }

                        }
                    });


                }
                else {
                    Toast toast = Toast.makeText(gContext, "This song is already in " + gSelectPlaylist, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 150);
                    toast.show();
                }




            }
        });

    }

    @Override
    public int getItemCount()
    {
        return gViewModel.getUserPlaylists().getValue().size();
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


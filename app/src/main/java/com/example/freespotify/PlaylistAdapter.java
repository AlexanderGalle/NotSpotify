package com.example.freespotify;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;


public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private ShareViewModel gViewModel;
    private List<Playlist> gPlaylists;
    private Context gContext;
    private Activity gActivity;
    private PlaylistsFragment gfragment;
    private FirebaseFirestore gDb;
    private FirebaseAuth gAuth;


    public PlaylistAdapter(List<Playlist> playlists, Context context, Activity activity, ShareViewModel viewModel, PlaylistsFragment fragment) {
        gPlaylists = playlists;
        gContext = context;
        gActivity = activity;
        gViewModel = viewModel;
        gfragment = fragment;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_playlistitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        gAuth = FirebaseAuth.getInstance();
        gDb = FirebaseFirestore.getInstance();


        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CurrentPlaylistFragment currentPlaylistFragment = new CurrentPlaylistFragment();

                ParentPlaylist parent = (ParentPlaylist) gfragment.getParentFragment();

                parent.getFrameLayout(currentPlaylistFragment);

                gViewModel.setPlaylistPosition(position);

            }
        });

        holder.name.setText(gPlaylists.get(position).getPlaylistName());
        holder.songCount.setText("Songs" + " " + gPlaylists.get(position).getSongs().size());


        List<String> option = new ArrayList<>();
        option.add("Select one");
        option.add("Change playlist name");
        option.add("Remove playlist");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(gActivity, android.R.layout.simple_spinner_item, option);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.menu.setAdapter(adapter);


        holder.menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).toString().equals("Change playlist name")) {

                    gViewModel.setPlaylistPosition(position);

                    EditPlaylistFragment editPlaylistFragment = new EditPlaylistFragment();

                    ParentPlaylist parent = (ParentPlaylist) gfragment.getParentFragment();

                    parent.getFrameLayout(editPlaylistFragment);

                }


                if (adapterView.getItemAtPosition(i).toString().equals("Remove playlist")) {

                    gViewModel.setPlaylistPosition(position);

                    Toast.makeText(gContext, "Deleting Playlist", Toast.LENGTH_SHORT).show();

                    DeletePlaylist deletePlaylist = new DeletePlaylist();
                    deletePlaylist.execute();


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return gPlaylists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton play;
        Spinner menu;
        TextView name, songCount;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            play = itemView.findViewById(R.id.play);
            menu = itemView.findViewById(R.id.menu);
            name = itemView.findViewById(R.id.name);
            songCount = itemView.findViewById(R.id.songCount);
            layout = itemView.findViewById(R.id.playlist);


        }
    }

    class DeletePlaylist extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... integers) {


            try {
                gDb.collection(gAuth.getCurrentUser().getDisplayName() + "Playlist").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.getString("name").equals(gViewModel.getUserPlaylists().getValue().get(gViewModel.getPlaylistPosition().getValue()).getPlaylistName())) {
                                gDb.collection(gAuth.getCurrentUser().getDisplayName() + "Playlist").document(documentSnapshot.getId()).delete();
                                gViewModel.getUserPlaylists().getValue().remove(gViewModel.getUserPlaylists().getValue().get(gViewModel.getPlaylistPosition().getValue()));
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

            Toast.makeText(gContext, "Playlist Deleted", Toast.LENGTH_SHORT).show();
            PlaylistsFragment playlistsFragment = new PlaylistsFragment();

            ParentPlaylist parent = (ParentPlaylist) gfragment.getParentFragment();

            parent.getFrameLayout(playlistsFragment);

        }
    }
}

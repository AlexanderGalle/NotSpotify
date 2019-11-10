package com.example.freespotify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ShareViewModel sViewModel;
    private ArrayList<String> sNames;
    private ArrayList<String> sArtist;
    private ArrayList<String> sLink;
    private Context sContext;
    private Activity sActivity;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<String> playlistNames;
    private String selectPlaylist;
    private Boolean exists,notExist;


    public Adapter(ArrayList<String> names, ArrayList<String> artist, ArrayList<String> link, Context context, Activity activity, ShareViewModel viewModel, List<String>playlistName)
    {
        sNames = names;
        sArtist = artist;
        sLink = link;
        sContext = context;
        sActivity = activity;
        sViewModel = viewModel;
        playlistNames = playlistName;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PlayButton(position,holder);
            }
        });

        List<String> option = new ArrayList<>();

        option.add("Select one");
        option.add("Add to Playlist");
        option.add("Remove from Playlist");




        ArrayAdapter<String> adapter = new ArrayAdapter<>(sActivity, android.R.layout.simple_spinner_item, option);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.menu.setAdapter(adapter);
        holder.menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, View view, int i, long l) {
                if(sViewModel.getUserPlaylists().getValue() != null) {

                    if (adapterView.getItemAtPosition(i).toString().equals("Add to Playlist")){


                        sViewModel.setSelectedSong(sNames.get(position));
                        ViewPager viewPager  =sActivity.findViewById(R.id.viewPager);
                        holder.menu.setSelection(0);
                        viewPager.setCurrentItem(1,true);

                        ParentPlaylist parent = sViewModel.getParent().getValue();

                        AddToPlaylistFragment addToPlaylistFragment = new AddToPlaylistFragment("Add");

                        parent.getFrameLayout(addToPlaylistFragment);


                    }

                    if (adapterView.getItemAtPosition(i).toString().equals("Remove from Playlist")){

                        sViewModel.setSelectedSong(sNames.get(position));
                        ViewPager viewPager  =sActivity.findViewById(R.id.viewPager);
                        holder.menu.setSelection(0);
                        viewPager.setCurrentItem(1,true);

                        ParentPlaylist parent = sViewModel.getParent().getValue();

                        AddToPlaylistFragment addToPlaylistFragment = new AddToPlaylistFragment("Remove");

                        parent.getFrameLayout(addToPlaylistFragment);


                    }


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        holder.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pause();
            }
        });
        holder.stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReleasePlayer();
                sViewModel.setCurrentName("");
                sViewModel.setCurrentArtist("");

                holder.currentName.setText(sViewModel.getCurrentName().getValue());
                holder.currentArtist.setText(sViewModel.getCurrentArtist().getValue());

            }
        });



        holder.name.setText(sNames.get(position));
        holder.artist.setText(sArtist.get(position));



    }
    @Override
    public int getItemCount()
    {
        return sNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageButton play,pause,stop;
        TextView name,artist,currentName,currentArtist;
        LinearLayout layout, currentPlaying;
        Spinner menu;
        public ViewHolder(View itemView)
        {
            super(itemView);
            play = itemView.findViewById(R.id.play);
            menu = itemView.findViewById(R.id.menu);
            name = itemView.findViewById(R.id.name);
            artist = itemView.findViewById(R.id.artist);
            layout = itemView.findViewById(R.id.song);
            pause = sActivity.findViewById(R.id.pause);
            stop = sActivity.findViewById(R.id.stop);
            currentName = sActivity.findViewById(R.id.current_name);
            currentArtist = sActivity.findViewById(R.id.current_artist);
            currentPlaying = sActivity.findViewById(R.id.now);





        }
    }

    private void ReleasePlayer(){
        if (sViewModel.getCurrentPlayer().getValue() != null)
        {
            sViewModel.getCurrentPlayer().getValue().release();
            sViewModel.setCurrentPlayer(null);
        }
    }

    private void Pause(){
        if(sViewModel.getCurrentPlayer().getValue() != null)
        {
            sViewModel.getCurrentPlayer().getValue().pause();
        }
    }

    private void Play(int position){

        if(sViewModel.getCurrentPlayer().getValue()== null)
        {
            sViewModel.setCurrentPlayer(MediaPlayer.create(sContext, Uri.parse(sLink.get(position))));

            sViewModel.getCurrentPlayer().getValue().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    ReleasePlayer();
                }
            });
        }

    }

    private void SetViewModel(int position, ViewHolder holder)
    {
        sViewModel.setCurrentName(sNames.get(position));
        sViewModel.setCurrentArtist(sArtist.get(position));

        holder.currentName.setText(sViewModel.getCurrentName().getValue());
        holder.currentArtist.setText(sViewModel.getCurrentArtist().getValue());


    }

    private void PlayButton(final int position, final ViewHolder holder){

        if(sViewModel.getPosition().getValue() != null)
        {
            if(position != sViewModel.getPosition().getValue()) {
                ReleasePlayer();
            }
        }

        sViewModel.setPosition(position);


        if(sViewModel.getPosition().getValue() != null) {

            SetViewModel(sViewModel.getPosition().getValue(), holder);

            Play(sViewModel.getPosition().getValue());


            if (sViewModel.getCurrentPlayer().getValue().isPlaying()) {
                ReleasePlayer();
                Play(sViewModel.getPosition().getValue());

            }

            sViewModel.getCurrentPlayer().getValue().start();

            sViewModel.getCurrentPlayer().getValue().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    ReleasePlayer();
                    if(position+1 < sNames.size()) {
                        sViewModel.setPosition(position + 1);
                    }
                    else {
                        sViewModel.setPosition(0);
                    }
                    PlayButton(sViewModel.getPosition().getValue(), holder);
                }
            });

        }
    }

}

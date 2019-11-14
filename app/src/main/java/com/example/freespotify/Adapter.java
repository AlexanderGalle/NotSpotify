package com.example.freespotify;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ShareViewModel gViewModel;
    private ArrayList<String> gNames;
    private ArrayList<String> gArtist;
    private ArrayList<String> gLink;
    private Context gContext;
    private Activity gActivity;



    public Adapter(ArrayList<String> names, ArrayList<String> artist, ArrayList<String> link, Context context, Activity activity, ShareViewModel viewModel)
    {
        gNames = names;
        gArtist = artist;
        gLink = link;
        gContext = context;
        gActivity = activity;
        gViewModel = viewModel;


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




        ArrayAdapter<String> adapter = new ArrayAdapter<>(gActivity, android.R.layout.simple_spinner_item, option);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.menu.setAdapter(adapter);
        holder.menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, View view, int i, long l) {
                if(gViewModel.getUserPlaylists().getValue() != null) {

                    if (adapterView.getItemAtPosition(i).toString().equals("Add to Playlist")){


                        gViewModel.setSelectedSong(gNames.get(position));
                        ViewPager viewPager  =gActivity.findViewById(R.id.viewPager);
                        holder.menu.setSelection(0);
                        viewPager.setCurrentItem(1,true);

                        ParentPlaylist parent = gViewModel.getParent().getValue();

                        AddToPlaylistFragment addToPlaylistFragment = new AddToPlaylistFragment("Add");

                        parent.getFrameLayout(addToPlaylistFragment);


                    }

                    if (adapterView.getItemAtPosition(i).toString().equals("Remove from Playlist")){

                        gViewModel.setSelectedSong(gNames.get(position));
                        ViewPager viewPager  =gActivity.findViewById(R.id.viewPager);
                        holder.menu.setSelection(0);
                        viewPager.setCurrentItem(1,true);

                        ParentPlaylist parent = gViewModel.getParent().getValue();

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
                gViewModel.setCurrentName("");
                gViewModel.setCurrentArtist("");

                holder.currentName.setText(gViewModel.getCurrentName().getValue());
                holder.currentArtist.setText(gViewModel.getCurrentArtist().getValue());

            }
        });



        holder.name.setText(gNames.get(position));
        holder.artist.setText(gArtist.get(position));



    }
    @Override
    public int getItemCount()
    {
        return gNames.size();
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
            pause = gActivity.findViewById(R.id.pause);
            stop = gActivity.findViewById(R.id.stop);
            currentName = gActivity.findViewById(R.id.current_name);
            currentArtist = gActivity.findViewById(R.id.current_artist);
            currentPlaying = gActivity.findViewById(R.id.now);





        }
    }

    private void ReleasePlayer(){
        if (gViewModel.getCurrentPlayer().getValue() != null)
        {
            gViewModel.getCurrentPlayer().getValue().release();
            gViewModel.setCurrentPlayer(null);
        }
    }

    private void Pause(){
        if(gViewModel.getCurrentPlayer().getValue() != null)
        {
            gViewModel.getCurrentPlayer().getValue().pause();
        }
    }

    private void Play(int position){

        if(gViewModel.getCurrentPlayer().getValue()== null)
        {
            gViewModel.setCurrentPlayer(MediaPlayer.create(gContext, Uri.parse(gLink.get(position))));

            gViewModel.getCurrentPlayer().getValue().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    ReleasePlayer();
                }
            });
        }

    }

    private void SetViewModel(int position, ViewHolder holder)
    {
        gViewModel.setCurrentName(gNames.get(position));
        gViewModel.setCurrentArtist(gArtist.get(position));

        holder.currentName.setText(gViewModel.getCurrentName().getValue());
        holder.currentArtist.setText(gViewModel.getCurrentArtist().getValue());


    }

    private void PlayButton(final int position, final ViewHolder holder){

        if(gViewModel.getPosition().getValue() != null)
        {
            if(position != gViewModel.getPosition().getValue()) {
                ReleasePlayer();
            }
        }

        gViewModel.setPosition(position);


        if(gViewModel.getPosition().getValue() != null) {

            SetViewModel(gViewModel.getPosition().getValue(), holder);

            Play(gViewModel.getPosition().getValue());


            if (gViewModel.getCurrentPlayer().getValue().isPlaying()) {
                ReleasePlayer();
                Play(gViewModel.getPosition().getValue());

            }

            gViewModel.getCurrentPlayer().getValue().start();

            gViewModel.getCurrentPlayer().getValue().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    ReleasePlayer();
                    if(position+1 < gNames.size()) {
                        gViewModel.setPosition(position + 1);
                    }
                    else {
                        gViewModel.setPosition(0);
                    }
                    PlayButton(gViewModel.getPosition().getValue(), holder);
                }
            });

        }
    }

}

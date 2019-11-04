package com.example.freespotify;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ShareViewModel mviewModel;
    private ArrayList<String> mNames;
    private ArrayList<String> mArtist;
    private ArrayList<String> mLink;
    private Context mContext;
    private Activity mActivity;


    public Adapter(ArrayList<String> names, ArrayList<String> artist, ArrayList<String> link, Context context, Activity activity, ShareViewModel viewModel)
    {
        mNames = names;
        mArtist = artist;
        mLink = link;
        mContext = context;
        mActivity = activity;
        mviewModel = viewModel;


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
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
            }
        });



        holder.name.setText(mNames.get(position));
        holder.artist.setText(mArtist.get(position));



    }
    @Override
    public int getItemCount()
    {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageButton play,menu,pause,stop;
        TextView name,artist,currentName,currentArtist;
        LinearLayout layout, currentPlaying;
        public ViewHolder(View itemView)
        {
            super(itemView);
            play = itemView.findViewById(R.id.play);
            menu = itemView.findViewById(R.id.menu);
            name = itemView.findViewById(R.id.name);
            artist = itemView.findViewById(R.id.artist);
            layout = itemView.findViewById(R.id.song);
            pause = mActivity.findViewById(R.id.pause);
            stop = mActivity.findViewById(R.id.stop);
            currentName = mActivity.findViewById(R.id.current_name);
            currentArtist = mActivity.findViewById(R.id.current_artist);
            currentPlaying = mActivity.findViewById(R.id.now);



        }
    }

    private void ReleasePlayer(){
        if (mviewModel.getCurrentPlayer().getValue() != null)
        {
            mviewModel.getCurrentPlayer().getValue().release();
            mviewModel.setCurrentPlayer(null);
        }
    }

    private void Pause(){
        if(mviewModel.getCurrentPlayer().getValue() != null)
        {
            mviewModel.getCurrentPlayer().getValue().pause();
        }
    }

    private void Play(int position){

        if(mviewModel.getCurrentPlayer().getValue()== null)
        {
            mviewModel.setCurrentPlayer(MediaPlayer.create(mContext, Uri.parse(mLink.get(position))));

            mviewModel.getCurrentPlayer().getValue().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    ReleasePlayer();
                }
            });
        }

    }

    private void SetViewModel(int position, ViewHolder holder)
    {
        mviewModel.setCurrentName(mNames.get(position));
        mviewModel.setCurrentArtist(mArtist.get(position));

        holder.currentName.setText(mviewModel.getCurrentName().getValue());
        holder.currentArtist.setText(mviewModel.getCurrentArtist().getValue());


    }

    private void PlayButton(final int position, final ViewHolder holder){

        mviewModel.setPosition(position);


        if(mviewModel.getPosition().getValue() != null) {

            /*Log.d("aaaaaaaa",holder.currentName.getText().toString());
            Log.d("aaaaaaaa",mviewModel.getCurrentName().getValue());
            if(!holder.currentName.equals(mviewModel.getCurrentName().getValue()))
            {
                ReleasePlayer();
            }*/


            Play(mviewModel.getPosition().getValue());


            if (mviewModel.getCurrentPlayer().getValue().isPlaying()) {
                ReleasePlayer();
                Play(mviewModel.getPosition().getValue());

            }

            mviewModel.getCurrentPlayer().getValue().start();

            mviewModel.getCurrentPlayer().getValue().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    ReleasePlayer();
                    if(position+1 < mNames.size()) {
                        mviewModel.setPosition(position + 1);
                    }
                    else {
                        mviewModel.setPosition(0);
                    }
                    PlayButton(mviewModel.getPosition().getValue(), holder);
                }
            });



            SetViewModel(mviewModel.getPosition().getValue(), holder);
        }
    }


}

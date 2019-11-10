package com.example.freespotify;

import android.media.MediaPlayer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.List;

public class ShareViewModel extends ViewModel {
    private MutableLiveData<String> currentName = new MutableLiveData<>();
    private MutableLiveData<String> currentArtist = new MutableLiveData<>();
    private MutableLiveData<Integer> position = new MutableLiveData<>();
    private MutableLiveData<Integer> playlistPosition = new MutableLiveData<>();
    private MutableLiveData<MediaPlayer> currentPlayer = new MutableLiveData<>();
    private MutableLiveData<List<Playlist>> userPlaylists = new MutableLiveData<>();
    private MutableLiveData<ParentPlaylist> parent = new MutableLiveData<>();
    private MutableLiveData<String> selectedSong = new MutableLiveData<>();



    public void setCurrentPlayer(MediaPlayer input)
    {
        currentPlayer.setValue(input);
    }

    public LiveData<MediaPlayer> getCurrentPlayer()
    {
        return currentPlayer;
    }

    public void setCurrentName(String input)
    {
        currentName.setValue(input);
    }

    public LiveData<String> getCurrentName()
    {
        return currentName;
    }

    public void setCurrentArtist(String input)
    {
        currentArtist.setValue(input);
    }

    public LiveData<String> getCurrentArtist()
    {
        return currentArtist;
    }

    public void setPosition(Integer input)
    {
        position.setValue(input);
    }

    public LiveData<Integer> getPosition()
    {
        return position;
    }

    public void setPlaylistPosition(Integer input)
    {
        playlistPosition.setValue(input);
    }

    public LiveData<Integer> getPlaylistPosition()
    {
        return playlistPosition;
    }

    public void setUserPlaylists(List<Playlist> input){userPlaylists.setValue(input);}

    public LiveData<List<Playlist>> getUserPlaylists(){return userPlaylists;}

    public void setParent(ParentPlaylist input){parent.setValue(input);}

    public LiveData<ParentPlaylist> getParent(){return parent;}

    public void setSelectedSong(String input){selectedSong.setValue(input);}

    public LiveData<String> getSelectedSong(){return selectedSong;}




}

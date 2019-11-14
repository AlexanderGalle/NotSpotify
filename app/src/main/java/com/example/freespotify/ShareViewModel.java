package com.example.freespotify;

import android.media.MediaPlayer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ShareViewModel extends ViewModel {
    private MutableLiveData<String> gCurrentName = new MutableLiveData<>();
    private MutableLiveData<String> gCurrentArtist = new MutableLiveData<>();
    private MutableLiveData<Integer> gPosition = new MutableLiveData<>();
    private MutableLiveData<Integer> gPlaylistPosition = new MutableLiveData<>();
    private MutableLiveData<MediaPlayer> gCurrentPlayer = new MutableLiveData<>();
    private MutableLiveData<List<Playlist>> gUserPlaylists = new MutableLiveData<>();
    private MutableLiveData<ParentPlaylist> gParent = new MutableLiveData<>();
    private MutableLiveData<String> gSelectedSong = new MutableLiveData<>();



    public void setCurrentPlayer(MediaPlayer input)
    {
        gCurrentPlayer.setValue(input);
    }

    public LiveData<MediaPlayer> getCurrentPlayer()
    {
        return gCurrentPlayer;
    }

    public void setCurrentName(String input)
    {
        gCurrentName.setValue(input);
    }

    public LiveData<String> getCurrentName()
    {
        return gCurrentName;
    }

    public void setCurrentArtist(String input)
    {
        gCurrentArtist.setValue(input);
    }

    public LiveData<String> getCurrentArtist()
    {
        return gCurrentArtist;
    }

    public void setPosition(Integer input)
    {
        gPosition.setValue(input);
    }

    public LiveData<Integer> getPosition()
    {
        return gPosition;
    }

    public void setPlaylistPosition(Integer input)
    {
        gPlaylistPosition.setValue(input);
    }

    public LiveData<Integer> getPlaylistPosition()
    {
        return gPlaylistPosition;
    }

    public void setUserPlaylists(List<Playlist> input){
        gUserPlaylists.setValue(input);}

    public LiveData<List<Playlist>> getUserPlaylists(){return gUserPlaylists;}

    public void setParent(ParentPlaylist input){
        gParent.setValue(input);}

    public LiveData<ParentPlaylist> getParent(){return gParent;}

    public void setSelectedSong(String input){
        gSelectedSong.setValue(input);}

    public LiveData<String> getSelectedSong(){return gSelectedSong;}




}

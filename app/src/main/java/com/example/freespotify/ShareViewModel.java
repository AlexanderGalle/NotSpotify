package com.example.freespotify;

import android.media.MediaPlayer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

public class ShareViewModel extends ViewModel {
    private MutableLiveData<String> currentName = new MutableLiveData<>();
    private MutableLiveData<String> currentArtist = new MutableLiveData<>();
    private MutableLiveData<Integer> position = new MutableLiveData<>();
    private MutableLiveData<MediaPlayer> currentPlayer = new MutableLiveData<>();


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


}

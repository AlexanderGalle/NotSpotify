package com.example.freespotify;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private String gName;
    private List<String> gSongNames;
    private List<Song> gSongs;

    public Playlist(String name, List<String> songNames){
        this.gName = name;
        this.gSongNames = songNames;
        this.gSongs = new ArrayList<>();

    }

    public String getPlaylistName(){return gName;}

    public void setPlaylistName(String input){this.gName = input;}

    public List<String> getSongNames(){return gSongNames;}

    public List<Song> getSongs(){return gSongs;}
    
}

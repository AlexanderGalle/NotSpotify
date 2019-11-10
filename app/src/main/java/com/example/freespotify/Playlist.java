package com.example.freespotify;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private String name;
    private List<String> songNames;
    private List<Song> songs;

    public Playlist(String name, List<String> songNames){
        this.name = name;
        this.songNames = songNames;
        this.songs = new ArrayList<>();

    }

    public String getPlaylistName(){return name;}

    public void setPlaylistName(String input){this.name = input;}

    public List<String> getSongNames(){return songNames;}

    public List<Song> getSongs(){return songs;}

    public void addSong(Song input){this.songs.add(input);}

    public void removeSong(Song input){this.songs.remove(input);}
}

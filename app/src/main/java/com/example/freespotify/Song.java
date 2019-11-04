package com.example.freespotify;

public class Song {

    private String name,artist,link,time;

    public Song(String name, String artist, String link, String time){
        this.name = name;
        this.artist = artist;
        this.link = link;
        this.time = time;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getArtist(){
        return artist;
    }

    public void setArtist(String artist){
        this.artist = artist;
    }

    public String getLink(){
        return link;
    }

    public void setLink(String link){
        this.link = link;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }
}

package com.example.freespotify;

public class Song {

    private String gName, gArtist, gLink, gTime;

    public Song(String name, String artist, String link, String time){
        this.gName = name;
        this.gArtist = artist;
        this.gLink = link;
        this.gTime = time;
    }

    public String getName(){
        return gName;
    }

    public void setName(String name){
        this.gName = name;
    }

    public String getArtist(){
        return gArtist;
    }

    public void setArtist(String artist){
        this.gArtist = artist;
    }

    public String getLink(){
        return gLink;
    }

    public void setLink(String link){
        this.gLink = link;
    }

    public String getTime(){
        return gTime;
    }

    public void setTime(String time){
        this.gTime = time;
    }
}

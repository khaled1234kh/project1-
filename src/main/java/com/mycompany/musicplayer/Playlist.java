package com.mycompany.musicplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Playlist {
    private String name;
    private List<Song> songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }

    public Song getSong(int index) {
        return songs.get(index);
    }

    public int getSize() {
        return songs.size();
    }

    public void shuffle() {
        Collections.shuffle(songs);
    }

    public void clear() {
        songs.clear();
    }
}
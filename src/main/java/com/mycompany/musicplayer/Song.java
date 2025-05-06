package com.mycompany.musicplayer;

import java.io.File;

public class Song {
    private String title;
    private String artist;
    private String album;
    private File file;
    private long duration;
    private boolean liked;

    public Song(String title, String artist, String album, File file, long duration) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.file = file;
        this.duration = duration;
        this.liked = false;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public File getFile() {
        return file;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    @Override
    public String toString() {
        return title;
    }
}
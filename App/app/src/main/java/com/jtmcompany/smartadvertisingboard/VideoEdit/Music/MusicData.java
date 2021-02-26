package com.jtmcompany.smartadvertisingboard.VideoEdit.Music;

import android.net.Uri;

public class MusicData {
    private Uri musicImg;
    public String musicTitle;
    private String musicSinger;
    private String albumId;
    private String musicId;

    public Uri getMusicImg() {
        return musicImg;
    }

    public void setMusicImg(Uri musicImg) {
        this.musicImg = musicImg;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public String getMusicSinger() {
        return musicSinger;
    }

    public void setMusicSinger(String musicSinger) {
        this.musicSinger = musicSinger;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }
}


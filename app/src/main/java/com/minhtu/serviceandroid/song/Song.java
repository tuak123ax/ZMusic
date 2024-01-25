package com.minhtu.serviceandroid.song;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;

    private String singer;
    private String resource;
    private String image;

    private String duration;
    private String type;

    private int currentProgress;

    public Song(String title, String resource, String image, String type) {
        this.title = title;
        this.resource = resource;
        this.image = image;
        this.type = type;
    }

    public Song(String title, String singer, String resource, String duration, String type) {
        this.title = title;
        this.singer = singer;
        this.resource = resource;
        this.duration = duration;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }
}

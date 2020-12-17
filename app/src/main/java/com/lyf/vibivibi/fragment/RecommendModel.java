package com.lyf.vibivibi.fragment;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jiayiyang on 17/4/28.
 */

public class RecommendModel {
    private String title;
    private String cover;
    private String play;
    private String reply;
    private String duration;
    private String name;

    public RecommendModel(String title, String cover, String play, String reply, String duration, String name) {
        this.title = title;
        this.cover = cover;
        this.play = play;
        this.reply = reply;
        this.duration = duration;
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

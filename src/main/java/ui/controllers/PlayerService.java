package ui.controllers;

import entities.Track;

import infrastructure.audio.AudioPlayer;

public class PlayerService {

    private Track currentTrack;
    private AudioPlayer player;

    public Track getCurrentTrack() {
        System.out.println("get track");
        return currentTrack;
    }

    public void setCurrentTrack(Track track) {
        System.out.println("set track:"+track.getMetadata().getTitle());
        this.currentTrack=track;
    }
}
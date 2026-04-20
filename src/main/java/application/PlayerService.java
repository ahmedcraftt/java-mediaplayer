package application;

import entities.Track;
import infrastructure.audio.AudioPlayer;
import infrastructure.audio.PlaybackState;
import infrastructure.audio.RepeatMode;

import java.util.List;

public class PlayerService {

    private final AudioPlayer player;

    public PlayerService(AudioPlayer player) {
        this.player = player;
    }

    public void play(Track track) {
        player.play(track);
    }

    public void playQueue(List<Track> tracks) {
        player.clearQueue();
        player.enqueueAll(tracks);
        player.playFromQueue();
    }

    public void next() {
        player.playNext();
    }

    public void pause() {
        player.pause();
    }

    public void resume() {
        player.resume();
    }

    public void stop() {
        player.stop();
    }

    public void setRepeatMode(RepeatMode mode) {
        player.setRepeatMode(mode);
    }

    public AudioPlayer getInternalPlayer() {
        return player;
    }

    public PlaybackState getState() {
        return player.getState();
    }

}
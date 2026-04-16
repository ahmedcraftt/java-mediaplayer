package mediaPlaying;

import entities.Track;

import java.util.List;

public class AudioPlayer {
    private Track currentTrack;
    private final PlaybackQueue queue = new PlaybackQueue();
    private final AudioEngine engine = new AudioEngine(this::playNext);
    private PlaybackState state = PlaybackState.STOPPED;
    private RepeatMode repeatMode = RepeatMode.STOP_WHEN_QUEUE_END;

    public void play(Track track){
        if (track == null) return;

        currentTrack = track;
        engine.play(track.getFilePath());
        state = PlaybackState.PLAYING;
    }
    public void enqueueAll(List<Track> tracks) {
        if (tracks == null || tracks.isEmpty()) return;
        for (Track track : tracks) {
            queue.add(track);
        }
    }
    public void clearQueue(){
        queue.clear();
    }
    public void playNext() {
        switch (repeatMode) {

            case LOOP_CURRENT_ONE -> {
                if (currentTrack != null) {
                    engine.stop(); // ensure fresh start
                    play(currentTrack);
                }
            }

            case PLAY_ONE -> {
                stop();
            }

            case STOP_WHEN_QUEUE_END, LOOP_CURRENT_QUEUE -> {
                Track next = queue.next();

                if (next == null) {
                    stop();
                    return;
                }

                play(next);
            }
        }
    }
    public void pause() {
        if (state == PlaybackState.PLAYING) {
            state = PlaybackState.PAUSED;
            engine.pause();
        }
    }
    public void stop() {
        if (state != PlaybackState.STOPPED) {
            engine.stop();
            currentTrack = null;
            state = PlaybackState.STOPPED;
        }
    }
    public void resume() {
        if (state == PlaybackState.PAUSED && currentTrack != null && !engine.isPlaying()) {
            state = PlaybackState.PLAYING;
            engine.resume();
        }
    }
    public void playFromQueue() {
        Track next = queue.next();
        if (next != null) {
            play(next);
        }
    }

    public RepeatMode getRepeatMode() {
        return repeatMode;
    }
    public void setRepeatMode(RepeatMode mode) {
        this.repeatMode = mode;
        queue.setLoopQueue(mode == RepeatMode.LOOP_CURRENT_QUEUE);
    }

    public PlaybackState getState() {
        return state;
    }
    public Track getCurrentTrack() {
        return currentTrack;
    }
    public AudioEngine getEngine() {
        return engine;
    }

    public PlaybackQueue getQueue() {
        return queue;
    }
    public void setShuffle(boolean enable) {
        queue.setShuffle(enable);
    }
    public boolean isShuffle() {
        return queue.isShuffleEnabled();
    }

}

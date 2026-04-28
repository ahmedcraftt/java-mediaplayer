package infrastructure.audio;

import entities.Track;

import java.nio.file.Path;
import java.util.List;

public class AudioPlayer {
    private Track currentTrack;
    private final PlaybackQueue queue = new PlaybackQueue();
    private final AudioEngine engine = new VLCJAudioEngine();
    private PlaybackState state = PlaybackState.STOPPED;
    private RepeatMode repeatMode = RepeatMode.STOP_WHEN_QUEUE_END;


    public void play(Track track){
        if (track == null) return;
        Path path = track.getFilePath();
        currentTrack = track;
        engine.play(path,this::playNext);
        state = PlaybackState.PLAYING;
        queue.printStatus(currentTrack);
    }

    public void playNext() {
        System.out.println("playing next");
        switch (repeatMode) {
            case LOOP_CURRENT_ONE -> {
                System.out.println("looping "+ currentTrack.getMetadata().getTitle());
                if (currentTrack != null) play(currentTrack);
            }

            case PLAY_ONE ->{
                System.out.println("Stoping");
                stop();
            }

            case STOP_WHEN_QUEUE_END -> {
                Track nextTrack = queue.next();
                System.out.println("playing "+ nextTrack.getMetadata().getTitle());
                if (nextTrack != null) {
                    play(nextTrack);
                } else {
                    stop();
                }
            }

            case LOOP_CURRENT_QUEUE -> {
                Track nextTrack = queue.next();
                System.out.println("playing "+ nextTrack.getMetadata().getTitle());
                if (nextTrack == null) {
                    queue.reset();
                    nextTrack = queue.next();
                }

                if (nextTrack != null) {
                    play(nextTrack);
                } else {
                    stop(); // Only hits if the queue is physically empty
                }
            }
        }
    }


    public void enqueueAll(List<Track> tracks) {
        if (tracks == null || tracks.isEmpty()) return;
        queue.addAll(tracks);
    }

    public void clearQueue(){
        queue.clear();
    }

    public void playPrev(){
        if (currentTrack != null) {
            Track track = queue.prev();
            play(track);
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

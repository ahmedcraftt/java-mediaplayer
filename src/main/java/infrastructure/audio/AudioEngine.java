package infrastructure.audio;

import java.nio.file.Path;

public interface AudioEngine {

    void play(Path path, Runnable onTrackFinished);
    void pause();
    void stop();
    void resume();
    boolean isPlaying();
    void setVolume(int volume);
    float getProgress();
    void seek(float position);
    void release();
}

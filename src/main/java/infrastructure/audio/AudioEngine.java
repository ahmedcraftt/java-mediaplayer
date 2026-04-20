package infrastructure.audio;

public interface AudioEngine {

    void play(String path);
    void pause();
    void stop();
    void resume();
    boolean isPlaying();
    void setVolume(int volume);
    float getProgress();
    void seek(float position);
    void release();
}

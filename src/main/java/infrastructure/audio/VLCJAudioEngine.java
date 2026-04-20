package infrastructure.audio;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

public class VLCJAudioEngine implements AudioEngine {
    private final MediaPlayer mediaPlayer;
    private final MediaPlayerFactory factory;

    public VLCJAudioEngine(Runnable onTrackFinished) {

        factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newMediaPlayer();

        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                onTrackFinished.run();
            }
        });
    }

    public void play(String path) {
        mediaPlayer.media().play(path);
    }

    public void pause() {
        mediaPlayer.controls().pause();
    }

    public void stop() {
        mediaPlayer.controls().stop();
    }

    public void resume() {
        mediaPlayer.controls().play();
    }

    public boolean isPlaying(){
        return mediaPlayer.status().isPlaying();
    }

    public void setVolume(int volume) {
        mediaPlayer.audio().setVolume(volume);
    }

    public float getProgress() {
        return mediaPlayer.status().position(); // 0.0 to 1.0
    }

    public void seek(float position) {
        mediaPlayer.controls().setPosition(position); // 0.0 to 1.0
    }

    public void release() {
        mediaPlayer.release();
        factory.release();
    }
}

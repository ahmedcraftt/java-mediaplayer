package infrastructure.audio;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.nio.file.Path;

public class VLCJAudioEngine implements AudioEngine {
    private final MediaPlayer mediaPlayer;
    private final MediaPlayerFactory factory;
    private Runnable currentOnFinished;

    public VLCJAudioEngine() {
        factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newMediaPlayer();

        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                System.out.println("FINISHED EVENT FIRED");
                if (currentOnFinished != null) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(50);
                            currentOnFinished.run();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
        });

    }

    public void play(Path path, Runnable onTrackFinished) {
        this.currentOnFinished = onTrackFinished;
        mediaPlayer.media().play(path.toAbsolutePath().toString());
    }

    // Remember to release resources!
    public void dispose() {
        mediaPlayer.release();
        factory.release();
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

    public void setRepeat(boolean repeat){
        mediaPlayer.media().setRepeat(repeat);
    }
    public void release() {
        mediaPlayer.release();
        factory.release();
    }


}

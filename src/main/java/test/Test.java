package test;

import entities.Song;
import entities.Track;
import mediaLibrary.MediaLibrary;
import mediaPlaying.AudioPlayer;
import mediaPlaying.PlaybackState;
import mediaPlaying.RepeatMode;
import mediaPlaying.VlcBootstrap;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws InterruptedException {

        VlcBootstrap.init();

        AudioPlayer player = new AudioPlayer();

        Track track = new Song();
        track.setTitle("Test Song");

         track.setFilePath(Path.of("/home/Ahmed/test/test.opus"));

        System.out.println("▶ Starting playback test...");

        player.play(track);

        Thread.sleep(2000);

        if (player.getState().name().equals("PLAYING")) {
            System.out.println("✅ PASS: Player is in PLAYING state");
        } else {
            System.out.println("❌ FAIL: Player state = " + player.getState());
        }

        boolean isPlaying = player.getState() == mediaPlaying.PlaybackState.PLAYING;

        System.out.println("Engine reports playing: " + isPlaying);

        Thread.sleep(5000);

        player.stop();

        System.out.println("⏹ Stopped playback test");
    }
}
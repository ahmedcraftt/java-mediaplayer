package test;

import entities.Song;
import entities.Track;
import mediaPlaying.AudioPlayer;
import mediaPlaying.PlaybackState;
import mediaPlaying.RepeatMode;
import mediaPlaying.VlcBootstrap;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] ignoredArgs) throws InterruptedException {
        // 0. Bootstrap VLC native libraries
        VlcBootstrap.init();

        // 1. Create test tracks and audio player
        AudioPlayer player = getAudioPlayer();

        System.out.println("=== Starting Playback Test ===");
        printQueue(player);

        // 2. Play first track
        System.out.println("\n[PLAY] First track...");
        player.playFromQueue();
        verify(player, "Song A", PlaybackState.PLAYING);

        Thread.sleep(10000); // listen 10 seconds

        // 3. Pause
        System.out.println("\n[PAUSE]");
        player.pause();
        verify(player, "Song A", PlaybackState.PAUSED);

        Thread.sleep(3000); // 3 seconds pause

        // 4. Resume
        System.out.println("\n[RESUME]");
        player.resume();
        verify(player, "Song A", PlaybackState.PLAYING);

        Thread.sleep(10000); // listen 10 more seconds

        // 5. Next track
        System.out.println("\n[NEXT]");
        player.playNext();
        verify(player, "Song B", PlaybackState.PLAYING);

        Thread.sleep(10000); // listen

        // 6. Previous track
        System.out.println("\n[PREVIOUS]");
        Track prev = player.getQueue().previous();
        if(prev != null) player.play(prev);
        verify(player, "Song A", PlaybackState.PLAYING);

        // 7. Repeat single track
        System.out.println("\n[REPEAT CURRENT ONE]");
        player.setRepeatMode(RepeatMode.LOOP_CURRENT_ONE);
        player.playNext(); // should repeat current track
        verify(player, "Song A", PlaybackState.PLAYING);

        Thread.sleep(10000); // listen

        // 8. Loop queue
        System.out.println("\n[LOOP QUEUE]");
        player.setRepeatMode(RepeatMode.LOOP_CURRENT_QUEUE);
        int loopCount = 0;
        while (!player.getQueue().isEmpty() && loopCount < 10) { // still limit to avoid infinite
            player.playNext();
            System.out.println("Now playing: " + player.getCurrentTrack().getTitle());
            //noinspection BusyWait
            Thread.sleep(10000); // 10 seconds per track
            loopCount++;
        }
        System.out.println("Queue loop finished.");

        // 9. Stop
        System.out.println("\n[STOP]");
        player.stop();
        verify(player, null, PlaybackState.STOPPED);

        System.out.println("\n=== Test Finished ===");
    }

    private static void verify(AudioPlayer player, String expectedTitle, PlaybackState expectedState) {
        String currentTitle = player.getCurrentTrack() != null ? player.getCurrentTrack().getTitle() : "null";
        boolean titleOk = (expectedTitle == null && player.getCurrentTrack() == null) ||
                (expectedTitle != null && expectedTitle.equals(currentTitle));
        boolean stateOk = player.getState() == expectedState;

        System.out.println("Current track: " + currentTitle + " | Expected: " + expectedTitle + " -> " + (titleOk ? "PASS" : "FAIL"));
        System.out.println("Player state: " + player.getState() + " | Expected: " + expectedState + " -> " + (stateOk ? "PASS" : "FAIL"));
        printQueue(player);
    }

    private static void printQueue(AudioPlayer player) {
        System.out.println("Queue history: ");
        player.getQueue().getHistory().forEach(t -> System.out.print("[" + t.getTitle() + "] "));
        Track next = player.getQueue().peekNext();
        if (next != null) System.out.print(" -> NEXT: " + next.getTitle());
        System.out.println();
    }

    private static AudioPlayer getAudioPlayer() {
        List<Track> playlist = new ArrayList<>();

        Track track1 = new Song();
        track1.setTitle("Song A");
        track1.setFilePath("C:\\Users\\Ahmed\\Music\\Hamza Namira - Akeed Rag3een.m4a");
        playlist.add(track1);

        Track track2 = new Song();
        track2.setTitle("Song B");
        track2.setFilePath("C:\\Users\\Ahmed\\Music\\Hamza Namira - Ana El Tayeb.m4a");
        playlist.add(track2);

        Track track3 = new Song();
        track3.setTitle("Song C");
        track3.setFilePath("C:\\Users\\Ahmed\\Music\\Hamza Namira - Eskendereya.m4a");
        playlist.add(track3);

        AudioPlayer player = new AudioPlayer();
        player.enqueueAll(playlist);
        return player;
    }
}
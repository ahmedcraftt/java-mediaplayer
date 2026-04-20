package test;

import entities.Track;
import infrastructure.media.JaudiotaggerManger;
import infrastructure.media.MetaDataManger;
import infrastructure.media.TrackFactory;
import infrastructure.audio.AudioPlayer;
import infrastructure.audio.PlaybackState;
import infrastructure.audio.RepeatMode;
import config.VlcBootstrap;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Test {
    static AudioPlayer player = new AudioPlayer();
    static MetaDataManger manger = new JaudiotaggerManger();

    public static void main(String[] ignoredArgs) {

        VlcBootstrap.init();

        // 🔹 Build playlist
        List<Track> playlist = new ArrayList<>();

        playlist.add(makeTrack("Track 1"));
        playlist.add(makeTrack("Track 2"));
        playlist.add(makeTrack("Track 3"));

        int currentIndex = 0;
        boolean shuffle = false;
        RepeatMode repeatMode = RepeatMode.STOP_WHEN_QUEUE_END;

        Scanner sc = new Scanner(System.in);

        System.out.println("🎵 CLI Player Test Ready");
        System.out.println("Commands: play, next, prev, stop, shuffle, repeat, state, exit");

        while (true) {
            System.out.print(">> ");
            String input = sc.nextLine().trim().toLowerCase();

            switch (input) {

                case "play" -> {
                    Track track = playlist.get(currentIndex);
                    System.out.println("▶ Playing: " + track.getTitle());
                    player.play(track);
                }

                case "next" -> {
                    if (shuffle) {
                        currentIndex = (int) (Math.random() * playlist.size());
                    } else {
                        currentIndex++;
                        if (currentIndex >= playlist.size()) {
                            switch (repeatMode) {
                                case LOOP_CURRENT_QUEUE -> currentIndex = 0;
                                case STOP_WHEN_QUEUE_END -> {
                                    System.out.println("⏹ End of playlist");
                                    player.stop();
                                    continue;
                                }
                                default -> currentIndex = playlist.size() - 1;
                            }
                        }
                    }

                    System.out.println("⏭ Next: " + playlist.get(currentIndex).getTitle());
                    player.play(playlist.get(currentIndex));
                }

                case "prev" -> {
                    currentIndex--;
                    if (currentIndex < 0) currentIndex = 0;

                    System.out.println("⏮ Previous: " + playlist.get(currentIndex).getTitle());
                    player.play(playlist.get(currentIndex));
                }

                case "stop" -> {
                    player.stop();
                    System.out.println("⏹ Stopped");
                }

                case "shuffle" -> {
                    shuffle = !shuffle;
                    System.out.println("🔀 Shuffle: " + shuffle);

                    if (shuffle) {
                        Collections.shuffle(playlist);
                        currentIndex = 0;
                    }
                }

                case "repeat" -> {
                    repeatMode = cycleRepeatMode(repeatMode);
                    System.out.println("🔁 Repeat mode: " + repeatMode);
                }

                case "state" -> {
                    PlaybackState state = player.getState();
                    System.out.println("📊 State: " + state);
                }

                case "exit" -> {
                    player.stop();
                    System.out.println("👋 Bye");
                    return;
                }

                default -> System.out.println("❌ Unknown command");
            }
        }
    }

    private static Track makeTrack(String title) {

        Track t = TrackFactory.createTrack("test.opus", manger.getDuration(Path.of("/home/Ahmed/test/test.opus")));
        t.setTitle(title);
        t.setFilePath(Path.of("/home/Ahmed/test/test.opus"));
        return t;
    }

    private static RepeatMode cycleRepeatMode(RepeatMode current) {
        return switch (current) {
            case PLAY_ONE -> RepeatMode.LOOP_CURRENT_ONE;
            case LOOP_CURRENT_ONE -> RepeatMode.STOP_WHEN_QUEUE_END;
            case STOP_WHEN_QUEUE_END -> RepeatMode.LOOP_CURRENT_QUEUE;
            case LOOP_CURRENT_QUEUE -> RepeatMode.PLAY_ONE;
        };
    }
}
package cli;

import application.LibraryService;
import application.MediaService;
import application.PlayerService;
import com.ahmed.utils.InputUtils;
import entities.Track;
import mediaLibrary.Library;

import java.nio.file.Path;
import java.util.List;

public class CliApp {

    private final MediaService mediaService;
    private final PlayerService playerService;
    private final LibraryService libraryService;

    public CliApp(MediaService mediaService, PlayerService playerService, LibraryService libraryService) {
        this.mediaService = mediaService;
        this.playerService = playerService;
        this.libraryService = libraryService;
    }

    public void start() {

        initializeLibrary();

        loadLibrary();

        System.out.println("🎵 Moka Player CLI");
        System.out.println("====================");

        while (true) {
            printMenu();

            int choice = InputUtils.readInt("Choose: ");

            switch (choice) {
                case 1 -> showTracks(mediaService.getTracks());
                case 2 -> showTracks(mediaService.getSongs());
                case 3 -> playTrack();
                case 4 -> next();
                case 5 -> previous();
                case 6 -> stop();
                case 0 -> {
                    System.out.println("👋 Exiting...");
                    return;
                }
                default -> System.out.println("❌ Invalid choice");
            }
        }
    }

    private void printMenu() {
        System.out.println("""
                
                1. Load Library
                2. Show All Tracks
                3. Show Songs
                4. Play Track
                5. Next
                6. Previous
                7. Stop
                0. Exit
                """);
    }

    private void loadLibrary() {
        mediaService.loadActiveLibrary();
        System.out.println("✅ Loaded media");
        showTracks(mediaService.getTracks());
        playerService.enqueueAll(mediaService.getTracks());
    }

    private void initializeLibrary() {

        if (!libraryService.hasLibraries()) {

            System.out.println("⚠ No libraries found.");
            System.out.println("Please create your first library.");

            String path = InputUtils.readString("Enter music folder path: ");
            String name = InputUtils.readString("Library name: ");

            Library lib = new Library(name, Path.of(path), true);

            libraryService.addLibrary(lib);
            libraryService.setActiveLibrary(lib);

            mediaService.loadActiveLibrary();

            return;
        }

        if (!libraryService.hasActiveLibrary()) {
            libraryService.setActiveLibrary(
                    libraryService.getLibraries().getFirst()
            );
        }

        mediaService.loadActiveLibrary();
    }

    private void showTracks(List<Track> tracks) {
        if (tracks.isEmpty()) {
            System.out.println("⚠ No tracks found");
            return;
        }

        for (int i = 0; i < tracks.size(); i++) {
            Track t = tracks.get(i);
            System.out.printf("%d. %s (%ds)%n", i, t.getTitle(), t.getDurationInSeconds());
        }
    }

    private void playTrack() {
        List<Track> tracks = mediaService.getTracks();

        if (tracks.isEmpty()) {
            System.out.println("⚠ No tracks loaded");
            return;
        }

        showTracks(tracks);

        int index = InputUtils.readInt("Select track index: ", 0, tracks.size() - 1);

        Track selected = tracks.get(index);

        playerService.play(selected);

        System.out.println("▶ Playing: " + selected.getTitle());
    }

    private void next() {
        playerService.next();
        System.out.println("⏭ Next track "+playerService.getCurrentTrack().getTitle());
    }

    private void previous() {
        playerService.previous();
        System.out.println("⏮ Previous track "+playerService.getCurrentTrack().getTitle());
    }

    private void stop() {
        playerService.stop();
        System.out.println("⏹ Stopped");
    }
}
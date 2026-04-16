package fileManger;

import entities.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;


public class MediaScanner {
    private static final Set<String> SUPPORTED_EXTENSIONS = Set.of(
            "mp3", "flac", "wav", "m4a", "ogg","aac","opus","wma","alac","aiff","amr","mid","ra"
    );
    private final List<Track> tracks = Collections.synchronizedList(new ArrayList<>());
    private final List<Path> failedFiles = Collections.synchronizedList(new ArrayList<>());
    private final Set<String> loadedPaths = Collections.synchronizedSet(new HashSet<>());

    private Consumer<Path> onFileScanned;


    public void scanDirectory(String rootPath) {
        Path root = Path.of(rootPath);

        if (!Files.exists(root)) {
            System.err.println("Path does not exist: " + rootPath);
            return;
        }

        try (var paths = Files.walk(root)) {

            paths.filter(Files::isRegularFile)
                    .filter(this::isAudioFile)
                    .forEach(this::processFile);

        } catch (IOException e) {
            System.err.println("Failed to scan directory: " + rootPath);
            e.printStackTrace();
        }
    }
    public void setOnFileScanned(Consumer<Path> onFileScanned) {
        this.onFileScanned = onFileScanned;
    }

    public List<Track> getTracks() {
        return Collections.unmodifiableList(tracks);
    }

    public List<Path> getFailedFiles() {
        return Collections.unmodifiableList(failedFiles);
    }

    private void processFile(Path path) {
        try {
            String absolutePath = path.toAbsolutePath().toString();

            if (loadedPaths.contains(absolutePath)) return;

            Track track = createTrackFromPath(path);

            if (track == null) {
                failedFiles.add(path);
                return;
            }

            readMetaData(track);

            tracks.add(track);
            loadedPaths.add(absolutePath);

            // GUI hook
            if (onFileScanned != null) {
                onFileScanned.accept(path);
            }

        } catch (Exception e) {
            failedFiles.add(path);
            System.err.println("Failed to read: " + path);
            e.printStackTrace();
        }
    }

    private boolean isAudioFile(Path path) {
        String name = path.getFileName().toString().toLowerCase();
        int dot = name.lastIndexOf('.');
        if (dot == -1) return false;

        String ext = name.substring(dot + 1);
        return SUPPORTED_EXTENSIONS.contains(ext);
    }

    private void readMetaData (Track track){
        try {
            MetaData.readMetadata(track);
        } catch (Exception e) {
            failedFiles.add(Path.of(track.getFilePath()));
            System.err.println("Metadata read failed: " + track.getFilePath());
        }
    }
    private Track createTrackFromPath(Path path) {
        Track track = new Song();

        track.setFilePath(path.toString());
        track.setDateAdded(java.time.LocalDate.now());
        MetaData.readMetadata(track);

        if (track.getType() != null) {
            switch (track.getType()) {
                case PODCAST -> track = new Podcast();
                case AUDIOBOOK -> track = new AudioBook();
                case SONG -> track = new Song();
            }
        } else {

            String file = path.getFileName().toString().toLowerCase();
            if (file.contains("podcast")) track = new Podcast();
            else if (file.contains("audiobook") || file.contains("book")) track = new AudioBook();
        }

        track.setFilePath(path.toString());
        track.setDateAdded(java.time.LocalDate.now());

        return track;
    }
}
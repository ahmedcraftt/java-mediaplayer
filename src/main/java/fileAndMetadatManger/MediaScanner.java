package fileAndMetadatManger;

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
    private final MetaDataManger manger = new JaudiotaggerManger();

    private Consumer<Path> onFileScanned;


    public void scanDirectory(Path root) {

        if (!Files.exists(root)) {
            System.err.println("Path does not exist: " + root);
            return;
        }

        try (var paths = Files.walk(root)) {

            paths.filter(Files::isRegularFile)
                    .filter(this::isAudioFile)
                    .forEach(this::processFile);

        } catch (IOException e) {
            System.err.println("Failed to scan directory: " + root);
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
        System.out.println("processing file: "+ path.getFileName());
        try {
            String absolutePath = path.toAbsolutePath().toString();
            if (loadedPaths.contains(absolutePath)) return;

            Track track = createTrackFromPath(path);

            tracks.add(track);
            loadedPaths.add(absolutePath);

            if (onFileScanned != null) {
                onFileScanned.accept(path);
            }
        } catch (Exception e) {
            failedFiles.add(path);
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
            manger.readMetadata(track);
        } catch (Exception e) {
            failedFiles.add(Path.of(track.getFilePath().toUri()));
            System.err.println("Metadata read failed: " + track.getFilePath().toString());
        }
    }
    private Track createTrackFromPath(Path path) {
        Track tempTrack = new Song();
        tempTrack.setFilePath(path);
        manger.readMetadata(tempTrack);

        Track finalTrack;

        String fileName = path.getFileName().toString().toLowerCase();
        if (fileName.contains("podcast")||fileName.contains("episode")) finalTrack = new Podcast();
        else if (fileName.contains("audiobook") || fileName.contains("book")) finalTrack = new AudioBook();
        else finalTrack = new Song();

        finalTrack.setFilePath(path);
        finalTrack.setDateAdded(java.time.LocalDate.now());

        manger.readMetadata(finalTrack);

        return finalTrack;
    }

}
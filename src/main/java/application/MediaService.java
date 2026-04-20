package application;

import entities.Track;
import infrastructure.media.MediaScanner;
import mediaLibrary.MediaLibrary;

import java.nio.file.Path;
import java.util.List;

public class MediaService {

    private final MediaScanner scanner;
    private final MediaLibrary library;

    public MediaService(MediaScanner scanner, MediaLibrary library) {
        this.scanner = scanner;
        this.library = library;
    }

    public List<Track> loadDirectory(Path path) {
        List<Track> tracks = scanner.scan(path);
        library.addAll(tracks);
        return library.getTracks();
    }

    public List<Track> getSongs() {
        return library.getSongs();
    }

    public List<Track> getAudioBooks(){
        return library.getAudiobooks();
    }

    public List<Track> getPodcasts(){
        return library.getPodcasts();
    }

    public List<Track> getTracks() {
        return library.getTracks();
    }
}
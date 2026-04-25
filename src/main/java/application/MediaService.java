package application;

import entities.Track;
import infrastructure.media.MediaScanner;
import mediaLibrary.MediaLibrary;
import java.util.List;

public class MediaService {

    private final MediaScanner scanner;
    private final MediaLibrary library;
    private final LibraryService libraryService;

    public MediaService(MediaScanner scanner,
                        MediaLibrary library,
                        LibraryService libraryService) {
        this.scanner = scanner;
        this.library = library;
        this.libraryService = libraryService;
    }

    public List<Track> loadActiveLibrary() {
        var path = libraryService.getActiveLibrary().getRootPath();

        List<Track> tracks = scanner.scan(path);

        library.clear();
        library.addAll(tracks);

        return library.getTracks();
    }

    public LibraryService getLibraryService() {
        return libraryService;
    }

    public List<Track> getSongs() {
        return library.getSongs();
    }

    public List<Track> getAudioBooks() {
        return library.getAudiobooks();
    }

    public List<Track> getPodcasts() {
        return library.getPodcasts();
    }

    public List<Track> getTracks() {
        return library.getTracks();
    }

}
package mediaLibrary;

import entities.*;
import fileManger.MediaScanner;

import java.util.*;
import java.util.stream.Collectors;

public class MediaLibrary {

    private final Set<String> loadedPaths = new HashSet<>();
    private final List<Track> tracks = new ArrayList<>();
    private final MediaScanner scanner = new MediaScanner();

    public void loadFromDirectory(String path) {
        scanner.scanDirectory(path);

        for (Track track : scanner.getTracks()) {
            String filePath = track.getFilePath();

            if (loadedPaths.add(filePath)) {
                tracks.add(track);
            }
        }
    }
    // Load tracks into library
    public void addAll(Collection<Track> newTracks) {
        tracks.addAll(newTracks);
    }

    public void addTrack(Track track) {
        tracks.add(track);
    }

    public List<Track> getAllTracks() {
        return Collections.unmodifiableList(tracks);
    }
    public List<Song> getSongs() {
        return tracks.stream()
                .filter(t -> t instanceof Song)
                .map(t -> (Song) t)
                .collect(Collectors.toList());
    }

    public List<Podcast> getPodcasts() {
        return tracks.stream()
                .filter(t -> t instanceof Podcast)
                .map(t -> (Podcast) t)
                .collect(Collectors.toList());
    }

    public List<AudioBook> getAudiobooks() {
        return tracks.stream()
                .filter(t -> t instanceof AudioBook)
                .map(t -> (AudioBook) t)
                .collect(Collectors.toList());
    }
    public List<Track> search(String query) {
        String q = query.toLowerCase();

        return tracks.stream()
                .filter(t ->
                        safe(t.getTitle()).contains(q) ||
                                safe(t.getGenre()).contains(q) ||
                                (t instanceof Song s && safe(s.getArtist()).contains(q)) ||
                                (t instanceof Podcast p && safe(p.getHost()).contains(q)) ||
                                (t instanceof AudioBook a && safe(a.getAuthor()).contains(q))
                )
                .collect(Collectors.toList());
    }
    public List<Track> sortByTitle() {
        return tracks.stream()
                .sorted(Comparator.comparing(t -> safe(t.getTitle())))
                .collect(Collectors.toList());
    }

    public List<Track> sortByDuration() {
        return tracks.stream()
                .sorted(Comparator.comparingLong(Track::getDurationInSeconds))
                .collect(Collectors.toList());
    }
    public List<Track> getShuffled() {
        List<Track> shuffled = new ArrayList<>(tracks);
        Collections.shuffle(shuffled);
        return shuffled;
    }

    private String safe(String s) {
        return s == null ? "" : s.toLowerCase();
    }

}
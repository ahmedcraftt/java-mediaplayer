package mediaLibrary;

import entities.Track;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class MediaLibrary {

    private final Map<String, Track> tracks = new HashMap<>();

    public void addAll(List<Track> newTracks) {
        for (Track t : newTracks) {
            tracks.putIfAbsent(t.getFilePath().toString(), t);
        }
    }

    public void addTrack(Track track) {
        tracks.putIfAbsent(track.getFilePath().toString(), track);
    }

    public List<Track> getTracks() {
        return new ArrayList<>(tracks.values());
    }

    public void removeTrack(Track track) {
        tracks.remove(track.getFilePath().toString());
    }

    public void clear() {
        tracks.clear();
    }

    // ===== QUERY LAYER =====

    public List<Track> getSongs() {
        return filterByType("SONG");
    }

    public List<Track> getPodcasts() {
        return filterByType("PODCAST");
    }

    public List<Track> getAudiobooks() {
        return filterByType("AUDIOBOOK");
    }

    private List<Track> filterByType(String type) {
        return tracks.values().stream()
                .filter(t -> t.getType().name().equals(type))
                .collect(Collectors.toList());
    }

    public List<Track> search(String query) {
        String q = query.toLowerCase();

        return tracks.values().stream()
                .filter(t ->
                        safe(t.getTitle()).contains(q) ||
                                safe(t.getGenre()).contains(q) ||
                                safe(t.getFileName()).contains(q)
                )
                .collect(Collectors.toList());
    }

    public List<Track> sortByTitle() {
        return sort(Comparator.comparing(t -> safe(t.getTitle())));
    }

    public List<Track> sortByDuration() {
        return sort(Comparator.comparingInt(Track::getDurationInSeconds));
    }

    private List<Track> sort(Comparator<Track> comparator) {
        return tracks.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private String safe(String s) {
        return s == null ? "" : s.toLowerCase();
    }
}
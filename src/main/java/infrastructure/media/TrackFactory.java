package infrastructure.media;

import entities.AudioBook;
import entities.Podcast;
import entities.Song;
import entities.Track;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class TrackFactory {

    private static final Set<String> SONG_KEYWORDS = new HashSet<>(Set.of("feat","ft","remix","official", "infrastructure/audio","song","acoustic","cover","music","album"));
    private static final Set<String> PODCAST_KEYWORDS = Set.of("episode","ep","podcast","show","season");
    private static final Set<String> ABOOK_KEYWORDS = Set.of("chapter","part","volume","book","unabridged","narrated by","authored by");

    private static final Pattern SONG_PATTERN = Pattern.compile(".*(feat\\.?|ft\\.?|remix|official\\s+audio|lyrics?).*");
    private static final Pattern PODCAST_PATTERN = Pattern.compile(".*(s\\d+e\\d+|episode\\s*\\d+|ep\\s*\\d+).*");
    private static final Pattern AUDIOBOOK_PATTERN = Pattern.compile(".*(chapter\\s*\\d+|part\\s*\\d+|volume\\s*\\d+|book\\s*\\d+).*");

    public static Track createTrack(String filename, int durationSeconds){
        int songScore = 0;
        int abookScore = 0;
        int podcastScore = 0;
        if (durationSeconds< 600){
            songScore +=3;
        }else if (durationSeconds < 10800) {
            podcastScore +=3;
        } else if (durationSeconds > 10800) {
            abookScore +=3;
        }

        Set<String> tokens = getTokens(filename);

        for (String t : tokens){
            if (SONG_KEYWORDS.contains(t)) songScore +=2;
            if (PODCAST_KEYWORDS.contains(t)) podcastScore +=2;
            if (ABOOK_KEYWORDS.contains(t)) abookScore +=2;
        }

        if (SONG_PATTERN.matcher(filename).matches()) songScore += 3;
        if (PODCAST_PATTERN.matcher(filename).matches()) podcastScore += 3;
        if (AUDIOBOOK_PATTERN.matcher(filename).matches()) abookScore += 3;

        if (songScore > podcastScore && songScore > abookScore) return new Song(filename,durationSeconds);
        if (podcastScore > songScore && podcastScore > abookScore) return new Podcast(filename,durationSeconds);
        if (abookScore > songScore && abookScore > podcastScore) return new AudioBook(filename,durationSeconds);

        return new Song(filename,durationSeconds);
    }

    private static String normalize(String filename) {
        return filename
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", " "); // remove symbols
    }

    private static Set<String> getTokens(String filename) {
        return new HashSet<>(Arrays.asList(
                normalize(filename).split("\\s+")
        ));
    }
}

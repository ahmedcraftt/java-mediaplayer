package entities;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class Song extends Track{
   private String artist;
   private String album;
   private String lyrics;

    public Song(String fileName, int duration , Path filePath) {
        super(fileName,duration,filePath);
        setType(MediaType.SONG);
    }

    @NotNull
    @Override
    public String getArtist() {
        return artist != null ? artist : "Unknown";
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }
}

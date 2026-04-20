package entities;

public class Song extends Track{
   private String artist;
   private String album;
   private String lyrics;

    public Song(String fileName, int duration) {
        super(fileName,duration);
        setType(MediaType.SONG);
    }

    @Override
    public String getArtist() {
        return artist;
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

package entities;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.time.LocalDate;

public abstract class Track {

   private MediaType type;
   private LocalDate dateCreated;
   private LocalDate dateAdded;
   private LocalDate dateModified;
   protected String genre;
   protected Path filePath;
   protected String title;
   protected String fileName;
   protected boolean favorite;
   protected int durationInSeconds;
   protected byte[] coverArt;
   protected String year;
   protected long fileSize;
   protected long bitrate;
   protected int sampleRate;
   protected String description;

   protected Track() {}

    protected Track(String fileName, int durationInSeconds) {
        this.fileName=fileName;
        this.durationInSeconds=durationInSeconds;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public byte[] getCoverArt() {
        return coverArt;
    }

    public void setCoverArt(byte[] coverArt) {
        this.coverArt = coverArt;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getBitrate() {
        return bitrate;
    }

    public void setBitrate(long bitrate) {
        this.bitrate = bitrate;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Path getFilePath() {
        return filePath;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public @NotNull String getArtist() {
      return "";
    }
}

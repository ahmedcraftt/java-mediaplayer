package entities;

public class AudioBook extends Track {

    private String author;
    private String narrator;
    private String series;
    private int chapterCount;

    public AudioBook(String fileName, int duration) {
        super(fileName,duration);
        setType(MediaType.AUDIOBOOK);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNarrator() {
        return narrator;
    }

    public void setNarrator(String narrator) {
        this.narrator = narrator;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public int getChapterCount() {
        return chapterCount;
    }

    public void setChapterCount(int chapterCount) {
        this.chapterCount = chapterCount;
    }

}

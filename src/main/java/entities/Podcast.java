package entities;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class Podcast extends Track {
    private int episodeNumber;
    private String channel;
    private String host;


    public Podcast(String fileName, int duration, Path filePath) {
        super(fileName,duration,filePath);
        setType(MediaType.PODCAST);

    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getHost() {
        return host;
    }

    @NotNull
    public String getArtist() {
        return host != null ? host : "Unknown";
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }
}

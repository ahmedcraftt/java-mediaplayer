package entities;

public class Podcast extends Track {
    private int episodeNumber;
    private String channel;
    private String host;


    public Podcast(String fileName, int duration) {
        super(fileName,duration);
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

    public String getArtist(){
        return getHost();
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

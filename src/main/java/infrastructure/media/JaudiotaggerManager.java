package infrastructure.media;

import entities.AudioBook;
import entities.Podcast;
import entities.Song;
import entities.Track;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.ZoneId;

public class JaudiotaggerManager implements MetaDataManager {

    public void writeMetaData(Track track){
        try {
            File file = new File(track.getFilePath().toUri());
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTagOrCreateAndSetDefault();

            safeSet(tag,FieldKey.TITLE, track.getTitle());
            safeSet(tag,FieldKey.GENRE,track.getGenre());
            safeSet(tag,FieldKey.YEAR,track.getYear());
            safeSet(tag,FieldKey.COMMENT,track.getDescription());
            if (track instanceof Song song){
                safeSet(tag,FieldKey.ARTIST,song.getArtist());
                safeSet(tag,FieldKey.ALBUM,song.getAlbum());
                safeSet(tag,FieldKey.LYRICS,song.getLyrics());
            }
            if (track instanceof Podcast podcast){
                safeSet(tag,FieldKey.ARTIST,podcast.getArtist());
                safeSet(tag,FieldKey.ALBUM,podcast.getChannel());
                safeSet(tag,FieldKey.TRACK, String.valueOf(podcast.getEpisodeNumber()));
            }
            if (track instanceof AudioBook book){
                safeSet(tag,FieldKey.ARTIST,book.getNarrator());
                safeSet(tag,FieldKey.ALBUM,book.getSeries());
                safeSet(tag,FieldKey.ALBUM_ARTIST, book.getAuthor());
            }
            audioFile.commit();
        } catch (Exception e){
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void readMetadata(Track track) {

        try {

            File file = new File(track.getFilePath().toUri());
            Path path = Path.of(track.getFilePath().toUri());
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);

            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();
            AudioHeader header = audioFile.getAudioHeader();

            if(tag != null) {

                String title = tag.getFirst(FieldKey.TITLE);
                if(title == null || title.isBlank() || title.equalsIgnoreCase("unknown")) {
                    title = file.getName();
                }
                track.setTitle(title.trim());
                track.setGenre(tag.getFirst(FieldKey.GENRE));
                track.setYear(tag.getFirst(FieldKey.YEAR));
                track.setDurationInSeconds(header.getTrackLength());

                Integer br = null;
                try {
                    br = Math.toIntExact(header.getBitRateAsNumber());
                } catch (Exception ignored) {}

                if (br != null) {
                    track.setBitrate(br);
                }

                int sr = header.getSampleRateAsNumber();
                track.setSampleRate(sr);

                org.jaudiotagger.tag.images.Artwork artwork = tag.getFirstArtwork();
                if (artwork != null && artwork.getBinaryData() != null) {
                    track.setCoverArt(artwork.getBinaryData());
                }
                track.setFileSize(attributes.size());
                track.setSampleRate(header.getSampleRateAsNumber());
                track.setDateCreated(toLocalDate(attributes.creationTime()));
                track.setDateModified(toLocalDate(attributes.lastModifiedTime()));
                track.setDescription(tag.getFirst(FieldKey.COMMENT));
                if (track instanceof Song song){
                    song.setArtist(tag.getFirst(FieldKey.ARTIST));
                    song.setAlbum(tag.getFirst(FieldKey.ALBUM));
                    song.setLyrics(tag.getFirst(FieldKey.LYRICS));
                }
                if (track instanceof Podcast podcast){
                    podcast.setChannel(tag.getFirst(FieldKey.ALBUM));
                    podcast.setHost(tag.getFirst(FieldKey.ARTIST));
                    podcast.setEpisodeNumber(safeParseInt(tag.getFirst(FieldKey.TRACK)));
                }
                if (track instanceof AudioBook book){
                    book.setAuthor(tag.getFirst(FieldKey.ALBUM_ARTIST));
                    book.setNarrator(tag.getFirst(FieldKey.ARTIST));
                    book.setSeries(tag.getFirst(FieldKey.ALBUM));
                    book.setChapterCount(safeParseInt(tag.getFirst(FieldKey.TRACK)));
                }
            }

        } catch(Exception e) {
            System.err.println("Metadata read failed for: " + track.getFilePath());
            e.printStackTrace();
        }

    }

    public int getDuration(Path path) {
        AudioFile audioFile = createAudioFile(path);
        AudioHeader header = audioFile.getAudioHeader();
        return header.getTrackLength();
    }

    public String getGenre(Path path){
       AudioFile audioFile = createAudioFile(path);
       Tag tag = audioFile.getTagOrCreateAndSetDefault();
       return tag.getFirst(FieldKey.GENRE);
    }

    public String getTitle(Path path){
        AudioFile audioFile = createAudioFile(path);
        Tag tag = audioFile.getTagOrCreateAndSetDefault();
        return tag.getFirst(FieldKey.TITLE);
    }

    public String getTrackNumber (Path path){
        AudioFile audioFile = createAudioFile(path);
        Tag tag = audioFile.getTagOrCreateAndSetDefault();
        return tag.getFirst(FieldKey.TRACK);
    }

    private AudioFile createAudioFile(Path path){
        File file = new File(path.toUri());
        AudioFile audioFile;
        try {
            audioFile = AudioFileIO.read(file);
            return audioFile;
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            throw new RuntimeException(e);
        }
    }

    private LocalDate toLocalDate(FileTime fileTime){
        return fileTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private int safeParseInt(String value) {
        try {
            return (value == null || value.isBlank()) ? 0 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void safeSet(Tag tag, FieldKey key, String value) throws Exception {
        if (value != null && !value.isBlank()) {
            tag.setField(key, value);
        }
    }

}

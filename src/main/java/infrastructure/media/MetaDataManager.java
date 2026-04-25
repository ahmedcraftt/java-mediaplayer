package infrastructure.media;

import entities.Track;

import java.nio.file.Path;

public interface MetaDataManager {
    void writeMetaData(Track track);
    void readMetadata(Track track);
    int getDuration(Path path);
    String getGenre(Path path);
    String getTitle(Path path);
   String getTrackNumber (Path path);
}

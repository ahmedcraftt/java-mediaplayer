package fileAndMetadatManger;

import entities.Track;

public interface MetaDataManger {
    void writeMetaData(Track track);
    void readMetadata(Track track);
}

package infrastructure.media;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Playlist;
import entities.PlaylistDTO;
import entities.Track;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class PlaylistStorage {

    private static final MetaDataManager manger = new JaudiotaggerManager();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static Path base = Path.of(System.getProperty("user.home"), ".moka_music_player", "playlists");

    public static void save(Playlist playlist) throws IOException {

        PlaylistDTO dto = new PlaylistDTO();
        dto.title = playlist.getTitle();
        dto.favorite = playlist.isFavorite();

        dto.trackPaths = playlist.getTracks()
                .stream()
                .map(t -> t.getFilePath().toString())
                .collect(Collectors.toList());

        Files.createDirectories(base.getParent());
        Files.writeString(base, gson.toJson(dto));
    }
    public static Playlist load() throws IOException {

        String json = Files.readString(base);
        PlaylistDTO dto = gson.fromJson(json, PlaylistDTO.class);

        Playlist playlist = new Playlist(dto.title, dto.favorite);

        for (String pathStr : dto.trackPaths) {
            Path path = Path.of(pathStr);
            String filename = path.getFileName().toString();
            Track t = TrackFactory.createTrack(filename,manger.getDuration(path));
            t.setFilePath(path);

            playlist.addTrack(t);
        }

        return playlist;
    }
}
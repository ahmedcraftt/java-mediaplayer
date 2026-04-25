package cli;

import application.LibraryService;
import application.MediaService;
import application.PlayerService;
import infrastructure.audio.AudioPlayer;
import infrastructure.media.JaudiotaggerManager;
import infrastructure.media.MediaScanner;
import infrastructure.media.MetaDataManager;
import mediaLibrary.MediaLibrary;

public class MainCli {

    public static void main(String[] args) {

        AudioPlayer player = new AudioPlayer();
        PlayerService playerService = new PlayerService(player);

        MetaDataManager metaDataManager = new JaudiotaggerManager();
        MediaScanner scanner = new MediaScanner(metaDataManager);
        MediaLibrary library = new MediaLibrary();
        LibraryService libraryService = new LibraryService();
        MediaService mediaService = new MediaService(scanner, library,libraryService);

        CliApp app = new CliApp(mediaService, playerService, libraryService);
        app.start();
    }
}
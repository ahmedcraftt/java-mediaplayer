package gui.main;

import application.LibraryService;
import application.MediaService;
import application.PlayerService;
import infrastructure.audio.AudioPlayer;
import infrastructure.media.JaudiotaggerManager;
import infrastructure.media.MediaScanner;
import infrastructure.media.MetaDataManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mediaLibrary.MediaLibrary;
import gui.controllers.MainViewController;


import java.io.IOException;

public class MainApplication extends Application {
    private final AudioPlayer player = new AudioPlayer();
    private final MetaDataManager metaDataManager = new JaudiotaggerManager();
    private final MediaScanner scanner = new MediaScanner(metaDataManager);
    private final MediaLibrary library = new MediaLibrary();
    private final PlayerService playerService = new PlayerService(player);
    private final LibraryService libraryService = new LibraryService();
    private final MediaService mediaService = new MediaService(scanner,library,libraryService);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/mainView.fxml"));
        Parent root = loader.load();
        MainViewController controller = loader.getController();
        controller.setMediaService(mediaService);
        controller.setPlayerService(playerService);
        Scene scene = new Scene(root,1000,700);

        stage.setTitle("Moka Player ☕");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setOnCloseRequest(_ -> System.out.println("Closing app..."));
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

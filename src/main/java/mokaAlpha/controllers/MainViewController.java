package mokaAlpha.controllers;

import entities.Track;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import mediaLibrary.MediaLibrary;
import mediaPlaying.AudioPlayer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;


import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MainViewController {
    @FXML private Button btnTracks;
    @FXML private Button btnSongs ;
    @FXML private Button btnBooks ;
    @FXML private Button btnPodcasts ;
    @FXML private Button btnPlay ;
    @FXML private Button btnNext ;
    @FXML private Button btnPrev ;
    @FXML private Button btnPlaylist ;
    @FXML private Button btnCurrentTrack ;
    @FXML private Button btnQueue ;
    @FXML private Button btnRepeatAndStop ;
    @FXML private AnchorPane contentArea ;

    private final MediaLibrary library = new MediaLibrary();
    private AudioPlayer player ;
    private MediaListViewController controller;

    private volatile boolean loaded = false;

    public void setPlayer(AudioPlayer player) {
        this.player = player;
    }

    @FXML
    private void initialize() {

        btnSongs.setDisable(true);
        btnBooks.setDisable(true);
        btnPodcasts.setDisable(true);
        btnPlaylist.setDisable(true);

        Task<Void> task = getVoidTask();
        btnTracks.setOnAction(e ->
                switchView(new ArrayList<>(library.getTracks()),ViewMode.TRACKS));

        btnSongs.setOnAction(e ->
                switchView(new ArrayList<>(library.getSongs()), ViewMode.SONGS));

        btnBooks.setOnAction(e ->
                switchView(new ArrayList<>(library.getAudiobooks()), ViewMode.BOOKS));

        btnPodcasts.setOnAction(e ->
                switchView(new ArrayList<>(library.getPodcasts()), ViewMode.PODCASTS));

        btnPlaylist.setOnAction(e -> loadPlaylistView());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @NotNull
    private Task<Void> getVoidTask() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                library.loadFromDirectory(Path.of("/home/Ahmed/test"));
                return null;
            }
        };

        task.setOnSucceeded(e -> {

            btnTracks.setDisable(false);
            btnSongs.setDisable(false);
            btnBooks.setDisable(false);
            btnPodcasts.setDisable(false);
            btnPlaylist.setDisable(false);

            loadMediaView(
                    new ArrayList<>(library.getSongs()),
                    ViewMode.SONGS
            );
        });
        return task;
    }

    private void switchView(List<Track> tracks, ViewMode mode) {
        if (controller == null) {
            loadMediaView(tracks, mode);
            return;
        }

        controller.setData(tracks);
        controller.setMode(mode);
    }
    private void loadMediaView(List<Track> tracks, ViewMode mode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mediaListView.fxml"));

            Parent view = loader.load();

            controller = loader.getController();

            if (player != null) {
                controller.setAudioPlayer(player);
            }

            controller.setData(tracks);
            controller.setMode(mode);

            contentArea.getChildren().setAll(view);

            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

        } catch (IOException e) {
            System.err.println("CRITICAL: Could not find or load mediaListView.fxml");
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.err.println("FXML Error: Check if fx:controller is set correctly in mediaListView.fxml");
            e.printStackTrace();
        }
    }



    private void loadPlaylistView() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/playlistView.fxml")
            );

            Parent view = loader.load();

            PlaylistViewController playlistController = loader.getController();

            if (player != null) {
                playlistController.setPlayer(player);
            }

            contentArea.getChildren().setAll(view);

            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

            this.controller = null;

        } catch (IOException e) {
            System.err.println("Failed to load playlistView.fxml");
            e.printStackTrace();
        }
    }
}

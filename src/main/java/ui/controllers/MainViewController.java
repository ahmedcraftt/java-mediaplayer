package ui.controllers;

import application.MediaService;
import entities.Track;

import infrastructure.audio.AudioPlayer;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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
    @FXML private Button btnArtists;
    @FXML private Button btnGenres;

    @FXML private AnchorPane contentArea ;

    private MediaService mediaService ;
    private AudioPlayer player;
    private MediaListViewController controller;

    public void setPlayer(AudioPlayer player) {
        this.player = player;
    }

    public void setMediaService(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @FXML
    private void initialize() {

        setButtonsEnabled(true);

        Task<Void> task = getVoidTask();
        btnTracks.setOnAction(e ->
                switchView(new ArrayList<>(mediaService.getTracks()),ViewMode.TRACKS));

        btnSongs.setOnAction(e ->
                switchView(new ArrayList<>(mediaService.getSongs()), ViewMode.SONGS));

        btnBooks.setOnAction(e ->
                switchView(new ArrayList<>(mediaService.getAudioBooks()), ViewMode.BOOKS));

        btnPodcasts.setOnAction(e ->
                switchView(new ArrayList<>(mediaService.getPodcasts()), ViewMode.PODCASTS));

        btnPlaylist.setOnAction(e -> loadPlaylistView());

        btnArtists.setOnAction(event -> loadCategoryView());

        btnGenres.setOnAction(event -> loadCategoryView());


        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @NotNull
    private Task<Void> getVoidTask() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                mediaService.loadActiveLibrary();
                return null;
            }
        };

        task.setOnSucceeded(e -> {

            setButtonsEnabled(false);

            loadMediaView(
                    new ArrayList<>(mediaService.getSongs()),
                    ViewMode.SONGS
            );
        });
        return task;
    }

    private void setButtonsEnabled(boolean enabled) {
        btnTracks.setDisable(enabled);
        btnSongs.setDisable(enabled);
        btnBooks.setDisable(enabled);
        btnPodcasts.setDisable(enabled);
        btnPlaylist.setDisable(enabled);
    }

    private void switchView(List<Track> tracks, ViewMode mode) {
        loadMediaView(tracks,mode);
        if (controller == null) {
            loadMediaView(tracks, mode);
            return;
        }

        controller.setData(tracks);
        controller.setMode(mode);
    }

    private void loadMediaView(List<Track> tracks, ViewMode mode) {
        try {
            FXMLLoader loader = loadView("/mediaListView.fxml");

            controller = loader.getController();

            if (player != null) {
                controller.setPlayer(player);
            }

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
            FXMLLoader loader = loadView("/playlistView.fxml");

            PlaylistViewController playlistController = loader.getController();

            if (player != null) {
                playlistController.setPlayer(player);
            }

        } catch (IOException e) {
            System.err.println("Failed to load playlistView.fxml");
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.err.println("FXML Error: Check if fx:controller is set correctly in playlistView.fxml");
            e.printStackTrace();
        }
    }

    private void loadCategoryView() {
        try {
            FXMLLoader loader = loadView("/categoryView.fxml");

            CategoryViewController categoryViewController = loader.getController();

            if (player != null) {
                categoryViewController.setPlayer(player);
            }
        } catch (IOException e) {
            System.err.println("CRITICAL: Could not find or load categoryView.fxml");
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.err.println("FXML Error: Check if fx:controller is set correctly in categoryView.fxml");
            e.printStackTrace();
        }
    }

    private FXMLLoader loadView(String resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(resource)
        );

        Parent view = loader.load();

        contentArea.getChildren().setAll(view);

        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);

        return loader;
    }
}

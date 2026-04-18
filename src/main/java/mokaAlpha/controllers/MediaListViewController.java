package mokaAlpha.controllers;

import entities.Track;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import mediaPlaying.AudioPlayer;

import java.util.Comparator;
import java.util.List;

public class MediaListViewController {

    @FXML private ListView<Track> listView;
    @FXML private TextField searchBar;
    @FXML private Button btnListPlay;
    @FXML private Button btnSort;
    @FXML private Button btnRefresh;

    private List<Track> currentData;
    private AudioPlayer player;
    private ViewMode currentMode;

    public void setAudioPlayer(AudioPlayer player) {
        this.player = player;
    }

    public void setData(List<Track> tracks) {
        this.currentData = tracks;
        listView.getItems().setAll(tracks);
    }

    public void setMode(ViewMode mode) {
        this.currentMode = mode;

        btnListPlay.setText(
                switch (mode) {
                    case BOOKS -> "Read";
                    case PODCASTS -> "Play Episode";
                    case PLAYLISTS -> "Open";
                    default -> "Play";
                }
        );
    }

    @FXML
    private void initialize() {
        setupListView();
        setupSearch();
        setupPlay();
        setupSort();
        setupRefresh();
    }

    private void setupListView() {
        listView.setCellFactory(lv -> new MyListCell());
    }

    public ViewMode getCurrentMode() {
        return currentMode;
    }

    private static class MyListCell extends ListCell<Track> {
        @Override
        protected void updateItem(Track item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.getTitle()); // upgrade later with artist etc.
            }
        }
    }

    private void setupSearch() {
        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isBlank()) {
                listView.getItems().setAll(currentData);
            } else {
                String q = newVal.toLowerCase();

                listView.getItems().setAll(
                        currentData.stream()
                                .filter(t ->
                                        safe(t.getTitle()).contains(q) ||
                                                safe(t.getGenre()).contains(q)
                                )
                                .toList()
                );
            }
        });
    }

    private void setupPlay() {
        btnListPlay.setOnAction(e -> {
            Track selected = listView.getSelectionModel().getSelectedItem();

            if (selected != null && player != null) {
                player.play(selected);
            }
        });
    }

    private void setupSort() {
        btnSort.setOnAction(e -> {
            listView.getItems().setAll(
                    listView.getItems().stream()
                            .sorted(Comparator.comparing(t -> safe(t.getTitle())))
                            .toList()
            );
        });
    }

    private void setupRefresh() {
        btnRefresh.setOnAction(e -> {
            listView.getItems().setAll(currentData);
        });
    }

    private String safe(String s) {
        return s == null ? "" : s.toLowerCase();
    }
}
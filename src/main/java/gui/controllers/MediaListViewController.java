package gui.controllers;

import application.PlayerService;
import entities.Track;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MediaListViewController {

    @FXML private ListView<Track> listView;
    @FXML private TextField searchBar;
    @FXML private Button btnListPlay;
    @FXML private MenuButton btnSort;
    @FXML private Button btnRefresh;

    private List<Track> currentData;
    private PlayerService playerService;

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public void setData(List<Track> tracks) {
        this.currentData = tracks;
        listView.getItems().setAll(tracks);
    }

    public void setMode(ViewMode mode) {

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

    private void sort(SortByModes mode) {
        List<Track> items = new ArrayList<>(listView.getItems());

        switch (mode) {
            case TITLE -> items.sort(Comparator.comparing(Track::getTitle, String.CASE_INSENSITIVE_ORDER));
            case FILE_NAME -> items.sort(Comparator.comparing(t -> t.getFilePath().getFileName().toString()));
            case ARTISTS -> items.sort(Comparator.comparing(Track::getArtist, String.CASE_INSENSITIVE_ORDER));
            case DURATION -> items.sort(Comparator.comparingInt(Track::getDurationInSeconds));
            case YEAR -> items.sort(Comparator.comparing(Track::getYear));
            case DATE_ADDED -> items.sort(Comparator.comparing(Track::getDateCreated));
            case DATE_MODIFIED -> items.sort(Comparator.comparing(t -> t.getFilePath().toFile().lastModified()));
        }

        listView.getItems().setAll(items);
    }

    @FXML private void sortByTitle() { sort(SortByModes.TITLE); }

    @FXML private void sortByFileName() { sort(SortByModes.FILE_NAME); }

    @FXML private void sortByArtists() { sort(SortByModes.ARTISTS); }

    @FXML private void sortByDuration() { sort(SortByModes.DURATION); }

    @FXML private void sortByYear() { sort(SortByModes.YEAR); }

    @FXML private void sortByDateAdded() { sort(SortByModes.DATE_ADDED); }

    @FXML private void sortByDateModified() { sort(SortByModes.DATE_MODIFIED); }

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

            if (selected != null && playerService != null) {
                playerService.play(selected);
            }
        });
    }

    private void setupSort() {
        btnSort.setOnAction(e -> listView.getItems().setAll(
                listView.getItems().stream()
                        .sorted(Comparator.comparing(t -> safe(t.getTitle())))
                        .toList()
        ));
    }

    private void setupRefresh() {
        btnRefresh.setOnAction(e -> listView.getItems().setAll(currentData));
    }

    private String safe(String s) {
        return s == null ? "" : s.toLowerCase();
    }
}
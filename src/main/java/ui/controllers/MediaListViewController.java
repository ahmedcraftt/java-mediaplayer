package ui.controllers;

import application.PlayerService;
import entities.Track;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MediaListViewController {

    @FXML private ListView<Track> contentList;
    @FXML private TextField searchBar;
    @FXML private Button btnListPlay;
    @FXML private MenuButton btnSort;
    @FXML private Button btnRefresh;

    @FXML private Label lblTitle;
    @FXML private Label lblArtist;
    @FXML private Label lblGenre;
    @FXML private Label lblDuration;
    @FXML private Label lblYear;
    @FXML private Label lblBitrate;
    @FXML private Label lblSampleRate;
    @FXML private Label lblFilePath;

    private List<Track> currentData;
    private PlayerService playerService;

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public void setData(List<Track> tracks) {
        this.currentData = tracks;
        contentList.getItems().setAll(tracks);
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
        setupRefresh();
        setupSelection();
    }

    private void setupListView() {
        contentList.setCellFactory(lv -> new MyListCell());
    }

    private void setupSelection() {
        contentList.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> {
                    if (selected != null) {
                        showMetadata(selected);
                    } else {
                        clearMetadata();
                    }
                }
        );
    }

    private void showMetadata(Track t) {
        lblTitle.setText("Title: " + safeText(t.getTitle()));
        lblArtist.setText("Artist: " + safeText(t.getArtist()));
        lblGenre.setText("Genre: " + safeText(t.getGenre()));

        lblDuration.setText("Duration: " + formatDuration(t.getDurationInSeconds()));
        lblYear.setText("Year: " + safeText(t.getYear()));

        lblBitrate.setText("Bitrate: " + t.getBitrate());
        lblSampleRate.setText("Sample Rate: " + t.getSampleRate());

        lblFilePath.setText("Path: " + t.getFilePath());
    }

    private void clearMetadata() {
        lblTitle.setText("");
        lblArtist.setText("");
        lblGenre.setText("");
        lblDuration.setText("");
        lblYear.setText("");
        lblBitrate.setText("");
        lblSampleRate.setText("");
        lblFilePath.setText("");
    }

    private void sort(SortByModes mode) {
        List<Track> items = new ArrayList<>(contentList.getItems());

        switch (mode) {
            case TITLE -> items.sort(Comparator.comparing(Track::getTitle, String.CASE_INSENSITIVE_ORDER));
            case FILE_NAME -> items.sort(Comparator.comparing(t -> t.getFilePath().getFileName().toString()));
            case ARTISTS -> items.sort(Comparator.comparing(Track::getArtist, String.CASE_INSENSITIVE_ORDER));
            case DURATION -> items.sort(Comparator.comparingInt(Track::getDurationInSeconds));
            case YEAR -> items.sort(Comparator.comparing(Track::getYear));
            case DATE_ADDED -> items.sort(Comparator.comparing(Track::getDateCreated));
            case DATE_MODIFIED -> items.sort(Comparator.comparing(t -> t.getFilePath().toFile().lastModified()));
        }

        contentList.getItems().setAll(items);
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
                setText(item.getTitle());
            }
        }
    }

    private void setupSearch() {
        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isBlank()) {
                contentList.getItems().setAll(currentData);
            } else {
                String q = newVal.toLowerCase();

                contentList.getItems().setAll(
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
            Track selected = contentList.getSelectionModel().getSelectedItem();

            if (selected != null && playerService != null) {
                playerService.play(selected);
            }
        });
    }


    private void setupRefresh() {
        btnRefresh.setOnAction(e -> contentList.getItems().setAll(currentData));
    }

    private String safe(String s) {
        return s == null ? "" : s.toLowerCase();
    }

    private String formatDuration(int sec) {
        int min = sec / 60;
        int s = sec % 60;
        return String.format("%d:%02d", min, s);
    }

    private String safeText(String value) {
        return (value == null || value.trim().isEmpty()) ? "—" : value.trim();
    }
}
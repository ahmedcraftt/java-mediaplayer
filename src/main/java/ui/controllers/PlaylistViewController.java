package ui.controllers;

import application.PlayerService;
import entities.Track;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class PlaylistViewController {
    @FXML private ListView<Track> listView;
    @FXML private TextField searchBar;

    private PlayerService playerService;

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

}

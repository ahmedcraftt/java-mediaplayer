package mokaAlpha.controllers;

import entities.Track;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import mediaPlaying.AudioPlayer;

import java.io.IOException;

public class PlaylistViewController {
    @FXML
    private ListView<Track> listView;
    @FXML private TextField searchBar;

    private AudioPlayer player ;

    public void setPlayer(AudioPlayer player) {
        this.player = player;
    }

}

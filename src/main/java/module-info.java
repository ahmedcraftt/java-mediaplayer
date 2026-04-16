module org.example.moka_music_player {
    requires javafx.controls;
    requires javafx.fxml;

    requires vlcj;
    requires jaudiotagger;

    exports mokaAlpha.GUI;

    opens mokaAlpha.GUI to javafx.fxml;
}
module org.example.moka_music_player {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    // Explicitly require JNA for native library access
    requires vlcj;
    requires com.sun.jna;
    requires com.sun.jna.platform;

    requires jaudiotagger;
    requires annotations;
    requires com.google.gson;

    // Export packages that need to be accessed by other modules or the JVM
    exports mokaAlpha.application;
    exports test;
    exports mediaPlaying;
    exports config;

    // Open packages for reflection (JavaFX and vlcj factory often need this)
    opens mokaAlpha.application to javafx.fxml;
    opens mokaAlpha.controllers to javafx.fxml;
    opens entities to javafx.base;
}

package mokaAlpha.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mokaAlpha.controllers.MainViewController;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/mainView.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        MainViewController controller = loader.getController();

        stage.setTitle("Moka Player ☕");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setOnCloseRequest(e -> {
            System.out.println("Closing app...");
        });
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

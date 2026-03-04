package com.app.sortamine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Sortamino extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Sortamino.class.getResource("sortamino-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        // scene.getStylesheets().add(Sortamino.class.getResource("style.css").toExternalForm());
        stage.getIcons().add(new Image(Sortamino.class.getResourceAsStream("Sortamine_Icon.png")));
        stage.setTitle("Sortamine");
        stage.setScene(scene);
        stage.show();
    }
}

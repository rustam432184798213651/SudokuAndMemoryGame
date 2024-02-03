package sudoku.sudoku_game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Sudoku extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Sudoku.class.getResource("View.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 720, 480);
        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());

        stage.setTitle("Sudoku");
        Image icon = new Image(getClass().getResourceAsStream("icon.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
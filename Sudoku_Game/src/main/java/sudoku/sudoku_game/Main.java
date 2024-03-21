package sudoku.sudoku_game;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Random;
public class Main extends Application {
    class EditableButton extends Button {
        TextField tf = new TextField();

        public EditableButton(String text) {
            setText(text);
            setOnMouseClicked(e -> {
                tf.setText(getText());
                setText("");
                setGraphic(tf);
            });

            tf.setOnAction(ae -> {
//              if (validateText(tf.getText())) {// this is where you would validate the text
                setText(tf.getText());
                setGraphic(null);
//            }
            });
        }
    }
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MemoryTestView.fxml"));
        GridPane Grid = new GridPane();
        int width = 750;
        int height = 600;
        int buttonSize = 150;
        int numberOfRows = 4;
        int numberOfColumns = 5;
        ArrayList<ArrayList<String>> initialNumbers = new ArrayList<ArrayList<String>>();
        Random rand = new Random();
        for(int i = 0; i < numberOfRows; i++){
            initialNumbers.add(new ArrayList<String>());
            for(int j = 0; j < numberOfColumns; j++) {
                initialNumbers.get(i).add(Integer.toString(rand.nextInt(10)));
            }
        }

        for(int i = 0; i < numberOfRows; i++) {
            for(int j = 0; j < numberOfColumns; j++) {
                Button btn = new Button(initialNumbers.get(i).get(j));
                btn.setMinSize(buttonSize, buttonSize);
                btn.setMaxSize(buttonSize, buttonSize);
                btn.setOnKeyPressed(new ButtonListener());
                Grid.add(btn, j, i);
            }
        }

        Scene scene = new Scene(Grid, width, height);
        stage.setTitle("MemoryTest");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 720, 480);
//        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
//        stage.setTitle("Sudoku");
//        Image icon = new Image(getClass().getResourceAsStream("icon.png"));
//        stage.getIcons().add(icon);
//        stage.setScene(scene);
//        stage.setResizable(false);
//        stage.show();
    }

    class ButtonListener implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent keyEvent) {
            if(Character.isDigit(keyEvent.getText().charAt(0))) {
                Button btn = (Button)keyEvent.getSource();
                btn.setText(keyEvent.getText());
            }
        }

    }

    public static void main(String[] args) {


        launch();
    }
}
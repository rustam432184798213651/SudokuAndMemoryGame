package sudoku.sudoku_game;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Timer;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.Key;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReferenceArray;

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
    public boolean fillMatrixWithRandomNumbers (ArrayList<ArrayList<String>> matrix) {
        if(matrix.isEmpty()) {
            return false;
        }
        Random rand = new Random();
        for(int i = 0; i < matrix.size(); i++) {
            for(int j = 0; j < matrix.getFirst().size(); j++) {
                matrix.get(i).set(j, Integer.toString(rand.nextInt(10)));
            }
        }
        return true;
    }
    public boolean fillButtonsWithNumbersFromMatrix (ArrayList<ArrayList<Button>> buttons, ArrayList<ArrayList<String>> matrix) {
        if(buttons.isEmpty()) {
            return false;
        }
        Random rand = new Random();
        for(int i = 0; i < buttons.size(); i++) {
            for(int j = 0; j < buttons.getFirst().size(); j++) {
                buttons.get(i).get(j).setText(matrix.get(i).get(j));
            }
        }
        return true;
    }
    public void hideAllNumbers(ArrayList<ArrayList<Button>> buttons) {
        if(buttons.isEmpty())
        {
            return;
        }
        for(int i = 0; i < buttons.size(); i++) {
            for(int j = 0; j < buttons.getFirst().size(); j++) {
                buttons.get(i).get(j).setText("");
            }
        }
    }
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        //FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MemoryTestView.fxml"));
        GridPane Grid = new GridPane();
        int width = 750;
        int height = 600;
        int buttonSize = 150;
        int numberOfRows = 4;
        int numberOfColumns = 5;
        Button nextButton = new Button("Next");
        nextButton.setId("nextButton");
        nextButton.setMaxSize(50, 50);
        nextButton.setMinSize(50, 50);
        ArrayList<ArrayList<String>> GivenNumbers = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<Button>> buttons = new ArrayList<ArrayList<Button>>();
        for(int i = 0; i < numberOfRows; i++){
            GivenNumbers.add(new ArrayList<String>());
            for(int j = 0; j < numberOfColumns; j++) {
                GivenNumbers.get(i).add("0");
            }
        }

        // Add next button


        for(int i = 0; i < numberOfRows; i++) {
            buttons.add(new ArrayList<Button>());
            for(int j = 0; j < numberOfColumns; j++) {
                buttons.get(i).add(new Button());
                Button btn = buttons.get(i).get(j);
                btn.setMinSize(buttonSize, buttonSize);
                btn.setMaxSize(buttonSize, buttonSize);
                btn.setOnKeyPressed(new ButtonListener());
                Grid.add(btn, j, i); // Second argument is column and third is row
            }
        }
        Scene scene = new Scene(Grid, width, height);
        scene.getStylesheets().add(getClass().getResource("MemoryTest.css").toExternalForm());
        stage.setTitle("MemoryTest");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();





        int numberOfTests = 1;
        boolean timerPassed = false;
        int numberOfCorrectAnswers = 0;
        for(int i = 0; i < numberOfTests; i++) {
            fillMatrixWithRandomNumbers(GivenNumbers);
            fillButtonsWithNumbersFromMatrix(buttons, GivenNumbers);
            try {
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), ev -> {
                    hideAllNumbers(buttons);
                }));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
                timerPassed = true;
            }
            catch(Exception er) {
                System.err.println(er.getMessage());
            }
//            while(!timerPassed || allButtonsAreFull()) {
//            }
//            timerPassed = false;
//            numberOfCorrectAnswers += checkIfCorrect();
        }




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

    public class MyRunnable implements Runnable {

        public MyRunnable(Object parameter) {
            try {
                Thread.sleep(2000);
            }
            catch(InterruptedException er) {
                System.err.println(er.getMessage());
            }
            hideAllNumbers((ArrayList<ArrayList<Button>>)parameter);
        }

        public void run() {
        }
    }

    public static void main(String[] args) {


        launch();
    }
}
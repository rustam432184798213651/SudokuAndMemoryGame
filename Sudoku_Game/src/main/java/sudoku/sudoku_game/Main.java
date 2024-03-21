package sudoku.sudoku_game;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
    public boolean allButtonsAreFull(ArrayList<ArrayList<Button>> buttons) {
        for(int i = 0; i < buttons.size(); i++) {
            for(int j = 0; j < buttons.getFirst().size(); j++) {
                if(buttons.get(i).get(j).getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean checkIfCorrect(ArrayList<ArrayList<Button>> buttons, ArrayList<ArrayList<String>> matrix) {
        for(int i = 0; i < buttons.size(); i++) {
            for(int j = 0; j < buttons.getFirst().size(); j++) {
                if (buttons.get(i).get(j).toString().equals(matrix.get(i).get(j))) {
                    return false;
                }
            }
        }
        return true;
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



        hideAllNumbers(buttons);

        AtomicInteger numberOfTests = new AtomicInteger(2);
        AtomicInteger numberOfCorrectAnswers = new AtomicInteger(0);
        fillMatrixWithRandomNumbers(GivenNumbers);
        fillButtonsWithNumbersFromMatrix(buttons, GivenNumbers);

        try {
                AtomicBoolean createdByUser = new AtomicBoolean(false);
                Timeline timeline = new Timeline();

                // Define the animation or tasks using KeyFrames
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(2), event -> {
                    if(numberOfTests.get() == 0) {
                        timeline.stop();
                        stage.close();
                        BorderPane borderPane = new BorderPane();
                        GridPane GridForLogicalTest = new GridPane();
                        Text text = new Text("Harry Potter is a magician!");
                        StackPane stack = new StackPane();
                        Agent agent = new Agent();
                        stack.getChildren().addAll(agent, text);
                        int rows = 2;
                        int columns = 2;
                        ArrayList<ArrayList<Button>> buttonsForLogicalTest = new ArrayList<ArrayList<Button>>();
                        for(int i = 0; i < rows; i++) {
                            buttonsForLogicalTest.add(new ArrayList<Button>());
                            for(int j = 0; j < columns; j++) {
                                buttonsForLogicalTest.get(i).add(new Button(""));
                            }
                        }
                        borderPane.setBottom(GridForLogicalTest);
                        borderPane.setTop(stack);
                        Scene sceneForLogicalTest = new Scene(borderPane, width, height);
                        sceneForLogicalTest.getStylesheets().add(getClass().getResource("LogicalTest.css").toExternalForm());
                        stage.setTitle("MemoryTest");
                        stage.setScene(sceneForLogicalTest);
                        stage.setResizable(false);
                        stage.show();

                    }
                    if (allButtonsAreFull(buttons)) {
                        if (createdByUser.get()) {
                            if (checkIfCorrect(buttons, GivenNumbers)) {
                                numberOfCorrectAnswers.set(numberOfCorrectAnswers.get() + 1);
                            }
                            numberOfTests.set(numberOfTests.get() - 1);

                            fillMatrixWithRandomNumbers(GivenNumbers);
                            fillButtonsWithNumbersFromMatrix(buttons, GivenNumbers);
                            createdByUser.set(false);
                        } else {
                            hideAllNumbers(buttons);
                            createdByUser.set(true);
                        }
                    }
                });

                // Add the KeyFrame to the Timeline
                timeline.getKeyFrames().add(keyFrame);

                // Set the number of cycles (-1 for indefinite loop)
                timeline.setCycleCount(Timeline.INDEFINITE);

                // Start the Timeline
                timeline.play();
           }
            catch(Exception er) {
                System.err.println(er.getMessage());
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
            if(!keyEvent.getText().isEmpty() && Character.isDigit(keyEvent.getText().charAt(0))) {
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
    public class Agent extends Rectangle {

        public Agent() {
            setWidth(60);
            setHeight(60);
            setArcWidth(60);
            setArcHeight(60);
            setFill(Color.BLUEVIOLET.deriveColor(0, 1.2, 1, 0.6));
            setStroke(Color.BLUEVIOLET);
        }
    }

    public static void main(String[] args) {


        launch();
    }
}
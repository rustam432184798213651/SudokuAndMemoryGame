package sudoku.sudoku_game;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.util.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Application {
    int numberOfQuestionsForEachTest = 1;
    // For Logical test
    Text currentQuestion = new Text();
    int currentIndex = 0;
    String correctAnswer = "";
    int numberOfCorrectAnswers = 0;
    TextField answer = new TextField();
    ArrayList<Dictionary<String, String>> questions = new ArrayList<Dictionary<String, String>>();

    public Dictionary<String, String> generateQuestionWithAnswer(String question, String answer) {
        Dictionary<String, String> dict= new Hashtable<>();
        dict.put("question", question);
        dict.put("answer", answer);
        return dict;
    }
    public void fillQuestions() {
        questions.add(generateQuestionWithAnswer("Катя зарабатывает больше чем Света. Оля зарабатывает меньше всех. Кто зарабатывает больше всех?", "Оля"));
        //questions.add(generateQuestionWithAnswer("Сколько месяцев в году имеют 28 дней?", "Все месяцы"));
//        questions.add(generateQuestionWithAnswer("Летели утки: одна впереди и две позади, одна позади и две впереди, одна между двумя и три в ряд. Сколько всего летело уток?", "3"));
//        questions.add(generateQuestionWithAnswer("Что в огне не горит и в воде не тонет?", "Лёд"));
//        questions.add(generateQuestionWithAnswer("Каких камней в море нет?", "Сухих"));
//        questions.add(generateQuestionWithAnswer("Какой болезнью на земле никто не болел?", "Морской"));
//        questions.add(generateQuestionWithAnswer("Какая цифра уменьшится на треть, если её перевернуть?", "9"));
//        questions.add(generateQuestionWithAnswer("Какой узел нельзя развязать?", "Железнодорожный"));
//        questions.add(generateQuestionWithAnswer("Человек научился у пауков строить подвесные мосты, у кошек перенял диафрагму в фотоаппарате и светоотражающие дорожные знаки. А какое изобретение появилось благодаря змеям?", "Шприц"));
    }

    // For memory test
    int numberOfCorrectAnswersForMemoryTest = 0;



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

        AtomicInteger numberOfTests = new AtomicInteger(numberOfQuestionsForEachTest);
        fillMatrixWithRandomNumbers(GivenNumbers);
        fillButtonsWithNumbersFromMatrix(buttons, GivenNumbers);
        fillQuestions();
        try {
                AtomicBoolean createdByUser = new AtomicBoolean(false);
                Timeline timeline = new Timeline();

                // Define the animation or tasks using KeyFrames
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(2), event -> {
                    if(numberOfTests.get() == 0) {
                        timeline.stop();
                        stage.close();
                        BorderPane borderPane = new BorderPane();
                        currentQuestion = new Text(questions.getFirst().get("question"));
                        correctAnswer = questions.getFirst().get("answer");
                        currentQuestion.setId("CurrentQuestion");
                        currentQuestion.setWrappingWidth(500);
                        StackPane stack = new StackPane();
                        Agent agent = new Agent();
                        agent.setId("agent");
                        stack.getChildren().addAll(agent, currentQuestion);
//                        int rows = 2;
//                        int columns = 2;
//                        ArrayList<ArrayList<Button>> buttonsForLogicalTest = new ArrayList<ArrayList<Button>>();
//                        int buttonForAnswerHeight = 80;
//                        int buttonForAnswerWidth  = 200;
//                        for(int i = 0; i < rows; i++) {
//                            buttonsForLogicalTest.add(new ArrayList<Button>());
//                            for(int j = 0; j < columns; j++) {
//                                Button btn = new Button(Integer.toString(i*columns + j));
//                                btn.setOnMouseClicked(new AnswerButton());
//                                btn.setMinSize(buttonForAnswerWidth, buttonForAnswerHeight);
//                                btn.setMaxSize(buttonForAnswerWidth, buttonForAnswerHeight);
//                                buttonsForLogicalTest.get(i).add(btn);
//                                GridForLogicalTest.add(btn, j, i);
//                            }
//                        }
//                        GridForLogicalTest.setId("AnswerButton");
//                        int gap = 6;
//                        GridForLogicalTest.setHgap(gap);
//                        GridForLogicalTest.setVgap(gap);

                        answer.setAlignment(Pos.TOP_LEFT);
                        answer.setMinSize(400, 200);
                        answer.setMaxSize(400, 200);
                        answer.setOnKeyReleased(new AnswerListener());
                        answer.setText("");

                        borderPane.setCenter(answer);
                        borderPane.setTop(stack);
                        Button nextButtonForLogicTest = new Button("Next");
                        nextButtonForLogicTest.setOnMouseClicked(new NextButton());
                        nextButtonForLogicTest.setId("test");

                        nextButtonForLogicTest.setMinSize(800, 50);
                        nextButtonForLogicTest.setMaxSize(800, 50);


                        borderPane.setBottom(nextButtonForLogicTest);
                        Scene sceneForLogicalTest = new Scene(borderPane, width, height);
                        sceneForLogicalTest.getStylesheets().add(getClass().getResource("LogicalTest.css").toExternalForm());
                        stage.setTitle("LogicalTest");
                        stage.setScene(sceneForLogicalTest);
                        stage.setResizable(false);
                        stage.show();

                    }
                    else{
                        if (allButtonsAreFull(buttons)) {
                            if (createdByUser.get()) {
                                if (checkIfCorrect(buttons, GivenNumbers)) {
                                    numberOfCorrectAnswersForMemoryTest += 1;
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
                    }
                });

                // Add the KeyFrame to the Timeline
                timeline.getKeyFrames().add(keyFrame);

                // Set the number of cycles (-1 for indefinite loop)
                timeline.setCycleCount(Timeline.INDEFINITE);

                // Start the Timeline
                timeline.play();

                // Start of needed game
                Timeline timelineForGame = new Timeline();

                KeyFrame keyFrameForGame = new KeyFrame(Duration.seconds(2), event-> {
                    if(numberOfQuestionsForEachTest == currentIndex) {
                        timelineForGame.stop();
                        stage.close();
                        if(numberOfCorrectAnswersForMemoryTest > numberOfCorrectAnswers) {
                            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View.fxml"));
                            try {
                                Scene SCENE = new Scene(fxmlLoader.load(), 720, 480);
                                SCENE.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
                                stage.setScene(SCENE);
                                stage.setResizable(false);
                                stage.show();
                            } catch (java.io.IOException er) {
                                System.err.println(er.getMessage());
                            }
                        }
                        else {

                            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ViewForMemoryGame.fxml"));
                            try{
                                Scene SCENE = new Scene(fxmlLoader.load());
                                stage.setTitle("Hello!");
                                stage.setScene(SCENE);
                                stage.show();
                            }
                            catch(java.io.IOException er) {
                                System.err.println(er.getMessage());
                            }

                       }

                    }
                });
                // Add the KeyFrame to the Timeline
                timelineForGame.getKeyFrames().add(keyFrameForGame);

                // Set the number of cycles (-1 for indefinite loop)
                timelineForGame.setCycleCount(Timeline.INDEFINITE);

                // Start the Timeline
                timelineForGame.play();

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

    class AnswerListener implements  EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent keyEvent) {
            //choosenAnswer = ((TextField)keyEvent.getSource()).getText();
        }
    }



    class NextButton implements  EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if(answer.getText().equals(correctAnswer)) {
                numberOfCorrectAnswers += 1;
            }



            if(currentIndex != questions.size()) {
                currentQuestion.setText(questions.get(currentIndex).get("question"));
                correctAnswer = questions.get(currentIndex).get("answer");
                answer.setText("");
            }
            if(currentIndex < questions.size()) {
                currentIndex += 1;
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
            setWidth(500);
            setHeight(350);
            setFill(Color.rgb(211, 211, 211));
            setStroke(Color.BLACK);
        }
    }

    public static void main(String[] args) {


        launch();
    }
}
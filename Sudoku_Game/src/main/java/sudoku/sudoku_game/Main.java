package sudoku.sudoku_game;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Key;
import java.util.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Application {
    int numberOfQuestionsForEachTest = 1;
    String userName = "";
    public static int numberOfStepsInSudoku = 0;
    public static int numberOfRoundsInMemoryGame = 0;
    public static String choosenGame = "";
    Stage stage = null;
    // For Logical test
    Text currentQuestion = new Text();
    int currentIndex = 0;
    String correctAnswer = "";
    int numberOfCorrectAnswers = 0;
    TextField answer = new TextField();
    boolean gameIsFinished = false;
    ArrayList<Dictionary<String, String>> questions = new ArrayList<Dictionary<String, String>>();

    public void updateLogFile(String str) {
        {
            try {
                // append data to a file
                Files.write(Paths.get("Sudoku_Game/src/main/resources/sudoku/sudoku_game/log.txt"), str.getBytes(),
                        StandardOpenOption.APPEND);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    public Dictionary<String, String> generateQuestionWithAnswer(String question, String answer) {
        Dictionary<String, String> dict= new Hashtable<>();
        dict.put("question", question);
        dict.put("answer", answer);
        return dict;
    }
    public void fillQuestions() {
        questions.add(generateQuestionWithAnswer("Катя зарабатывает больше чем Света. Оля зарабатывает меньше всех. Кто зарабатывает больше всех?", "Катя"));
//        questions.add(generateQuestionWithAnswer("Сколько месяцев в году имеют 28 дней?", "Все месяцы"));
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
                if (!buttons.get(i).get(j).getText().equals(matrix.get(i).get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
    public class Tests {
        Stage stage = null;
        boolean canPress = false;
        Tests(Stage stage_a) {
            stage = stage_a;
        }
        int widthForMemoryTest = 450;
        int heightForMemoryTest = 450;
        String titleForMemoryTest = "MemoryTest";
        String CssFileForMemoryTest = "MemoryTest.css";
        int widthOfAnswerSectionForMemoryTest = 400;
        int heightOfAnswerSectionForMemoryTest = 200;
        String CssFileForLogicalTest = "LogicalTest.css";
        String titleForLogicalTest = "LogicalTest";
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
            canPress = true;

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
            canPress = false;
            return true;
        }
        class ButtonListener implements EventHandler<KeyEvent> {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(canPress && !keyEvent.getText().isEmpty() && Character.isDigit(keyEvent.getText().charAt(0))) {
                    Button btn = (Button)keyEvent.getSource();
                    btn.setText(keyEvent.getText());
                }
            }

        }
        public void run() {
            GridPane Grid = new GridPane();
            int buttonSize = 150;
            int numberOfRows = 3;
            int numberOfColumns = 3;
            String defaultTextForAnswerSectionForMemoryTest = "";
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

            Scene scene = new Scene(Grid, widthForMemoryTest, heightForMemoryTest);
            scene.getStylesheets().add(getClass().getResource(CssFileForMemoryTest).toExternalForm());
            stage.setTitle(titleForMemoryTest);
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
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(2.5), event -> {
                    if(numberOfTests.get() == 0) {
                        timeline.stop();
                        stage.close();
                        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent t) {
                                gameIsFinished = true;
                                System.out.println("Something happened");
                            }
                        });
                        BorderPane borderPane = new BorderPane();
                        currentQuestion = new Text(questions.getFirst().get("question"));
                        correctAnswer = questions.getFirst().get("answer");
                        currentQuestion.setId("CurrentQuestion");
                        currentQuestion.setWrappingWidth(500);
                        StackPane stack = new StackPane();
                        Agent agent = new Agent();
                        agent.setId("agent");
                        stack.getChildren().addAll(agent, currentQuestion);

                        answer.setAlignment(Pos.TOP_LEFT);
                        answer.setMinSize(widthOfAnswerSectionForMemoryTest, heightOfAnswerSectionForMemoryTest);
                        answer.setMaxSize(widthOfAnswerSectionForMemoryTest, heightOfAnswerSectionForMemoryTest);
                        answer.setText(defaultTextForAnswerSectionForMemoryTest);
                        answer.setId("AnswerButton");
                        borderPane.setCenter(answer);
                        borderPane.setTop(stack);
                        Button nextButtonForLogicTest = new Button("Next");
                        nextButtonForLogicTest.setOnMouseClicked(new NextButton());
                        nextButtonForLogicTest.setId("test");

                        nextButtonForLogicTest.setMinSize(800, 50);
                        nextButtonForLogicTest.setMaxSize(800, 50);


                        borderPane.setBottom(nextButtonForLogicTest);
                        Scene sceneForLogicalTest = new Scene(borderPane, 750, 600);
                        sceneForLogicalTest.getStylesheets().add(getClass().getResource(CssFileForLogicalTest).toExternalForm());
                        stage.setTitle(titleForLogicalTest);
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



            }
            catch(Exception er) {
                System.err.println(er.getMessage());
            }
        }
    }
    boolean getUserName = false;
    @Override
    public void start(Stage stage_) throws IOException, InterruptedException {
        stage = stage_;
        BorderPane pane = new BorderPane();
        TextField nameOfUserField = new TextField();
        Button nextButton = new Button("Продолжить");
        Text txt = new Text("\t\t    Введите свое имя");
        txt.setId("userPageTxt");

        nameOfUserField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                userName = ((TextField)keyEvent.getSource()).getText();
            }
        });
        nextButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                getUserName = true;
            }
        });

        int size = 100;
        int sizeOfScene = 400;
        nextButton.setMinSize(sizeOfScene, 50);
        nextButton.setMaxSize(sizeOfScene, 50);
        nameOfUserField.setMinSize(size*2, 50);
        nameOfUserField.setMaxSize(size*2, 50);
        nameOfUserField.setId("nameOfUsedField");

        pane.setTop(txt);
        pane.setCenter(nameOfUserField);

        pane.setBottom(nextButton);
        Scene sceneForUserName = new Scene(pane, sizeOfScene, sizeOfScene);
        sceneForUserName.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());

        stage.setScene(sceneForUserName);
        stage.show();


        Timeline timeline3 = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(2.5), event -> {
            if(getUserName) {
                timeline3.stop();
                Tests tests = new Tests(stage);
                tests.run();
                Game game = new Game(stage);
                game.runGame();
            }
        });
        timeline3.getKeyFrames().add(keyFrame);

        // Set the number of cycles (-1 for indefinite loop)
        timeline3.setCycleCount(Timeline.INDEFINITE);

        // Start the Timeline
        timeline3.play();




//        new Thread(
//                ()-> {
//                    while(!gameIsFinished) {
//
//                    }
//                    System.out.println(numberOfCorrectAnswers);
//                    System.out.println(numberOfCorrectAnswersForMemoryTest);
//                }
//        ).start();



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

    public class Sudoku {
        Stage stage = null;
        int width = 720;
        int height = 480;
        String ViewFileName = "View.fxml";
        String CssFileName = "Style.css";
        Sudoku(Stage stage_a) {
            stage = stage_a;
        }
        public void run() {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(ViewFileName));
            try {
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent windowEvent) {
                        gameIsFinished = true;
                        System.out.println("Changed gameIsFinished to true");
                    }
                });
                Scene SCENE = new Scene(fxmlLoader.load(), width, height);
                SCENE.getStylesheets().add(getClass().getResource(CssFileName).toExternalForm());
                stage.setScene(SCENE);
                stage.setResizable(false);
                stage.show();

                Timeline timeline2 = new Timeline();
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.001), event-> {
                    if(gameIsFinished) {
                        timeline2.stop();

                        BorderPane pane = new BorderPane();
                        Text numberOfTriesOnSudoku = new Text("количество попыток на судоку: " + numberOfStepsInSudoku);
                        stage.setTitle("Results");
                        Text correctAnswerOnMemory = new Text("количетсов правильных ответов на тест по памяти: " + numberOfCorrectAnswersForMemoryTest);
                        Text correctAnswerOnLogic = new Text("количество правильных ответов на тест по логике: " + numberOfCorrectAnswers);
                        GridPane grid = new GridPane();
                        grid.add(numberOfTriesOnSudoku, 0, 0);
                        grid.add(correctAnswerOnMemory, 0, 1);
                        grid.add(correctAnswerOnLogic, 0, 2);
                        numberOfTriesOnSudoku.setId("numberOfTriesOnMemoryGame");
                        correctAnswerOnMemory.setId("correctAnswerOnMemory");
                        correctAnswerOnLogic.setId("correctAnswerOnLogic");
                        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent windowEvent) {
                                updateLogFile("User name: " + userName +"\n" + numberOfTriesOnSudoku.getText() + "\n" + correctAnswerOnMemory.getText() + "\n" + correctAnswerOnLogic.getText() + "\n\n");
                            }
                        });
                        pane.setCenter(grid);
                        Scene SCENE2 = new Scene(pane, 800, 500);
                        SCENE2.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
                        stage.setScene(SCENE2);
                        stage.show();

                    }
                });
                timeline2.getKeyFrames().add(keyFrame);

                // Set the number of cycles (-1 for indefinite loop)
                timeline2.setCycleCount(Timeline.INDEFINITE);

                // Start the Timeline
                timeline2.play();
            } catch (java.io.IOException er) {
                System.err.println(er.getMessage());
            }
        }
    }
    FXMLLoader fxmlLoader = null;
    public class MemoryGame {
        Stage stage = null;
        String ViewFileName = "ViewForMemoryGame.fxml";
        MemoryGame(Stage stage_a) {
            stage = stage_a;
        }
        public void run() {
            fxmlLoader = new FXMLLoader(Main.class.getResource(ViewFileName));
            try{
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent windowEvent) {
                        gameIsFinished = true;
                        System.out.println("Changed gameIsFinished to true");
                    }
                });
                Scene SCENE = new Scene(fxmlLoader.load());
                stage.setTitle("Memory Game");
                stage.setScene(SCENE);
                stage.show();

                Timeline timeline2 = new Timeline();
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.001), event-> {
                    if(gameIsFinished) {
                        timeline2.stop();
                        BorderPane pane = new BorderPane();
                        stage.setTitle("Results");
                        Text numberOfTriesOnMemoryGame = new Text("количество попыток на игру на память: " + numberOfRoundsInMemoryGame);
                        Text correctAnswerOnMemory = new Text("количетсов правильных ответов на тест по памяти: " + numberOfCorrectAnswersForMemoryTest);
                        Text correctAnswerOnLogic = new Text("количество правильных ответов на тест по логике: " + numberOfCorrectAnswers);
                        GridPane grid = new GridPane();
                        grid.add(numberOfTriesOnMemoryGame, 0, 0);
                        grid.add(correctAnswerOnMemory, 0, 1);
                        grid.add(correctAnswerOnLogic, 0, 2);
                        numberOfTriesOnMemoryGame.setId("numberOfTriesOnMemoryGame");
                        correctAnswerOnMemory.setId("correctAnswerOnMemory");
                        correctAnswerOnLogic.setId("correctAnswerOnLogic");
                        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent windowEvent) {
                                updateLogFile("User name: " + userName +"\n" + numberOfTriesOnMemoryGame.getText() + "\n" + correctAnswerOnMemory.getText() + "\n" + correctAnswerOnLogic.getText() + "\n\n");
                            }
                        });
                        pane.setCenter(grid);
                        Scene SCENE2 = new Scene(pane, 800, 500);
                        SCENE2.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
                        stage.setScene(SCENE2);
                        stage.show();

                    }
                });
                timeline2.getKeyFrames().add(keyFrame);

                // Set the number of cycles (-1 for indefinite loop)
                timeline2.setCycleCount(Timeline.INDEFINITE);

                // Start the Timeline
                timeline2.play();
            }
            catch(java.io.IOException er) {
                System.err.println(er.getMessage());
            }
        }
    }
    public class Game {
        Stage stage = null;

        double DurationSeconds = 2;
        Game(Stage stage_a) {
            stage = stage_a;
        }
        public void runGame() {
            // Start of needed game
            Timeline timelineForGame = new Timeline();

            KeyFrame keyFrameForGame = new KeyFrame(Duration.seconds(DurationSeconds), event-> {
                if(numberOfQuestionsForEachTest == currentIndex) {
                    timelineForGame.stop();
                    stage.close();

                    if(numberOfCorrectAnswersForMemoryTest > numberOfCorrectAnswers) {
                       Sudoku sd = new Sudoku(stage);
                       sd.run();
                       Main.choosenGame = "Sudoku";
                    }
                    else {
                        MemoryGame mg = new MemoryGame(stage);
                        mg.run();
                        Main.choosenGame = "MemoryGame";
                    }
//                    try {
//                        Files.write(Paths.get("result_of_players.txt"), ("number of correct answers for memory test: " + numberOfCorrectAnswersForMemoryTest).getBytes(), StandardOpenOption.APPEND);
//                        Files.write(Paths.get("result_of_players.txt"), ("number of correct answers for logical test: " + numberOfCorrectAnswers).getBytes(), StandardOpenOption.APPEND);
//                    }catch (IOException e) {
//                        System.err.println(e.getMessage());
//                    }

                }
            });
            // Add the KeyFrame to the Timeline
            timelineForGame.getKeyFrames().add(keyFrameForGame);

            // Set the number of cycles (-1 for indefinite loop)
            timelineForGame.setCycleCount(Timeline.INDEFINITE);

            // Start the Timeline
            timelineForGame.play();
        }
    }





    class NextButton implements  EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if(answer.getText().equals(correctAnswer)) {

                numberOfCorrectAnswers += 1;
            }

            if(currentIndex < questions.size()) {
                currentIndex += 1;
            }
            if(currentIndex != questions.size()) {
                currentQuestion.setText(questions.get(currentIndex).get("question"));
                correctAnswer = questions.get(currentIndex).get("answer");
                answer.setText("");
            }

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
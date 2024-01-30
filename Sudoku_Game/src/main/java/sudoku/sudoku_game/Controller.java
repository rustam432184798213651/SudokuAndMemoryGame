package sudoku.sudoku_game;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Controller implements Initializable {

    @FXML
    Button button_one;
    @FXML
    Button button_two;
    @FXML Button button_three;
    @FXML Button button_four;
    @FXML Button button_five;
    @FXML Button button_six;
    @FXML Button button_seven;
    @FXML Button button_eight;
    @FXML Button button_nine;
    @FXML Canvas canvas;
    GameBoard gameboard;
    int player_selected_row = -1;
    int player_selected_col = -1;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("Start");
        gameboard = new GameBoard();
        GraphicsContext context = canvas.getGraphicsContext2D();
        drawOnCanvas(context);
    }


    private void drawOnCanvas(GraphicsContext context) {
        context.clearRect(0, 0, 450, 450);
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int position_y = row * 50 + 2;
                int position_x = col * 50 + 2;
                int width = 46;
                context.setFill(Color.rgb(200, 200, 200));
                context.fillRoundRect(position_x, position_y, width, width, 10, 10);
            }
        }

        context.setStroke(Color.rgb(193, 87, 87));
        context.setLineWidth(3);
        if (player_selected_col > -1) {
            context.strokeRoundRect(player_selected_col * 50 + 2, player_selected_row * 50 + 2, 46, 46, 10, 10);
        }

        int[][] player = gameboard.getPlayer();

        // for loop is the same as before
        for(int row = 0; row<9; row++) {
            for(int col = 0; col<9; col++) {

                int position_y = row * 50 + 35;
                int position_x = col * 50 + 15;

                context.setFill(Color.BLACK);
                context.setFont(new Font(30));

                if(player[row][col]!=0) {
                    String txt = Integer.toString(player[row][col]);
                    context.fillText(txt, position_x, position_y);
                }
            }
        }

    }

    public void canvasMouseClicked() {
        canvas.setOnMouseClicked(e->{
            int mouse_x = (int) e.getX();
            int mouse_y = (int) e.getY();
            player_selected_row = (int) (mouse_y / 50); // update player selected row
            player_selected_col = (int) (mouse_x / 50); // update player selected column
            drawOnCanvas(canvas.getGraphicsContext2D());
        });
    }


    public void buttonOnePressed() {
        System.out.println(1);
        gameboard.modifyPlayer(1, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonTwoPressed() {
        System.out.println(2);
        gameboard.modifyPlayer(2, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonThreePressed() {
        System.out.println(3);
        gameboard.modifyPlayer(3, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonFourPressed() {
        System.out.println(4);
        gameboard.modifyPlayer(4, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonFivePressed() {
        System.out.println(5);
        gameboard.modifyPlayer(5, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonSixPressed() {
        System.out.println(6);
        gameboard.modifyPlayer(6, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonSevenPressed() {
        System.out.println(7);
        gameboard.modifyPlayer(7, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonEightPressed() {
        System.out.println(8);
        gameboard.modifyPlayer(8, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void buttonNinePressed() {
        System.out.println(9);
        gameboard.modifyPlayer(9, player_selected_row, player_selected_col);
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public class GameBoard {
        /* Array that will contain the complete solution to the board */
        private int[][] solution;
        /* Array that will contain ONLY the numbers initially drawn on the board and that the player can't change */
        private int[][] initial;
        /* Array that will contain player's numbers */
        private int[][] player;

        public GameBoard() {
            solution = new int[][]
                    {
                            {5, 3, 8, 4, 6, 1, 7, 9, 2},
                            {6, 9, 7, 3, 2, 5, 8, 1, 4},
                            {2, 1, 4, 7, 8, 9, 5, 6, 3},
                            {9, 4, 1, 2, 7, 8, 6, 3, 5},
                            {7, 6, 2, 1, 5, 3, 9, 4, 8},
                            {8, 5, 3, 9, 4, 6, 1, 2, 7},
                            {3, 8, 9, 5, 1, 2, 4, 7, 6},
                            {4, 2, 6, 8, 9, 7, 3, 5, 1},
                            {1, 7, 5, 6, 3, 4, 2, 8, 9}
                    };

            // 0's will be rendered as empty space and will be editable by player
            initial = new int[][]
                    {
                            {0, 0, 0, 4, 0, 0, 0, 9, 0},
                            {6, 0, 7, 0, 0, 0, 8, 0, 4},
                            {0, 1, 0, 7, 0, 9, 0, 0, 3},
                            {9, 0, 1, 0, 7, 0, 0, 3, 0},
                            {0, 0, 2, 0, 0, 0, 9, 0, 0},
                            {0, 5, 0, 0, 4, 0, 1, 0, 7},
                            {3, 0, 0, 5, 0, 2, 0, 7, 0},
                            {4, 0, 6, 0, 0, 0, 3, 0, 1},
                            {0, 7, 0, 0, 0, 4, 0, 0, 0}
                    };

            // player's array is initialized as a 9x9 full of zeroes
            player = new int[9][9];
            resetPlayer();
        }

        // returns the solution array
        public int[][] getSolution() {
            return solution;
        }

        public int[][] getInitial() {
            return initial;
        }

        public void resetPlayer() {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    player[row][col] = initial[row][col];
                }
            }
        }

        public int[][] getPlayer() {
            return player;
        }
        public void modifyPlayer(int val, int row, int col) {
            if (val >= 0 && val <= 9)
                player[row][col] = val;
            else
                System.out.println("Value passed to player falls out of range");
        }
    }
}
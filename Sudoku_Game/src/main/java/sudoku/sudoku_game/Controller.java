package sudoku.sudoku_game;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class Controller implements Initializable {

    @FXML Button one;
    @FXML Button two;
    @FXML Button three;
    @FXML Button four;
    @FXML Button five;
    @FXML Button six;
    @FXML Button seven;
    @FXML Button eight;
    @FXML Button nine;
    @FXML Canvas canvas;
    GameBoard gameboard;
    int player_selected_row = -1;
    int player_selected_col = -1;
    Color line_color = Color.WHITE;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("Start");
        gameboard = new GameBoard(9, 30);
        GraphicsContext context = canvas.getGraphicsContext2D();
        drawOnCanvas(context);
    }
    public void newGame() {
        System.out.println("New Game");
        gameboard.newValues();
        player_selected_row = player_selected_col = -1;
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public void check(){
        System.out.println("Check");
        if (gameboard.check()) {
            System.out.println("True");
            line_color = Color.GREEN;
        } else {
            System.out.println("False");
            line_color = Color.RED;
        }

        player_selected_row = player_selected_col = -1;
        drawOnCanvas(canvas.getGraphicsContext2D());
        line_color = Color.WHITE;
    }
    private void drawOnCanvas(GraphicsContext context) {
        int width = 40;
        int initial[][] = gameboard.getInitial();
        int[][] player = gameboard.getPlayer();
        context.clearRect(0, 0, 450, 450);
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int position_y = row * 50 + 2;
                int position_x = col * 50 + 2;

                if (initial[row][col] != 0) {
                    context.setFill(Color.rgb(255, 69, 0)); // New Game input
                } else if (player[row][col] != 0) {
                    context.setFill(Color.rgb(255, 69, 0)); // Player input
                } else {
                    context.setFill(Color.rgb( 255, 94, 14)); // Empty input
                }
                context.fillRoundRect(position_x, position_y, width, width, 15, 15);
            }
        }

        context.setStroke(Color.rgb(193, 87, 87));
        context.setLineWidth(3);
        if (player_selected_col > -1) {
            context.strokeRoundRect(player_selected_col * 50 + 2, player_selected_row * 50 + 2, width, width, 10, 10);
        }

        // for loop is the same as before
        for(int row = 0; row<9; row++) {
            for(int col = 0; col<9; col++) {
                double ratio = (width*1.0) / 46.0;
                int position_y = (int)Math.round(row * 50 + 35*ratio);
                int position_x = (int)Math.round(col * 50 + 15*ratio);

                context.setFill(Color.BLACK);
                context.setFont(new Font(30));


                if(player[row][col]!=0) {
                    String txt = Integer.toString(player[row][col]);
                    context.fillText(txt, position_x, position_y);
                }
            }
        }
        context.setStroke(line_color);
        for (int i = 1; i <= 2; i++) {
            context.strokeLine(i * 150, 0, i * 150, 450);
            context.strokeLine(0, i * 150, 450, i * 150);
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

    public void reset() {
        System.out.println("Reset");
        gameboard.resetPlayer();
        player_selected_row = player_selected_col = -1;
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    @FXML public void handleMousePress(MouseEvent event) {
        String value = ((Button)event.getSource()).getText();
        if (gameboard.getInitial()[player_selected_row][player_selected_col] != 0) {
            return;
        }
        try {
            gameboard.modifyPlayer(Integer.parseInt(value), player_selected_row, player_selected_col);
        }
        catch(ValueError error) {
            System.err.println(error.getMessage());
            System.exit(1);
        }
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    public class GameBoard {
        int N;
        int SRN;
        int K;
        private int[][] solution;
        private int[][] initial;
        private int[][] player;

        public boolean isValidSudoku(char[][] board) {
            if (board == null || board.length != 9 || board[0].length != 9) {
                throw new IllegalArgumentException("Input is invalid");
            }

            int rowColBox = 0;

            for (int i = 0; i < 9; i++) { // Index of row, column and box
                for (int j = 0; j < 9; j++) {
                    // Check ith row
                    rowColBox = validateAndAddCell(board[i][j], 0, rowColBox);
                    if (rowColBox == -1) {
                        return false;
                    }

                    // Check ith column
                    rowColBox = validateAndAddCell(board[j][i], 1, rowColBox);
                    if (rowColBox == -1) {
                        return false;
                    }

                    // Check ith box
                    int boxRow = 3 * (i / 3) + (j / 3);
                    int colRow = 3 * (i % 3) + (j % 3);
                    rowColBox = validateAndAddCell(board[boxRow][colRow], 2, rowColBox);
                    if (rowColBox == -1) {
                        return false;
                    }
                }
                rowColBox = 0;
            }

            return true;
        }

        private int validateAndAddCell(char c, int type, int rowColBox) {
            if (c == '.') {
                return rowColBox;
            }
            if (c < '1' || c > '9') {
                return -1;
            }

            int bitIdx = type * 9 + (c - '1');
            if (((rowColBox >> bitIdx) & 1) == 1) {
                return -1;
            }

            return rowColBox | (1 << bitIdx);
        }
        public GameBoard(int N, int K) {
            this.N = N;
            this.K = K;
            this.SRN = (int) Math.sqrt(N);
            solution = new int[N][N];
            initial = new int[N][N];
            player = new int[N][N];
            resetPlayer();
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
        public int[][] getInitial() {
            return initial;
        }
        public void modifyPlayer(int val, int row, int col) throws ValueError {
            if (val >= 0 && val <= 9)
                player[row][col] = val;
            else
                throw new ValueError("Value passed to player falls out of range");
        }

        Boolean check() {
            char[][] board = new char[9][9];

            for (int row = 0; row < N; row++) {
                for (int col = 0; col < N; col++) {
                    board[row][col] = (char)(player[row][col] + '0');
                }
            }
            return isValidSudoku(board);
        }

        public void newValues() {
            for (int row = 0; row < N; row++) {
                for (int col = 0; col < N; col++) {
                    solution[row][col] = 0;
                    initial[row][col] = 0;
                    player[row][col] = 0;
                }
            }
            fillDiagonal();
            fillRemaining(0, SRN);
            removeKDigits();
            resetPlayer();
        }

        // Fill the diagonal SRN number of SRN x SRN matrices
        void fillDiagonal() {
            for (int i = 0; i < N; i = i + SRN) {
                fillBox(i, i);
            }
        }

        // Returns false if given 3 x 3 block contains num.
        boolean unUsedInBox(int rowStart, int colStart, int num) {
            for (int i = 0; i < SRN; i++)
                for (int j = 0; j < SRN; j++)
                    if (solution[rowStart + i][colStart + j] == num)
                        return false;

            return true;
        }

        // Fill a 3 x 3 matrix.
        void fillBox(int row, int col) {
            int num;
            for (int i = 0; i < SRN; i++) {
                for (int j = 0; j < SRN; j++) {
                    do {
                        num = randomGenerator(N);
                    }
                    while (!unUsedInBox(row, col, num));
                    solution[row + i][col + j] = num;
                }
            }
        }

        // Random generator
        int randomGenerator(int num) {
            return (int) Math.floor((Math.random() * num + 1));
        }

        // Check if safe to put in cell
        boolean CheckIfSafe(int i, int j, int num) {
            return (unUsedInRow(i, num) &&
                    unUsedInCol(j, num) &&
                    unUsedInBox(i - i % SRN, j - j % SRN, num));
        }

        // check in the row for existence
        boolean unUsedInRow(int i, int num) {
            for (int j = 0; j < N; j++)
                if (solution[i][j] == num)
                    return false;
            return true;
        }

        // check in the row for existence
        boolean unUsedInCol(int j, int num) {
            for (int i = 0; i < N; i++)
                if (solution[i][j] == num)
                    return false;
            return true;
        }

        // A recursive function to fill remaining
        // matrix
        boolean fillRemaining(int i, int j) {
            if (j >= N && i < N - 1) {
                i = i + 1;
                j = 0;
            }
            if (i >= N && j >= N)
                return true;

            if (i < SRN) {
                if (j < SRN)
                    j = SRN;
            } else if (i < N - SRN) {
                if (j == (int) (i / SRN) * SRN)
                    j = j + SRN;
            } else {
                if (j == N - SRN) {
                    i = i + 1;
                    j = 0;
                    if (i >= N)
                        return true;
                }
            }

            for (int num = 1; num <= N; num++) {
                if (CheckIfSafe(i, j, num)) {
                    solution[i][j] = num;
                    if (fillRemaining(i, j + 1))
                        return true;

                    solution[i][j] = 0;
                }
            }
            return false;
        }

        // Remove the K no. of digits to
        // complete game
        public void removeKDigits() {
            for (int row = 0; row < N; row++) {
                for (int col = 0; col < N; col++) {
                    initial[row][col] = solution[row][col];
                }
            }
            int count = K;
            while (count != 0) {
                int cellId = randomGenerator(N * N) - 1;
                int i = (cellId / N);
                int j = cellId % N;
                if (j != 0)
                    j = j - 1;
                Boolean can = (initial[i][j] != 0);
                for (int k = 0, c1 = 0, c2 = 0; k < N; k++) {
                    if (initial[i][k] == 0) c1++;
                    if (initial[k][j] == 0) c2++;
                    if (c1 >= 7 || c2 >= 7) {
                        can = false;
                    }
                }

                if (can) {
                    count--;
                    initial[i][j] = 0;
                } else if (initial[i][N - 1] != 0) {
                    count--;
                    initial[i][N - 1] = 0;
                }
            }
        }

    }
}
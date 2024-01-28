module sudoku.sudoku_game {
    requires javafx.controls;
    requires javafx.fxml;


    opens sudoku.sudoku_game to javafx.fxml;
    exports sudoku.sudoku_game;
}
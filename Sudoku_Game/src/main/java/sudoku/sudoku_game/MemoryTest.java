package sudoku.sudoku_game;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.Group;

public class MemoryTest  implements Initializable {
    @FXML Canvas canvas;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("Start");
        GraphicsContext graphics_context = canvas.getGraphicsContext2D();
        graphics_context.setFill(Color.RED);
        graphics_context.fillRect(20, 20, 70, 70);


        // set fill for oval
        graphics_context.setFill(Color.BLUE);
        graphics_context.fillOval(30, 30, 70, 70);
        //drawOnCanvas(context);
    }


    public void run (){
//        Group group = new Group(canvas);
//
//        // create a scene
//        Scene scene = new Scene(group, 200, 200);
    }
}

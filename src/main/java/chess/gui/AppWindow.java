/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.gui;

import chess.gui.Menu.MenuPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Class representing the main container for the chess board and GUI.
 *
 * @author Nick Houser
 */
public class AppWindow extends Application {

    /**
     * Entry point for the UI application.
     *
     * @param stage a stage to add all UI components to
     * @throws Exception if initialization fails
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Chess");

        BoardController controller = new BoardController();

        BoardPane board = new BoardPane(controller);
        MenuPane menu = new MenuPane(controller);

        GridPane layout = new GridPane();
        layout.add(board, 0, 0);
        layout.add(menu, 1, 0);

        Scene scene = new Scene(layout);
        stage.setScene(scene);

        stage.show();
    }
}

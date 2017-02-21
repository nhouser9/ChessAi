/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * TODO
 * threading
 * AI move progress bar
 * positional piece values
 * logging
 * using opponent's clock
 * perform deeper analysis on selected moves?
 * finish calculating capture lines?
 * reuse old tree
 * cache
 * allow user to promote into a rook
 * enforce consistency between board / piece position
 */
import gui.AppWindow;
import javafx.application.Application;

/**
 * Main class and entry point for the app.
 *
 * @author Nick Houser
 */
public class ChessEngine {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(AppWindow.class, new String[0]);
    }
}

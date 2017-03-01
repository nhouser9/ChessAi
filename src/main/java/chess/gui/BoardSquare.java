/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.gui;

import chess.chessboard.Board;
import chess.chessboard.pieces.Piece;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Extension of the JavaFX Group class which represents two images that comprise
 * a square: the background and the piece.
 *
 * @author Nick Houser
 */
public class BoardSquare extends Group {

    //location variables
    public final int col;
    public final int row;

    //cached piece last shown on this square
    private Piece cachedPiece;

    /**
     * Constructor which calls super() and saves location variables.
     *
     * @param col the column index of this square
     * @param row the row index of this square
     * @param children the child nodes to display in this group
     */
    public BoardSquare(int col, int row, Node... children) {
        super(children);
        this.col = col;
        this.row = row;
    }

    /**
     * Method which asks this square to refresh its images to match the board
     * state.
     *
     * @param board the board state to reflect
     */
    public void refreshState(Piece occupant) {
        if (cachedPiece == occupant) {
            return;
        }

        cachedPiece = occupant;
        List<Node> images = getChildren();

        if (images.size() > 1) {
            images.remove(1);
        }

        if (occupant != null) {
            String path = occupant.color.toString() + "_" + occupant.getClass().getSimpleName() + ".png";
            Image pieceImage = new Image(getClass().getClassLoader().getResource(path).toExternalForm());
            ImageView view = new ImageView(pieceImage);
            view.setBlendMode(BlendMode.SRC_OVER);
            images.add(view);
        }
    }
}

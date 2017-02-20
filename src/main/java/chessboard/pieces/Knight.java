/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.pieces;

import chessboard.Board;
import chessboard.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a Knight, a piece which can move in an L shape and which
 * can jump over other pieces.
 *
 * @author Nick Houser
 */
public class Knight extends Piece {

    /**
     * Constructor which simply calls super().
     *
     * @param color the color of the Rook
     * @param xPosition the initial x position
     * @param yPosition the initial y position
     */
    public Knight(Color color, int xPosition, int yPosition) {
        super(color, xPosition, yPosition);
    }

    /**
     * Method which returns a list of valid moves for the Knight. Knights can
     * move in an L shape and can jump over intervening pieces.
     *
     * @param board the board to move on
     * @return a list of valid moves for the piece
     */
    @Override
    public List<Point> validMoves(Board board) {
        List<Point> toReturn = new ArrayList<>();
        Point target;

        target = new Point(position().x + 2, position().y + 1);
        addIfValid(toReturn, board, target, true, true);

        target = new Point(position().x + -2, position().y + 1);
        addIfValid(toReturn, board, target, true, true);

        target = new Point(position().x + 2, position().y - 1);
        addIfValid(toReturn, board, target, true, true);

        target = new Point(position().x - 2, position().y - 1);
        addIfValid(toReturn, board, target, true, true);

        target = new Point(position().x + 1, position().y + 2);
        addIfValid(toReturn, board, target, true, true);

        target = new Point(position().x - 1, position().y + 2);
        addIfValid(toReturn, board, target, true, true);

        target = new Point(position().x + 1, position().y - 2);
        addIfValid(toReturn, board, target, true, true);

        target = new Point(position().x - 1, position().y - 2);
        addIfValid(toReturn, board, target, true, true);

        return toReturn;
    }
}

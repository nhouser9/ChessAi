/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.pieces;

import chessboard.Board;
import chessboard.Color;
import chessboard.Direction;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a King, a piece which can move to any adjacent square and
 * which must be captured to win the game.
 *
 * @author Nick Houser
 */
public class King extends Piece {

    /**
     * Constructor which simply calls super().
     *
     * @param color the color of the Rook
     * @param xPosition the initial x position
     * @param yPosition the initial y position
     */
    public King(Color color, int xPosition, int yPosition) {
        super(color, xPosition, yPosition);
    }

    /**
     * Method which returns a list of valid moves for the King. Kings can move
     * to any adjacent square, including diagonally.
     *
     * @param board the board to move on
     * @return a list of valid moves for the piece
     */
    @Override
    public List<Point> validMoves(Board board) {
        List<Point> toReturn = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            Point adjacent = new Point(position().x + dir.x(), position().y + dir.y());
            addIfValid(toReturn, board, adjacent, true, true);

            if (board.canCastle(dir, color)) {
                Point castleTarget = new Point(position().x + (2 * dir.x()), position().y);
                addIfValid(toReturn, board, castleTarget, true, false);
            }
        }

        return toReturn;
    }
}

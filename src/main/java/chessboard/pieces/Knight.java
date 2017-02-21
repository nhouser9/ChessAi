/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.pieces;

import ai.rules.NumberOfMoves;
import chessboard.Board;
import chessboard.Color;
import chessboard.moves.GenericMove;
import chessboard.moves.MoveFactory;
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
    public List<GenericMove> validMoves(Board board) {
        List<GenericMove> toReturn = new ArrayList<>();
        Point target;

        int[] ones = {-1, 1};
        int[] twos = {-2, 2};
        for (int one : ones) {
            for (int two : twos) {
                MoveFactory.addIfValid(toReturn, board, this, position().x + one, position().y + two, true, true);
                MoveFactory.addIfValid(toReturn, board, this, position().x + two, position().y + one, true, true);
            }
        }

        return toReturn;
    }

    /**
     * Method which adds all relevant positional rules to this piece.
     */
    @Override
    public void addPositionalRules() {
        addPositionalRule(new NumberOfMoves());
    }

    /**
     * Method which returns the material value for the piece, which is a number
     * representing the worth of the type of piece.
     *
     * @return the material value of the piece
     */
    @Override
    public double materialValue() {
        return 3.0;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.pieces;

import ai.rules.NumberOfMoves;
import chessboard.Board;
import chessboard.Color;
import chessboard.Direction;
import chessboard.moves.GenericMove;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a Rook, a piece which can move sideways, forward, and
 * backward.
 *
 * @author Nick Houser
 */
public class Rook extends Piece {

    /**
     * Constructor which simply calls super().
     *
     * @param color the color of the Rook
     * @param xPosition the initial x position
     * @param yPosition the initial y position
     */
    public Rook(Color color, int xPosition, int yPosition) {
        super(color, xPosition, yPosition);
    }

    /**
     * Method which adds all relevant positional rules to this piece.
     */
    @Override
    public void addPositionalRules() {
        addPositionalRule(new NumberOfMoves());
    }

    @Override
    public double materialValue() {
        return 5.0;
    }

    /**
     * Method which returns a list of valid moves for the Rook. Rooks can move
     * forward, backward, or sideways, and cannot move past other pieces.
     *
     * @param board the board to move on
     * @return a list of valid moves for the piece
     */
    @Override
    public List<GenericMove> validMoves(Board board) {
        List<GenericMove> toReturn = new ArrayList<>();

        addDirectionalMoves(toReturn, board, Direction.NORTH);
        addDirectionalMoves(toReturn, board, Direction.SOUTH);
        addDirectionalMoves(toReturn, board, Direction.EAST);
        addDirectionalMoves(toReturn, board, Direction.WEST);

        return toReturn;
    }

    /**
     * Method which returns a copy of the Piece.
     *
     * @return a copy of this Piece
     */
    @Override
    public Rook copy() {
        return new Rook(color, position().x, position().y);
    }
}

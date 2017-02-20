/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard.pieces;

import chessboard.Board;
import chessboard.Color;
import chessboard.Direction;
import chessboard.moves.GenericMove;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a Queen, a piece which can move in diagonal or straight
 * lines but cannot move through other pieces.
 *
 * @author Nick Houser
 */
public class Queen extends Piece {

    /**
     * Constructor which simply calls super().
     *
     * @param color the color of the Rook
     * @param xPosition the initial x position
     * @param yPosition the initial y position
     */
    public Queen(Color color, int xPosition, int yPosition) {
        super(color, xPosition, yPosition);
    }

    /**
     * Method which returns a list of valid moves for the Queen. Queens can move
     * diagonally or in straight lines, and cannot move past other pieces.
     *
     * @param board the board to move on
     * @return a list of valid moves for the piece
     */
    @Override
    public List<GenericMove> validMoves(Board board) {
        List<GenericMove> toReturn = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            addDirectionalMoves(toReturn, board, dir);
        }

        return toReturn;
    }

}

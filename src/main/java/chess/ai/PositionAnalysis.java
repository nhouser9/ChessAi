/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ai;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.pieces.King;
import chess.chessboard.pieces.Piece;

/**
 * Class which analyzes a position and assigns it a value. This is used for
 * deciding which move is best.
 *
 * @author Nick Houser
 */
public class PositionAnalysis {

    //values for all of the respective factors
    protected static final double VALUE_CHECKMATE = 10000;

    //the value of this position
    public final double value;

    /**
     * Method which returns the value of a passed position.
     *
     * @param toAnalyze the board to analyze
     */
    public PositionAnalysis(Board toAnalyze) {
        if (checkMate(toAnalyze)) {
            value = VALUE_CHECKMATE * colorFactor(toAnalyze.activePlayer().enemy());
        } else {
            value = pieceValues(toAnalyze);
        }
    }

    private boolean checkMate(Board toAnalyze) {
        Color active = toAnalyze.activePlayer();
        King activeKing = toAnalyze.findKing(active);
        if (activeKing == null) {
            return false;
        }
        if (!activeKing.threatened(toAnalyze)) {
            return false;
        }
        for (GenericMove kingMove : activeKing.validMoves(toAnalyze)) {
            if (!kingMove.endangersKing()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method which applies the rules for material value of each piece.
     *
     * @param toAnalyze the board to analyze
     */
    private double pieceValues(Board toAnalyze) {
        double toReturn = 0.0;
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                Piece occupant = toAnalyze.occupant(col, row);

                if (occupant == null) {
                    continue;
                }

                double pieceValue = occupant.materialValue() + occupant.positionalValue(toAnalyze);

                toReturn = toReturn + (colorFactor(occupant.color) * pieceValue);
            }
        }
        return toReturn;
    }

    /**
     * Method which determines the sign of a beneficial position for the given
     * color. By convention negative values indicate a winning position for
     * black, while positive ones indicate a winning position for white.
     *
     * @param color the color to check the factor for
     * @return the sign of the color's factor
     */
    private int colorFactor(Color color) {
        if (color == Color.WHITE) {
            return 1;
        } else {
            return -1;
        }
    }
}

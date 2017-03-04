/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard;

import chess.chessboard.moves.GenericMove;
import chess.chessboard.notation.History;
import chess.chessboard.pieces.Bishop;
import chess.chessboard.pieces.King;
import chess.chessboard.pieces.Knight;
import chess.chessboard.pieces.Pawn;
import chess.chessboard.pieces.Piece;
import chess.chessboard.pieces.Queen;
import chess.chessboard.pieces.Rook;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Class representing the game board.
 *
 * @author Nick Houser
 */
public class Board {

    /**
     * The number of squares per one side of a chess board.
     */
    public static final int SQUARES_PER_SIDE = 8;

    /**
     * Method which checks whether a given co-ordinate is on the board.
     *
     * @param x the x co-ordinate to check
     * @param y the y co-ordinate to check
     * @return true if the passed position is within bounds, false otherwise
     */
    public static boolean inBounds(int x, int y) {
        if (x < 0) {
            return false;
        }

        if (y < 0) {
            return false;
        }

        if (x >= SQUARES_PER_SIDE) {
            return false;
        }

        if (y >= SQUARES_PER_SIDE) {
            return false;
        }

        return true;
    }

    /**
     * Method which returns the pieces necessary to set up the pieces for a
     * normal game.
     *
     * @return a list of Pieces that are on a regularly setup chessboard
     */
    public static List<Piece> initialState() {
        List<Piece> initialPieces = new LinkedList<>();

        initialPieces.add(new Rook(Color.WHITE, 0, Color.WHITE.homeRow()));
        initialPieces.add(new Knight(Color.WHITE, 1, Color.WHITE.homeRow()));
        initialPieces.add(new Bishop(Color.WHITE, 2, Color.WHITE.homeRow()));
        initialPieces.add(new Queen(Color.WHITE, 3, Color.WHITE.homeRow()));
        initialPieces.add(new King(Color.WHITE, 4, Color.WHITE.homeRow()));
        initialPieces.add(new Bishop(Color.WHITE, 5, Color.WHITE.homeRow()));
        initialPieces.add(new Knight(Color.WHITE, 6, Color.WHITE.homeRow()));
        initialPieces.add(new Rook(Color.WHITE, 7, Color.WHITE.homeRow()));

        for (int col = 0; col < SQUARES_PER_SIDE; col++) {
            initialPieces.add(new Pawn(Color.WHITE, col, Color.WHITE.pawnRow()));
            initialPieces.add(new Pawn(Color.BLACK, col, Color.BLACK.pawnRow()));
        }

        initialPieces.add(new Rook(Color.BLACK, 0, Color.BLACK.homeRow()));
        initialPieces.add(new Knight(Color.BLACK, 1, Color.BLACK.homeRow()));
        initialPieces.add(new Bishop(Color.BLACK, 2, Color.BLACK.homeRow()));
        initialPieces.add(new Queen(Color.BLACK, 3, Color.BLACK.homeRow()));
        initialPieces.add(new King(Color.BLACK, 4, Color.BLACK.homeRow()));
        initialPieces.add(new Bishop(Color.BLACK, 5, Color.BLACK.homeRow()));
        initialPieces.add(new Knight(Color.BLACK, 6, Color.BLACK.homeRow()));
        initialPieces.add(new Rook(Color.BLACK, 7, Color.BLACK.homeRow()));

        return initialPieces;
    }

    //the color whose turn it is
    private Color activePlayer;

    //the move history of the board
    private History history;

    //the array of squares on the board
    private Square[][] squares;

    //links to both kings for easy retrieval
    private Map<Color, King> kings;

    //a list of possible moves from this position
    private List<GenericMove> possibleMoves;

    /**
     * Constructor which initializes the piece array to the initial chess board.
     * Defaults to the initial chess state if no list of pieces is passed in.
     *
     * @param initialPieces the initial pieces to add to the board
     * @param active the player whose turn it is
     */
    public Board(List<Piece> initialPieces, Color active) {
        initializeFields(active);
        setupPieces(initialPieces);
    }

    /**
     * Method which returns the player whose turn it is.
     *
     * @return the color whose turn it is
     */
    public Color activePlayer() {
        return activePlayer;
    }

    /**
     * Method which changes control of the turn to the next player.
     */
    public void passTurn() {
        activePlayer = activePlayer.enemy();
    }

    /**
     * Method which returns the square at the given position.
     *
     * @param xPosition the x position to check
     * @param yPosition the y position to check
     * @return the Square at the passed position
     */
    public Square square(int xPosition, int yPosition) {
        return squares[xPosition][yPosition];
    }

    /**
     * Method which returns the move history for this board.
     *
     * @return the History object representing the past moves made on the board
     */
    public History history() {
        return history;
    }

    /**
     * Helper method which locates the king of a specific color.
     *
     * @param color the color of king to locate
     * @return the requested King
     */
    public King findKing(Color color) {
        if (kings.containsKey(color)) {
            return kings.get(color);
        }
        return null;
    }

    /**
     * Method which returns a list of possible moves from this board state.
     *
     * @return a list of possible moves from this board state
     */
    public List<GenericMove> validMoves() {
        if (possibleMoves == null) {
            possibleMoves = calculatePossibleMoves(activePlayer);
        }
        return Collections.unmodifiableList(possibleMoves);
    }

    /**
     * Method which resets the list of possible moves, which will force
     * recalculation.
     */
    public void resetValidMoves() {
        possibleMoves = null;
    }

    /**
     * Helper method which calculates the list of possible moves.
     *
     * @param player the player whose moves should be calculated
     * @return a list of possible moves in the current position
     */
    private List<GenericMove> calculatePossibleMoves(Color player) {
        List<GenericMove> toReturn = new LinkedList<>();
        for (int col = 0; col < SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < SQUARES_PER_SIDE; row++) {
                Piece occupant = square(col, row).occupant();
                if (occupant == null || occupant.color != player) {
                    continue;
                }
                for (GenericMove target : occupant.validMoves(this)) {
                    if (target.endangersKing()) {
                        continue;
                    }
                    toReturn.add(target);
                }
            }
        }
        return toReturn;
    }

    /**
     * Helper method which initializes fields to their default values.
     *
     * @param active the color whose turn it is
     */
    private void initializeFields(Color active) {
        squares = new Square[SQUARES_PER_SIDE][SQUARES_PER_SIDE];
        for (int col = 0; col < SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < SQUARES_PER_SIDE; row++) {
                squares[col][row] = new Square(col, row);
            }
        }
        activePlayer = active;
        history = new History();
        possibleMoves = null;
        kings = new HashMap<>();
    }

    /**
     * Helper method which adds the inital pieces to the board.
     *
     * @param initialPieces the initial pieces to add
     */
    private void setupPieces(List<Piece> initialPieces) {
        for (Piece piece : initialPieces) {
            square(piece.position().x, piece.position().y).setOccupant(piece);
            if (piece instanceof King) {
                kings.put(piece.color, (King) piece);
            }
        }
    }

    /**
     * Method which returns a deep copy of this Board.
     *
     * @return a deep copy of this Board
     */
    public Board copyOf() {
        List<Piece> occupants = new LinkedList<>();

        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                Piece occupant = square(col, row).occupant();
                if (occupant != null) {
                    Piece copy = occupant.copyOf();
                    occupants.add(copy);
                }
            }
        }

        Board toReturn = new Board(occupants, activePlayer);

        return toReturn;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard;

import chessboard.moves.GenericMove;
import chessboard.moves.MoveFactory;
import chessboard.moves.NormalMove;
import chessboard.pieces.Bishop;
import chessboard.pieces.King;
import chessboard.pieces.Knight;
import chessboard.pieces.Pawn;
import chessboard.pieces.Piece;
import chessboard.pieces.Queen;
import chessboard.pieces.Rook;
import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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

    //the move history
    private Stack<GenericMove> moveHistory;
    //the array of pieces on the board

    private Piece[][] pieces;

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
     * Method which checks whether the active player can castle, which should be
     * true only if neither the king nor the left rook has moved, the king is
     * not in check, and the space to the side of the king is not under threat,
     * and none of the spaces required are occupied.
     *
     * @param direction the direction in which to castle; must be east or west
     * @param kingColor the color of the king trying to castle
     * @return true if the active player can castle left, false otherwise
     */
    public boolean canCastle(Direction direction, Color kingColor) {
        if (kingColor != activePlayer) {
            return false;
        }

        if (direction != Direction.WEST && direction != Direction.EAST) {
            return false;
        }

        int xIndex = 4;
        Point kingPoint = new Point(xIndex, activePlayer.homeRow());
        Piece kingSquareOccupant = occupant(kingPoint.x, kingPoint.y);
        if (kingSquareOccupant == null || kingSquareOccupant.hasMoved()) {
            return false;
        }

        xIndex = xIndex + direction.x();
        if (occupant(xIndex, activePlayer.homeRow()) != null) {
            return false;
        }

        xIndex = xIndex + direction.x();
        if (occupant(xIndex, activePlayer.homeRow()) != null) {
            return false;
        }

        if (MoveFactory.create(this, kingSquareOccupant, kingPoint.x, kingPoint.y).endangersKing()) {
            return false;
        }

        kingPoint = new Point(kingPoint.x + direction.x(), kingPoint.y);
        if (MoveFactory.create(this, kingSquareOccupant, kingPoint.x, kingPoint.y).endangersKing()) {
            return false;
        }

        while (inBounds(xIndex + direction.x(), 0)) {
            xIndex = xIndex + direction.x();
        }
        Piece rookSquareOccupant = occupant(xIndex, activePlayer.homeRow());
        if (rookSquareOccupant == null || rookSquareOccupant.hasMoved()) {
            return false;
        }

        return true;
    }

    /**
     * Helper method which locates the king of a specific color.
     *
     * @param color the color of king to locate
     * @return the requested King
     */
    public King findKing(Color color) {
        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                Piece occupant = occupant(col, row);
                if (occupant != null && occupant instanceof King && occupant.color == color) {
                    return (King) occupant;
                }
            }
        }
        return null;
    }

    /**
     * Method which returns the player whose turn it is.
     *
     * @return the color whose turn it is
     */
    public Color getActivePlayer() {
        return activePlayer;
    }

    /**
     * Method which changes control of the turn to the next player.
     */
    public void passTurn() {
        activePlayer = activePlayer.enemy();
    }

    /**
     * Method which checks the current board state for the king being
     * threatened. This is used to determine move validity.
     *
     * @param toCheck the King whose safety should be checked
     * @return true if the king is under threat, false otherwise
     */
    public boolean kingThreatened(King toCheck) {
        //knights
        Piece occupant;
        Point[] potentialKnightThreats = {
            new Point(toCheck.position().x - 2, toCheck.position().y - 1),
            new Point(toCheck.position().x - 1, toCheck.position().y - 2),
            new Point(toCheck.position().x + 2, toCheck.position().y - 1),
            new Point(toCheck.position().x + 1, toCheck.position().y - 2),
            new Point(toCheck.position().x - 2, toCheck.position().y + 1),
            new Point(toCheck.position().x - 1, toCheck.position().y + 2),
            new Point(toCheck.position().x + 2, toCheck.position().y + 1),
            new Point(toCheck.position().x + 1, toCheck.position().y + 2)
        };
        for (Point potentialKnightThreat : potentialKnightThreats) {
            if (inBounds(potentialKnightThreat.x, potentialKnightThreat.y)) {
                occupant = occupant(potentialKnightThreat.x, potentialKnightThreat.y);
                if (occupant != null && occupant instanceof Knight && occupant.color != toCheck.color) {
                    return true;
                }
            }
        }

        //pawns, bishops, rooks, queens, and kings
        for (Direction dir : Direction.values()) {
            int col = toCheck.position().x + dir.x();
            int row = toCheck.position().y + dir.y();
            occupant = null;
            while (inBounds(col, row)) {
                if (occupant(col, row) != null) {
                    occupant = occupant(col, row);
                    break;
                }
                col = col + dir.x();
                row = row + dir.y();
            }
            if (occupant != null && occupant.color != toCheck.color) {
                if (occupant instanceof Queen) {
                    return true;
                } else if (occupant instanceof King) {
                    int deltaX = Math.abs(occupant.position().x - toCheck.position().x);
                    int deltaY = Math.abs(occupant.position().y - toCheck.position().y);
                    if (deltaX <= 1 & deltaY <= 1) {
                        return true;
                    }
                } else if (occupant instanceof Pawn) {
                    int deltaX = Math.abs(occupant.position().x - toCheck.position().x);
                    int yAfterCapture = occupant.position().y + occupant.color.forwardDirection().y();
                    if (yAfterCapture == toCheck.position().y && deltaX == 1) {
                        return true;
                    }
                } else if (occupant instanceof Rook) {
                    if (dir == Direction.NORTH || dir == Direction.SOUTH || dir == Direction.EAST || dir == Direction.WEST) {
                        return true;
                    }
                } else if (occupant instanceof Bishop) {
                    if (dir == Direction.NORTHWEST || dir == Direction.SOUTHWEST || dir == Direction.NORTHEAST || dir == Direction.SOUTHEAST) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Method which returns the occupant of the given position.
     *
     * @param xPosition the x position to check
     * @param yPosition the y position to check
     * @return the Piece at the passed position
     */
    public Piece occupant(int xPosition, int yPosition) {
        return pieces[xPosition][yPosition];
    }

    /**
     * Sets the occupant of the given position to the passed Piece.
     *
     * @param xPosition the x position to set
     * @param yPosition the y position to set
     * @param occupant the Piece to set in the position
     */
    public void setOccupant(int xPosition, int yPosition, Piece occupant) {
        pieces[xPosition][yPosition] = occupant;
    }

    /**
     * Method which returns a list of past moves made on the board.
     *
     * @return a list of past moves made on the board
     */
    public GenericMove lastMove() {
        if (moveHistory.isEmpty()) {
            return null;
        }
        return moveHistory.peek();
    }

    public void removeLastMove() {
        moveHistory.pop();
    }

    public void addToHistory(GenericMove move) {
        moveHistory.add(move);
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
                Piece occupant = occupant(col, row);
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
        pieces = new Piece[SQUARES_PER_SIDE][SQUARES_PER_SIDE];
        activePlayer = active;
        moveHistory = new Stack<>();
        possibleMoves = null;
    }

    /**
     * Helper method which adds the inital pieces to the board.
     *
     * @param initialPieces the initial pieces to add
     */
    private void setupPieces(List<Piece> initialPieces) {
        for (Piece piece : initialPieces) {
            pieces[piece.position().x][piece.position().y] = piece;
        }
    }
}

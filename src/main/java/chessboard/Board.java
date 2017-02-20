/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard;

import chessboard.moves.GenericMove;
import chessboard.moves.Move;
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
    private Stack<Move> moveHistory;
    //the array of pieces on the board

    private Piece[][] pieces;

    //a list of possible moves from this position
    private List<Move> possibleMoves;

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

        if (moveSacksKing(new Move(this, kingSquareOccupant, kingPoint))) {
            return false;
        }

        kingPoint = new Point(kingPoint.x + direction.x(), kingPoint.y);
        if (moveSacksKing(new Move(this, kingSquareOccupant, kingPoint))) {
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
     * Method which returns a deep copy of this Board.
     *
     * @return a deep copy of this Board
     */
    public Board copyOf() {
        List<Piece> occupants = new LinkedList<>();

        for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
                Piece occupant = occupant(col, row);
                if (occupant != null) {
                    Piece copy = occupant.copyOf();
                    occupants.add(copy);
                }
            }
        }

        Board toReturn = new Board(occupants, activePlayer);

        return toReturn;
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
     * Method which attempts to execute a given move on this board. Returns
     * whether the move was a success. If so, the move will be made and added to
     * the move history.
     *
     * @param toExecute the move to make
     * @return true if the move succeeded, false otherwise
     */
    public boolean move(Move toExecute) {
        if (validMoves().contains(toExecute)) {
            updateBoard(toExecute);
            activePlayer = activePlayer.enemy();
            moveHistory.add(toExecute);
            possibleMoves = null;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method which checks whether a given move would allow the king to be
     * captured, which would make it illegal. Sets up the board as if the move
     * had been made then delegats the actual check to another method.
     *
     * @param toExecute the move to check
     * @return true if the move is illegal because it allows capture of the
     * king, false otherwise
     */
    public boolean moveSacksKing(Move toExecute) {
        updateBoard(toExecute);

        King king = findKing(toExecute.piece.color);
        boolean toReturn;
        if (king == null) {
            toReturn = false;
        } else {
            toReturn = kingThreatened(king);
        }

        revertBoard(toExecute);

        return toReturn;
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
    public Move lastMove() {
        if (moveHistory.isEmpty()) {
            return null;
        }
        return moveHistory.peek();
    }

    public void removeLastMove() {
        moveHistory.pop();
    }

    //public void addToHistory(GenericMove move) {
    //moveHistory.add(move);
    //}
    /**
     * Method which attempts to revert a given move on this board. The move will
     * be undone and removed from the history.
     */
    public void revertMove() {
        Move lastMove = moveHistory.pop();
        revertBoard(lastMove);
        activePlayer = activePlayer.enemy();
        possibleMoves = null;
    }

    /**
     * Method which returns a list of possible moves from this board state.
     *
     * @return a list of possible moves from this board state
     */
    public List<Move> validMoves() {
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
    private List<Move> calculatePossibleMoves(Color player) {
        List<Move> toReturn = new LinkedList<>();
        for (int col = 0; col < SQUARES_PER_SIDE; col++) {
            for (int row = 0; row < SQUARES_PER_SIDE; row++) {
                Piece occupant = occupant(col, row);
                if (occupant == null || occupant.color != player) {
                    continue;
                }
                for (Point target : occupant.validMoves(this)) {
                    Move potential = new Move(this, occupant, target);
                    if (moveSacksKing(potential)) {
                        continue;
                    }
                    toReturn.add(potential);
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
     * Helper method which reverts the board and the Piece that was moved to
     * reflect the previous game state. Includes handling moves that affect more
     * than one piece, like en passant, queening, and castling.
     *
     * @param toExecute the move to make
     */
    private void revertBoard(Move toRevert) {
        //handle normal case
        Piece mover = toRevert.piece;
        pieces[toRevert.from.x][toRevert.from.y] = mover;
        mover.setPosition(toRevert.from);
        mover.subMoveCount();

        //handle en passant
        if (toRevert.enPassant) {
            pieces[toRevert.to.x][toRevert.to.y - mover.color.forwardDirection().y()] = toRevert.target;
            pieces[toRevert.to.x][toRevert.to.y] = null;
        } else {
            pieces[toRevert.to.x][toRevert.to.y] = toRevert.target;
        }

        //handle castle
        int homeRow = mover.color.homeRow();
        if (toRevert.castle) {
            if (toRevert.to.x > 4) {
                pieces[7][homeRow] = pieces[5][homeRow];
                pieces[5][homeRow] = null;
                pieces[7][homeRow].setPosition(new Point(7, homeRow));
            } else {
                pieces[0][homeRow] = pieces[3][homeRow];
                pieces[3][homeRow] = null;
                pieces[0][homeRow].setPosition(new Point(0, homeRow));
            }
        }
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

    /**
     * Helper method which updates the board and the Piece that was moved to
     * reflect the new game state. Includes handling moves that affect more than
     * one piece, like en passant, queening, and castling.
     *
     * @param toExecute the move to make
     */
    private void updateBoard(Move toExecute) {
        //handle normal case
        pieces[toExecute.from.x][toExecute.from.y] = null;
        pieces[toExecute.to.x][toExecute.to.y] = toExecute.piece;
        Piece mover = toExecute.piece;
        mover.setPosition(toExecute.to);
        mover.addMoveCount();

        //handle en passant
        if (toExecute.enPassant) {
            pieces[toExecute.to.x][toExecute.to.y + 1] = null;
            pieces[toExecute.to.x][toExecute.to.y - 1] = null;
        }

        //handle castle
        int homeRow = mover.color.homeRow();
        if (toExecute.castle) {
            if (toExecute.to.x > 4) {
                pieces[5][homeRow] = pieces[7][homeRow];
                pieces[7][homeRow] = null;
                pieces[5][homeRow].setPosition(new Point(5, homeRow));
            } else {
                pieces[3][homeRow] = pieces[0][homeRow];
                pieces[0][homeRow] = null;
                pieces[3][homeRow].setPosition(new Point(3, homeRow));
            }
        }

        //handle queening
        if (mover instanceof Pawn && mover.position().y == mover.color.queenRow()) {
            pieces[mover.position().x][mover.position().y] = new Queen(mover.color, mover.position().x, mover.position().y);
        }
    }

}

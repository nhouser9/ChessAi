/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard.notation;

import chess.chessboard.Board;
import chess.chessboard.Color;
import chess.chessboard.Direction;
import chess.chessboard.moves.GenericMove;
import chess.chessboard.moves.InitialPawnMove;
import chess.chessboard.pieces.King;
import chess.chessboard.pieces.Piece;

/**
 * <pre>
 * Class which handles building a fen representation of a board. More on fen
 * chess notation:
 * https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
 *
 * Summary of the article:
 *
 * A FEN record contains six fields. The separator between fields is a space.
 * The fields are:
 *
 * 1. Piece placement (from white's perspective). Each rank is described,
 * starting with rank 8 and ending with rank 1; within each rank, the contents
 * of each square are described from file "a" through file "h". Following the
 * Standard Algebraic Notation (SAN), each piece is identified by a single
 * letter taken from the standard English names (pawn = "P", knight = "N",
 * bishop = "B", rook = "R", queen = "Q" and king = "K"). White pieces are
 * designated using upper-case letters ("PNBRQK") while black pieces use
 * lowercase ("pnbrqk"). Empty squares are noted using digits 1 through 8 (the
 * number of empty squares), and "/" separates ranks.
 *
 * 2. Active color. "w" means White moves next, "b" means Black.
 *
 * 3. Castling availability. If neither side can castle, this is "-". Otherwise,
 * this has one or more letters: "K" (White can castle kingside), "Q" (White can
 * castle queenside), "k" (Black can castle kingside), and/or "q" (Black can
 * castle queenside).
 *
 * 4. En passant target square in algebraic notation. If there's no en passant
 * target square, this is "-". If a pawn has just made a two-square move, this
 * is the position "behind" the pawn. This is recorded regardless of whether
 * there is a pawn in position to make an en passant capture.
 *
 * 5. Halfmove clock: This is the number of halfmoves since the last capture or
 * pawn advance. This is used to determine if a draw can be claimed under the
 * fifty-move rule.
 *
 * 6. Fullmove number: The number of the full move. It starts at 1, and is
 * incremented after Black's move.
 * </pre>
 *
 * @author Nick Houser
 */
public class Fen {

    //the maximum text length of a fen
    private static final int MAX_FEN_SIZE = 85;

    //the stringbuilder which will contain the fen
    private StringBuilder fen;

    /**
     * Constructor which builds the fen.
     *
     * @param board
     */
    public Fen(Board board) {
        fen = new StringBuilder(MAX_FEN_SIZE);
        addPieces(board);
        fen.append(" ");
        addActivePlayer(board);
        fen.append(" ");
        addAvailableCastles(board);
        fen.append(" ");
        addEnPassantTarget(board);
        fen.append(" ");
        addHalfMoveClock(board);
        fen.append(" ");
        addFullMoveClock(board);
    }

    /**
     * Helper method which adds the first part of a fen, which is the pieces on
     * the board.
     *
     * @param board the board to build a fen from
     */
    private void addPieces(Board board) {
        for (int row = 0; row < Board.SQUARES_PER_SIDE; row++) {
            int empties = 0;

            for (int col = 0; col < Board.SQUARES_PER_SIDE; col++) {
                Piece occupant = board.square(col, row).occupant();

                if (occupant == null) {
                    empties = empties + 1;

                    if (col == Board.SQUARES_PER_SIDE - 1 && empties != 0) {
                        fen.append(empties);
                    }
                } else {
                    if (empties > 0) {
                        fen.append(empties);
                        empties = 0;
                    }

                    fen.append(occupant.asFen());
                }
            }

            if (row != Board.SQUARES_PER_SIDE - 1) {
                fen.append("/");
            }
        }
    }

    /**
     * Helper method which adds the second part of a fen, which is a
     * representation of the active player.
     *
     * @param board the board to build a fen from
     */
    private void addActivePlayer(Board board) {
        fen.append(Character.toLowerCase(board.activePlayer().toString().charAt(0)));
    }

    /**
     * Helper method which adds the third part of a fen, which a description of
     * whether the kings can still castle and if so, on which side of the board.
     *
     * @param board the board to build a fen from
     */
    private void addAvailableCastles(Board board) {
        King whiteKing = board.findKing(Color.WHITE);
        boolean whiteKingside = whiteKing.castleStillLegal(board, Direction.EAST);
        boolean whiteQueenside = whiteKing.castleStillLegal(board, Direction.WEST);

        King blackKing = board.findKing(Color.BLACK);
        boolean blackKingside = blackKing.castleStillLegal(board, Direction.EAST);
        boolean blackQueenside = blackKing.castleStillLegal(board, Direction.WEST);

        if (!(whiteKingside || whiteQueenside || blackKingside || blackQueenside)) {
            fen.append("-");
            return;
        }

        if (whiteKingside) {
            fen.append("K");
        }

        if (whiteQueenside) {
            fen.append("Q");
        }

        if (blackKingside) {
            fen.append("k");
        }

        if (blackQueenside) {
            fen.append("q");
        }
    }

    /**
     * Helper method which adds the fourth part of a fen, which is the square
     * which can be targeted by en passant, or, in other words, the square
     * behind the last pawn move if that move was a double move.
     *
     * @param board the board to build a fen from
     */
    private void addEnPassantTarget(Board board) {
        GenericMove lastMove = board.history().last();

        if (lastMove != null && lastMove instanceof InitialPawnMove) {
            fen.append(((InitialPawnMove) lastMove).enPassantTarget.toString());
        } else {
            fen.append("-");
        }
    }

    /**
     * Helper method which adds the fifth part of a fen, which is the half move
     * clock, a number representing the number of moves since a piece has been
     * captured or a pawn has been pushed. This is used for calculating whether
     * games will be drawn because of length.
     *
     *
     * @param board the board to build a fen from
     */
    private void addHalfMoveClock(Board board) {
        fen.append(board.history().halfMoveClock());
    }

    /**
     * Helper method which adds the sixth part of a fen, which is the full move
     * clock, or the number of full turns that have passed in the game.
     *
     * @param board the board to build a fen from
     */
    private void addFullMoveClock(Board board) {
        fen.append(board.history().fullMoveClock());
    }

    /**
     * Override of the toString method, which returns the full fen as a string.
     *
     * @return the fen as a string
     */
    @Override
    public String toString() {
        return fen.toString();
    }
}

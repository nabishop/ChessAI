package Engine;

import Models.*;

import java.util.ArrayList;
import java.util.List;

public class MoveEngine {
    private final Board boardObj;

    public MoveEngine(Board boardObj){
        this.boardObj = boardObj;
    }

    public List<Move> getAllPossibleMoves() {
        List<Move> moves = new ArrayList<>();
        Piece[][] board = boardObj.getBoard();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                Piece piece = board[i][j];
                if (piece == null) {
                    continue;
                }

                if (piece instanceof Bishop) {
                    moves.addAll(getAllBishopMoves(board, piece, i, j));
                }
                if (piece instanceof King) {
                    moves.addAll(getAllKingMoves(board, piece, i, j));
                }
                if (piece instanceof Knight) {
                    moves.addAll(getAllKnightMoves(board, piece, i, j));
                }
                if (piece instanceof Pawn) {
                    moves.addAll(getAllPawnMoves(board, piece, i, j));
                }
                if (piece instanceof Queen) {
                    moves.addAll(getAllQueenMoves(board, piece, i, j));
                }
                if (piece instanceof Rook) {
                    moves.addAll(getAllRookMoves(board, piece, i, j));
                }
            }
        }



        return moves;
    }

    private List<Move> getAllBishopMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        List<Move> moves = new ArrayList<>();

        return moves;
    }

    private List<Move> getAllKingMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        List<Move> moves = new ArrayList<>();

        return moves;
    }

    private List<Move> getAllKnightMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        List<Move> moves = new ArrayList<>();

        return moves;
    }

    private List<Move> getAllPawnMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        List<Move> moves = new ArrayList<>();

        return moves;
    }

    private List<Move> getAllQueenMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        List<Move> moves = new ArrayList<>();

        return moves;
    }

    private List<Move> getAllRookMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        List<Move> moves = new ArrayList<>();

        return moves;
    }

    private List<Move> getDiagMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        List<Move> moves = new ArrayList<>();

        for (int i = startI, j = startJ; i < board.length && j < board.length; i++, j++) {
            Move move = evaluateAddMove(board, piece, startI, startJ, i, j);
            if (move == null || move.isCapture()) {
                break;
            }
            moves.add(move);
        }

        return moves;
    }

    private Move evaluateAddMove(Piece[][] board, Piece piece, int startI, int startJ, int i, int j) {
        Piece newPosPiece = board[i][j];
        // free move
        if (newPosPiece == null) {
            return new Move(startI, startJ, i, j, false);
        }
        // capturing
        if (!newPosPiece.getColor().equals(piece.getColor())) {
            return new Move(startI, startJ, i, j, true);
        }
        // dead end
        if (newPosPiece.getColor().equals(piece.getColor())) {
            return null;
        }
    }
}

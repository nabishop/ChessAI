package Engine;

import Models.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoveEngine {
    private final Board boardObj;

    public MoveEngine(Board boardObj){
        this.boardObj = boardObj;
    }

    public Move getNextBestMove() {
        List<Move> moves = getAllPossibleMoves();
        moves.removeAll(Collections.singleton(null));
        Collections.sort(moves);
        return moves.get(0);
    }

    private List<Move> getAllPossibleMoves() {
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
        return getDiagMoves(board, piece, startI, startJ);
    }

    private List<Move> getAllKingMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        return Stream.of(
                // up
                evaluateAddMove(board, piece, startI, startJ, startI + 1, startJ),
                // up right
                evaluateAddMove(board, piece, startI, startJ, startI + 1, startJ + 1),
                // up left
                evaluateAddMove(board, piece, startI, startJ, startI + 1, startJ - 1),
                // left
                evaluateAddMove(board, piece, startI, startJ, startI, startJ - 1),
                // right
                evaluateAddMove(board, piece, startI, startJ, startI, startJ + 1),
                // down
                evaluateAddMove(board, piece, startI, startJ, startI - 1, startJ),
                // down right
                evaluateAddMove(board, piece, startI, startJ, startI - 1, startJ + 1),
                // down left
                evaluateAddMove(board, piece, startI, startJ, startI - 1, startJ - 1)
        ).collect(Collectors.toList());
    }

    private List<Move> getAllKnightMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        List<Move> moves = new ArrayList<>();
        // up 2 right 1
        // up 2 left 1
        // down 2 right 1
        // down 2 left 1
        // right 2 up 1
        // right 2 down 1
        // left 2 up 1
        // left 2 down 1

        return Stream.of(
                evaluateAddMove(board, piece, startI, startJ, startI + 2, startJ + 1),
                evaluateAddMove(board, piece, startI, startJ, startI + 2, startJ - 1),
                evaluateAddMove(board, piece, startI, startJ, startI - 2, startJ + 1),
                evaluateAddMove(board, piece, startI, startJ, startI - 2, startJ - 1),
                evaluateAddMove(board, piece, startI, startJ, startI + 1, startJ + 2),
                evaluateAddMove(board, piece, startI, startJ, startI - 1, startJ + 2),
                evaluateAddMove(board, piece, startI, startJ, startI + 1, startJ - 2),
                evaluateAddMove(board, piece, startI, startJ, startI - 1, startJ - 2)
        ).collect(Collectors.toList());
    }

    private List<Move> getAllPawnMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        List<Move> moves = new ArrayList<>();
        Pawn pawn = (Pawn) piece;

        // up 2 if not moved
        // up 1 if moved
        // diag right if opposing piece
        // diag left if opposing piece
        moves.add(evaluateAddMove(board, piece, startI, startJ, startI + 1, startJ));
        if (!pawn.isMoved()) {
            moves.add(evaluateAddMove(board, piece, startI, startJ, startI + 2, startJ));
        }
        if (startI + 1 < board.length && startJ + 1 < board.length && board[startI + 1][startJ + 1] != null) {
            moves.add(evaluateAddMove(board, piece, startI, startJ, startI + 1, startJ + 1));
        }
        if (startI + 1 < board.length && startJ - 1 > 0 && board[startI + 1][startJ - 1] != null) {
            moves.add(evaluateAddMove(board, piece, startI, startJ, startI + 1, startJ - 1));
        }

        return moves;
    }

    private List<Move> getAllQueenMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        List<Move> allStraight = getStraightMoves(board, piece, startI, startJ);
        List<Move> allDiag = getDiagMoves(board, piece, startI, startJ);
        return Stream.concat(allStraight.stream(), allDiag.stream())
                .collect(Collectors.toList());
    }

    private List<Move> getAllRookMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        return getStraightMoves(board, piece, startI, startJ);
    }

    private List<Move> getDiagMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        List<Move> moves = new ArrayList<>();

        // up right
        for (int i = startI + 1, j = startJ + 1; i < board.length && j < board.length; i++, j++) {
            Move move = evaluateAddMove(board, piece, startI, startJ, i, j);
            if (move == null || move.isCapture()) {
                break;
            }
            moves.add(move);
        }
        // up left
        for (int i = startI + 1, j = startJ - 1; i < board.length && j >= 0; i++, j--) {
            Move move = evaluateAddMove(board, piece, startI, startJ, i, j);
            if (move == null || move.isCapture()) {
                break;
            }
            moves.add(move);
        }
        // bottom right
        for (int i = startI - 1, j = startJ + 1; i >= 0 && j < board.length; i--, j++) {
            Move move = evaluateAddMove(board, piece, startI, startJ, i, j);
            if (move == null || move.isCapture()) {
                break;
            }
            moves.add(move);
        }
        // bottom left
        for (int i = startI - 1, j = startJ - 1; i >= 0 && j >= 0; i--, j--) {
            Move move = evaluateAddMove(board, piece, startI, startJ, i, j);
            if (move == null || move.isCapture()) {
                break;
            }
            moves.add(move);
        }

        return moves;
    }

    private List<Move> getStraightMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        List<Move> moves = new ArrayList<>();

        // up
        for (int i = startI + 1; i < board.length; i++) {
            Move move = evaluateAddMove(board, piece, startI, startJ, i, startJ);
            if (move == null || move.isCapture()) {
                break;
            }
            moves.add(move);
        }
        // down
        for (int i = startI - 1; i >= 0; i--) {
            Move move = evaluateAddMove(board, piece, startI, startJ, i, startJ);
            if (move == null || move.isCapture()) {
                break;
            }
            moves.add(move);
        }
        // right
        for (int j = startJ + 1; j < board.length; j++) {
            Move move = evaluateAddMove(board, piece, startI, startJ, startI, j);
            if (move == null || move.isCapture()) {
                break;
            }
            moves.add(move);
        }
        // left
        for (int j = startJ - 1; j >= 0; j--) {
            Move move = evaluateAddMove(board, piece, startI, startJ, startI, j);
            if (move == null || move.isCapture()) {
                break;
            }
            moves.add(move);
        }

        return moves;
    }

    private Move evaluateAddMove(Piece[][] board, Piece piece, int startI, int startJ, int i, int j) {
        if (i < 0 || j < 0 || i >= board.length || j >= board.length) {
            return null;
        }
        Piece newPosPiece = board[i][j];
        // free move
        if (newPosPiece == null) {
            return new Move(startI, startJ, i, j, false);
        }
        // capturing
        else if (!newPosPiece.getColor().equals(piece.getColor())) {
            return new Move(startI, startJ, i, j, true);
        }
        // dead end
        else {
            return null;
        }
    }
}

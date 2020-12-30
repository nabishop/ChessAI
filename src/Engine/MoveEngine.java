package Engine;

import Models.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoveEngine {
    private final Board boardObj;
    private final String aiColor;
    private final String otherColor;

    public MoveEngine(Board boardObj, String aiColor){
        this.boardObj = boardObj;
        this.aiColor = aiColor;
        this.otherColor = aiColor.equals("white") ? "black" : "white";
    }

    public Move getNextBestMove() {
        int depth = 6;

        MovePossibility nextMove = getNextBestMoveRecurse(this.aiColor, depth, boardObj);
        return nextMove.getMove();
    }

    private MovePossibility getNextBestMoveRecurse(String color, int depth, Board recurseBoard) {
        if (depth == 0) {
            return new MovePossibility(null, aiColor, otherColor, recurseBoard);
        }
        // maximize AI
        if (color.equals(aiColor)) {
            int max_value = Integer.MIN_VALUE;
            MovePossibility maxMovePos = null;
            for (MovePossibility maxPos : getAllPossibleMoves(color, recurseBoard)) {
                MovePossibility  minPos = getNextBestMoveRecurse(this.otherColor, depth - 1, maxPos.getBoard());
                int value = minPos.getAiScore() - minPos.getOtherScore();
                if (value > max_value) {
                    max_value = value;
                    maxMovePos = maxPos;
                }
            }
            return maxMovePos;
        }
        // minimize other
        else {
            int min_value = Integer.MAX_VALUE;
            MovePossibility minMovePos = null;
            for (MovePossibility minPos : getAllPossibleMoves(color, recurseBoard)) {
                MovePossibility  maxPos = getNextBestMoveRecurse(this.aiColor, depth - 1, minPos.getBoard());
                int value = maxPos.getAiScore() - maxPos.getOtherScore();
                if (value < min_value) {
                    min_value = value;
                    minMovePos = minPos;
                }
            }
            return minMovePos;
        }
    }

    private List<MovePossibility> getAllPossibleMoves(String color, Board recuseBoard) {
        List<MovePossibility> moves = new ArrayList<>();
        Piece[][] board = recuseBoard.getBoard();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                Piece piece = board[i][j];
                if (piece == null || !piece.getColor().equals(color)) {
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
                    moves.addAll(getAllPawnMoves(board, piece, i, j, color));
                }
                if (piece instanceof Queen) {
                    moves.addAll(getAllQueenMoves(board, piece, i, j));
                }
                if (piece instanceof Rook) {
                    moves.addAll(getAllRookMoves(board, piece, i, j));
                }
            }
        }

        moves.removeAll(Collections.singleton(null));
        return moves;
    }

    private List<MovePossibility> getAllBishopMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        return getDiagMoves(board, piece, startI, startJ);
    }

    private List<MovePossibility> getAllKingMoves(Piece[][] board, Piece piece, int startI, int startJ) {
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

    private List<MovePossibility> getAllKnightMoves(Piece[][] board, Piece piece, int startI, int startJ) {
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

    private List<MovePossibility> getAllPawnMoves(Piece[][] board, Piece piece, int startI, int startJ, String color) {
        List<MovePossibility> moves = new ArrayList<>();
        Pawn pawn = (Pawn) piece;
        int moveDirection = color.equals("white") ? -1 : 1;
        int twoMove = startI + (2 * moveDirection);
        int oneMove = startI + (moveDirection);

        // up 2 if not moved
        if (!pawn.isMoved() && (twoMove < board.length && twoMove >= 0) && board[oneMove][startJ] == null && board[twoMove][startJ] == null) {
            moves.add(evaluateAddMove(board, piece, startI, startJ, twoMove, startJ));
        }
        // up 1 if moved
        boolean oneMoveInBound = oneMove < board.length && oneMove >= 0;
        if (oneMoveInBound && board[startI][startJ] == null) {
            moves.add(evaluateAddMove(board, piece, startI, startJ, oneMove, startJ));
        }
        // diag right if opposing piece
        if (oneMoveInBound && startJ + 1 < board.length && board[oneMove][startJ + 1] != null) {
            moves.add(evaluateAddMove(board, piece, startI, startJ, oneMove, startJ + 1));
        }
        // diag left if opposing piece
        if (oneMoveInBound && startJ - 1 > 0 && board[oneMove][startJ - 1] != null) {
            moves.add(evaluateAddMove(board, piece, startI, startJ, oneMove, startJ - 1));
        }

        return moves;
    }

    private List<MovePossibility> getAllQueenMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        List<MovePossibility> allStraight = getStraightMoves(board, piece, startI, startJ);
        List<MovePossibility> allDiag = getDiagMoves(board, piece, startI, startJ);
        return Stream.concat(allStraight.stream(), allDiag.stream())
                .collect(Collectors.toList());
    }

    private List<MovePossibility> getAllRookMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        return getStraightMoves(board, piece, startI, startJ);
    }

    private List<MovePossibility> getDiagMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        List<MovePossibility> moves = new ArrayList<>();

        // up right
        for (int i = startI + 1, j = startJ + 1; i < board.length && j < board.length; i++, j++) {
            MovePossibility move = evaluateAddMove(board, piece, startI, startJ, i, j);
            moves.add(move);
            if (move == null || move.getMove().getMoveValue() > 0) {
                break;
            }
        }
        // up left
        for (int i = startI + 1, j = startJ - 1; i < board.length && j >= 0; i++, j--) {
            MovePossibility move = evaluateAddMove(board, piece, startI, startJ, i, j);
            moves.add(move);
            if (move == null || move.getMove().getMoveValue() > 0) {
                break;
            }
        }
        // bottom right
        for (int i = startI - 1, j = startJ + 1; i >= 0 && j < board.length; i--, j++) {
            MovePossibility move = evaluateAddMove(board, piece, startI, startJ, i, j);
            moves.add(move);
            if (move == null || move.getMove().getMoveValue() > 0) {
                break;
            }
        }
        // bottom left
        for (int i = startI - 1, j = startJ - 1; i >= 0 && j >= 0; i--, j--) {
            MovePossibility move = evaluateAddMove(board, piece, startI, startJ, i, j);
            moves.add(move);
            if (move == null || move.getMove().getMoveValue() > 0) {
                break;
            }
        }

        return moves;
    }

    private List<MovePossibility> getStraightMoves(Piece[][] board, Piece piece, int startI, int startJ) {
        List<MovePossibility> moves = new ArrayList<>();

        // up
        for (int i = startI + 1; i < board.length; i++) {
            MovePossibility move = evaluateAddMove(board, piece, startI, startJ, i, startJ);
            moves.add(move);
            if (move == null || move.getMove().getMoveValue() > 0) {
                break;
            }
        }
        // down
        for (int i = startI - 1; i >= 0; i--) {
            MovePossibility move = evaluateAddMove(board, piece, startI, startJ, i, startJ);
            moves.add(move);
            if (move == null || move.getMove().getMoveValue() > 0) {
                break;
            }
        }
        // right
        for (int j = startJ + 1; j < board.length; j++) {
            MovePossibility move = evaluateAddMove(board, piece, startI, startJ, startI, j);
            moves.add(move);
            if (move == null || move.getMove().getMoveValue() > 0) {
                break;
            }
        }
        // left
        for (int j = startJ - 1; j >= 0; j--) {
            MovePossibility move = evaluateAddMove(board, piece, startI, startJ, startI, j);
            moves.add(move);
            if (move == null || move.getMove().getMoveValue() > 0) {
                break;
            }
        }

        return moves;
    }

    private MovePossibility evaluateAddMove(Piece[][] board, Piece piece, int startI, int startJ, int i, int j) {
        if (i < 0 || j < 0 || i >= board.length || j >= board.length) {
            return null;
        }
        Piece newPosPiece = board[i][j];
        Move newMove = null;
        // free move
        if (newPosPiece == null) {
            newMove = new Move(startI, startJ, i, j, 0);
        }
        // capturing
        else if (!newPosPiece.getColor().equals(piece.getColor())) {
            newMove = new Move(startI, startJ, i, j, newPosPiece.getValue());
        }

        if (newMove == null) {
            return null;
        }

        Board boardAfterMove = new Board(copyBoard(board));
        boardAfterMove.makeMove(newMove, false);

        return new MovePossibility(newMove, aiColor, otherColor, boardAfterMove);
    }

    private Piece[][] copyBoard(Piece[][] old) {
        Piece[][] n = new Piece[old.length][old.length];
        for (int i = 0; i < old.length; i++) {
            System.arraycopy(old[i], 0, n[i], 0, old.length);
        }
        return n;
    }
}

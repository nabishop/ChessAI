package Engine;

import Models.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoveEngine {
    private final Board boardObj;
    private final String aiColor;
    private final Map<String, Double> boardMap;

    public MoveEngine(Board boardObj, String aiColor, Map<String, Double> boardMap) {
        this.boardObj = boardObj;
        this.aiColor = aiColor;
        this.boardMap = boardMap;
    }

    public MovePossibility getNextBestMove() {
        int depth = 4;
        List<MovePossibility> allPossibleMoves = getAllPossibleMoves(this.aiColor, this.boardObj);
        Collections.shuffle(allPossibleMoves);

        double bestMoveValue = -Double.MAX_VALUE;
        MovePossibility bestMoveFound = null;
        String otherColor = getOtherColor(this.aiColor);

        for (MovePossibility movePossibility : allPossibleMoves) {
            MovePossibility oppMove = minimaxAB(otherColor, false, depth - 1, movePossibility.getBoard(), -Double.MAX_VALUE, Double.MAX_VALUE);
            double value = oppMove.getScore();
            if (value >= bestMoveValue) {
                bestMoveValue = value;
                bestMoveFound = movePossibility;
            }
        }

        return bestMoveFound;
    }

    private MovePossibility minimaxAB(String nowMoving, boolean max, int depth, Board recurseBoard, double alpha, double beta) {

        String otherColor = getOtherColor(nowMoving);
        MovePossibility noMove = new MovePossibility(null, this.aiColor, nowMoving, recurseBoard, this.boardMap);

        if (depth == 0) {
            return noMove;
        }
        List<MovePossibility> allPossibleMoves = getAllPossibleMoves(nowMoving, recurseBoard);
        Collections.shuffle(allPossibleMoves);

        MovePossibility bestMove = null;
        double bestValue = max ? -Double.MAX_VALUE : Double.MAX_VALUE;

        for (MovePossibility move : allPossibleMoves) {
            MovePossibility possibleMove = minimaxAB(otherColor, !max, depth - 1, move.getBoard(), alpha, beta);

            double value = possibleMove.getScore();
            if (max) {
                if (value >= bestValue) {
                    bestValue = value;
                    bestMove = possibleMove;
                }
                alpha = Math.max(alpha, value);
            }
            // minimize other
            else {
                if (value <= bestValue) {
                    bestValue = value;
                    bestMove = possibleMove;
                }
                beta = Math.min(value, beta);
            }

            // AB Pruning
            if (beta <= alpha) {
                break;
            }
        }

        return bestMove == null ? noMove : bestMove;
    }

    public List<MovePossibility> getAllPossibleMoves(String color, Board recuseBoard) {
        List<MovePossibility> moves = new ArrayList<>();
        Piece[][] board = recuseBoard.getBoard();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                Piece piece = board[i][j];
                if (piece == null || !piece.getColor().equals(color)) {
                    continue;
                }

                if (piece instanceof Bishop) {
                    moves.addAll(getAllBishopMoves(board, piece, i, j, color));
                }
                if (piece instanceof King) {
                    moves.addAll(getAllKingMoves(board, piece, i, j, color));
                }
                if (piece instanceof Knight) {
                    moves.addAll(getAllKnightMoves(board, piece, i, j, color));
                }
                if (piece instanceof Pawn) {
                    moves.addAll(getAllPawnMoves(board, piece, i, j, color));
                }
                if (piece instanceof Queen) {
                    moves.addAll(getAllQueenMoves(board, piece, i, j, color));
                }
                if (piece instanceof Rook) {
                    moves.addAll(getAllRookMoves(board, piece, i, j, color));
                }
            }
        }

        moves.removeAll(Collections.singleton(null));

        // keep track if any of the moves can check the opponents king
        List<MovePossibility> checkKingMoves = new ArrayList<>();

        for (MovePossibility mp : moves) {
            if (mp.isOtherKingChecked()) {
                checkKingMoves.add(mp);
            }
        }

        // if any moves could check the opponent, only do those, assume opp will check you
        return checkKingMoves.size() > 0 ? checkKingMoves : moves;
    }

    private List<MovePossibility> getAllBishopMoves(Piece[][] board, Piece piece, int startI, int startJ, String color) {
        return getDiagMoves(board, piece, startI, startJ, color);
    }

    private List<MovePossibility> getAllKingMoves(Piece[][] board, Piece piece, int startI, int startJ, String color) {
        return Stream.of(
                // up
                evaluateAddMove(board, piece, startI, startJ, startI + 1, startJ, color),
                // up right
                evaluateAddMove(board, piece, startI, startJ, startI + 1, startJ + 1, color),
                // up left
                evaluateAddMove(board, piece, startI, startJ, startI + 1, startJ - 1, color),
                // left
                evaluateAddMove(board, piece, startI, startJ, startI, startJ - 1, color),
                // right
                evaluateAddMove(board, piece, startI, startJ, startI, startJ + 1, color),
                // down
                evaluateAddMove(board, piece, startI, startJ, startI - 1, startJ, color),
                // down right
                evaluateAddMove(board, piece, startI, startJ, startI - 1, startJ + 1, color),
                // down left
                evaluateAddMove(board, piece, startI, startJ, startI - 1, startJ - 1, color)
        ).collect(Collectors.toList());
    }

    private List<MovePossibility> getAllKnightMoves(Piece[][] board, Piece piece, int startI, int startJ, String color) {
        // up 2 right 1
        // up 2 left 1
        // down 2 right 1
        // down 2 left 1
        // right 2 up 1
        // right 2 down 1
        // left 2 up 1
        // left 2 down 1

        return Stream.of(
                evaluateAddMove(board, piece, startI, startJ, startI + 2, startJ + 1, color),
                evaluateAddMove(board, piece, startI, startJ, startI + 2, startJ - 1, color),
                evaluateAddMove(board, piece, startI, startJ, startI - 2, startJ + 1, color),
                evaluateAddMove(board, piece, startI, startJ, startI - 2, startJ - 1, color),
                evaluateAddMove(board, piece, startI, startJ, startI + 1, startJ + 2, color),
                evaluateAddMove(board, piece, startI, startJ, startI - 1, startJ + 2, color),
                evaluateAddMove(board, piece, startI, startJ, startI + 1, startJ - 2, color),
                evaluateAddMove(board, piece, startI, startJ, startI - 1, startJ - 2, color)
        ).collect(Collectors.toList());
    }

    private List<MovePossibility> getAllPawnMoves(Piece[][] board, Piece piece, int startI, int startJ, String color) {
        List<MovePossibility> moves = new ArrayList<>();
        Pawn pawn = (Pawn) piece;
        int moveDirection = color.equals(ModelConstants.WHITE_COLOR) ? -1 : 1;
        int twoMove = startI + (2 * moveDirection);
        int oneMove = startI + (moveDirection);

        // up 2 if not moved
        if (!pawn.isMoved() && (twoMove < board.length && twoMove >= 0) && board[oneMove][startJ] == null && board[twoMove][startJ] == null) {
            moves.add(evaluateAddMove(board, piece, startI, startJ, twoMove, startJ, color));
        }
        // up 1 if moved
        boolean oneMoveInBound = oneMove < board.length && oneMove >= 0;
        if (oneMoveInBound && board[oneMove][startJ] == null) {
            moves.add(evaluateAddMove(board, piece, startI, startJ, oneMove, startJ, color));
        }
        // diag right if opposing piece
        if (oneMoveInBound && startJ + 1 < board.length && board[oneMove][startJ + 1] != null) {
            moves.add(evaluateAddMove(board, piece, startI, startJ, oneMove, startJ + 1, color));
        }
        // diag left if opposing piece
        if (oneMoveInBound && startJ - 1 > 0 && board[oneMove][startJ - 1] != null) {
            moves.add(evaluateAddMove(board, piece, startI, startJ, oneMove, startJ - 1, color));
        }

        return moves;
    }

    private List<MovePossibility> getAllQueenMoves(Piece[][] board, Piece piece, int startI, int startJ, String color) {
        List<MovePossibility> allStraight = getStraightMoves(board, piece, startI, startJ, color);
        List<MovePossibility> allDiag = getDiagMoves(board, piece, startI, startJ, color);
        return Stream.concat(allStraight.stream(), allDiag.stream())
                .collect(Collectors.toList());
    }

    private List<MovePossibility> getAllRookMoves(Piece[][] board, Piece piece, int startI, int startJ, String color) {
        return getStraightMoves(board, piece, startI, startJ, color);
    }

    private List<MovePossibility> getDiagMoves(Piece[][] board, Piece piece, int startI, int startJ, String color) {
        List<MovePossibility> moves = new ArrayList<>();

        // up right
        for (int i = startI + 1, j = startJ + 1; i < board.length && j < board.length; i++, j++) {
            MovePossibility move = evaluateAddMove(board, piece, startI, startJ, i, j, color);
            moves.add(move);
            if (move == null || move.getMove().getMoveValue() > 0) {
                break;
            }
        }
        // up left
        for (int i = startI + 1, j = startJ - 1; i < board.length && j >= 0; i++, j--) {
            MovePossibility move = evaluateAddMove(board, piece, startI, startJ, i, j, color);
            moves.add(move);
            if (move == null || move.getMove().getMoveValue() > 0) {
                break;
            }
        }
        // bottom right
        for (int i = startI - 1, j = startJ + 1; i >= 0 && j < board.length; i--, j++) {
            MovePossibility move = evaluateAddMove(board, piece, startI, startJ, i, j, color);
            moves.add(move);
            if (move == null || move.getMove().getMoveValue() > 0) {
                break;
            }
        }
        // bottom left
        for (int i = startI - 1, j = startJ - 1; i >= 0 && j >= 0; i--, j--) {
            MovePossibility move = evaluateAddMove(board, piece, startI, startJ, i, j, color);
            moves.add(move);
            if (move == null || move.getMove().getMoveValue() > 0) {
                break;
            }
        }

        return moves;
    }

    private List<MovePossibility> getStraightMoves(Piece[][] board, Piece piece, int startI, int startJ, String color) {
        List<MovePossibility> moves = new ArrayList<>();

        // up
        for (int i = startI + 1; i < board.length; i++) {
            MovePossibility move = evaluateAddMove(board, piece, startI, startJ, i, startJ, color);
            moves.add(move);
            if (move == null || move.getMove().getMoveValue() > 0) {
                break;
            }
        }
        // down
        for (int i = startI - 1; i >= 0; i--) {
            MovePossibility move = evaluateAddMove(board, piece, startI, startJ, i, startJ, color);
            moves.add(move);
            if (move == null || move.getMove().getMoveValue() > 0) {
                break;
            }
        }
        // right
        for (int j = startJ + 1; j < board.length; j++) {
            MovePossibility move = evaluateAddMove(board, piece, startI, startJ, startI, j, color);
            moves.add(move);
            if (move == null || move.getMove().getMoveValue() > 0) {
                break;
            }
        }
        // left
        for (int j = startJ - 1; j >= 0; j--) {
            MovePossibility move = evaluateAddMove(board, piece, startI, startJ, startI, j, color);
            moves.add(move);
            if (move == null || move.getMove().getMoveValue() > 0) {
                break;
            }
        }

        return moves;
    }

    private MovePossibility evaluateAddMove(Piece[][] board, Piece piece, int startI, int startJ, int i, int j, String color) {
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

        Board boardAfterMove = new Board(copyBoard(board), this.boardObj.isWhiteMoved());
        // see if this piece can be taken by moving here, subtract score if it can be
        boardAfterMove.makeMove(newMove);
        MovePossibility movePossibility = new MovePossibility(newMove, this.aiColor, color, boardAfterMove, this.boardMap);

        return movePossibility.isValidMove() ? movePossibility : null;
    }

    private Piece[][] copyBoard(Piece[][] old) {
        Piece[][] n = new Piece[old.length][old.length];
        for (int i = 0; i < old.length; i++) {
            for (int j = 0; j < old[i].length; j++) {
                Piece p = old[i][j];
                n[i][j] = p == null ? null : p.clone();
            }
        }
        return n;
    }

    private String getOtherColor(String color) {
        return color.equals(ModelConstants.WHITE_COLOR) ? ModelConstants.BLACK_COLOR : ModelConstants.WHITE_COLOR;
    }
}

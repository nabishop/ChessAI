package Models;

import Utils.Scoring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Board {
    private static final int BOARD_SIZE = 8;
    private final Piece[][] board;
    private String colorLastMove = "";
    private boolean whiteMoved = false;

    public Board() {
        this.board = createBoard();
    }

    public Board(Piece[][] board, boolean whiteMoved) {
        this.whiteMoved = whiteMoved;
        this.board = board;
    }

    public Board(String toStringBoard) {
        this.whiteMoved = true; // assumes board is not in original state
        this.board = this.createBoardFromToString(toStringBoard);
    }

    public boolean isWhiteMoved() {
        return whiteMoved;
    }

    public Piece[][] getBoard() {
        return board;
    }

    private Piece[][] createBoard() {
        Piece[][] board = new Piece[BOARD_SIZE][BOARD_SIZE];

        board[0][0] = new Rook("black");
        board[0][1] = new Knight("black");
        board[0][2] = new Bishop("black");
        board[0][3] = new Queen("black");
        board[0][4] = new King("black");
        board[0][5] = new Bishop("black");
        board[0][6] = new Knight("black");
        board[0][7] = new Rook("black");
        board[1][0] = new Pawn("black");
        board[1][1] = new Pawn("black");
        board[1][2] = new Pawn("black");
        board[1][3] = new Pawn("black");
        board[1][4] = new Pawn("black");
        board[1][5] = new Pawn("black");
        board[1][6] = new Pawn("black");
        board[1][7] = new Pawn("black");

        board[7][0] = new Rook("white");
        board[7][1] = new Knight("white");
        board[7][2] = new Bishop("white");
        board[7][3] = new Queen("white");
        board[7][4] = new King("white");
        board[7][5] = new Bishop("white");
        board[7][6] = new Knight("white");
        board[7][7] = new Rook("white");
        board[6][0] = new Pawn("white");
        board[6][1] = new Pawn("white");
        board[6][2] = new Pawn("white");
        board[6][3] = new Pawn("white");
        board[6][4] = new Pawn("white");
        board[6][5] = new Pawn("white");
        board[6][6] = new Pawn("white");
        board[6][7] = new Pawn("white");

        return board;
    }

    @Override
    public String toString() {
        StringBuilder boardStr = new StringBuilder();
        boardStr.append("  ");
        for (int i = 0; i < BOARD_SIZE; i++) {
            boardStr.append(String.format("%2s", Character.toString('A' + i)));
        }
        boardStr.append("\n");

        for (int i = 0; i < BOARD_SIZE; i++) {
            boardStr.append(BOARD_SIZE - i).append(" ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                Piece piece = board[i][j];

                boardStr.append(String.format("%2s", Objects.requireNonNullElse(piece, "X")));
            }
            boardStr.append("  ").append(BOARD_SIZE - i).append("\n");
        }
        boardStr.append("  ");
        for (int i = 0; i < BOARD_SIZE; i++) {
            boardStr.append(String.format("%2s", Character.toString('A' + i)));
        }
        boardStr.append("\n");

        return boardStr.toString();
    }

    public boolean canGameContinue() {
        boolean blackKing = false;
        boolean whiteKing = false;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Piece piece = this.board[i][j];
                if (piece instanceof King) {
                    if (piece.getColor().equals("white")) {
                        whiteKing = true;
                    } else {
                        blackKing = true;
                    }
                }
            }
        }

        return blackKing && whiteKing;
    }

    public boolean canMakeMove(Move move) {
        return getMoveValue(move) >= 0;
    }

    public int getMoveValue(Move move) {
        Piece movePiece = board[move.getFromI()][move.getFromJ()];
        if (movePiece == null) {
            return -1;
        }

        Piece toPiece = board[move.getToI()][move.getToJ()];
        // check if the piece is being moved to a spot of the same color
        if (toPiece != null && toPiece.getColor().equals(movePiece.getColor())) {
            return -1;
        }
        int toPieceValue = toPiece == null ? 0 : toPiece.getValue();
        int iDiff = Math.abs(move.getFromI() - move.getToI());
        int jDiff = Math.abs(move.getFromJ() - move.getToJ());
        //System.out.println("iDiff: " + iDiff + " jDiff: " + jDiff);

        // makes sure piece moved
        if (iDiff + jDiff == 0) {
            return -1;
        }

        // check rules
        if (movePiece instanceof Bishop) {
            if (iDiff != jDiff) {
                return -1;
            }
            return canMoveDiag(iDiff, move) ? toPieceValue : -1;
        } else if (movePiece instanceof King) {
            // diag
            if (iDiff == 1 && jDiff == 1) {
                if (toPiece == null) {
                    return toPieceValue;
                } else {
                    return toPiece.getColor().equals(movePiece.getColor()) ? -1 : toPieceValue;
                }
            }
            return iDiff + jDiff == 1 ? toPieceValue : -1;
        } else if (movePiece instanceof Knight) {
            return (iDiff == 2 && jDiff == 1) || (iDiff == 1 && jDiff == 2) ? toPieceValue : -1;
        } else if (movePiece instanceof Pawn) {
            Pawn pawn = (Pawn) movePiece;
            // more than 1 double jump
            if (pawn.isMoved() && iDiff > 1) {
                return -1;
            }

            // cannot go up two and also over 1
            if (!pawn.isMoved() && iDiff == 2 && jDiff > 0) {
                return -1;
            }

            // cannot go up 3+ or side 2+
            if (iDiff > 2 || jDiff > 1) {
                return -1;
            }

            // cannot go straight if there is a piece there
            if (jDiff == 0 && toPiece != null) {
                return -1;
            }

            // cannot go just sideways
            if ((jDiff > 0) && (iDiff == 0)) {
                return -1;
            }

            // piece blocking
            if (jDiff == 0 && !canMoveStraight(iDiff, jDiff, move)) {
                return -1;
            }

            int verticalDiff = move.getFromI() - move.getToI();
            // black pawns cannot go up
            if (movePiece.getColor().equals(ModelConstants.BLACK_COLOR) && verticalDiff > 0) {
                return -1;
            }
            // white pawns cannot go down
            if (movePiece.getColor().equals(ModelConstants.WHITE_COLOR) && verticalDiff < 0) {
                return -1;
            }

            // check upgrade to queen
            if (move.getToI() == 0 || move.getToI() == 7) {
                return new Queen("").getValue();
            }

            return toPieceValue;
        } else if (movePiece instanceof Queen) {
            if ((iDiff == 0 && jDiff != 0) || (iDiff != 0 && jDiff == 0)) {
                return canMoveStraight(iDiff, jDiff, move) ? toPieceValue : -1;
            }
            if (iDiff == jDiff) {
                return canMoveDiag(iDiff, move) ? toPieceValue : -1;
            }
            return -1;
        } else if (movePiece instanceof Rook) {
            if (iDiff != 0 && jDiff != 0) {
                return -1;
            }

            return canMoveStraight(iDiff, jDiff, move) ? toPieceValue : -1;
        }

        return toPieceValue;
    }

    private boolean canMoveStraight(int iDiff, int jDiff, Move move) {
        boolean moveI = iDiff > 0;
        boolean iAdd = move.getToI() - move.getFromI() > 0;
        boolean jAdd = move.getToJ() - move.getFromJ() > 0;

        int nextI = move.getFromI();
        int nextJ = move.getFromJ();
        Piece startPiece = board[move.getFromI()][move.getFromJ()];

        for (int x = 0; x < iDiff + jDiff; x++) {
            if (moveI) {
                nextI = iAdd ? nextI + 1 : nextI - 1;
            } else {
                nextJ = jAdd ? nextJ + 1 : nextJ - 1;
            }
            Piece piece = board[nextI][nextJ];
            if (piece != null && piece.getColor().equals(startPiece.getColor())) {
                return false;
            }
            if (piece != null && !piece.getColor().equals(startPiece.getColor())) {
                if (x + 1 < iDiff + jDiff) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean canMoveDiag(int iDiff, Move move) {
        // cannot hop over pieces in path
        boolean iAdd = move.getToI() - move.getFromI() > 0;
        boolean jAdd = move.getToJ() - move.getFromJ() > 0;
        Piece startPiece = board[move.getFromI()][move.getFromJ()];

        int nextI = move.getFromI();
        int nextJ = move.getFromJ();
        for (int x = 0; x < iDiff; x++) {
            nextI = iAdd ? nextI + 1 : nextI - 1;
            nextJ = jAdd ? nextJ + 1 : nextJ - 1;
            Piece piece = board[nextI][nextJ];
            if (piece != null && piece.getColor().equals(startPiece.getColor())) {
                return false;
            }
            if (piece != null && !piece.getColor().equals(startPiece.getColor())) {
                if (x + 1 < iDiff) {
                    return false;
                }
            }
        }
        return true;
    }

    public void makeMove(Move move) {
        if (board[move.getFromI()][move.getFromJ()] instanceof Pawn) {
            Pawn p = (Pawn) board[move.getFromI()][move.getFromJ()];
            p.setMoved(true);

            // check if the pawn should be upgraded to a queen
            if (move.getToI() == 0 || move.getToI() == 7) {
                board[move.getFromI()][move.getFromJ()] = new Queen(p.getColor());
            }
        }
        this.colorLastMove = board[move.getFromI()][move.getFromJ()].getColor();

        if (colorLastMove.equals("white")) {
            this.whiteMoved = true;
        }

        board[move.getToI()][move.getToJ()] = board[move.getFromI()][move.getFromJ()];
        board[move.getFromI()][move.getFromJ()] = null;
    }

    public boolean canPieceBeTaken(int pieceI, int pieceJ, String movedColor) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Piece piece = board[i][j];
                if (piece != null && !piece.getColor().equals(movedColor)) {
                    if (this.canMakeMove(new Move(i, j, pieceI, pieceJ, -1))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;
        return Arrays.equals(board, board1.board);
    }

    public String getIdentity() {
        StringBuilder code = new StringBuilder();
        code.append(colorLastMove);
        for (Piece[] pieces : board) {
            for (int j = 0; j < board.length; j++) {
                Piece p = pieces[j];
                if (p == null) {
                    code.append("0");
                } else {
                    code.append(p.getPieceIdentity());
                }
            }
        }
        return code.toString();
    }

    private Piece[][] createBoardFromToString(String toString) {
        Piece[][] board = new Piece[BOARD_SIZE][BOARD_SIZE];

        String[] splitStrings = toString.split("\n");

        // create the board
        for (int i = 0; i < splitStrings.length; i++) {
            // skipping first and last rows since they are just fluff
            if (i == 0 || i == splitStrings.length - 1) {
                continue;
            }

            String row = splitStrings[i];
            String noWhiteSpace = row.replace(" ", "");

            // translate each piece to appropriate board piece
            for (int j = 0; j < noWhiteSpace.length(); j++) {
                // skipping first and last characters since they are just fluff
                if (j == 0 || j == noWhiteSpace.length() - 1) {
                    continue;
                }

                String id = noWhiteSpace.substring(j, j+1);
                Piece boardPiece = null;
                if (ModelConstants.ASCII_TO_PIECE.containsKey(id)) {
                    boardPiece = ModelConstants.ASCII_TO_PIECE.get(id);
                }

                // -1 because of the filler first row/col values
                board[i - 1][j - 1] = boardPiece;
            }
        }

        return board;
    }
}

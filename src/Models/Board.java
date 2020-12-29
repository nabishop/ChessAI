package Models;

public class Board {
    private static final int BOARD_SIZE = 8;
    private final Piece[][] board;

    public Board() {
        this.board = createBoard();
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
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Piece piece = board[i][j];

                if (piece != null) {
                    boardStr.append(String.format("%2s", piece.toString()));
                }
                else {
                    boardStr.append(String.format("%2s", "X"));
                }
            }
            boardStr.append("\n");
        }
        return boardStr.toString();
    }

    public boolean isCheckmate() {
        return false;
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
        System.out.println("fromI: " + move.getFromI() + " fromJ: " +move.getFromJ());
        System.out.println("toI: " + move.getToI() + " toJ: " +move.getToJ());
        System.out.println("iDiff: " + iDiff + " jDiff: " +jDiff);

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
        }
        else if (movePiece instanceof King) {
            return iDiff + jDiff == 1 ? toPieceValue : -1;
        }
        else if (movePiece instanceof Knight) {
            return (iDiff == 2 && jDiff == 1) || (iDiff == 1 && jDiff == 2) ? toPieceValue : -1;
        }
        else if (movePiece instanceof Pawn) {
            Pawn pawn = (Pawn) movePiece;
            // more than 1 diag
            if (iDiff > 1 && jDiff > 1) {
                return -1;
            }

            if ((iDiff > 2) || (pawn.isMoved() && iDiff > 1) || (jDiff == 0 && toPiece != null) || (jDiff > 0 && toPiece == null)) {
                return -1;
            }
            return toPieceValue;
        }
        else if (movePiece instanceof Queen) {
            if ((iDiff == 0 && jDiff != 0) || (iDiff != 0 && jDiff == 0)) {
                return canMoveStraight(iDiff, jDiff, move) ? toPieceValue : -1;
            }
            if (iDiff == jDiff) {
                return canMoveDiag(iDiff, move) ? toPieceValue : -1;
            }
            return -1;
        }
        else if (movePiece instanceof Rook) {
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
        for (int x = 0; x < iDiff + jDiff; x++) {
            if (moveI) {
                nextI = iAdd ? nextI + 1 : nextI - 1;
            }
            else {
                nextJ = jAdd ? nextJ + 1 : nextJ - 1;
            }
            Piece piece = board[nextI][nextJ];
            if (piece != null) {
                return false;
            }
        }

        return true;
    }

    private boolean canMoveDiag(int iDiff, Move move) {
        // cannot hop over pieces in path
        boolean iAdd = move.getToI() - move.getFromI() > 0;
        boolean jAdd = move.getToJ() - move.getFromJ() > 0;

        int nextI = move.getFromI();
        int nextJ = move.getFromJ();
        for (int x = 0; x < iDiff; x++) {
            nextI = iAdd ? nextI + 1 : nextI - 1;
            nextJ = jAdd ? nextJ + 1 : nextJ - 1;
            Piece piece = board[nextI][nextJ];
            if (piece != null) {
                return false;
            }
        }
        return true;
    }

    public void makeMove(Move move) {
        if (board[move.getFromI()][move.getFromJ()] instanceof Pawn) {
            ((Pawn) board[move.getFromI()][move.getFromJ()]).setMoved(true);
        }
        board[move.getToI()][move.getToJ()] = board[move.getFromI()][move.getFromJ()];
        board[move.getFromI()][move.getFromJ()] = null;
    }
}

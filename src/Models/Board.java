package Models;

public class Board {
    private static final int BOARD_SIZE = 8;
    private Piece[][] board;

    public Board() {
        this.board = createBoard();
    }

    private Piece[][] createBoard() {
        Piece[][] board = new Piece[BOARD_SIZE][BOARD_SIZE];

        board[0][0] = new Rook("white");
        board[0][1] = new Knight("white");
        board[0][2] = new Bishop("white");
        board[0][3] = new Queen("white");
        board[0][4] = new King("white");
        board[0][5] = new Bishop("white");
        board[0][6] = new Knight("white");
        board[0][7] = new Rook("white");
        board[1][0] = new Pawn("white");
        board[1][1] = new Pawn("white");
        board[1][2] = new Pawn("white");
        board[1][3] = new Pawn("white");
        board[1][4] = new Pawn("white");
        board[1][5] = new Pawn("white");
        board[1][6] = new Pawn("white");
        board[1][7] = new Pawn("white");

        board[7][0] = new Rook("black");
        board[7][1] = new Knight("black");
        board[7][2] = new Bishop("black");
        board[7][3] = new Queen("black");
        board[7][4] = new King("black");
        board[7][5] = new Bishop("black");
        board[7][6] = new Knight("black");
        board[7][7] = new Rook("black");
        board[6][0] = new Pawn("black");
        board[6][1] = new Pawn("black");
        board[6][2] = new Pawn("black");
        board[6][3] = new Pawn("black");
        board[6][4] = new Pawn("black");
        board[6][5] = new Pawn("black");
        board[6][6] = new Pawn("black");
        board[6][7] = new Pawn("black");

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
                    boardStr.append(String.format("%2s", " ."));
                }
            }
            boardStr.append("\n");
        }
        return boardStr.toString();
    }
}

package Engine;

import Models.King;
import Models.Move;
import Models.Piece;

public class BoardHeatMap {
    public static int[][] centerHeatMap = getCenterHeatMap();

    private static int[][] getCenterHeatMap() {
        int[][] board = new int[8][8];

        for (int i = 0; i < 8; i ++) {
            for (int j = 0; j < 8; j++) {
                if (i >= 2 && i <= 5 && j >= 2 && j <= 5) {
                    board[i][j] = 2;
                }
                if ((i == 3 || i == 4) && (j == 3 || j == 4)) {
                    board[i][j] = 3;
                }
            }
        }

        return board;
    }

    public static int getKingHeatMapValue(Piece[][] pieces, Move move, String player) {
        // find king location
        int kingI = 0;
        int kingJ = 0;

        if (move == null) {
            return 0;
        }

        for (int i = 0; i < pieces.length; i ++) {
            for (int j = 0; j < pieces.length; j++) {
                Piece piece = pieces[i][j];
                if (piece != null && !piece.getColor().equals(player) && piece instanceof King) {
                    kingI = i;
                    kingJ = j;
                    break;
                }
            }
        }

        int iDiff = Math.abs(kingI - move.getFromI());
        int jDiff = Math.abs(kingJ - move.getFromJ());
        if (iDiff == 0 && jDiff == 0) {
            return 5;
        }
        if (iDiff <= 1 && jDiff <= 1) {
            return 2;
        }
        if (iDiff <= 2 && jDiff <= 2) {
            return 1;
        }
        return 0;
    }
}

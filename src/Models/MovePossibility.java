package Models;

import Utils.BoardHeatMap;

import java.util.Map;

public class MovePossibility {
    private final Move move;
    private final double score;
    private final Board board;
    private final String aiColor;

    public MovePossibility(Move move, String aiColor, String movingColor, Board board, Map<String, Double> boardMap) {
        this.move = move;
        this.board = board;
        this.aiColor = aiColor;
        this.score = calculatePlayerScore(boardMap, movingColor);
    }

    public String getAiColor() {
        return aiColor;
    }

    public double getScore() {
        return this.score;
    }

    public Board getBoard() {
        return board;
    }

    public Move getMove() {
        return move;
    }

    private double calculatePlayerScore(Map<String, Double> boardMap, String movingColor) {
        double score = 0;
        int movingKingI = 0;
        int movingKingJ = 0;

        Piece[][] pieces = board.getBoard();
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces.length; j++) {
                Piece piece = pieces[i][j];
                if (piece != null) {
                    score += ((piece.getValue() + BoardHeatMap.centerHeatMap[i][j] + BoardHeatMap.getKingHeatMapValue(pieces, this.move, movingColor)) * (piece.getColor().equals(aiColor) ? 1 : -1));
                    if (piece.getColor().equals(movingColor) && piece instanceof King) {
                        movingKingI = i;
                        movingKingJ = j;
                    }
                }
            }
        }
        if (move != null) {
            int moveI = move.getToI();
            int moveJ = move.getToJ();
            boolean isKingMoving = pieces[moveI][moveJ] instanceof King;

            boolean canMovedPieceBeTaken = this.board.canPieceBeTaken(moveI, moveJ, isKingMoving, movingColor);
            if (canMovedPieceBeTaken) {
                //System.out.println(movingColor + " " + true);
                //System.out.println("before: " + score + " after " + (pieces[move.getToI()][move.getToJ()].getValue() * (aiColor.equals(movingColor) ? -1 : 1)));
                score += (pieces[move.getToI()][move.getToJ()].getValue() * (aiColor.equals(movingColor) ? -1 : 1));
            }
            // if king can be taken, dont want to double count
            if (!isKingMoving && this.board.canPieceBeTaken(movingKingI, movingKingJ, true, movingColor)) {
                score += (1000 * (aiColor.equals(movingColor) ? -1 : 1));
            }
        }

        //double mlScore = boardMap.getOrDefault(this.board.getIdentity(), 0.0);
        return score;
    }
}

package Models;

import Engine.BoardHeatMap;

import java.util.Map;

public class MovePossibility {
    private final Move move;
    private final double score;
    private final Board board;
    private final String color;

    public MovePossibility(Move move, String aiColor, Board board, Map<String, Double> boardMap) {
        this.move = move;
        this.board = board;
        this.color = aiColor;
        this.score = calculatePlayerScore(boardMap);
    }

    public String getColor() {
        return color;
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

    private double calculatePlayerScore(Map<String, Double> boardMap) {
        double score = 0;

        Piece[][] pieces = board.getBoard();
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces.length; j++) {
                Piece piece = pieces[i][j];
                if (piece != null) {
                    score += ((piece.getValue() + BoardHeatMap.centerHeatMap[i][j] + BoardHeatMap.getKingHeatMapValue(pieces, move, color)) * (piece.getColor().equals(color) ? 1 : -1));
                }
            }
        }
        return score + boardMap.getOrDefault(this.board.getIdentity(), 0.0);
    }
}

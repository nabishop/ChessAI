package Models;

import Engine.BoardHeatMap;

public class MovePossibility {
    private final Move move;
    private final int aiScore;
    private final int otherScore;
    private final Board board;

    public MovePossibility(Move move, String ai, String other, Board board) {
        this.move = move;
        this.board = board;
        this.aiScore =  calculatePlayerScore(ai);
        this.otherScore = calculatePlayerScore(other);
    }

    public int getOtherScore() {
        return otherScore;
    }

    public int getAiScore() {
        return aiScore;
    }

    public Board getBoard() {
        return board;
    }

    public Move getMove() {
        return move;
    }

    private int calculatePlayerScore(String color) {
        int score = 0;

        Piece[][] pieces = board.getBoard();
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces.length; j++) {
                Piece piece = pieces[i][j];
                if (piece != null && piece.getColor().equals(color)) {
                    score += piece.getValue() + BoardHeatMap.centerHeatMap[i][j] + BoardHeatMap.getKingHeatMapValue(pieces, this.move, color);
                }
            }
        }
        return score;
    }
}

package Models;

import Engine.BoardHeatMap;

public class MovePossibility {
    private final Move move;
    private final int score;
    private final String aiColor;
    private final Board board;

    public MovePossibility(Move move, String aiColor, Board board) {
        this.move = move;
        this.board = board;
        this.aiColor = aiColor;
        this.score = calculatePlayerScore(aiColor);
    }

    public String getAiColor() {
        return aiColor;
    }

    public int getScore() {
        return this.score;
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
                if (piece != null) {
                    score += (((piece.getValue() * 3) + BoardHeatMap.centerHeatMap[i][j] + BoardHeatMap.getKingHeatMapValue(pieces, move, color)) * (piece.getColor().equals(color) ? 1 : -1));
                }
            }
        }
        return score;
    }
}

package Models;

import Utils.BoardHeatMap;

import java.util.Map;

public class MovePossibility {
    private final Move move;
    private final double score;
    private final Board board;
    private final String aiColor;
    private boolean validMove = true;

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

    public boolean isValidMove() {
        return validMove;
    }

    private double calculatePlayerScore(Map<String, Double> boardMap, String movingColor) {
        double score = 0;

        Piece[][] pieces = board.getBoard();
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces.length; j++) {
                Piece piece = pieces[i][j];
                if (piece != null) {
                    score += ((piece.getValue() + BoardHeatMap.centerHeatMap[i][j] + BoardHeatMap.getKingHeatMapValue(pieces, this.move, movingColor)) * (piece.getColor().equals(aiColor) ? 1 : -1));

                    // cannot move a piece if it is puts the king in check
                    if (piece instanceof King && piece.getColor().equals(movingColor)) {
                        if (this.board.canPieceBeTaken(i, j, movingColor)) {
                            this.validMove = false;
                        }
                    }
                }
            }
        }
        if (move != null) {
            int moveI = move.getToI();
            int moveJ = move.getToJ();
            Piece movingPiece = pieces[moveI][moveJ];

            boolean canMovedPieceBeTaken = this.board.canPieceBeTaken(moveI, moveJ, movingColor);
            if (canMovedPieceBeTaken) {
                score += (movingPiece.getValue() * (aiColor.equals(movingColor) ? -1 : 0));
            }
        }

        double mlScore = boardMap.getOrDefault(this.board.getIdentity(), 0.0);
        return score + mlScore;
    }
}

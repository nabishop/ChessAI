package Models;

public class MovePossibility {
    private final Move move;
    private final String ai;
    private final int aiScore;
    private final String other;
    private final int otherScore;
    private final Board board;

    public MovePossibility(Move move, String ai, String other, Board board) {
        this.move = move;
        this.ai = ai;
        this.other = other;
        this.board = board;
        this.aiScore = calculatePlayerScore(ai);
        this.otherScore = calculatePlayerScore(other);
    }

    public int getAiScore() {
        return aiScore;
    }

    public int getOtherScore() {
        return otherScore;
    }

    public Board getBoard() {
        return board;
    }

    public Move getMove() {
        return move;
    }

    private int calculatePlayerScore(String color) {
        int score = 0;

        for (Piece[] pieces : board.getBoard()) {
            for (int j = 0; j < board.getBoard().length; j++) {
                Piece piece = pieces[j];
                if (piece != null && piece.getColor().equals(color)) {
                    score += piece.getValue();
                }
            }
        }
        return score;
    }
}

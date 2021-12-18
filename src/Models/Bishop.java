package Models;

public class Bishop extends Piece {
    public Bishop(String color) {
        super(color, ModelScores.BISHOP_SCORE);
    }

    @Override
    String getPieceIdentiy() {
        return super.getColor() + "bp";
    }

    @Override
    public String toString() {
        return super.getColor().equals("black") ? "\u2657" : "\u265D";
    }

    @Override
    public Piece clone() {
        return new Bishop(this.getColor());
    }
}

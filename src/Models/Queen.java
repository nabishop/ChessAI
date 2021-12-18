package Models;

public class Queen extends Piece {
    public Queen(String color) {
        super(color, ModelScores.QUEEN_SCORE);
    }

    @Override
    String getPieceIdentiy() {
        return super.getColor() + "qn";
    }

    @Override
    public String toString() {
        return super.getColor().equals("black") ? "\u2655" : "\u265B";
    }

    @Override
    public Piece clone() {
        return new Queen(this.getColor());
    }
}

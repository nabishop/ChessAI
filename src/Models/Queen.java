package Models;

public class Queen extends Piece {
    public Queen(String color) {
        super(color, ModelConstants.QUEEN_SCORE);
    }

    @Override
    String getPieceIdentity() {
        return super.getColor() + "qn";
    }

    @Override
    public String toString() {
        return super.getColor().equals(ModelConstants.BLACK_COLOR) ?
                ModelConstants.BLACK_QUEEN_ASCII :
                ModelConstants.WHITE_QUEEN_ASCII;
    }

    @Override
    public Piece clone() {
        return new Queen(this.getColor());
    }
}

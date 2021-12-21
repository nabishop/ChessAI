package Models;

public class Knight extends Piece {
    public Knight(String color) {
        super(color, ModelConstants.KNIGHT_SCORE);
    }

    @Override
    String getPieceIdentity() {
        return super.getColor() + "kt";
    }

    @Override
    public String toString() {
        return super.getColor().equals(ModelConstants.BLACK_COLOR) ?
                ModelConstants.BLACK_KNIGHT_ASCII :
                ModelConstants.WHITE_KNIGHT_ASCII;
    }

    @Override
    public Piece clone() {
        return new Knight(this.getColor());
    }
}

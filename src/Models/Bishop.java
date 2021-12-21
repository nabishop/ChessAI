package Models;

public class Bishop extends Piece {
    public Bishop(String color) {
        super(color, ModelConstants.BISHOP_SCORE);
    }

    @Override
    String getPieceIdentity() {
        return super.getColor() + "bp";
    }

    @Override
    public String toString() {
        return super.getColor().equals(ModelConstants.BLACK_COLOR) ?
                ModelConstants.BLACK_BISHOP_ASCII :
                ModelConstants.WHITE_BISHOP_ASCII;
    }

    @Override
    public Piece clone() {
        return new Bishop(this.getColor());
    }
}

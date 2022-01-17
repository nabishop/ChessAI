package Models;

public class King extends Piece {
    private boolean moved = false;

    public King(String color) {
        super(color, ModelConstants.KING_SCORE);
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    @Override
    String getPieceIdentity() {
        return super.getColor() + "kg";
    }

    @Override
    public String toString() {
        return super.getColor().equals(ModelConstants.BLACK_COLOR) ?
                ModelConstants.BLACK_KING_ASCII :
                ModelConstants.WHITE_KING_ASCII;
    }

    @Override
    public Piece clone() {
        return new King(this.getColor());
    }
}

package Models;

public class Rook extends Piece {
    private boolean moved = false;

    public Rook(String color) {
        super(color, ModelConstants.ROOK_SCORE);
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    @Override
    String getPieceIdentity() {
        return super.getColor() + "rk";
    }

    @Override
    public String toString() {
        return super.getColor().equals(ModelConstants.BLACK_COLOR) ?
                ModelConstants.BLACK_ROOK_ASCII :
                ModelConstants.WHITE_ROOK_ASCII;
    }

    @Override
    public Piece clone() {
        return new Rook(this.getColor());
    }
}

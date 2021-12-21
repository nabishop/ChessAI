package Models;

public class Pawn extends Piece {
    private boolean moved = false;

    public Pawn(String color) {
        super(color, ModelConstants.PAWN_SCORE);
    }

    @Override
    String getPieceIdentity() {
        return super.getColor() + "pn";
    }

    @Override
    public String toString() {
        return super.getColor().equals(ModelConstants.BLACK_COLOR) ?
                ModelConstants.BLACK_PAWN_ASCII :
                ModelConstants.WHITE_PAWN_ASCII;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    @Override
    public Piece clone() {
        Pawn p = new Pawn(this.getColor());
        p.setMoved(this.isMoved());
        return p;
    }
}

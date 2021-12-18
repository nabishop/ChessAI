package Models;

public class Pawn extends Piece {
    private boolean moved = false;

    public Pawn(String color) {
        super(color, ModelScores.PAWN_SCORE);
    }

    @Override
    String getPieceIdentiy() {
        return super.getColor() + "pn";
    }

    @Override
    public String toString() {
        return super.getColor().equals("black") ? "\u2659" : "\u265F";
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

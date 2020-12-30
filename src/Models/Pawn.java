package Models;

public class Pawn extends Piece{
    private boolean moved = false;
    public Pawn(String color){
        super(color, 10);
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
}

package Models;

public class Rook extends Piece {
    private boolean moved = false;

    public Rook(String color){
        super(color, 50);
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isMoved() {
        return moved;
    }

    @Override
    String getPieceIdentiy() {
        return super.getColor() + "rk";
    }

    @Override
    public String toString() {
        return super.getColor().equals("black") ? "\u2656" : "\u265C";
    }
}

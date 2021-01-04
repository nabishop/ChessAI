package Models;

public class King extends Piece {
    private boolean moved = false;

    public King(String color) {
        super(color, 1000);
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isMoved() {
        return moved;
    }

    @Override
    String getPieceIdentiy() {
        return super.getColor() + "kg";
    }

    @Override
    public String toString() {
        return super.getColor().equals("black") ? "\u2654" : "\u265A";
    }
}

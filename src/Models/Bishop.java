package Models;

public class Bishop extends Piece {
    public Bishop(String color) {
        super(color, 30);
    }

    @Override
    String getPieceIdentiy() {
        return super.getColor() + "bp";
    }

    @Override
    public String toString() {
        return super.getColor().equals("black") ? "\u2657" : "\u265D";
    }
}

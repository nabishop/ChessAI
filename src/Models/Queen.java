package Models;

public class Queen extends Piece{
    public Queen(String color) {
        super(color, 250);
    }

    @Override
    public String toString() {
        return super.getColor().equals("black") ? "\u2655" : "\u265B";
    }
}

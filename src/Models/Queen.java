package Models;

public class Queen extends Piece{
    public Queen(String color) {
        super(color, 10);
    }

    @Override
    public String toString() {
        return super.getColor().equals("black") ? "\u2655" : "\u265B";
    }
}

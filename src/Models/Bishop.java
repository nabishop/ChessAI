package Models;

public class Bishop extends Piece {
    public Bishop(String color){
        super(color, 4);
    }

    @Override
    public String toString() {
        return super.getColor().equals("white") ? "\u2657" : "\u265D";
    }
}
package Models;

public class Rook extends Piece {
    public Rook(String color){
        super(color, 5);
    }

    @Override
    public String toString() {
        return super.getColor().equals("black") ? "\u2656" : "\u265C";
    }
}

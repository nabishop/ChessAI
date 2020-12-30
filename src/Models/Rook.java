package Models;

public class Rook extends Piece {
    public Rook(String color){
        super(color, 50);
    }

    @Override
    public String toString() {
        return super.getColor().equals("black") ? "\u2656" : "\u265C";
    }
}

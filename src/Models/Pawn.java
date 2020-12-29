package Models;

public class Pawn extends Piece{
    public Pawn(String color){
        super(color, 1);
    }

    @Override
    public String toString() {
        return super.getColor().equals("white") ? "\u2659" : "\u265F";
    }
}

package Models;

public class King extends Piece{
    public King(String color){
        super(color, 100);
    }

    @Override
    public String toString() {
        return super.getColor().equals("white") ? "\u2654" : "\u265A";
    }
}

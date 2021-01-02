package Models;

public class King extends Piece{
    public King(String color){
        super(color, 1000);
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

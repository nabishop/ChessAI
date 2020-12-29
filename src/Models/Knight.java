package Models;

public class Knight extends Piece{
    public Knight(String color){
        super(color, 3);
    }

    @Override
    public String toString() {
        return super.getColor().equals("black") ? "\u2658" : "\u265E";
    }
}

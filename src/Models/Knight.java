package Models;

public class Knight extends Piece{
    public Knight(String color){
        super(color, 30);
    }

    @Override
    public String toString() {
        return super.getColor().equals("black") ? "\u2658" : "\u265E";
    }
}

package Models;

public abstract class Piece {
    private String color;
    private int value;

    public Piece(String color, int value) {
        this.color = color;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getColor() {
        return color;
    }
}

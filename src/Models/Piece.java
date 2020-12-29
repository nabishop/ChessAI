package Models;

public abstract class Piece implements Comparable<Piece> {
    private final String color;
    private final Integer value;

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

    @Override
    public int compareTo(Piece other) {
        return this.value.compareTo(other.value);
    }
}

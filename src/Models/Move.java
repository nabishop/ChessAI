package Models;

public class Move {
    private final int fromI;
    private final int fromJ;
    private final int toI;
    private final int toJ;

    public Move(String from, String to) {
        int[] fromPos = getPosFromString(from);
        this.fromI = fromPos[0];
        this.fromJ = fromPos[1];

        int[] toPos = getPosFromString(to);
        this.toI = toPos[0];
        this.toJ = toPos[1];
    }

    private int[] getPosFromString(String str) {
        int[] pos = new int[2];
        int asciiA = 97;
        int i = Math.abs(8 - Integer.parseInt(str.substring(1)));
        int j = asciiA - str.charAt(0);

        if (j > 7) {
            System.out.println("Invalid Input: " + str);
            System.exit(1);
        }

        pos[0] = i;
        pos[1] = j;
        return pos;
    }

    public int getFromI() {
        return fromI;
    }

    public int getFromJ() {
        return fromJ;
    }

    public int getToI() {
        return toI;
    }

    public int getToJ() {
        return toJ;
    }
}

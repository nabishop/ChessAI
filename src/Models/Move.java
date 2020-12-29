package Models;

public class Move implements Comparable<Move>{
    private final int fromI;
    private final int fromJ;
    private final int toI;
    private final int toJ;
    private Boolean capture = false;

    public Move(String from, String to) {
        int[] fromPos = getPosFromString(from);
        this.fromI = fromPos[0];
        this.fromJ = fromPos[1];

        int[] toPos = getPosFromString(to);
        this.toI = toPos[0];
        this.toJ = toPos[1];
    }

    public Move(int fromI, int fromJ, int toI, int toJ, boolean capture) {
        this.fromI = fromI;
        this.fromJ = fromJ;
        this.toI = toI;
        this.toJ = toJ;
        this.capture = capture;
    }

    public boolean isCapture() {
        return capture;
    }

    private int[] getPosFromString(String str) {
        int[] pos = new int[2];
        int asciiLowerA = 97;
        int i = Math.abs(8 - Integer.parseInt(str.substring(1)));
        int j = str.charAt(0) - asciiLowerA;

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

    @Override
    public int compareTo(Move o) {
        return this.capture.compareTo(o.capture);
    }
}

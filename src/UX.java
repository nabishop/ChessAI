import Models.Move;

import java.util.Scanner;

public class UX {
    private String enemyColor;
    private Scanner n;

    public UX() {
        this.n = new Scanner(System.in);
        this.getUserColor();
    }

    private void getUserColor() {
        System.out.println("Are you white?");
        String ans = this.n.next();

        this.enemyColor = ans.equals("y") ? "white" : "black";
    }

    public String getEnemyColor() {
        return enemyColor;
    }

    public Move getNextMove(boolean whiteTurn) {
        System.out.println("Turn: " + (whiteTurn ? "(white)" : "(black)") + "\nFrom: ");
        String moveFrom = this.n.next().toLowerCase();
        System.out.println("To: ");
        String moveTo = this.n.next().toLowerCase();

        return new Move(moveFrom, moveTo);
    }
}


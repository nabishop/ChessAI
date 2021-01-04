import Models.Move;

import java.util.Scanner;

public class UX {
    private String aiColor;
    private String enemyColor;
    private final Scanner n;

    public UX() {
        this.n = new Scanner(System.in);
        this.getUserColor();
    }

    private void getUserColor() {
        System.out.println("Is the enemy white?");
        String ans = this.n.next();

        this.aiColor = ans.equals("y") ? "black" : "white";
        this.enemyColor = ans.equals("y") ? "white" : "black";
    }

    public String getAiColor() {
        return aiColor;
    }

    public String getEnemyColor() {
        return enemyColor;
    }

    public Move getNextMove(boolean whiteTurn) {
        System.out.println("Turn: " + (whiteTurn ? "(white)" : "(black)") + "\nFrom: ");
        String moveFrom = this.n.next().toLowerCase();
        System.out.println("To: ");
        String moveTo = this.n.next().toLowerCase();

        if (moveFrom.length() != 2 || moveTo.length() != 2 ||
                moveFrom.substring(0, 1).matches("-?\\d+") || moveTo.substring(0, 1).matches("-?\\d+") ||
                !moveFrom.substring(1, 2).matches("-?\\d+") || !moveTo.substring(1, 2).matches("-?\\d+")) {
            return null;
        }

        return new Move(moveFrom, moveTo);
    }
}


import Engine.BoardHeatMap;
import Engine.MoveEngine;
import Models.Board;
import Models.Move;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        UX ux = new UX();
        MoveEngine moveEngine = new MoveEngine(board, ux.getEnemyColor().equals("white") ? "black" : "white");

        boolean enemyWhite = ux.getEnemyColor().equals("white");
        boolean whiteTurn = true;
        while (!board.isCheckmate()) {
            System.out.println(board.toString());
            System.out.println(whiteTurn == enemyWhite ? "YOU" : "COMPUTER");
            if (whiteTurn != enemyWhite) {
                Move move = moveEngine.getNextBestMove();
                board.makeMove(move, true);
            }
            else {
                while(true) {
                    Move move = ux.getNextMove(whiteTurn);
                    boolean canMakeMove = board.canMakeMove(move);
                    if (canMakeMove) {
                        board.makeMove(move, true);
                        break;
                    }
                    System.out.println("error making move");
                }
            }
            whiteTurn = !whiteTurn;
        }
    }
}

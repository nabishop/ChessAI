import Engine.MoveEngine;
import Models.Board;
import Models.Move;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        MoveEngine moveEngine = new MoveEngine(board);
        UX ux = new UX();

        boolean enemyWhite = ux.getEnemyColor().equals("white");
        boolean whiteTurn = true;
        while (!board.isCheckmate()) {
            System.out.println(board.toString());
            System.out.println(whiteTurn == enemyWhite ? "YOU" : "COMPUTER");
            if (whiteTurn != enemyWhite) {
                Move move = moveEngine.getNextBestMove(whiteTurn ? "white" : "black");
                board.makeMove(move);
            }
            else {
                while(true) {
                    Move move = ux.getNextMove(whiteTurn);
                    boolean canMakeMove = board.canMakeMove(move);
                    if (canMakeMove) {
                        board.makeMove(move);
                        break;
                    }
                    System.out.println("error making move");
                }
            }
            whiteTurn = !whiteTurn;
        }
    }
}

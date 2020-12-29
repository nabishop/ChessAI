import Models.Board;
import Models.Move;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        System.out.println(board.toString());
        UX ux = new UX();

        boolean enemyWhite = ux.getEnemyColor().equals("white");
        boolean whiteTurn = true;
        while (!board.isCheckmate()) {
            if (whiteTurn && enemyWhite) {
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
            System.out.println(board.toString());
            System.out.println("white turn: " + whiteTurn);
            whiteTurn = !whiteTurn;
        }
    }
}

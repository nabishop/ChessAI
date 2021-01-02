import Engine.MoveEngine;
import Models.Board;
import Models.Move;
import Models.MovePossibility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final String MAP_FILE_PATH = "/Users/nbishop/Documents/Chess/board.properties";
    private static final double INITIAL_MAP_SCORE = 0;
    private static final double MAP_SCORE_INCREMENT = 1.5;
    private static final double MAP_SCORE_WINNER = 15;

    public static void main(String[] args) throws IOException {
        train();
        //fight();
    }

    private static void fight() throws IOException {
        UX ux = new UX();
        Map<String, Double> boardMap = loadBoardMap();

        Board board = new Board();
        MoveEngine engine = new MoveEngine(board, ux.getAiColor(), boardMap);

        boolean whiteTurn = true;
        System.out.println(board.toString());
        while (board.canGameContinue()) {
            if ((whiteTurn && ux.getAiColor().equals("white")) || (!whiteTurn && ux.getAiColor().equals("black"))) {
                MovePossibility newMove = engine.getNextBestMove();
                board.makeMove(newMove.getMove(), true);
                System.out.println("COMPUTER MOVED: " + newMove.getMove().toString());
            } else {
                while (true) {
                    Move move = ux.getNextMove(whiteTurn);

                    if (board.canMakeMove(move)) {
                        board.makeMove(move, true);
                        System.out.println("PLAYER MOVED: " + move.toString());
                        break;
                    }
                }
            }


            System.out.println(board.toString() + "\n");
            whiteTurn = !whiteTurn;
        }
    }

    private static void train() throws IOException {
        Queue<MovePossibility> moveHistory = new LinkedList<>();
        Map<String, Double> boardMap = loadBoardMap();
        System.out.println(boardMap.size());

        int runTimes = 25000;
        int longestGame = 0;
        double averageMoves = 0;
        for (int run = 0; run < runTimes; run++) {
            System.out.println("Running #" + (run + 1) + "... (" + (runTimes - run) + " left)");
            Board board = new Board();
            MoveEngine blackMoveEngine = new MoveEngine(board, "black", boardMap);
            MoveEngine whiteMoveEngine = new MoveEngine(board, "white", boardMap);

            boolean whiteTurn = true;
            String winner = "";
            int moves = 0;
            while (board.canGameContinue()) {
                winner = whiteTurn ? "white" : "black";

                MovePossibility newMove = whiteTurn ? whiteMoveEngine.getNextBestMove() : blackMoveEngine.getNextBestMove();

                // checkmate, cannot make a move
                if (newMove == null) {
                    winner = whiteTurn ? "black" : "white";
                    break;
                }
                board.makeMove(newMove.getMove(), true);

                if (moveHistory.size() >= 6) {
                    MovePossibility oldMove = moveHistory.poll();
                    String oldIdentity = oldMove.getBoard().getIdentity();
                    double boardScore = boardMap.getOrDefault(oldIdentity, INITIAL_MAP_SCORE);

                    // if this move was good for me
                    if (oldMove.getScore() <= newMove.getScore()) {
                        boardScore += MAP_SCORE_INCREMENT;
                    } else {
                        boardScore -= MAP_SCORE_INCREMENT;
                    }
                    boardMap.put(oldIdentity, boardScore);
                }
                System.out.println(winner + " " + (moves + 1));
                System.out.println(newMove.getMove().toString());
                System.out.println(board.toString() + "\n");

                moveHistory.add(newMove);
                whiteTurn = !whiteTurn;
                moves++;
            }

            MovePossibility remainingMove = moveHistory.poll();
            while (remainingMove != null) {
                String oldIdentity = remainingMove.getBoard().getIdentity();
                double boardScore = boardMap.getOrDefault(oldIdentity, INITIAL_MAP_SCORE);

                // if this move was good for me
                if (remainingMove.getAiColor().equals(winner)) {
                    boardScore += MAP_SCORE_WINNER;
                } else {
                    boardScore -= MAP_SCORE_WINNER;
                }
                boardMap.put(oldIdentity, boardScore);

                remainingMove = moveHistory.poll();
            }

            if (moves > longestGame) {
                longestGame = moves;
            }
            averageMoves += moves;

            System.out.println("Run #" + (run + 1) + " - Moves: " + moves);
            System.out.println("average game so far is " + (averageMoves / (double) (run + 1)) + " moves\n");
            saveBoardMap(boardMap);
        }

        System.out.println("longest game was " + longestGame + " moves");
        System.out.println("average game was " + (averageMoves / (double) runTimes) + " moves");
    }

    private static Map<String, Double> loadBoardMap() throws IOException {
        Map<String, Double> boardMap = new HashMap<>();
        Properties properties = new Properties();
        properties.load(new FileInputStream(MAP_FILE_PATH));

        for (String key : properties.stringPropertyNames()) {
            boardMap.put(key, Double.valueOf(properties.get(key).toString()));
        }

        return boardMap;
    }

    private static void saveBoardMap(Map<String, Double> boardMap) throws IOException {
        Properties properties = new Properties();

        for (Map.Entry<String, Double> entry : boardMap.entrySet()) {
            properties.put(entry.getKey(), Double.toString(entry.getValue()));
        }

        properties.store(new FileOutputStream(MAP_FILE_PATH), null);
    }
}

import Engine.MoveEngine;
import Models.Board;
import Models.Move;
import Models.MovePossibility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final String MAP_FILE_PATH = "/Users/nbishop/Documents/Chess/board.properties";
    private static final double INITIAL_MAP_SCORE = 0;
    private static final double MAP_SCORE_INCREMENT = 1;
    private static final double MAP_SCORE_WINNER = 5;

    public static void main(String[] args) throws IOException {
        //UX ux = new UX();

        Queue<MovePossibility> moveHistory = new LinkedList<>();
        Map<String, Double> boardMap = loadBoardMap();

        int runTimes = 100;
        int longestGame = 0;
        for (int run = 0; run < runTimes; run++) {
            Board board = new Board();
            MoveEngine blackMoveEngine = new MoveEngine(board, "black", boardMap);
            MoveEngine whiteMoveEngine = new MoveEngine(board, "white", boardMap);

            boolean whiteTurn = true;
            String winner = "";
            int moves = 0;
            while (board.canGameContinue()) {
                System.out.println(whiteTurn ? "WHITE" : "BLACK");
                winner = whiteTurn ? "white" : "black";

                MovePossibility newMove = whiteTurn ? whiteMoveEngine.getNextBestMove("white") : blackMoveEngine.getNextBestMove("black");
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
                    System.out.println("BOARD SCORE: " + boardScore + " COLOR: " + oldMove.getColor());
                    boardMap.put(oldIdentity, boardScore);
                }

                moveHistory.add(newMove);
                whiteTurn = !whiteTurn;
                moves++;

                System.out.println(moves + " - " + newMove.getMove().toString());
                System.out.println(board.toString());

                System.out.println();
            }

            MovePossibility remainingMove = moveHistory.poll();
            while (remainingMove != null) {
                String oldIdentity = remainingMove.getBoard().getIdentity();
                double boardScore = boardMap.getOrDefault(oldIdentity, INITIAL_MAP_SCORE);

                // if this move was good for me
                if (remainingMove.getColor().equals(winner)) {
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
        }

        System.out.println("longest game was " +  longestGame + " moves");
        saveBoardMap(boardMap);
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

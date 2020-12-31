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
    public static void main(String[] args) throws IOException {
        Board board = new Board();
        //UX ux = new UX();

        Queue<MovePossibility> moveHistory = new LinkedList<>();
        Map<Integer, Double> boardMap = loadBoardMap();
        MoveEngine blackMoveEngine = new MoveEngine(board, "black");
        MoveEngine whiteMoveEngine = new MoveEngine(board, "white");

        boolean whiteTurn = true;
        while (board.canGameContinue()) {
            System.out.println(board.toString());
            System.out.println(whiteTurn ? "WHITE" : "BLACK");

            MovePossibility newMove = whiteTurn ? whiteMoveEngine.getNextBestMove("white") : blackMoveEngine.getNextBestMove("black");
            board.makeMove(newMove.getMove(), true);

            if (moveHistory.size() >= 6) {
                MovePossibility oldMove = moveHistory.poll();

                // if this move was good for me
                if (oldMove.getScore() <= newMove.getScore()) {

                }
                else {

                }
            }

            moveHistory.add(newMove);
            whiteTurn = !whiteTurn;

            System.out.println(newMove.getMove().toString());
            System.out.println();
        }

        saveBoardMap(boardMap);
    }

    private static Map<Integer, Double> loadBoardMap() throws IOException {
        Map<Integer, Double> boardMap = new HashMap<>();
        Properties properties = new Properties();
        properties.load(new FileInputStream(MAP_FILE_PATH));

        for (String key : properties.stringPropertyNames()) {
            boardMap.put(Integer.valueOf(key), Double.valueOf(properties.get(key).toString()));
        }

        return boardMap;
    }

    private static void saveBoardMap(Map<Integer, Double> boardMap) throws IOException {
        Properties properties = new Properties();

        for (Map.Entry<Integer, Double> entry : boardMap.entrySet()) {
            properties.put(Integer.toString(entry.getKey()), Double.toString(entry.getValue()));
        }

        properties.store(new FileOutputStream(MAP_FILE_PATH), null);
    }
}

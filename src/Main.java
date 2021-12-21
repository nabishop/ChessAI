import Engine.MoveEngine;
import Models.*;
import Utils.Scoring;

import java.io.*;
import java.util.*;

public class Main {
    private static final String MAP_FILE_PATH = "/Users/nicholasbishop/Documents/GitHub/ChessAI/board.properties";

    public static void main(String[] args) throws IOException {
        boolean verbose = true;
        train(verbose);
        //fight();
    }

    private static void fight() throws IOException {
        UX ux = new UX();
        Map<String, Double> boardMap = loadBoardMap();

        Board board = new Board();
        MoveEngine engine = new MoveEngine(board, ux.getAiColor(), boardMap);

        boolean whiteTurn = true;
        System.out.println(board);
        while (board.canGameContinue()) {
            if ((whiteTurn && ux.getAiColor().equals("white")) || (!whiteTurn && ux.getAiColor().equals("black"))) {
                MovePossibility newMove = engine.getNextBestMove();
                board.makeMove(newMove.getMove());
                System.out.println("COMPUTER MOVED: " + newMove.getMove().toString());
            } else {
                while (true) {
                    Move move = null;
                    while (move == null) {
                        move = ux.getNextMove(whiteTurn);
                    }

                    if (board.canMakeMove(move)) {
                        board.makeMove(move);
                        System.out.println("PLAYER MOVED: " + move);
                        break;
                    }
                }
            }


            System.out.println(board + "\n");
            whiteTurn = !whiteTurn;
        }
    }

    private static boolean castleMove(Move move) {
        return true;
    }

    private static void train(boolean verbose) throws IOException {
        Queue<MovePossibility> moveHistory = new LinkedList<>();
        List<Move> lastSixMoves = new ArrayList<>();
        Map<String, Double> boardMap = loadBoardMap();
        System.out.println("Loaded " + boardMap.size() + " Game States...\n");

        int runTimes = 25000;
        int longestGame = 0;
        double averageMoves = 0;
        int whiteWins = 0;
        int blackWins = 0;
        int draws = 0;

        for (int run = 0; run < runTimes; run++) {
            lastSixMoves.clear();
            System.out.println("Running #" + (run + 1) + "... (" + (runTimes - run) + " left)");
            Board board = new Board();
            MoveEngine blackMoveEngine = new MoveEngine(board, "black", boardMap);
            MoveEngine whiteMoveEngine = new MoveEngine(board, "white", boardMap);

            boolean whiteTurn = true;
            String winner = "";
            int moves = 0;
            boolean draw = false;
            while (board.canGameContinue()) {
                winner = whiteTurn ? "white" : "black";

                MovePossibility newMove = whiteTurn ? whiteMoveEngine.getNextBestMove() : blackMoveEngine.getNextBestMove();

                // checkmate, cannot make a move
                if (newMove == null) {
                    winner = whiteTurn ? "black" : "white";
                    System.out.println("WINNER: " + winner);
                    if (winner.equals("white")) {
                        whiteWins++;
                    } else {
                        blackWins++;
                    }
                    break;
                }
                board.makeMove(newMove.getMove());

                // draw checking, checks the last 6 moves from each player
                lastSixMoves.add(newMove.getMove());
                if (lastSixMoves.size() == 12) {
                    // last
                    if ((lastSixMoves.get(0).equals(lastSixMoves.get(4)) &&
                            lastSixMoves.get(0).equals(lastSixMoves.get(8)) &&
                            lastSixMoves.get(1).equals(lastSixMoves.get(5)) &&
                            lastSixMoves.get(1).equals(lastSixMoves.get(9)) &&
                            lastSixMoves.get(2).equals(lastSixMoves.get(6)) &&
                            lastSixMoves.get(2).equals(lastSixMoves.get(10)) &&
                            lastSixMoves.get(3).equals(lastSixMoves.get(7)) &&
                            lastSixMoves.get(3).equals(lastSixMoves.get(11))) ||
                            moves > 2000) {
                        draw = true;
                        draws++;
                        System.out.println("DRAW BY REPETITION");
                        break;
                    }
                    lastSixMoves.remove(0);
                }

                // check insufficient material (no pawns) both sides either
                List<Piece> blackPieces = new ArrayList<>();
                List<Piece> whitePieces = new ArrayList<>();
                boolean canInsufficientMaterial = true;
                Piece[][] b = board.getBoard();
                /*
                    A lone king
                    a king and bishop
                    a king and knight
                */

                for (Piece[] pb : b) {
                    for (Piece p : pb) {
                        if (p != null) {
                            if (p instanceof Queen || p instanceof Rook || p instanceof Pawn) {
                                canInsufficientMaterial = false;
                                break;
                            }

                            if (p.getColor().equals("white")) {
                                whitePieces.add(p);
                            } else {
                                blackPieces.add(p);
                            }
                        }
                    }
                }

                if (canInsufficientMaterial && blackPieces.size() <= 2 && whitePieces.size() <= 2) {
                    draw = true;
                    draws++;
                    System.out.println("DRAW BY INSUFFICIENT MATERIALS");
                    break;
                }

                // ML
                if (moveHistory.size() >= 12) {
                    MovePossibility oldMove = moveHistory.poll();
                    String oldIdentity = oldMove.getBoard().getIdentity();

                    double boardScore = boardMap.getOrDefault(oldIdentity, Scoring.ML_INITIAL_MAP_SCORE);
                    // if this move was good for me
                    if (oldMove.getScore() < newMove.getScore()) {
                        boardScore += Scoring.ML_MAP_SCORE_INCREMENT;
                    } else if (oldMove.getScore() > newMove.getScore()) {
                        boardScore -= Scoring.ML_MAP_SCORE_INCREMENT;
                    }
                    boardMap.put(oldIdentity, boardScore);
                }
                if (verbose) {
                    System.out.println(winner + " " + (moves + 1));
                    System.out.println(newMove.getMove().toString());
                    System.out.println(winner + " score: " + newMove.getScore());
                    System.out.println(board + "\n");
                }

                if (moves % 50 == 0) {
                    System.out.println(moves + " Moves..");
                }

                moveHistory.add(newMove);
                whiteTurn = !whiteTurn;
                moves++;
            }

            MovePossibility remainingMove = moveHistory.poll();
            while (remainingMove != null) {
                String oldIdentity = remainingMove.getBoard().getIdentity();
                double boardScore = boardMap.getOrDefault(oldIdentity, Scoring.ML_INITIAL_MAP_SCORE);

                if (!draw) {
                    // if this move was good for me
                    if (remainingMove.getAiColor().equals(winner)) {
                        boardScore += Scoring.ML_MAP_SCORE_WINNER;
                    } else {
                        boardScore -= Scoring.ML_MAP_SCORE_WINNER;
                    }
                } else {
                    boardScore += Scoring.ML_MAP_SCORE_DRAW;
                }

                boardMap.put(oldIdentity, boardScore);

                remainingMove = moveHistory.poll();
            }

            if (moves > longestGame) {
                longestGame = moves;
            }
            averageMoves += moves;

            System.out.println("White wins: " + whiteWins + "\tBlack wins: " + blackWins + "\tDraws: " + draws);
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
        try {
            properties.load(new FileInputStream(MAP_FILE_PATH));
        } catch (FileNotFoundException e) {
            File boardMapFile = new File(MAP_FILE_PATH);
            if (!boardMapFile.createNewFile()) {
                throw new IOException("Failure to create desired board map");
            }
        }

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

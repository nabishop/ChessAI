package Models;

import java.util.HashMap;

public final class ModelConstants {
    // colors
    public static final String BLACK_COLOR = "black";
    public static final String WHITE_COLOR = "white";

    // scores
    public static final int ROOK_SCORE = 50;
    public static final int KNIGHT_SCORE = 30;
    public static final int BISHOP_SCORE = 30;
    public static final int KING_SCORE = 1000;
    public static final int QUEEN_SCORE = 100;
    public static final int PAWN_SCORE = 10;

    // ascii of pieces with appropriate color
    public static final String BLACK_BISHOP_ASCII = "\u2657";
    public static final String WHITE_BISHOP_ASCII = "\u265D";
    public static final String BLACK_KING_ASCII = "\u2654";
    public static final String WHITE_KING_ASCII = "\u265A";
    public static final String BLACK_KNIGHT_ASCII = "\u2658";
    public static final String WHITE_KNIGHT_ASCII = "\u265E";
    public static final String BLACK_PAWN_ASCII = "\u2659";
    public static final String WHITE_PAWN_ASCII = "\u265F";
    public static final String BLACK_QUEEN_ASCII = "\u2655";
    public static final String WHITE_QUEEN_ASCII = "\u265B";
    public static final String BLACK_ROOK_ASCII = "\u2656";
    public static final String WHITE_ROOK_ASCII = "\u265C";

    public final static HashMap<String, Piece> ASCII_TO_PIECE = generateAsciiToPiece();

    private static HashMap<String, Piece> generateAsciiToPiece() {
        HashMap<String, Piece> asciiToPiece = new HashMap<>();
        asciiToPiece.put(BLACK_BISHOP_ASCII, new Bishop(BLACK_COLOR));
        asciiToPiece.put(WHITE_BISHOP_ASCII, new Bishop(WHITE_COLOR));
        asciiToPiece.put(BLACK_KING_ASCII, new King(BLACK_COLOR));
        asciiToPiece.put(WHITE_KING_ASCII, new King(WHITE_COLOR));
        asciiToPiece.put(BLACK_KNIGHT_ASCII, new Knight(BLACK_COLOR));
        asciiToPiece.put(WHITE_KNIGHT_ASCII, new Knight(WHITE_COLOR));
        asciiToPiece.put(BLACK_PAWN_ASCII, new Pawn(BLACK_COLOR));
        asciiToPiece.put(WHITE_PAWN_ASCII, new Pawn(WHITE_COLOR));
        asciiToPiece.put(BLACK_QUEEN_ASCII, new Queen(BLACK_COLOR));
        asciiToPiece.put(WHITE_QUEEN_ASCII, new Queen(WHITE_COLOR));
        asciiToPiece.put(BLACK_ROOK_ASCII, new Rook(BLACK_COLOR));
        asciiToPiece.put(WHITE_ROOK_ASCII, new Rook(WHITE_COLOR));
        return asciiToPiece;
    }
}

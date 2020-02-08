package com.chess.engine.board;

import java.util.*;

public class BoardUtils {
    //Currently only columns that result in edge case for a Knight move
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);
    public static final boolean[] FIRST_ROW = initRow(0);
    public static final boolean[] SECOND_ROW = initRow(8);
    public static final boolean[] SEVENTH_ROW = initRow(48);
    public static final boolean[] EIGHTH_ROW = initRow(56);
    //The chessboard is 8x8
    public static final int START_TILE_INDEX = 0;
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;

    public static final List<String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();

    private BoardUtils(){
        throw new RuntimeException("You cannot instantiate Board Utils");
    }
    //Determines an array of coordinates for every column
    private static boolean[] initColumn(int columnNumber){
        final boolean[] column = new boolean[64];
        do{
            //The column is an array of 64 and so only the tiles for that column will be set to true
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        }while (columnNumber < NUM_TILES);
        return column;
    }

    public static boolean[] initRow(int rowNumber){
        final boolean[] row = new boolean[NUM_TILES];
        do {
            row[rowNumber] = true;
            rowNumber++;
        }while(rowNumber % NUM_TILES_PER_ROW !=0);

        return row;
    }
    private Map<String,Integer> initializePositionToCoordinateMap(){
        final Map<String,Integer> positionToCoordinate = new HashMap<>();
        for (int i = START_TILE_INDEX; i < NUM_TILES; i++){
            positionToCoordinate.put(ALGEBRAIC_NOTATION.get(i), i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }
    private static List<String> initializeAlgebraicNotation() {
        return Collections.unmodifiableList(Arrays.asList(
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"));
    }
    //A chess board only has 64 tiles
    public static boolean isValidTileCoordinate(final int coordinate){
        return coordinate >= 0 && coordinate < 64;
    }

    public int getCoordinateAtPosition(final String position){
        return POSITION_TO_COORDINATE.get(position);
    }
    public static String getPositionAtCoordinate(final int coordinate){
        return ALGEBRAIC_NOTATION.get(coordinate);
    }
}

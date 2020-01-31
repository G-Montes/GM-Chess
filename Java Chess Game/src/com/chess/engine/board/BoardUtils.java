package com.chess.engine.board;

import com.google.common.collect.ImmutableList;

public class BoardUtils {
    //Currently only columns that result in edge case for a Knight move
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);

    public static final boolean[] SECOND_ROW = initRow(8);
    public static final boolean[] SEVENTH_ROW = initRow(48);
    //The chessboard is 8x8
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;

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


    //A chess board only has 64 tiles
    public static boolean isValidTileCoordinate(final int coordinate){
        return coordinate >= 0 && coordinate < 64;
    }
}

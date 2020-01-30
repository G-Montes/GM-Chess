package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

/*
* This Tile class helps define a tile. The key features of this first run are that:
* a)this will capture the tile coordinate
* b)this contains two subclasses that determine whether or not a tile is occupied
*   by a piece. If it is occupied, it will eventually determine which piece type is
*   currently occupying it.
*/
public abstract class Tile {
    protected final int tileCoordinate;
    private static final Map<Integer,EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();
    //Creates all the tiles at once at the beginning
    private static Map<Integer,EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer,EmptyTile> emptyTileMap = new HashMap<>();

        for (int i = 0; i < BoardUtils.NUM_TILES; i++){
            emptyTileMap.put(i,new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }
    //Tile gets created that is either empty or occupied by a piece
    public static Tile createTile(final int tileCoordinate, final  Piece piece){
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }
    //0-63 are the possible coordinates since this is an 8x8 board
    private Tile(final int tileCoordinate){

        this.tileCoordinate = tileCoordinate;
    }
    public abstract boolean isTileOccupied();
    public abstract Piece getPiece();
    //For when a tile is unoccupied by a piece
    public static final class EmptyTile extends Tile {
        private EmptyTile(final int coordinate){
            super(coordinate);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        public Piece getPiece(){
            return null;
        }
    }
    //For when a tile is occupied by a piece, which will require the tile coordinate and the piece
    public static final class OccupiedTile extends Tile{
        private final Piece pieceOnTile;

        private OccupiedTile(int tileCoordinate, final Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }
        @Override
        public boolean isTileOccupied() {
            return true;
        }
        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }
    }
}

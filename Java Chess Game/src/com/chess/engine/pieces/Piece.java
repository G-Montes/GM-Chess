package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import java.util.Collection;

public abstract class Piece {

    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    //Determines the piece coordinate and whether it is a black or white piece
    Piece(final int piecePosition, final Alliance pieceAlliance) {
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        //TO DO MORE WORK HERE
        this.isFirstMove = false;
    }

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }
    public boolean isFirstMove(){
        return this.isFirstMove;
    }
    //To hold the coordinates of legal moves a piece has
    public abstract Collection<Move> calculateLegalMoves(final Board board);
}


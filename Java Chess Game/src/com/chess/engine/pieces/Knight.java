package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17,-15,-10,-6,6,10,15,17};

    Knight(final int piecePosition, final Alliance pieceAlliance){
        super(piecePosition,pieceAlliance);

    }

    @Override
    /*This will iterate through candidate moves and determine whether on not the move is
     * legal. It will first check whether the position would be on a board, and if so checks
     * to see is there a piece occupying the tile. If a piece is present, piece alliance is
     * checked to determine if capturing is a possibility.
     */
    public List<Move> calculateLegalMoves(Board board) {
        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();


        for(final int currentCandidate : CANDIDATE_MOVE_COORDINATES){
            candidateDestinationCoordinate = this.piecePosition + currentCandidate;

            if(true/*isValidTileCoordinate*/){
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new Move());
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if (this.pieceAlliance != pieceAlliance){
                        legalMoves.add(new Move());
                    }
                }

            }
        }

        return ImmutableList.copyOf(legalMoves);
    }
}

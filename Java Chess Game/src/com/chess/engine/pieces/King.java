package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends Piece {
    private static final int[] CANDIDATE_MOVE_VECTOR = {-9,-8,-7,-1,1,7,8,9};
    King(int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for(int currentCandidateOffset : CANDIDATE_MOVE_VECTOR){
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if(isEighthColumnExclusion(this.piecePosition, currentCandidateOffset) ||
               isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
                continue;
            }
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                /*if there's no piece on the target tile, then it can be added to the list of legal moves
                 *that only involve positional change
                 * */
                if (!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new Move.MajorMove(board,this, candidateDestinationCoordinate));
                } else {
                    /*If the tile is occupied, then we have to determine whether the piece that occupies,
                     * is an ally. This determines whether a capturing move will be added to the legal move list
                     */
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance){
                        legalMoves.add(new Move.AttackMove(board,this,candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1 ||
                                                            candidateOffset == -9 ||
                                                            candidateOffset == -7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -7 ||
                                                            candidateOffset == 1 ||
                                                            candidateOffset == 9);
    }
}

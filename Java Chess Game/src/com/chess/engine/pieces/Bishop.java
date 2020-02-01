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

import static com.chess.engine.board.Move.*;

public class Bishop extends Piece {
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9,-7,7,9};

    public Bishop(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.BISHOP, piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestinationCoordinate = this.piecePosition;

            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                //test for edge cases that would occur in 1st and 8th column
                if(isFirstColumnExclusion(candidateDestinationCoordinate,candidateCoordinateOffset) ||
                   isEighthColumnExclusion(candidateDestinationCoordinate,candidateCoordinateOffset)){
                    break;
                }
                candidateDestinationCoordinate += candidateCoordinateOffset;

                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    /*if there's no piece on the target tile, then it can be added to the list of legal moves
                     *that only involve positional change
                     */
                    if (!candidateDestinationTile.isTileOccupied()){
                        legalMoves.add(new MajorMove(board,this, candidateDestinationCoordinate));
                    } else {
                        /*If the tile is occupied, then we have to determine whether the piece that occupies,
                         * is an ally. This determines whether a capturing move will be added to the legal move list
                         */
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        if (this.pieceAlliance != pieceAlliance){
                            legalMoves.add(new AttackMove(board,this,candidateDestinationCoordinate,
                                                          pieceAtDestination));
                        }
                        /*A break is useful because if there's another piece occupying a candidate vector coordinate
                         *then there's no need to check the rest of the vector as they're inaccessible by the piece.
                         */
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
    }

    //Edge cases for the bishop that would result in an illegal candidate move
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7);
    }
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 9);
    }
}

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

public class Knight extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17,-15,-10,-6,6,10,15,17};

    public Knight(final int piecePosition, final Alliance pieceAlliance){
        super(piecePosition,pieceAlliance);
    }
    @Override
    /*This will iterate through candidate moves and determine whether on not the move is
     * legal. It will first check whether the position would be on a board, and if so checks
     * to see is there a piece occupying the tile. If a piece is present, piece alliance is
     * checked to determine if capturing is a possibility.
     */
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        //This for loop will iterate through all move possibilities for the knight for a given coordinate
        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){

            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            //First a check to determine if the candidate move would be in range (0-64)
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                /*Will check the edge cases for the knight for when the knight lands on the 1st, 2nd, 7th, and 8th
                *column. If it is true, then it's an illegal move and the loop can skip over the rest and check the
                *other coordinates
                */
                if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                   isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                   isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                   isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
                    continue;
                }

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                /*if there's no piece on the target tile, then it can be added to the list of legal moves
                 *that only involve positional change
                 * */
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
                }

            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    //The edge cases that would result in a potential knight move to be out of bounds
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 ||
                                                            candidateOffset == -10 ||
                                                            candidateOffset == 6 ||
                                                            candidateOffset == 15);
    }
    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 ||
                                                             candidateOffset == 6);
    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 ||
                                                              candidateOffset == 10);
    }
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (currentPosition == -15 ||
                                                             currentPosition == -6 ||
                                                             currentPosition == 10 ||
                                                             currentPosition == 17);
    }
}

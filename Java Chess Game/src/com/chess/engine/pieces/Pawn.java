package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {
    private static final int[] CANDIDATE_MOVE_VECTOR = {7,8,9,16};
    Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for(int currentCandidateOffset : CANDIDATE_MOVE_VECTOR){
            /*Since the pawns have directionality, we have to determine whether the piece is black or white
             *It will go in a negative direction if it is a white piece and positive if it is black
             */
            final int candidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * currentCandidateOffset);
            //For when the destination tile is occupied by another piece
            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }
            //When the pawn piece only wants to make a move up the board
            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                //Will be adjusted to be a pawn move instead of a piece move
                legalMoves.add(new Move.MajorMove(board,this, candidateDestinationCoordinate));
            }
            /*For candidate moves that involved moving up two rows in a single move, we must check:
             *a) whether this would be the pawn's first move
             *b) and whether the piece is in its respective started position based on color
             */
            else if(currentCandidateOffset == 16 &&
                     this.isFirstMove() &&
                    (BoardUtils.SECOND_ROW[this.piecePosition] &&  this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite())){
                //another check is needed to determine whether or not there's a piece in the way
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                   !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                }
            }
            //Covers the cases for the attacking moves
            /*For this conditional, what we're checking the edge cases that would result from a pawn being at the edge
             *of a board. This first conditional will check to see if there are any edge cases that would occur
             *if the the piece attempted an offset of 7. For further clarification, see *A */
            else if((currentCandidateOffset == 7) &&
                    !(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset, this.pieceAlliance) ||
                      isEighthColumnExclusion(this.piecePosition, currentCandidateOffset, this.pieceAlliance))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        //Will need to set another attack move for this piece
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }
            }
            /*For this, there's a check for edge cases that would result from having an offset to 9. For further clarification, see *A
             */
            else if ((currentCandidateOffset == 9) &&
                    !(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset, this.pieceAlliance) ||
                            isEighthColumnExclusion(this.piecePosition, currentCandidateOffset, this.pieceAlliance))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        //Will need to set another attack move for this piece
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }
            }


        }
        return ImmutableList.copyOf(legalMoves);
    }
    //Edge cases for the queen that would result in an illegal candidate move
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset, final Alliance alliance){
        if (alliance.isWhite()){
            return BoardUtils.FIRST_COLUMN[currentPosition] && candidateOffset == 9;
        }
        else{
            return BoardUtils.FIRST_COLUMN[currentPosition] && candidateOffset == 7;
        }
    }
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset, final Alliance alliance){
        if(alliance.isWhite()){
            return BoardUtils.EIGHTH_COLUMN[currentPosition] && candidateOffset == 7;
        }
        else{
            return BoardUtils.EIGHTH_COLUMN[currentPosition] && candidateOffset == 9;
        }
    }
}
/*A The way this works is that each column has it's own method. If the pawn is NOT in the column then the test will fail, meaning that
 *the piece is not in the correct column to trigger an exclusion. In case the pawn IS in the appropriate column, then the test will only
 *return true if the candidate offset would trigger an exclusion given the pawn color. For example, if the candidateOffset is 7, then a
 *black pawn on the first column would result in true in the isFirstColumnExclusion but false in the isEighthColumnExclusion (since the
 *black is on the first column). A white piece in the same conditions on the other hand would return false for both methods. Refer to
 *Logic_Behind_Pawn_Attack_Implementation for all possible cases. */


/*
            //Original Code used to implement attacking moves. Kept as backup in case original leads to fewer errors
            else if (currentCandidateOffset == 7 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                     (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        //Will need to set another attack move for this piece
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }
            }
            else if (currentCandidateOffset == 9 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack() ||
                      (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())))){
                if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        //Will need to set another attack move for this piece
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }
            }*/

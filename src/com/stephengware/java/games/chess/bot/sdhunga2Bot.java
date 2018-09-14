package com.stephengware.java.games.chess.bot;

import java.util.Iterator;

import com.stephengware.java.games.chess.bot.Bot;
import com.stephengware.java.games.chess.state.*;
import com.stephengware.java.games.chess.bot.Result;

/**
 * A chess bot which doesn't select its next move at random.
 * 
 * @author Shrey Dhungana
 **/

public class sdhunga2Bot extends Bot {
	/**
	 * Constructs a new chess bot named "sdhunga2"
	 *
	 * */
   // private State childNext = new State();

	public sdhunga2Bot() {
		super("sdhunga2");
	}

	//method to choose moves
	@Override
	protected State chooseMove(State root) {
		Result retVal = new Result(root, 0.0);
        Result validVal = retVal;
		boolean isMax = false;
		int depth = 4;

		//use of iterative deepening to explore the nodes permissible by the search limit

        if(root.player.equals(Player.WHITE)) {
            isMax = true;            //if maximizing player
        }
        retVal = abPruning(root, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, depth, isMax);
        if(retVal.myState.previous.equals(root)) {
            return retVal.myState;
        }
        while(!retVal.myState.previous.equals(root)){
            retVal.myState = retVal.myState.previous;
        }
        return retVal.myState;

	}

	/* function to implement alpha-beta pruning algorithm and return Result(State,Double)
       alpha-beta algorithm , adapted from Wikipedia
    */

	private Result abPruning(State myState, double alpha, double beta, int depth, boolean isMax ){

        if(myState.over || depth == 0){
	     	return new Result(myState, calculateScore(myState));        //return the result
        }
		 //v = negative infinity

        if(isMax){
             Double scoreValue = Double.NEGATIVE_INFINITY;
             Result retVal = new Result();
             Result validVal = retVal;
             Iterator<State> iterator = myState.next().iterator();
             while(!myState.searchLimitReached() && iterator.hasNext()) {

                 Result child = new Result(iterator.next(), 0.0);
                 scoreValue = Math.max(scoreValue, abPruning(child.myState, alpha, beta, depth - 1, false).score);
                 if(!myState.searchLimitReached() && Double.compare(scoreValue, alpha) > 0){
                     validVal = child;
                 }
                 alpha = Math.max(alpha, scoreValue);
                 if(beta <= alpha)
                     break;
             }

             retVal = new Result(validVal.myState, scoreValue);
             return retVal ;
        }//max ends

        //min starts

        else{
		     Double scoreValue = Double.POSITIVE_INFINITY;
             Result retVal = new Result();
             Result validVal = retVal;
             Iterator<State> iterator = myState.next().iterator();
		     while(!myState.searchLimitReached() && iterator.hasNext() ){
		         Result child = new Result(iterator.next(), 0.0);
		         scoreValue = Math.min(scoreValue, abPruning(child.myState, alpha, beta, depth -1, true).score);
		         if(!myState.searchLimitReached() && Double.compare(scoreValue, beta)< 0){
                     validVal = child;
                 }
                 beta = Math.min(beta, scoreValue);
		         if(beta <= alpha)
		             break;
             }
             retVal =  new Result(validVal.myState, scoreValue);
		     return retVal;
        }//min ends

    }//end of abPruning algorithm

	//function to calculate score given some State

	private double calculateScore(State myState){
		     double maxScore = 0;
		     double minScore = 0;
		     double score = 0.0;
		     maxScore += myState.board.countPieces(Player.WHITE) - myState.board.countPieces(Player.BLACK);
             if(myState.over){

                 if(myState.check) {

                     if (myState.player.equals(Player.WHITE)) {
                         return -1000;
                     }
                     return 1000;
                 }
                 //if stall-mate
                 return 0;
             }
            if(myState.check) {
                if (myState.player.equals(Player.WHITE)) {
                    return maxScore -= 2;
                }
                    return minScore -= 2;
            }


             for(Piece piece : myState.board){

                 if(piece.player.equals(Player.WHITE)){

                     if(piece instanceof Queen){
                         if(myState.board.countPieces() > 20 ) {
                             maxScore += 9 + piece.rank*0.08;
                         }
                         maxScore += 10 + piece.rank*0.01;
                     }
                     else if(piece instanceof Rook){
                         if(myState.board.countPieces() > 20 ) {
                             maxScore += 5 + piece.rank * 0.03;
                         }
                         maxScore += 5.5 + piece.rank * 0.05;
                     }
                     else if(piece instanceof Bishop){
                         if(myState.board.countPieces() > 18 ) {
                             maxScore += 4;
                         }
                         maxScore += 4 + piece.rank* 0.04;

                     }
                     else if (piece instanceof Knight){
                         if(myState.board.countPieces() <= 12){
                             maxScore += 3.5;
                         }
                         maxScore += 4 + piece.rank * 0.2;
                     }
                     else if(piece instanceof Pawn){
                       //  maxScore += 1 + ((piece.rank)*0.05);
                         if(myState.turn < 3){
                             if(piece.file == 2 || piece.file == 4 || piece.file ==5){
                                 maxScore += 4 + (piece.rank)* 0.05;
                             }
                         }
                         maxScore += 1 + ((piece.rank)*0.05);
                     }

                 }// white(max player) ends
                 else{

                     if(piece instanceof Queen){
                         if(myState.board.countPieces() > 20 ) {
                             minScore += 9 + ( 7 - piece.rank)*0.24;
                         }
                         minScore += 10 + (7 - piece.rank)*0.1;
                     }
                     else if(piece instanceof Rook){
                         if(myState.board.countPieces() > 20 ) {
                             minScore += 5 + (7 - piece.rank) * 0.03;
                         }
                         minScore += 5.5 + piece.rank * 0.05;
                     }
                     else if(piece instanceof Bishop){
                         if(myState.board.countPieces() > 18 ) {
                             minScore += 4;
                         }
                         minScore += 4 + (7 - piece.rank)* 0.04;

                     }
                     else if (piece instanceof Knight){

                         if(myState.board.countPieces() <= 18){
                             minScore += 3.5;
                         }
                         minScore += 4 + (7 - piece.rank )* 0.2;
                     }
                     else if(piece instanceof Pawn){

                         if(myState.turn < 3){
                             if(piece.file == 2 || piece.file ==4 || piece.file == 5){
                                 minScore += 4 + (7 - piece.rank) * 0.05;
                             }
                         }
                         minScore += 1 + ((7 - piece.rank)*0.05);
                     }

                 }
             }//for loop ends
             score = maxScore - minScore;
             return score;
	}//end of calculate

}//end of class

package com.stephengware.java.games.chess.bot;

import com.stephengware.java.games.chess.state.*;
import com.stephengware.java.games.chess.bot.Bot;
import com.sun.istack.internal.NotNull;

import java.util.Iterator;

/**
 * A chess bot which does rudimentary planning
 * @author Shrey
 */
public class MyBot extends Bot {

	//public State choosenMove;

    public MyBot() {
		super("sdhunga2");
	}
	@Override
	protected State chooseMove(State root){
        Decision retVal = new Decision(root, 0);
        Decision bestVal = retVal;
        boolean isMax = false;
        for(int depth = 2; !root.searchLimitReached(); depth +=2){
            if(root.player.equals(Player.WHITE)){
                isMax = true;
            }
            retVal = abPruning(root, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, depth, isMax);
            if(!(root.searchLimitReached())){
                bestVal = retVal;
            }

        }
            while (!bestVal.state.previous.equals(root)) {
                bestVal.state = bestVal.state.previous;
                System.out.println(bestVal.state);
               }

            return bestVal.state;


    }//end of choose move

    //alphabeta pruning algorithm

	private  Decision abPruning(State myState, double alpha, double beta, int depth, boolean isMax ){
	    if(myState.over || depth <= 0){
	        return new Decision(myState, heuristic(myState));  //
        }
        Decision val = new Decision();
        Decision bestVal = new Decision();

        if(isMax){   //for maximizing player
            double best = alpha;
            Iterator<State> iterator = myState.next().iterator();
            while(!myState.searchLimitReached() && iterator.hasNext()){
                Decision nextNode = new Decision(iterator.next(),0.0);

                val = abPruning(nextNode.state, alpha, beta ,depth -=1,false);
                if(val.utility > best){
                    best = val.utility;
                    bestVal = val;
                }
                if(best >= beta){
                    return bestVal;
                }
                 alpha = Math.max(alpha, best);

            }
            return bestVal;

	        }                       //when minimizing player
            else{
                double min = Double.POSITIVE_INFINITY;
                Iterator<State> iterator = myState.next().iterator();
                while(!myState.searchLimitReached() && iterator.hasNext()){
                    Decision nextNode = new Decision(iterator.next(),0.0);
                    val= abPruning(nextNode.state, alpha, beta , depth -=1, true);
                    }
                    if(val.utility < min){
                        min = val.utility;
                        bestVal = val;
                    }
                    if(min <= alpha){
                        return bestVal;
                    }
                    beta = Math.min(beta,min);
                }
                return bestVal;

        }//end of abpruning

    private double heuristic(State myState){
        double whiteVal = 0.0;
        double blackVal = 0.0;
        double utility = 0.0;
        double retval = 800;
        int pieceNum = myState.board.countPieces();

        whiteVal += myState.board.countPieces(Player.WHITE) - myState.board.countPieces(Player.BLACK);
        if(myState.over) {
            if (myState.check) {
                if (myState.player.equals(Player.WHITE)){
                    return retval;
                }
                else {
                    return -retval;
                }
            }
            else{
                return 0;
            }

        }           //when it is not over
        else{
            for(Piece piece: myState.board){
                if(piece.player.equals(Player.WHITE)){

                    if(piece instanceof Pawn){
                        whiteVal += (1 + (piece.rank - 1) * 0.01);
                        if(myState.turn <=3){
                            if(piece.file == 2 || piece.file == 4 ){
                                whiteVal += 0.2+(piece.file)*0.05;
                            }
                            else if(piece.file == 1 || piece.file == 5){
                                whiteVal += 0.05 + (piece.file)*0.05;
                            }
                            else if(piece.file ==3 || piece.file == 6){
                                whiteVal += 0.1;
                            }
                        }
                        else{
                            whiteVal -= 0.3 +(piece.rank-1)*0.01;
                        }

                    }
                    else if(piece instanceof Knight){
                            if(pieceNum >= 16) {

                            if (piece.rank <= 1 || piece.rank >= 6) {
                                whiteVal -= 0.05;
                            }
                            if (piece.file <= 1 || piece.file >= 7) {
                                whiteVal -= 0.05;
                            }
                        }

                            whiteVal += 4.1 + (piece.rank)*0.04;
                    }

                    else if(piece instanceof Rook){
                        if(pieceNum > 18)

                         whiteVal += 6 + (piece.file - 1)*0.06;
                    }

                    else if(piece instanceof Bishop){
                        whiteVal += 3.4 + (piece.rank - 1)*0.04;
                    }
                    else if(piece instanceof Queen){
                        if(pieceNum > 1){
                            whiteVal += 9 + (piece.rank-1) *0.2;
                        }


                    }
                    else if(piece instanceof King){
                        if(pieceNum > 5)
                            whiteVal -= 1 + (piece.rank -1)*0.04;
                        else{
                            whiteVal = 4 + (piece.rank -1)*02;
                        }
                    }

                }

                else{
                    if(piece instanceof Pawn){
                        blackVal += (1 + (7-piece.rank) * 0.01);
                        if(myState.turn < 3){
                            if(piece.file == 3 || piece.file == 4 ){
                                blackVal += 0.1;
                            }
                            else if(piece.file == 2 || piece.file == 5){
                                blackVal += 0.05;
                            }
                            else if(piece.file ==1 || piece.file == 6){
                                blackVal -= 0.05;
                            }
                            else{
                                blackVal -= 0.1;
                            }
                        }
                    }
                    else if(piece instanceof Rook){
                        blackVal -= 5.1 + (7- piece.rank)*0.01;
                    }
                    else if(piece instanceof Knight){
                        blackVal -= 4 + (7 - piece.rank)*0.4;
                        if(piece.rank <= 1 || piece.rank >= 6){
                            blackVal -= 0.05;
                        }
                        if(piece.file <= 1 || piece.file >= 6){
                            blackVal -= 0.05;
                        }
                    }

                    else if(piece instanceof Bishop){
                        blackVal -= 3.8 * (7 - piece.rank)*0.12;
                    }

                    else if(piece instanceof Queen){
                        blackVal -= 9;
                    }

                    else if(piece instanceof King){
                        blackVal -= 4;
                    }
                }
            }//End of for loop		*/
        }
        utility = whiteVal - blackVal;
        return utility;
	}//end of heuristic

}//end of class

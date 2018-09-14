package com.stephengware.java.games.chess.bot;

import com.stephengware.java.games.chess.state.State;

public class Result {

    //instance variables for this class

    public State myState;
    public double score;

    //constructors

    public Result(State givenState, double givenValue){
        this.myState = givenState;
        this.score = givenValue;

    }

    public Result() {
        this.myState = null;
        this.score = 0.0;

    }


}//end of class Result

package com.stephengware.java.games.chess.bot;

import com.stephengware.java.games.chess.state.*;


public class Decision {

    State state;
    double utility;

    public Decision(){
        this.state = null;
        this.utility = 0.0;
    }

    public Decision(State myState, double myUtility ){
        this.state = myState;
        this.utility = myUtility;
    }


}//class Decision

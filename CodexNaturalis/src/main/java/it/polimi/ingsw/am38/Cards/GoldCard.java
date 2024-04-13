package it.polimi.ingsw.am38.Cards;

import it.polimi.ingsw.am38.Enum.Kingdom;
import it.polimi.ingsw.am38.Miscellaneous.GoldCardPlayableCondition;
import it.polimi.ingsw.am38.Miscellaneous.GoldCardPointsCondition;

public class GoldCard extends PlayableCard{

    private Kingdom kingdom;
    private Kingdom kingdomNeeded;
    private int pointsWon;
    private boolean face; //true is face up, false is face down
    private Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE;
    private Corner faceDownNW, faceDownNE, faceDownSW, faceDownSE;

    //pointsCondition miss here
    public GoldCard(Kingdom k, int p){  //creating cards
        kingdom = k;
        pointsWon = p;
    }
    public int pointCheck(){
        return 0; //momentary
    }
}

package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Kingdom;
import it.polimi.ingsw.am38.Model.Miscellaneous.GoldCardPlayableCondition;
import it.polimi.ingsw.am38.Model.Miscellaneous.GoldCardPointsCondition;

public class GoldCard extends PlayableCard{

    private Kingdom kingdom;
    private Kingdom kingdomNeeded;
    private int pointsWon;
    private boolean face; //true is face up, false is face down
    private Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE;
    private Corner faceDownNW, faceDownNE, faceDownSW, faceDownSE;
    private GoldCardPointsCondition pointsCondition;
    private GoldCardPlayableCondition playableCondition;

    //pointsCondition miss here
    public GoldCard(Kingdom kingdom, int points, GoldCardPlayableCondition playConds, GoldCardPointsCondition pointsCond){  //creating cards
        this.kingdom = kingdom;
        pointsWon = points;
        pointsCondition = pointsCond;
        playableCondition = playConds;

        /*Corner faceUpNW = new Corner();
        Corner faceUpNE = new Corner();
        Corner faceUpSW = new Corner();
        Corner faceUpSE = new Corner();

        Corner faceDownNW = new Corner();
        Corner faceDownNE = new Corner();
        Corner faceDownSW = new Corner();
        Corner faceDownSE = new Corner();*/
    }
    public int pointCheck(){
        return 0; //momentary
    }
}

package it.polimi.ingsw.am38.Cards;

import it.polimi.ingsw.am38.Enum.Kingdom;

public class GoldCard extends PlayableCard{

    private Kingdom kingdom;
    private Kingdom kingdomNeeded;
    private int pointsWon;

    //pointsCondition miss here

    public int pointCheck(){
        return 0; //momentary
    }
}

package it.polimi.ingsw.am38.Cards;

import it.polimi.ingsw.am38.Enum.Kingdom;

public class ResourceCard extends PlayableCard{

    private Kingdom kingdom;

    private boolean face; //true is face up, false is face down
    private Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE;
    private Corner faceDownNW, faceDownNE, faceDownSW, faceDownSE;
    private int givenPoints;

    public ResourceCard(Kingdom k, int p){  //creating cards
        kingdom = k;
        givenPoints = p;
    }
}

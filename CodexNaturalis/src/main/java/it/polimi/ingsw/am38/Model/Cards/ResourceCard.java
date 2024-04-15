package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Kingdom;


public class ResourceCard extends PlayableCard{

    private Kingdom kingdom;

    private boolean face; //true is face up, false is face down
    private Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE;
    private Corner faceDownNW, faceDownNE, faceDownSW, faceDownSE;
    private int givenPoints;

    public ResourceCard(Kingdom kingdom, int points, Corner[] corners){  //momentary, need JSON
        this.kingdom = kingdom;
        this.givenPoints = points;

        /*Corner faceUpNW = new Corner();
        Corner faceUpNE = new Corner();
        Corner faceUpSW = new Corner();
        Corner faceUpSE = new Corner();

        Corner faceDownNW = new Corner();
        Corner faceDownNE = new Corner();
        Corner faceDownSW = new Corner();
        Corner faceDownSE = new Corner();*/

    }
}

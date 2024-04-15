package it.polimi.ingsw.am38.Cards;

import it.polimi.ingsw.am38.Enum.Kingdom;

public class StarterCard extends PlayableCard{
    private Kingdom[] midResources;
    private boolean face; //true is face up, false is face down
    private Corner faceUpNW, faceUpNE, faceUpSW, faceUpSE;
    private Corner faceDownNW, faceDownNE, faceDownSW, faceDownSE;

    public StarterCard(Kingdom[] kingdoms){

        //here to put a for to insert middle kingdoms

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

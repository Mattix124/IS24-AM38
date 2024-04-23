package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Exception.NotPlaceableException;
import it.polimi.ingsw.am38.Exception.NotYourDrawPhaseException;
import it.polimi.ingsw.am38.Exception.NotYourMainPhaseException;
import it.polimi.ingsw.am38.Exception.NotYourTurnException;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * the group of cards that the Players has (never more than 3)
 */
public class Hand {
    /**
     * arrayList, limited to 3 elements, containing all the Cards the Player has available to play
     */
    private final ArrayList<PlayableCard> cards;

    /**
     * constructor method for this class
     */
    public Hand(){
        cards = new ArrayList<>(3);
    }

    public void addCard(PlayableCard card){
        cards.add(card);
    }
    public void removeCard(PlayableCard card){
            cards.remove(card);
    }
    public PlayableCard getCard(int i){
        return this.cards.get(i);
    }
}

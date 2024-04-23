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
    /**
     * method used by the Player to draw a Card from any deck and add it to his hand (only when it's his draw phase)
     * @param card the Card selected to be drawn
     * @throws NotYourDrawPhaseException tells the Player he can't draw when it's not his draw phase
     */
    public void drawCard(PlayableCard card) throws NotYourDrawPhaseException {
        if(cards.size() < 3)
            cards.add(card);
        else
            throw new NotYourDrawPhaseException("You can't draw a card right now!");
    }
    public PlayableCard getCard(int i){
        return this.cards.get(i);
    }
}

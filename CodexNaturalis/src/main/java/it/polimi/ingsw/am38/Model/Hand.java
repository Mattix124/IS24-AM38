package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Exception.InvalidInputException;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * the group of cards that the Players has (never more than 3)
 */
public class Hand implements Serializable {
    /**
     * arrayList, limited to 3 elements, containing all the Cards the Player has available to play
     */
    private final ArrayList<PlayableCard> cardsInHand;

    /**
     * constructor method for this class
     */
    public Hand(){
        cardsInHand = new ArrayList<>(3);
    }

    /**
     * method used to add a PlayableCard to the cards ArrayList
     * @param card to add to the Hand
     */
    public void addCard(PlayableCard card){
        cardsInHand.add(card);
    }

    /**
     * method used to remove a PlayableCard from the cards ArrayList (when it's played)
     * @param card to remove from the Hand
     */
    public void removeCard(PlayableCard card){
        cardsInHand.remove(card);
    }

    /**
     * getter for the PlayableCard in cardsInHand chosen
     * @param i the index of the PlayableCard to return
     * @return the PlayableCard which index is the given parameter
     */
    public PlayableCard getCard(int i) throws InvalidInputException{
        if(cardsInHand.get(i-1) != null)
            return this.cardsInHand.get(i-1);
        else throw new InvalidInputException("You can only choose from the cards in your hand, numbers from 1 to 3!");
    }
}

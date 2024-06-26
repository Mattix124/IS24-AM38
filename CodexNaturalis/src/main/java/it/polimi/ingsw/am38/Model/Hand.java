package it.polimi.ingsw.am38.Model;

import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The group of cards that the Players has (never more than 3)
 */
public class Hand implements Serializable {
    @Serial
    private static final long serialVersionUID = 1658767354768L;

    /**
     * ArrayList, limited to 3 elements, containing all the Cards the Player has available to play
     */
    private final ArrayList<PlayableCard> cardsInHand;

    /**
     * Constructor method for this class
     */
    public Hand(){
        cardsInHand = new ArrayList<>(3);
    }

    /**
     * Method used to add a PlayableCard to the cards ArrayList
     *
     * @param card to add to the Hand
     */
    public void addCard(PlayableCard card){
        if(cardsInHand.size() == 3){
            for (PlayableCard c : cardsInHand)
                if (c.getCardID() == 0)
                    cardsInHand.set(cardsInHand.indexOf(c), card);
        }else
            cardsInHand.add(card);
    }

    /**
     * Method used to remove a PlayableCard from the cards ArrayList (when it's played)
     *
     * @param card to remove from the Hand
     */
    public void removeCard(PlayableCard card){
        int index = cardsInHand.indexOf(card);
        cardsInHand.set(index, new ResourceCard(0, "null", null, null, 0, "null", "null", "null", "null", "null", "null", "null", "null"));
    }

    /**
     * Getter for the PlayableCard in cardsInHand chosen
     *
     * @param i the index of the PlayableCard to return
     * @return the PlayableCard which index is the given parameter
     */
    public PlayableCard getCard(int i){
            return this.cardsInHand.get(i);
    }

    /**
     * Getter method for the list of cards in this Hand
     *
     * @return an ArrayList containing all the PlayableCards present in this Hand
     */
    public ArrayList<PlayableCard> getCardsInHand(){
        return this.cardsInHand;
    }
}

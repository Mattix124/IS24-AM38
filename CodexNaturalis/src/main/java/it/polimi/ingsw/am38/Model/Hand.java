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
     * name of the owner of this group of Cards
     */
    private final String nickName;

    /**
     * constructor method for this class
     * @param player is the nickName of the Player "constructing" this hand
     */
    public Hand(String player){
        this.nickName = player;
        cards = new ArrayList<>(3);
    }

    /**
     * method used by the Player to draw a Card from any deck and add it to his hand (only when it's his draw phase)
     * @param card the Card selected to be drawn
     * @throws NotYourDrawPhaseException tells the Player he can't draw when it's not his draw phase
     */
    public void drawCard(PlayableCard card) throws NotYourDrawPhaseException {
        if(cards.size() == 2)
            cards.add(card);
        else
            throw new NotYourDrawPhaseException("You can't draw a card right now!");
    }

    /**
     * method used by the Player to play a Card from his Hand on his Field at the chosen Coordinates
     * (only when it's his main phase and that position is legal)
     * @param card the Card the Player wants to play
     * @param coords the position in which the Player wants to play the Card
     * @throws NotPlaceableException tells the Player when his placing wasn't allowed there
     * @throws NotYourMainPhaseException tells the Player when it's not their main phase (phase where they can play a Card)
     */
    public void playCard(PlayableCard card, Coords coords) throws NotPlaceableException, NotYourMainPhaseException {

    }
}

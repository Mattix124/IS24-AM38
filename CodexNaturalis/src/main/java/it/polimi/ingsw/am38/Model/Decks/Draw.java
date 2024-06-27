package it.polimi.ingsw.am38.Model.Decks;

import it.polimi.ingsw.am38.Exception.EmptyDeckException;
import it.polimi.ingsw.am38.Model.Player;

/**
 * Interface to allow the draw from different type of decks
 */
public interface Draw {
    /**
     * Method to draw a card
     *
     * @param player who wants to draw
     * @param n index of the card to draw
     * @throws EmptyDeckException
     */
    public void draw(Player player, int n) throws EmptyDeckException;
}

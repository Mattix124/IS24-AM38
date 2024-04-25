package it.polimi.ingsw.am38.Model.Decks;

import it.polimi.ingsw.am38.Exception.EmptyDeckException;
import it.polimi.ingsw.am38.Model.Player;

public interface Draw {
    public void draw(Player player, Integer i) throws EmptyDeckException;
}

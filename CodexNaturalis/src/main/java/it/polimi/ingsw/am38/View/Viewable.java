package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Cards.*;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * implemented by both CLI/TUI and GUI, contains all the methods they implement
 */
public interface Viewable {
    //----------------------------------------------------------------------------------------- on demand / when needed
    /**
     * shows the field of the Player having the chosen nickname
     * @param nickname a String containing the name of the Player whose field is being requested
     */
    void showPlayerField(String nickname);

    /**
     * updates the score of the Player who just ended their turn (4 times each round)
     * @param nickname a String containing the name of the Player whose turn just ended
     * @param score an int representing the Score of the given Player
     */
    void updateScore(String nickname, int score);

    /**
     * updates the Hand of the Player after he plays a card and draws a card (tbd, maybe 2 different methods)
     */
    void updateHand(int n, PlayableCard card);
    void updateEnemiesHandColors(String nick, Symbol[] handColors);
    void updateScreen();

    /**
     * shows all the existing and available information while this Player has to choose his personal Objective between the 2 possible choices
     * @param pc an HashMap of all Players' nicknames (key-String) and their Color (value)
     * @param hcc an HashMap of all Players' nicknames (key-String) and the colors of the cards in their Hand (value-Symbol[])
     * @param psc an HashMap of all Players' nicknames (key-String) and their choice of facing for their StarterCard (value-StarterCard)
     * @param ownHand a LinkedList containing the PlayableCard in this Player's Hand
     * @param sharedObj1 an ObjectiveCard representing the first shared Objective
     * @param sharedObj2 an ObjectiveCard representing the second shared Objective
     * @param objChoice1 an ObjectiveCard representing the first choice for this Player's personal Objective
     * @param objChoice2 an ObjectiveCard representing the second choice for this Player's personal Objective
     */
    void personalObjectiveChoice(HashMap<String, Color> pc, HashMap<String, Symbol[]> hcc, HashMap<String, StarterCard> psc, LinkedList<PlayableCard> ownHand, ObjectiveCard sharedObj1, ObjectiveCard sharedObj2, ObjectiveCard objChoice1, ObjectiveCard objChoice2);

    /**
     * shows all the existing and available information while this Player has to choose his StarterCard facing
     * @param sc the StarterCard this Player drew
     * @param gt a Symbol variable representing the color of the card on top of the gold deck
     * @param rt a Symbol variable representing the color of the card on top of the resource deck
     * @param g1 the first face-up GoldCard available to be drawn
     * @param g2 the second face-up GoldCard available to be drawn
     * @param r1 the first face-up ResourceCard available to be drawn
     * @param r2 the second face-up ResourceCard available to be drawn
     */
    void starterCardFacingChoice(StarterCard sc, Symbol gt, Symbol rt, GoldCard g1, GoldCard g2, ResourceCard r1, ResourceCard r2);

    //------------------------------------------------------------------------------------------------- static elements

    /**
     * setter method for this Player's chosen personal Objective
     * @param objective the ObjectiveCard chosen by this Player
     */
    void setPersonalObjective(ObjectiveCard objective);

    //------------------------------------------------------------------------------------------------------------ chat

    void receiveMessage(String messageReceived);
    //...many more missing
}

package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Cards.*;
import it.polimi.ingsw.am38.Network.Client.ClientCommandInterpreter;
import it.polimi.ingsw.am38.Network.Client.ClientWriter;

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
    void updateEnemiesHandColors(String nick, Symbol[] handColors);
    void updateScreen();
    void printHelp();
    ClientWriter startView(ClientCommandInterpreter clientCommandInterpreter);
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
    void personalObjectiveChoice(String nickname, HashMap<String, Color> pc, HashMap<String, Symbol[]> hcc, HashMap<String, StarterCard> psc, LinkedList<PlayableCard> ownHand, ObjectiveCard sharedObj1, ObjectiveCard sharedObj2, ObjectiveCard objChoice1, ObjectiveCard objChoice2, HashMap<String, VisibleElements> pve);

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

    void setSymbolsTab(String nickname, VisibleElements symTab);

    void setCardInField(String nick, PlayableCard card, int x, int y);

    void updateDraw(Symbol colorG, Symbol colorR, GoldCard gc1, GoldCard gc2, ResourceCard rc1, ResourceCard rc2, LinkedList<PlayableCard> card);

    void showFirstScreen(String thisNick);
    //------------------------------------------------------------------------------------------------- static elements

    /**
     * setter method for this Player's chosen personal Objective
     * @param objective the ObjectiveCard chosen by this Player
     */
    void setPersonalObjective(ObjectiveCard objective);

    //------------------------------------------------------------------------------------------------------------ chat

    /**
     * updates the chat by adding the new message received
     * @param messageReceived a String containing the message received
     */
    void receiveMessage(String messageReceived);
    //---------------------------------------------------------------------------------------------------- Errors/Stuff

    void sendString(String s);
    void priorityString(String s);
    void displayString(String s);
    void setCardDisplay(PlayableCard card,int x, int y);
    void receiveOwnMessage(String s);
    void playersTurn(String name);
    void displayStringLogin(String s);
    void setHandAfterPlacement(LinkedList<PlayableCard> cardsInHand);
    void updateOtherPlayerDraw(String nickname, GoldCard gfu1, GoldCard gfu2, ResourceCard rfu1, ResourceCard rfu2, Symbol gtc, Symbol rtc, Symbol[] hcc);
}

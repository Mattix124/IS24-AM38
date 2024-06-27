package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Cards.*;
import it.polimi.ingsw.am38.Network.Client.ClientCommandInterpreter;
import it.polimi.ingsw.am38.Network.Client.ClientWriter;
import it.polimi.ingsw.am38.Network.Packet.PlayerDisconnectionResendInfo;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Implemented by both CLI/TUI and GUI, contains all the methods they implement
 */
public interface Viewable {
    //----------------------------------------------------------------------------------------- on demand / when needed
    /**
     * Shows the field of the Player having the chosen nickname (used only by CLI)
     *
     * @param nickname a String containing the name of the Player whose field is being requested
     */
    void showPlayerField(String nickname);

    /**
     * Updates the score of the Player who just ended their turn (4 times each round)
     *
     * @param nickname a String containing the name of the Player whose turn just ended
     * @param score an int representing the Score of the given Player
     */
    void updateScore(String nickname, int score);

    /**
     *
     *
     * @param nick
     * @param handColors
     */
    void updateEnemiesHandColors(String nick, String[] handColors);

    /**
     * Method used only by TUI/CLI to update the entire view
     */
    void updateScreen();

    /**
     * Method use only by TUI/CLI to print the help box
     */
    void printHelp();

    /**
     * Method that start the view chosen (GUI or CLI)
     *
     * @param clientCommandInterpreter to let the 2 types of view to send the command to the server
     * @return ClientWriter if the view chosen is TUI, null otherwise
     */
    ClientWriter startView(ClientCommandInterpreter clientCommandInterpreter);

    /**
     * Shows all the existing and available information while this Player has to choose his personal Objective between the 2 possible choices
     *
     * @param pc an HashMap of all Players' nicknames (key-String) and their Color (value)
     * @param hcc an HashMap of all Players' nicknames (key-String) and the colors of the cards in their Hand (value-Symbol[])
     * @param psc an HashMap of all Players' nicknames (key-String) and their choice of facing for their StarterCard (value-StarterCard)
     * @param ownHand a LinkedList containing the PlayableCard in this Player's Hand
     * @param sharedObj1 an ObjectiveCard representing the first shared Objective
     * @param sharedObj2 an ObjectiveCard representing the second shared Objective
     * @param objChoice1 an ObjectiveCard representing the first choice for this Player's personal Objective
     * @param objChoice2 an ObjectiveCard representing the second choice for this Player's personal Objective
     */
    void personalObjectiveChoice(Symbol topG, Symbol topR, String nickname, HashMap<String, Color> pc, HashMap<String, String[]> hcc, HashMap<String, StarterCard> psc, LinkedList<PlayableCard> ownHand, ObjectiveCard sharedObj1, ObjectiveCard sharedObj2, ObjectiveCard objChoice1, ObjectiveCard objChoice2, HashMap<String, VisibleElements> pve);

    /**
     * Shows all the existing and available information while this Player has to choose his StarterCard facing
     *
     * @param sc the StarterCard this Player drew
     * @param gt a Symbol variable representing the color of the card on top of the gold deck
     * @param rt a Symbol variable representing the color of the card on top of the resource deck
     * @param g1 the first face-up GoldCard available to be drawn
     * @param g2 the second face-up GoldCard available to be drawn
     * @param r1 the first face-up ResourceCard available to be drawn
     * @param r2 the second face-up ResourceCard available to be drawn
     */
    void starterCardFacingChoice(StarterCard sc, Symbol gt, Symbol rt, GoldCard g1, GoldCard g2, ResourceCard r1, ResourceCard r2);

    /**
     * Setter for the table with the elements shawn on the field (used only by CLI)
     *
     * @param nickname of the player
     * @param symTab updated VisibleElements to put in the table
     */
    void setSymbolsTab(String nickname, VisibleElements symTab);

    /**
     * Method that tells all the clients that a player has successfully placed a card
     *
     * @param nick of the player who has placed
     * @param card placed
     * @param x coordinates of the card placed
     * @param y coordinates of the card placed
     */
    void setCardInField(String nick, PlayableCard card, int x, int y);

    /**
     * Shows the deck and the hand updated after a draw
     *
     * @param colorG kingdom of the card on top of the gold deck
     * @param colorR kingdom of the card on top of the resource deck
     * @param gc1 the first face-up GoldCard
     * @param gc2 the second face-up GoldCard
     * @param rc1 the first face-up ResourceCard
     * @param rc2 the second face-up ResourceCard
     * @param card drawn
     */
    void updateDraw(Symbol colorG, Symbol colorR, GoldCard gc1, GoldCard gc2, ResourceCard rc1, ResourceCard rc2, LinkedList<PlayableCard> card);

    /**
     * Shows the screen in which the game actually start after the setup phase
     *
     * @param thisNick nickname of the player
     */
    void showFirstScreen(String thisNick);
    //------------------------------------------------------------------------------------------------- static elements

    /**
     * Setter method for this Player's chosen personal Objective
     *
     * @param objective the ObjectiveCard chosen by this Player
     */
    void setPersonalObjective(ObjectiveCard objective);

    //------------------------------------------------------------------------------------------------------------ chat

    /**
     * Updates the chat by adding the new message received
     *
     * @param messageReceived a String containing the message received
     */
    void receiveMessage(String messageReceived);
    //---------------------------------------------------------------------------------------------------- Errors/Stuff

    /**
     * Method used only by CLI to print messages
     *
     * @param s
     */
    void sendString(String s);

    /**
     * Method used to communicate errors to the clients
     *
     * @param s
     */
    void priorityString(String s);

    /**
     * Method to display messages
     *
     * @param s
     */
    void displayString(String s);

    /**
     * Method used to set the Card Display and update the game screen (used only by CLI)
     *
     * @param card the PlayableCard to display
     * @param x its x coordinate
     * @param y its y coordinate
     */
    void updateCardDisplay(PlayableCard card,int x, int y);

    /**
     * Method to print in the chat a message sent by the client itself
     *
     * @param s a String containing the message sent (already formatted correctly)
     */
    void receiveOwnMessage(String s);

    /**
     * Method that prints the messages for each player's start of turn
     *
     * @param name a String containing the nickname of the Player whose turn is starting
     */
    void playersTurn(String name);

    /**
     *
     *
     * @param s
     */
    void displayStringLogin(String s);

    /**
     * Updates the cards in the hand (used only by CLI)
     *
     * @param cardsInHand cards in the hand of the player to be shawn
     */
    void setHandAfterPlacement(LinkedList<PlayableCard> cardsInHand);

    /**
     * Method used to update the game screen with all the information changed after an opponent draws a card
     *
     * @param nickname a String containing the name of the Player who drew a card
     * @param gfu1 GoldCard face up 1
     * @param gfu2 GoldCard face up 2
     * @param rfu1 ResourceCard face up 1
     * @param rfu2 ResourceCard face up 2
     * @param gtc Symbol of the card on top of the GoldDeck
     * @param rtc Symbol of the card on top of the ResourceDeck
     * @param hcc array of Strings containing the Hand cards colors
     */
    void updateOtherPlayerDraw(String nickname, GoldCard gfu1, GoldCard gfu2, ResourceCard rfu1, ResourceCard rfu2, Symbol gtc, Symbol rtc, String[] hcc);


    // RECONNECTION ONLY METHODS

    void reconnectionInitialSetter(String ownNick, ObjectiveCard shObj1, ObjectiveCard shObj2, ObjectiveCard pObj, Symbol gt, Symbol rt, GoldCard g1, GoldCard g2, ResourceCard r1, ResourceCard r2, LinkedList<PlayableCard> cardsInHand, HashMap<String, PlayerDisconnectionResendInfo> pdr);

    void reconnectionCardsToPlay(String nick, PlayableCard cardToPlay, int x, int y);

    void computeScreen();//cli only probably
}

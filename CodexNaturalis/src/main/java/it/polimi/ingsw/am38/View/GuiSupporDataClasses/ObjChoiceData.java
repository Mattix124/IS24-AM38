package it.polimi.ingsw.am38.View.GuiSupporDataClasses;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;


import java.util.HashMap;
import java.util.LinkedList;

/**
 * Class that contains all the data needed to choose the objective card
 */
public class ObjChoiceData {
    /**
     * Nickname of the player
     */
    private String nickname;
    /**
     * HashMap that contains the color of the pawn of all player using as keys their nicknames
     */
    private HashMap<String, Color> pc;
    /**
     * HashMap that contains the kingdoms of the hands of all players using as keys their nicknames
     */
    private HashMap<String, String[]> hcc;
    /**
     * HashMap that contains the starter cards of all players using as keys their nicknames
     */
    private HashMap<String, StarterCard> psc;
    /**
     * List that contains the hand of the player
     */
    private LinkedList<PlayableCard> ownHand;
    /**
     * First common objective card
     */
    private ObjectiveCard sharedObj1;
    /**
     * Second common objective card
     */
    private ObjectiveCard sharedObj2;
    /**
     * First objective card to choose from
     */
    private ObjectiveCard objChoice1;
    /**
     * Second objective card to choose from
     */
    private ObjectiveCard objChoice2;
    /**
     * Kingdom of the card on top of the gold deck
     */
    private Symbol topG;
    /**
     * Kingdom of the card on top of the resource deck
     */
    private Symbol topR;

    /**
     *
     *
     * @param nickname
     * @param pc
     * @param hcc
     * @param psc
     * @param ownHand
     * @param sharedObj1
     * @param sharedObj2
     * @param objChoice1
     * @param objChoice2
     * @param pve
     * @param topR
     * @param topG
     */
    public ObjChoiceData(String nickname, HashMap<String, Color> pc, HashMap <String, String[]> hcc, HashMap <String, StarterCard> psc, LinkedList<PlayableCard> ownHand, ObjectiveCard sharedObj1, ObjectiveCard sharedObj2, ObjectiveCard objChoice1, ObjectiveCard objChoice2, HashMap <String, VisibleElements> pve,Symbol topR,Symbol topG){
        this.nickname = nickname;
        this.pc = pc;
        this.hcc = hcc;
        this.psc = psc;
        this.ownHand = ownHand;
        this.sharedObj1 = sharedObj1;
        this.sharedObj2 = sharedObj2;
        this.objChoice1 = objChoice1;
        this.objChoice2 = objChoice2;
        this.topR = topR;
        this.topG = topG;

//        this.pve = pve;
    }

    /**
     * Getter for the attribute nickname
     *
     * @return nickname
     */
    public String getNickname() {return nickname;}

    /**
     * Getter for the attribute pc
     *
     * @return pc
     */
    public HashMap<String, Color> getPc() {return pc;}

    /**
     * Getter for the attribute psc
     *
     * @return psc
     */
    public HashMap<String, StarterCard> getPsc() {return psc;}

    /**
     * Getter for the attribute hcc
     *
     * @return hcc
     */
    public HashMap<String, String[]> getHcc() {return hcc;}
/**
 *
 */
//    public HashMap<String, VisibleElements> getPve() {return pve;}

    /**
     * Getter for the attribute ownHand
     *
     * @return ownHand
     */
    public LinkedList<PlayableCard> getOwnHand() {return ownHand;}

    /**
     * Getter for the attribute objChoice1
     *
     * @return objChoice1
     */
    public ObjectiveCard getObjChoice1() {return objChoice1;}

    /**
     * Getter for the attribute objChoice2
     *
     * @return objChoice2
     */
    public ObjectiveCard getObjChoice2() {return objChoice2;}

    /**
     * Getter for the attribute sharedObj1
     *
     * @return sharedObj1
     */
    public ObjectiveCard getSharedObj1() {return sharedObj1;}

    /**
     * Getter for the attribute sharedObj2
     *
     * @return sharedObj2
     */
    public ObjectiveCard getSharedObj2() {return sharedObj2;}

    /**
     * Getter for the attribute  topG
     *
     * @return topG
     */
    public Symbol getTopG()
    {
        return topG;
    }

    /**
     * Getter for the attribute topR
     *
     * @return topR
     */
    public Symbol getTopR()
    {
        return topR;
    }
}

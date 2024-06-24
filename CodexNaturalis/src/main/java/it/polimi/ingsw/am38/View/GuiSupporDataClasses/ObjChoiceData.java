package it.polimi.ingsw.am38.View.GuiSupporDataClasses;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;


import java.util.HashMap;
import java.util.LinkedList;

public class ObjChoiceData {
    private String nickname;
    private HashMap<String, Color> pc;
    private HashMap<String, Symbol[]> hcc;
    private HashMap<String, StarterCard> psc;
    private LinkedList<PlayableCard> ownHand;
    private HashMap <String, VisibleElements> pve;
    private ObjectiveCard sharedObj1;
    private ObjectiveCard sharedObj2;
    private ObjectiveCard objChoice1;
    private ObjectiveCard objChoice2;


    public ObjChoiceData(String nickname, HashMap<String, Color> pc, HashMap <String, Symbol[]> hcc, HashMap <String, StarterCard> psc, LinkedList<PlayableCard> ownHand, ObjectiveCard sharedObj1, ObjectiveCard sharedObj2, ObjectiveCard objChoice1, ObjectiveCard objChoice2, HashMap <String, VisibleElements> pve){
        this.nickname = nickname;
        this.pc = pc;
        this.hcc = hcc;
        this.psc = psc;
        this.ownHand = ownHand;
        this.sharedObj1 = sharedObj1;
        this.sharedObj2 = sharedObj2;
        this.objChoice1 = objChoice1;
        this.objChoice2 = objChoice2;
//        this.pve = pve;
    }

    public String getNickname() {return nickname;}

    public HashMap<String, Color> getPc() {return pc;}

    public HashMap<String, StarterCard> getPsc() {return psc;}

    public HashMap<String, Symbol[]> getHcc() {return hcc;}

//    public HashMap<String, VisibleElements> getPve() {return pve;}

    public LinkedList<PlayableCard> getOwnHand() {return ownHand;}

    public ObjectiveCard getObjChoice1() {return objChoice1;}

    public ObjectiveCard getObjChoice2() {return objChoice2;}

    public ObjectiveCard getSharedObj1() {return sharedObj1;}

    public ObjectiveCard getSharedObj2() {return sharedObj2;}

    public HashMap <String, VisibleElements> getPve()
    {
        return pve;
    }
}

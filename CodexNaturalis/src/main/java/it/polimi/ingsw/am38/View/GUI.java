package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.HelloApplication;
import it.polimi.ingsw.am38.Model.Cards.*;

import java.util.HashMap;
import java.util.LinkedList;

public class GUI implements Viewable {
    @Override
    public void showPlayerField(String nickname) {

    }

    @Override
    public void updateScore(String nickname, int score) {

    }

    @Override
    public void updateEnemiesHandColors(String nick, Symbol[] handColors) {

    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void printHelp() {

    }

    @Override
    public void personalObjectiveChoice(HashMap<String, Color> pc, HashMap<String, Symbol[]> hcc, HashMap<String, StarterCard> psc, LinkedList<PlayableCard> ownHand, ObjectiveCard sharedObj1, ObjectiveCard sharedObj2, ObjectiveCard objChoice1, ObjectiveCard objChoice2) {

    }

    @Override
    public void starterCardFacingChoice(StarterCard sc, Symbol gt, Symbol rt, GoldCard g1, GoldCard g2, ResourceCard r1, ResourceCard r2) {
        int id;

        id = sc.getCardID();
        HelloApplication.setStarterCard(id);
    }

    @Override
    public void setSymbolsTab(HashMap<Symbol, Integer> sym) {

    }

    @Override
    public void setCardInField(String nick, PlayableCard card, int x, int y) {

    }

    @Override
    public void updateDraw(Symbol colorG, Symbol colorR, GoldCard gc1, GoldCard gc2, ResourceCard rc1, ResourceCard rc2, LinkedList<PlayableCard> card) {

    }

    @Override
    public void showFirstScreen() {

    }

    @Override
    public void setPersonalObjective(ObjectiveCard objective) {

    }

    @Override
    public void receiveMessage(String messageReceived) {

    }

    @Override
    public void sendString(String s) {

    }

    @Override
    public void priorityString(String s, int scale) {

    }

    @Override
    public void displayString(String s) {

    }

    @Override
    public void setCardDisplay(PlayableCard card, int x, int y) {

    }
}

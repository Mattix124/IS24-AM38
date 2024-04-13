package it.polimi.ingsw.am38.Cards;

import it.polimi.ingsw.am38.Miscellaneous.ObjectiveCardCondition;

public class ObjectiveCard extends Card{

    boolean personalOrShared;
    public ObjectiveCard(boolean is){  //to be improved
        personalOrShared = is;
    }
    private boolean isPublic(){
        return true;
    }
}

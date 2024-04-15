package it.polimi.ingsw.am38.Model.Cards;

import it.polimi.ingsw.am38.Enum.Item;
import it.polimi.ingsw.am38.Enum.Kingdom;

public class ObjectiveCard extends Card{

    private Kingdom[] objectiveKingdom;
    private String objectiveSequence;
    private Item[] objectiveItem;
    boolean personalOrShared;
    public ObjectiveCard(boolean is, Kingdom[] objKingdom, Item[] objItem, String sequence){  //to be improved

        personalOrShared = is;
    }
    private boolean isPublic(){

        return personalOrShared;
    }
}

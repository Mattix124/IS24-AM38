package it.polimi.ingsw.am38.Cards;

import it.polimi.ingsw.am38.Enum.Item;
import it.polimi.ingsw.am38.Enum.Orientation;

public class Corner {
    private Orientation orientation;
    private boolean exists;
    private Item content;

    public  Corner(Orientation o, Item i){  //create the corner
        orientation = o;
        exists = true;
        content = i;
    }
}

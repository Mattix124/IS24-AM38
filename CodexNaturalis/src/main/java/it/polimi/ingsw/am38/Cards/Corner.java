package it.polimi.ingsw.am38.Cards;

import it.polimi.ingsw.am38.Enum.Item;
import it.polimi.ingsw.am38.Enum.Orientation;

public class Corner {
    private Orientation orientation;
    private boolean exists;
    private Item content;

    public  Corner(Orientation orientation, Item item){     //create the corner
        if(orientation == null) {
            exists = false;
        }else{
            exists = true;
            this.orientation = orientation;
            content = item;
        }
    }

    public boolean getExists(){
        return this.exists;
    }
}

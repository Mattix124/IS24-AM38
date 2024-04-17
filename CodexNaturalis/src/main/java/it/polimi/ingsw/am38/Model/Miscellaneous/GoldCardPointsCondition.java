package it.polimi.ingsw.am38.Model.Miscellaneous;

import it.polimi.ingsw.am38.Enum.Item;
import it.polimi.ingsw.am38.Enum.Symbol;

public class GoldCardPointsCondition {
    private Symbol item;

    public GoldCardPointsCondition(String condition){

        switch (condition){
            case "quill" : this.item = Symbol.QUILL; break;

            case "inkwell" : this.item = Symbol.INKWELL; break;

            case "manuscript" : this.item = Symbol.MANUSCRIPT; break;

            case "null" : this.item = null; break;
        }
    }

    public Symbol getGoldPointsCondition(){
        return this.item;
    }
}

package it.polimi.ingsw.am38.Model.Miscellaneous;

import it.polimi.ingsw.am38.Enum.Symbol;

public class GoldCardPlayableCondition {
    private Symbol[] resourceRequired = {null, null, null, null, null};

    public GoldCardPlayableCondition(String first, String second, String third, String fourth, String fifth){

        switch(first){
            case("fungi"):  this.resourceRequired[0] = Symbol.FUNGI; break;
            case("plant"):  this.resourceRequired[0] = Symbol.PLANT; break;
            case("animal"): this.resourceRequired[0] = Symbol.ANIMAL; break;
            case("insect"): this.resourceRequired[0] = Symbol.INSECT; break;
            case("null"):   this.resourceRequired[0] = null; break;
        }
        
        switch(second){
            case("fungi"):  this.resourceRequired[1] = Symbol.FUNGI; break;
            case("plant"):  this.resourceRequired[1] = Symbol.PLANT; break;
            case("animal"): this.resourceRequired[1] = Symbol.ANIMAL; break;
            case("insect"): this.resourceRequired[1] = Symbol.INSECT; break;
            case("null"):   this.resourceRequired[1] = null; break;
        }
        
        switch(third){
            case("fungi"):  this.resourceRequired[2] = Symbol.FUNGI; break;
            case("plant"):  this.resourceRequired[2] = Symbol.PLANT; break;
            case("animal"): this.resourceRequired[2] = Symbol.ANIMAL; break;
            case("insect"): this.resourceRequired[2] = Symbol.INSECT; break;
            case("null"):   this.resourceRequired[2] = null; break;
        }

        switch(fourth){
            case("fungi"):  this.resourceRequired[3] = Symbol.FUNGI; break;
            case("plant"):  this.resourceRequired[3] = Symbol.PLANT; break;
            case("animal"): this.resourceRequired[3] = Symbol.ANIMAL; break;
            case("insect"): this.resourceRequired[3] = Symbol.INSECT; break;
            case("null"):   this.resourceRequired[3] = null; break;
        }
        
        switch(fifth){
            case("fungi"):  this.resourceRequired[4] = Symbol.FUNGI; break;
            case("plant"):  this.resourceRequired[4] = Symbol.PLANT; break;
            case("animal"): this.resourceRequired[4] = Symbol.ANIMAL; break;
            case("insect"): this.resourceRequired[4] = Symbol.INSECT; break;
            case("null"):   this.resourceRequired[4] = null; break;
        }
    }

    public Symbol[] getGoldPlayableCondition(){
        return this.resourceRequired;
    }
}

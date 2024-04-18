package it.polimi.ingsw.am38.Model.Miscellaneous;

import it.polimi.ingsw.am38.Enum.Kingdom;
import it.polimi.ingsw.am38.Enum.Symbol;

public class GoldCardPlayableCondition {
    private Kingdom[] resourceRequired = {null, null, null, null, null};

    public GoldCardPlayableCondition(String first, String second, String third, String fourth, String fifth){

        switch(first){
            case("fungi"):  this.resourceRequired[0] = Kingdom.FUNGI; break;
            case("plant"):  this.resourceRequired[0] = Kingdom.PLANT; break;
            case("animal"): this.resourceRequired[0] = Kingdom.ANIMAL; break;
            case("insect"): this.resourceRequired[0] = Kingdom.INSECT; break;
            case("null"):   this.resourceRequired[0] = null; break;
        }
        
        switch(second){
            case("fungi"):  this.resourceRequired[1] = Kingdom.FUNGI; break;
            case("plant"):  this.resourceRequired[1] = Kingdom.PLANT; break;
            case("animal"): this.resourceRequired[1] = Kingdom.ANIMAL; break;
            case("insect"): this.resourceRequired[1] = Kingdom.INSECT; break;
            case("null"):   this.resourceRequired[1] = null; break;
        }
        
        switch(third){
            case("fungi"):  this.resourceRequired[2] = Kingdom.FUNGI; break;
            case("plant"):  this.resourceRequired[2] = Kingdom.PLANT; break;
            case("animal"): this.resourceRequired[2] = Kingdom.ANIMAL; break;
            case("insect"): this.resourceRequired[2] = Kingdom.INSECT; break;
            case("null"):   this.resourceRequired[2] = null; break;
        }

        switch(fourth){
            case("fungi"):  this.resourceRequired[3] = Kingdom.FUNGI; break;
            case("plant"):  this.resourceRequired[3] = Kingdom.PLANT; break;
            case("animal"): this.resourceRequired[3] = Kingdom.ANIMAL; break;
            case("insect"): this.resourceRequired[3] = Kingdom.INSECT; break;
            case("null"):   this.resourceRequired[3] = null; break;
        }
        
        switch(fifth){
            case("fungi"):  this.resourceRequired[4] = Kingdom.FUNGI; break;
            case("plant"):  this.resourceRequired[4] = Kingdom.PLANT; break;
            case("animal"): this.resourceRequired[4] = Kingdom.ANIMAL; break;
            case("insect"): this.resourceRequired[4] = Kingdom.INSECT; break;
            case("null"):   this.resourceRequired[4] = null; break;
        }
    }

    public Kingdom[] getGoldPlayableCondition(){
        return this.resourceRequired;
    }
}

package it.polimi.ingsw.am38.Model.Miscellaneous;

import it.polimi.ingsw.am38.Enum.Kingdom;
import it.polimi.ingsw.am38.Enum.Symbol;

public class GoldCardPlayableCondition {
    private Kingdom[] resourceRequired = {null, null, null, null, null};

    public GoldCardPlayableCondition(String first, String second, String third, String fourth, String fifth){


        if(first.equals("fungi")){this.resourceRequired[0] = Kingdom.FUNGI;}
        else if(first.equals("plant")){this.resourceRequired[0] = Kingdom.PLANT;}
        else if(first.equals("animal")){this.resourceRequired[0] = Kingdom.ANIMAL;}
        else if(first.equals("insect")){this.resourceRequired[0] = Kingdom.INSECT;}
        else if(first.equals("null")){this.resourceRequired[0] = null;}

        if(second.equals("fungi")){this.resourceRequired[1] = Kingdom.FUNGI;}
        else if(second.equals("plant")){this.resourceRequired[1] = Kingdom.PLANT;}
        else if(second.equals("animal")){this.resourceRequired[1] = Kingdom.ANIMAL;}
        else if(second.equals("insect")){this.resourceRequired[1] = Kingdom.INSECT;}
        else if(second.equals("null")){this.resourceRequired[1] = null;}

        if(third.equals("fungi")){this.resourceRequired[2] = Kingdom.FUNGI;}
        else if(third.equals("plant")){this.resourceRequired[2] = Kingdom.PLANT;}
        else if(third.equals("animal")){this.resourceRequired[2] = Kingdom.ANIMAL;}
        else if(third.equals("insect")){this.resourceRequired[2] = Kingdom.INSECT;}
        else if(third.equals("null")){this.resourceRequired[2] = null;}

        if(fourth.equals("fungi")){this.resourceRequired[3] = Kingdom.FUNGI;}
        else if(fourth.equals("plant")){this.resourceRequired[3] = Kingdom.PLANT;}
        else if(fourth.equals("animal")){this.resourceRequired[3] = Kingdom.ANIMAL;}
        else if(fourth.equals("insect")){this.resourceRequired[3] = Kingdom.INSECT;}
        else if(fourth.equals("null")){this.resourceRequired[3] = null;}

        if(fifth.equals("fungi")){this.resourceRequired[4] = Kingdom.FUNGI;}
        else if(fifth.equals("plant")){this.resourceRequired[4] = Kingdom.PLANT;}
        else if(fifth.equals("animal")){this.resourceRequired[4] = Kingdom.ANIMAL;}
        else if(fifth.equals("insect")){this.resourceRequired[4] = Kingdom.INSECT;}
        else if(fifth.equals("null")){this.resourceRequired[4] = null;}
    }

    public Kingdom[] getGoldPlayableCondition(){
        return this.resourceRequired;
    }
}

package it.polimi.ingsw.am38.Model.Decks;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;

public class ObjectiveDeck extends Deck{
    private ObjectiveCard[] pool;

    public ObjectiveDeck(){
        String strJson = getJsonFile("C:\\Users\\morit\\OneDrive\\Documenti\\Uni\\3Anno\\PRJ_IdS\\Repo\\IS24-AM38\\CodexNaturalis\\JSON\\objectiveCard.json");

        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(strJson, JsonArray.class); //putting the json data in the string strJson

        for(int i=0; i<16; i++) {
            JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();  //getting every "card" from the json

            String cardID = jsonObject1.get("cardID").getAsString();
            String objType = jsonObject1.get("objType").getAsString();
            String imgFront = jsonObject1.get("imgFront").getAsString();
            String imgBack = jsonObject1.get("imgBack").getAsString();
            int pointGiven = jsonObject1.get("pointGiven").getAsInt();

            int ID = Integer.valueOf(cardID);

            if(objType.equals("diagonal")){

                String kingdom = jsonObject1.get("kingdom").getAsString();

                JsonObject jsonObject2 = jsonObject1.get("lines").getAsJsonObject();  //creating the obj for cornerFront and getting its info

                int first = jsonObject2.get("first").getAsInt();
                int second = jsonObject2.get("second").getAsInt();
                int third = jsonObject2.get("third").getAsInt();

                ObjectiveCard objectiveCard = new ObjectiveCard(ID, kingdom, objType, imgFront, imgBack, pointGiven, null, null, null, first, second, third);

                this.pool[i] = objectiveCard;

            }else if(objType.equals("shapeL")){

                String kingdom = jsonObject1.get("kingdom").getAsString();

                JsonObject jsonObject2 = jsonObject1.get("strangerCard").getAsJsonObject();

                String kingdom2 = jsonObject2.get("kingdom2").getAsString();
                String position = jsonObject2.get("position").getAsString();

                ObjectiveCard objectiveCard = new ObjectiveCard(ID, kingdom, objType, imgFront, imgBack, pointGiven, kingdom2, position, null, 0, 0, 0);

                this.pool[i] = objectiveCard;

            }else{

                String item = jsonObject1.get("item").getAsString();

                ObjectiveCard objectiveCard = new ObjectiveCard(ID, null, objType, imgFront, imgBack, pointGiven, null, null, item, 0, 0, 0);

                this.pool[i] = objectiveCard;
            }
        }
    }
}

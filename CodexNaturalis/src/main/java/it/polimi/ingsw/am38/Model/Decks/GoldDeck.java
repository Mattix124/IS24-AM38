package it.polimi.ingsw.am38.Model.Decks;

import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import com.google.gson.JsonArray;

import java.io.BufferedReader;
import java.io.FileReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am38.Model.Miscellaneous.GoldCardPointsCondition;

public class GoldDeck extends Deck{
    private GoldCard[] pool;
    public Boolean isEmpty(){
        return false; //TBD
    }
    public GoldCard draw(){
        return null; //TBD
    }


    public GoldDeck(){

        String strJson = getJsonFile("C:\\Users\\morit\\OneDrive\\Documenti\\Uni\\3Anno\\PRJ_IdS\\Repo\\IS24-AM38\\CodexNaturalis\\JSON\\goldenCard.json");

        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(strJson, JsonArray.class); //putting the json data in the string strJson

        for(int i=0; i<40; i++){
            JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();  //getting every "card" from the json

            String cardID = jsonObject1.get("cardID").getAsString();
            String kingdom = jsonObject1.get("kingdom").getAsString();
            String imgFront = jsonObject1.get("imgFront").getAsString();
            String imgBack = jsonObject1.get("imgBack").getAsString();
            String condPointType = jsonObject1.get("conditionPointType").getAsString();
            int pointGiven = jsonObject1.get("pointGiven").getAsInt();

            int ID = Integer.valueOf(cardID);

            JsonObject jsonObject2 = jsonObject1.get("cornerFront").getAsJsonObject();  //creating the obj for cornerFront and getting its info

            String FNW = jsonObject2.get("NW").getAsString();
            String FNE = jsonObject2.get("NE").getAsString();
            String FSW = jsonObject2.get("SW").getAsString();
            String FSE = jsonObject2.get("SE").getAsString();

            JsonObject jsonObject3 = jsonObject1.get("cornerBack").getAsJsonObject();  //same as for corner front

            String BNW = jsonObject3.get("NW").getAsString();
            String BNE = jsonObject3.get("NE").getAsString();
            String BSW = jsonObject3.get("SW").getAsString();
            String BSE = jsonObject3.get("SE").getAsString();

            JsonObject jsonObject4 = jsonObject1.get("conditionPlace").getAsJsonObject();  //same as previous

            String first = jsonObject4.get("first").getAsString();
            String second = jsonObject4.get("second").getAsString();
            String third = jsonObject4.get("third").getAsString();
            String fourth = jsonObject4.get("fourth").getAsString();
            String fifth = jsonObject4.get("fifth").getAsString();//get data from json till here

            GoldCard goldCard = new GoldCard(ID, kingdom, imgFront, imgBack, condPointType, pointGiven, FNW, FNE, FSW, FSE,
                    BNW, BNE, BSW, BSE, first, second, third, fourth, fifth);  //create the gold card to be inserted in the deck

            this.pool[i] = goldCard;

        }
    }

}

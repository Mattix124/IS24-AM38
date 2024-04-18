package it.polimi.ingsw.am38.Model.Decks;

import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import com.google.gson.JsonArray;

import java.io.InputStreamReader;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * GoldDeck take the data from the json file e send them to the constructor of the starter cards
 */
public class GoldDeck extends Deck{
    /** This attribute is the deck itself, an array of gold cards */
    private GoldCard[] pool;
    public GoldCard draw(){
        return null; //TBD
    }

    /**
     * This constructor, using gson methods, take cards info from the json, send them the to the gold cards constructor and put the
     * card created in the array that represents the deck
     */
    public GoldDeck(){
        Gson gson = new Gson();
        // Search file in /src/main/resources/ directory, path is valid for every machine so that there's no need to
        // change this for each PC. Seems to be useful for .jar dependencies too
        JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(GoldDeck.class.getClassLoader().getResourceAsStream("goldCard.json"))));
        JsonArray jsonArray = gson.fromJson(jsonReader, JsonArray.class);

        for(int i=0; i<40; i++){
            /** These attributes are special objects that permits to read data from the json */
            JsonObject jsonObject1, jsonObject2, jsonObject3, jsonObject4;
            /** This attribute is the card where to put the data */
            GoldCard goldCard;

            jsonObject1 = jsonArray.get(i).getAsJsonObject();  //getting every "card" from the json

            String cardID = jsonObject1.get("cardID").getAsString();
            String kingdom = jsonObject1.get("kingdom").getAsString();
            String imgFront = jsonObject1.get("imgFront").getAsString();
            String imgBack = jsonObject1.get("imgBack").getAsString();
            String condPointType = jsonObject1.get("conditionPointType").getAsString();
            int pointGiven = jsonObject1.get("pointGiven").getAsInt();

            int ID = Integer.valueOf(cardID);

            jsonObject2 = jsonObject1.get("cornerFront").getAsJsonObject();  //creating the obj for cornerFront and getting its info

            String FNW = jsonObject2.get("NW").getAsString();
            String FNE = jsonObject2.get("NE").getAsString();
            String FSW = jsonObject2.get("SW").getAsString();
            String FSE = jsonObject2.get("SE").getAsString();

            jsonObject3 = jsonObject1.get("cornerBack").getAsJsonObject();  //same as for corner front

            String BNW = jsonObject3.get("NW").getAsString();
            String BNE = jsonObject3.get("NE").getAsString();
            String BSW = jsonObject3.get("SW").getAsString();
            String BSE = jsonObject3.get("SE").getAsString();

            jsonObject4 = jsonObject1.get("conditionPlace").getAsJsonObject();  //same as previous

            String first = jsonObject4.get("first").getAsString();
            String second = jsonObject4.get("second").getAsString();
            String third = jsonObject4.get("third").getAsString();
            String fourth = jsonObject4.get("fourth").getAsString();
            String fifth = jsonObject4.get("fifth").getAsString();//get data from json till here

            goldCard = new GoldCard(ID, kingdom, imgFront, imgBack, condPointType, pointGiven, FNW, FNE, FSW, FSE,
                    BNW, BNE, BSW, BSE, first, second, third, fourth, fifth);  //create the gold card to be inserted in the deck

            this.pool[i] = goldCard;

        }
    }

}

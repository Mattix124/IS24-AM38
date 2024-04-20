package it.polimi.ingsw.am38.Model.Decks;

import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import com.google.gson.JsonArray;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * GoldDeck take the data from the json file e send them to the constructor of the starter cards
 */
public class GoldDeck extends Deck{
    /** This attribute is the deck itself, an array of gold cards */
    private LinkedList<GoldCard> pool = new LinkedList<>();

    /**
     * This attribute contains 1 of the 2 cards that eligible to be drawn from the "ground"
     */
    private GoldCard Ground0;
    /**
     * This attribute contains 1 of the 2 cards that eligible to be drawn from the "ground"
     */
    private GoldCard Ground1;
    /**
     * This constructor, using gson methods, take cards info from the json, send them the to the gold cards constructor and put the
     * card created in the array that represents the deck and shuffles it
     */
    public GoldDeck(){
        Gson gson = new Gson();
        // Search file in /src/main/resources/ directory, path is valid for every machine so that there's no need to
        // change this for each PC. Seems to be useful for .jar dependencies too
        JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(GoldDeck.class.getClassLoader().getResourceAsStream("goldCard.json"))));
        JsonArray jsonArray = gson.fromJson(jsonReader, JsonArray.class);
        int i = 0;
        for(JsonElement element : jsonArray){ // for each element in the json file

            JsonObject jsonObject1, jsonObject2, jsonObject3, jsonObject4;

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

            pool.add(goldCard);
            i++;
        }
        Collections.shuffle(pool); // shuffle the deck using shuffle method from java.util
    }

    /**
     * This method take out a card from the deck and return the card.
     * @return the first card of the deck.
     */
    public GoldCard draw()
    {
        GoldCard g = pool.getFirst();
        pool.remove(g);
        return g;
    }

    /**
     * The method retrieve 1 of the 2 card on the ground.
     * @param i This parameter allows the caller to choose which card draw.
     * @return The card on the ground that corresponds to the parameter.
     */
    public GoldCard drawFromGround(int i)
    {
        GoldCard g;
        if (i == 0)
        {
            g = Ground0;
            Ground0 = draw();
            return Ground0;
        }
        else
        {
            g = Ground1;
            Ground1 = draw();
            return Ground1;
        }
    }

    /**
     * setter method for the pair of face-up GoldCards that the Players can choose from instead of randomly drawing
     */
    public void setUpGround() {
        this.Ground0 = draw();
        this.Ground1 = draw();
    }
}

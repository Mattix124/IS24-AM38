package it.polimi.ingsw.am38.Model.Decks;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

/**
 * ObjectiveDeck take the data from the json file e send them to the constructor of the starter cards
 */
public class ObjectiveDeck extends Deck{
    private LinkedList<ObjectiveDeck> pool;
    /** This attribute is the deck itself, an array of objective cards */

    /**
     * This constructor, using gson methods, take cards info from the json, send them the to the objective cards constructor and put the
     * card created in the array that represents the deck
     */
    public ObjectiveDeck(){
        Gson gson = new Gson();
        LinkedList<ObjectiveCard> pool = new LinkedList<>();

        // Search file in /src/main/resources/ directory, path is valid for every machine so that there's no need to
        // change this for each PC. Seems to be useful for .jar dependencies too
        JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(ObjectiveDeck.class.getClassLoader().getResourceAsStream("objectiveCard.json"))));
        JsonArray jsonArray = gson.fromJson(jsonReader, JsonArray.class);

        for(int i=0; i<16; i++) {
            /** These attributes are special objects that permits to read data from the json */
            JsonObject jsonObject1, jsonObject2;
            /** This attribute is the card where to put the data */
            ObjectiveCard objectiveCard = null;

            jsonObject1 = jsonArray.get(i).getAsJsonObject();  //getting every "card" from the json

            String cardID = jsonObject1.get("cardID").getAsString();
            String objType = jsonObject1.get("objType").getAsString();
            String imgFront = jsonObject1.get("imgFront").getAsString();
            String imgBack = jsonObject1.get("imgBack").getAsString();
            int pointGiven = jsonObject1.get("pointGiven").getAsInt();

            int ID = Integer.valueOf(cardID);

            switch (objType) {
                case "diagonal" -> {
                    String kingdom = jsonObject1.get("kingdom").getAsString();

                    jsonObject2 = jsonObject1.get("lines").getAsJsonObject();  //creating the obj for cornerFront and getting its info

                    int first = jsonObject2.get("first").getAsInt();
                    int second = jsonObject2.get("second").getAsInt();
                    int third = jsonObject2.get("third").getAsInt();

                    objectiveCard = new ObjectiveCard(ID, kingdom, objType, imgFront, imgBack, pointGiven, null, null, null, first, second, third);
                }
                case "shapeL" -> {
                    String kingdom = jsonObject1.get("kingdom").getAsString();

                    jsonObject2 = jsonObject1.get("strangerCard").getAsJsonObject();

                    String kingdom2 = jsonObject2.get("kingdom2").getAsString();
                    String position = jsonObject2.get("position").getAsString();

                    objectiveCard = new ObjectiveCard(ID, kingdom, objType, imgFront, imgBack, pointGiven, kingdom2, position, null, 0, 0, 0);
                }
                case "duo" -> {
                    String item = jsonObject1.get("item").getAsString();

                    objectiveCard = new ObjectiveCard(ID, null, objType, imgFront, imgBack, pointGiven, null, null, item, 0, 0, 0);

                    pool.add(objectiveCard);
                }
                case "trio" -> {
                    String item = jsonObject1.get("item").getAsString();

                    String kingdom = jsonObject1.get("kingdom").getAsString();

                    if (!kingdom.equals("null")) {
                        objectiveCard = new ObjectiveCard(ID, kingdom, objType, imgFront, imgBack, pointGiven, null, null, null, 0, 0, 0);
                    } else {
                        objectiveCard = new ObjectiveCard(ID, null, objType, imgFront, imgBack, pointGiven, null, null, item, 0, 0, 0);
                    }
                }
            }
            pool.add(objectiveCard);
        }
        Collections.shuffle(pool); // shuffle the deck using shuffle method from java.util
    }
}

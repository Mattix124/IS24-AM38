package it.polimi.ingsw.am38.Model.Decks;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
public class ObjectiveDeck{
	/** This attribute is the deck itself, an array of objective cards */
	private final LinkedList<ObjectiveCard> pool = new LinkedList<>();

    /**
     * This constructor, using gson methods, take cards info from the json, send them the to the objective cards constructor and put the
     * card created in the array that represents the deck
     */
    public ObjectiveDeck() {
        Gson gson = new Gson();

        // Search file in /src/main/resources/ directory, path is valid for every machine so that there's no need to
        // change this for each PC. Seems to be useful for .jar dependencies too
        JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(ObjectiveDeck.class.getClassLoader().getResourceAsStream("objectiveCard.json"))));
        JsonArray jsonArray = gson.fromJson(jsonReader, JsonArray.class);
        int i = 0;
        for(JsonElement element : jsonArray){ // for each element in the json file
            JsonObject jsonObject1, jsonObject2;
            ObjectiveCard objectiveCard = null;

            jsonObject1 = jsonArray.get(i).getAsJsonObject();  //getting every "card" from the json

            String cardID = jsonObject1.get("cardID").getAsString();
            String objType = jsonObject1.get("objType").getAsString();
            String imgFront = "images/front/" + cardID + "-front.svgz";
            String imgBack = "images/back/" + cardID + "-back.svgz";
            int pointGiven = jsonObject1.get("pointGiven").getAsInt();

            int ID = Integer.parseInt(cardID);

            switch (objType) {
                case "diagonal" -> {

                    String kingdom = jsonObject1.get("kingdom").getAsString();
					String position = jsonObject1.get("position").getAsString();
                    objectiveCard = new ObjectiveCard(ID, kingdom, objType, imgFront, imgBack, pointGiven, "null", position, "null");
                }
                case "shapeL" -> {
                    String kingdom = jsonObject1.get("kingdom").getAsString();

                    jsonObject2 = jsonObject1.get("strangerCard").getAsJsonObject();

                    String kingdom2 = jsonObject2.get("kingdom2").getAsString();
                    String position = jsonObject2.get("position").getAsString();

                    objectiveCard = new ObjectiveCard(ID, kingdom, objType, imgFront, imgBack, pointGiven, kingdom2, position, "null");
                }
                case "duo" -> {
                    String item = jsonObject1.get("item").getAsString();

                    objectiveCard = new ObjectiveCard(ID, "null", objType, imgFront, imgBack, pointGiven, "null", "null", item);
                }
                case "trio" -> {
                    String kingdom = jsonObject1.get("kingdom").getAsString();

                    objectiveCard = new ObjectiveCard(ID, kingdom, objType, imgFront, imgBack, pointGiven, "null", "null", "null");
                }
                case "all" -> objectiveCard = new ObjectiveCard(ID, "null", objType, imgFront, imgBack, pointGiven, "null", "null", "null");
            }
            pool.add(objectiveCard); // each objective card is added after the switch (but obviously still inside the loop)
            i++;
        }
        Collections.shuffle(pool); // shuffle the deck using shuffle method from java.util
    }

	/**
	 * method used to draw a pair of ObjectiveCards
	 * @return a LinkedList of 2 ObjectiveCards
	 */
	public LinkedList <ObjectiveCard> drawTwo(){
		LinkedList <ObjectiveCard> list = new LinkedList <>();
		list.add(this.pool.removeFirst());
        list.add(this.pool.removeFirst());
		return list;
	}

    /** @return the list of cards created */
    public LinkedList<ObjectiveCard> getPool() {
        return pool;
    }
}

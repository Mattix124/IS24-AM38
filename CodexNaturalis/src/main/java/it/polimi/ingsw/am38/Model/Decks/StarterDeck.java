package it.polimi.ingsw.am38.Model.Decks;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

/**
 * StarterDeck take the data from the json file e send them to the constructor of the starter cards
 */
public class StarterDeck{
	/**
	 * This attribute is the deck itself, an array of starter cards
	 */
	private final LinkedList <StarterCard> pool = new LinkedList <>();

	/**
	 * This constructor, using gson methods, take cards info from the json, send them the to the starter cards constructor and put the
	 * card created in the array that represents the deck
	 */
	public StarterDeck()
	{
		Gson gson = new Gson();
		// Search file in /src/main/resources/ directory, path is valid for every machine so that there's no need to
		// change this for each PC. Seems to be useful for .jar dependencies too
		JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(StarterDeck.class.getClassLoader().getResourceAsStream("starterCard.json"))));
		JsonArray  jsonArray  = gson.fromJson(jsonReader, JsonArray.class);
		int i = 0;
		for(JsonElement element : jsonArray){ // for each element in the json file
			/** These attributes are special objects that permits to read data from the json */
			JsonObject jsonObject1, jsonObject2, jsonObject3, jsonObject4;
			/** This attribute is the card where to put the data */
			StarterCard starterCard;

			jsonObject1 = jsonArray.get(i).getAsJsonObject();  //getting every "card" from the json

			String cardID = jsonObject1.get("cardID").getAsString();
			String imgFront = "images/front/" + cardID + "-front.svgz";
			String imgBack = "images/back/" + cardID + "-back.svgz";

			int ID = Integer.parseInt(cardID);

			jsonObject2 = jsonObject1.get("cornerFront").getAsJsonObject();  //creating the obj for cornerFront and getting its info

			String FNW = jsonObject2.get("NW").getAsString();
			String FNE = jsonObject2.get("NE").getAsString();
			String FSW = jsonObject2.get("SW").getAsString();
			String FSE = jsonObject2.get("SE").getAsString();

			jsonObject3 = jsonObject1.get("cornerBack").getAsJsonObject();  //same as for corner front

			String BNW = jsonObject3.get("NW").getAsString();
			String BNE = jsonObject3.get("NE").getAsString();
			String BSW = jsonObject3.get("SW").getAsString();
			String BSE = jsonObject3.get("SE").getAsString();//get data from json till here

			jsonObject4 = jsonObject1.get("centralResource").getAsJsonObject();  //same as for corner front

			String first = jsonObject4.get("first").getAsString();
			String second = jsonObject4.get("second").getAsString();
			String third = jsonObject4.get("third").getAsString();

			starterCard = new StarterCard(ID, imgFront, imgBack, FNW, FNE, FSW, FSE, BNW, BNE, BSW, BSE, first, second, third);  //create the gold card to be inserted in the deck

			pool.add(starterCard);
			i++;
		}
		Collections.shuffle(pool); // shuffle the deck using shuffle method from java.util
	}

	/**
	 * This method take out a card from the deck and return the card.
	 * @return the first card of the deck.
	 */
	public StarterCard getStarter()
	{
		StarterCard s;
		s = pool.getFirst();
		pool.remove(s);
		return s;
	}
	/** @return the list of cards created */
	public LinkedList<StarterCard> getPool() {
		return pool;
	}
}

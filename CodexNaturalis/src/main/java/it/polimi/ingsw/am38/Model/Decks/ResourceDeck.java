package it.polimi.ingsw.am38.Model.Decks;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.am38.Exception.EmptyDeckException;
import it.polimi.ingsw.am38.Exception.InvalidInputException;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import it.polimi.ingsw.am38.Model.Player;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

/**
 * ResourceDeck take the data from the json file e send them to the constructor of the starter cards
 */
public class ResourceDeck implements Draw{
    /** This attribute is the deck itself, an array of resource cards */
    private final LinkedList<ResourceCard> pool = new LinkedList<>();
    /**
     * This attribute contains 1 of the 2 cards that eligible to be drawn from the "ground"
     */
    private ResourceCard Ground0;
    /**
     * This attribute contains 1 of the 2 cards that eligible to be drawn from the "ground"
     */
    private ResourceCard Ground1;
    private String topCardColor;

    /**
     * This constructor, using gson methods, take cards info from the json, send them the to the resource cards constructor and put the
     * card created in the array that represents the deck and shuffles it
     */
    public ResourceDeck(){
        Gson gson = new Gson();
        // Search file in /src/main/resources/ directory, path is valid for every machine so that there's no need to
        // change this for each PC. Seems to be useful for .jar dependencies too
        JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(ResourceDeck.class.getClassLoader().getResourceAsStream("resourceCard.json"))));
        JsonArray jsonArray = gson.fromJson(jsonReader, JsonArray.class);
        int i = 0;
        for(JsonElement element : jsonArray){ // for each element in the json file
            JsonObject jsonObject1, jsonObject2, jsonObject3;
            ResourceCard resourceCard;

            jsonObject1 = jsonArray.get(i).getAsJsonObject();  //getting every "card" from the json

            String cardID = jsonObject1.get("cardID").getAsString();
            String kingdom = jsonObject1.get("kingdom").getAsString();
            String imgFront = "images/front/" + cardID + "-front.svgz";
            String imgBack = "images/back/" + cardID + "-back.svgz";
            int pointGiven = jsonObject1.get("pointGiven").getAsInt();

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

            resourceCard = new ResourceCard(ID, kingdom, imgFront, imgBack, pointGiven, FNW, FNE, FSW, FSE,
                    BNW, BNE, BSW, BSE);  //create the resource card to be inserted in the deck


            pool.add(resourceCard); // add each card to the pool
            i++;
        }
        Collections.shuffle(pool); // shuffle the deck using shuffle method from java.util
    }

    /**
     * method to draw a new random ResourceCard from the ResourceCardDeck
     * @param player which draws the ResourceCard
     * @throws EmptyDeckException if there's no more ResourceCards left to draw
     */
    public void draw(Player player) throws EmptyDeckException {
        player.getHand().addCard(takeCard());
    }
    /**
     * The method lets the caller draw a ResourceCard from the deck (i = null) or one of the 2 on the
     * table (i = 0 or i = 1)
     * @param i This parameter allows the caller to choose which card to draw
     * @param player This parameter is used to give the card extracted to the player passed
     */
    public void draw(Player player, int i) throws EmptyDeckException{
        switch (i) {
            case 0 -> {
                player.getHand().addCard(this.Ground0);
                if(!pool.isEmpty())
                    Ground0 = takeCard();
            }
            case 1 -> {
                player.getHand().addCard(this.Ground1);
                if(!pool.isEmpty())
                    Ground0 = takeCard();
            }
        }
    }

    /**
     * This method is used to take out a card from the deck pool.
     * @return the card extracted.
     * @throws EmptyDeckException if the deck is empty.
     */
    private ResourceCard takeCard() throws EmptyDeckException{
        if(!pool.isEmpty())
            return pool.removeFirst();
        else throw new EmptyDeckException("There are no cards left in this deck!");
    }
    /**
     * Setter method for the pair of face-up ResourceCards on the table
     */
    public void setUpGround(){
        try {
            this.Ground0 = takeCard();
            this.Ground1 = takeCard();
        } catch (EmptyDeckException e) {
            throw new RuntimeException("this should never happen since during setup the decks can't be empty", e);
        }
    }
    /** @return the list of cards created */
    public LinkedList<ResourceCard> getPool() {
        return pool;
    }
    /** @return one of the two card face up to be drawn from the players */
    public ResourceCard getGround0() {
        return Ground0;
    }
    /** @return one of the two card face up to be drawn from the players */
    public ResourceCard getGround1() {
        return Ground1;
    }
}

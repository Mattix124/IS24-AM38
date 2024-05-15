package it.polimi.ingsw.am38.Network.Client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Cards.*;
import it.polimi.ingsw.am38.Model.Decks.GoldDeck;
import it.polimi.ingsw.am38.Model.Decks.ObjectiveDeck;
import it.polimi.ingsw.am38.Model.Decks.ResourceDeck;
import it.polimi.ingsw.am38.Model.Decks.StarterDeck;

import java.io.InputStreamReader;
import java.util.*;

public class ClientDATA {
    /**
     * color chosen by this Player
     */
    private Color color;
    /**
     * nickname chosen by this Player
     */
    private String nickname;
    /**
     * ArrayList of all GoldCards
     */
    private final ArrayList<GoldCard> goldCards = new ArrayList<GoldCard>();
    /**
     * ArrayList of all ResourceCards
     */
    private final ArrayList<ResourceCard> resourceCards = new ArrayList<ResourceCard>();
    /**
     * ArrayList of all StarterCards
     */
    private StarterCard starterCard;
    /**
     * ArrayList of all ObjectiveCards
     */
    private final ArrayList<ObjectiveCard> objectiveCards = new ArrayList<>();
    /**
     * Map matching each Color (Player) with his map of all PlayableCards played by them: Coords are the key and
     * Integer is the cardID
     */
    private final HashMap<String, HashMap<Coords, Integer>> cardsOnFields = new HashMap<>();

    /**
     * map linking each Color to the nickname of the player who chose that Color
     */
    private final HashMap<String, Color> players = new HashMap<>();
    /**
     * map linking each Color to its scores
     */
    private final HashMap<String, Integer> scores = new HashMap<>();
    /**
     * map linking each Color to his hand represented by the 3 cardID of the cards in it
     */
    private final HashMap<String, Integer[]> hands = new HashMap<>();
    private int faceUpGoldCard1;
    private int faceUpGoldCard2;
    private int faceUpResourceCard1;
    private int faceUpResourceCard2;

    public ClientDATA() {
        buildGoldCards();
        buildResourceDeck();
        buildObjectiveDeck();
    }

    /**
     * This constructor, using gson methods, take cards info from the json, send them the to the gold cards constructor and put the
     * card created in the Arraylist
     */
    public void buildGoldCards() {
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
            String imgFront = "images/front/" + cardID + "-front.svgz";
            String imgBack = "images/back/" + cardID + "-back.svgz";
            String condPointType = jsonObject1.get("conditionPointType").getAsString();
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
            String BSE = jsonObject3.get("SE").getAsString();

            jsonObject4 = jsonObject1.get("conditionPlace").getAsJsonObject();  //same as previous

            String first = jsonObject4.get("first").getAsString();
            String second = jsonObject4.get("second").getAsString();
            String third = jsonObject4.get("third").getAsString();
            String fourth = jsonObject4.get("fourth").getAsString();
            String fifth = jsonObject4.get("fifth").getAsString();//get data from json till here

            goldCard = new GoldCard(ID, kingdom, imgFront, imgBack, condPointType, pointGiven, FNW, FNE, FSW, FSE,
                    BNW, BNE, BSW, BSE, first, second, third, fourth, fifth);  //create the gold card to be inserted in the ArrayList

            this.goldCards.add(goldCard);
            i++;
        }
    }

    /**
     * This constructor, using gson methods, take cards info from the json, send them the to the resource cards constructor and put the
     * card created in the ArrayList
     */
    public void buildResourceDeck(){
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


            this.resourceCards.add(resourceCard); // add each card to the ArrayList
            i++;
        }
    }

    /**
     * This constructor, using gson methods, take cards info from the json, send them the to the objective cards constructor and put the
     * card created in the ArrayList
     */
    public void buildObjectiveDeck() {
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
            objectiveCards.add(objectiveCard); // each objective card is added after the switch (but obviously still inside the loop)
            i++;
        }
    }

    /**
     * This constructor, using gson methods, take cards info from the json, send them the to the starter cards constructor and put the
     * card created in the ArrayList
     */
    public void setStarterDeck(int cardID)
    {
        /*Gson gson = new Gson();
        // Search file in /src/main/resources/ directory, path is valid for every machine so that there's no need to
        // change this for each PC. Seems to be useful for .jar dependencies too
        JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(StarterDeck.class.getClassLoader().getResourceAsStream("starterCard.json"))));
        JsonArray  jsonArray  = gson.fromJson(jsonReader, JsonArray.class);
        int i = 0;
        for(JsonElement element : jsonArray){ // for each element in the json file
            JsonObject jsonObject1, jsonObject2, jsonObject3, jsonObject4;
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

            starterCards.add(starterCard);
            i++;
        }*/
    }

    /**
     * adds the card with the given id at the given coordinates x and y to the given player's cardsOnField
     * @param nickname of the player
     * @param cardID the id of the card added
     * @param x coordinate
     * @param y coordinate
     */
    public void addCardToPlayerField(String nickname, int cardID, int x, int y, boolean facing){
        Coords c = new Coords(x, y);
        setCardFacing(cardID, facing);
        cardsOnFields.get(nickname).put(c, cardID);
    }

    //-------------------------------------------------------------------------------------------------GetterMethods

    /**
     * method used to get a PlayableCard from its cardID
     * @param id of the PlayableCard wanted
     * @return the PlayableCard wanted
     */
    public PlayableCard getPlayableCardFromList(int id) {
        if (id < 41){
            List<ResourceCard> gc = resourceCards.stream().filter(c -> c.getCardID() == id).toList();
            return gc.getFirst();
        }else{
            List<GoldCard> gc = goldCards.stream().filter(c->c.getCardID() == id).toList();
            return gc.getFirst() ;
        }
    }

    /**
     * method used to get an ObjectiveCard from its cardID
     * @param id of the ObjectiveCard wanted
     * @return the ObjectiveCard wanted
     */
    private ObjectiveCard getObjectiveCardFromList(int id){
        List<ObjectiveCard> gc = objectiveCards.stream().filter(c -> c.getCardID() == id).toList();
        return gc.getFirst();
    }

    /**
     * getter for the card with the given coordinates (x and y) and the given player
     * @param nickname of the player from which field the card is gotten
     * @param x coordinate
     * @param y coordinate
     * @return the card at the given coordinates of the given player's field
     */
    public PlayableCard getCardFromPlayerField(String nickname, int x, int y){
        Coords c = new Coords(x, y);
        return getPlayableCardFromList(cardsOnFields.get(nickname).get(c));
    }

    public LinkedList<String> getPlayersNicknames(){
        return new LinkedList<String>(players.keySet().stream().toList());
    }

    public String getNickname() {
        return nickname;
    }

    //--------------------------------------------------------------------------------------------------SetterMethods

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setColor(Color color){
        this.color = color;
    }
    public void setPlayersNicknames(LinkedList<String> playersToAdd){
        for (String s : playersToAdd) players.put(s, null);
    }

    /**
     * sets the PlayableCard with given id facing to the chosen one
     * @param id card's facing to set
     * @param facing true is face up, false is face down
     */
    private void setCardFacing(int id, boolean facing){
        if (id < 41){
            resourceCards.stream().filter(c -> c.getCardID() == id).forEach(c->c.setFace(facing));
        }else{
            goldCards.stream().filter(c->c.getCardID() == id).forEach(c->c.setFace(facing));
        }
    }

}

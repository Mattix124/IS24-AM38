package it.polimi.ingsw.am38.Network.Client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Model.Cards.*;
import it.polimi.ingsw.am38.Model.Decks.GoldDeck;
import it.polimi.ingsw.am38.Model.Decks.ObjectiveDeck;
import it.polimi.ingsw.am38.Model.Decks.ResourceDeck;
import it.polimi.ingsw.am38.Model.Decks.StarterDeck;

import java.io.InputStreamReader;
import java.util.*;

/**
 * Class that contains all the data of the client
 */
public class ClientDATA {
    private static ClientDATA clientDATA;
    /**
     * Nickname chosen by this Player
     */
    private String nickname;
    /**
     * ArrayList of all GoldCards
     */
    private final ArrayList<GoldCard> goldCards = new ArrayList<GoldCard>(40);
    /**
     * ArrayList of all ResourceCards
     */
    private final ArrayList<ResourceCard> resourceCards = new ArrayList<ResourceCard>(40);
    /**
     * ArrayList of all StarterCards
     */
    private final ArrayList<StarterCard> starterCards = new ArrayList<>(6);
    /**
     * Arraylist of all ObjectiveCards
     */
    private final ArrayList<ObjectiveCard> objectiveCards = new ArrayList<>(16);
    /**
     * Map matching each Player's nickname with his map of all PlayableCards played by them: Coords are the key and
     * Integer is the cardID
     */
    private final HashMap<String, HashMap2<Coords, Integer>> cardsOnFields = new HashMap<>();
    /**
     * Map matching each Player's nickname with his own Map of visible Symbols on his field (keys = symbols, value = number)
     */
    private final HashMap<String, VisibleElements> symbolsOnFields = new HashMap<>();

    /**
     * Map linking each Color to the nickname of the player who chose that Color
     */
    private final HashMap<String, Color> players = new HashMap<>();
    /**
     * Map linking each Player to their score
     */
    private final HashMap<String, Integer> scores = new HashMap<>();
    /**
     * Map linking each Color to his hand represented by the 3 cardID of the cards in it
     */
    private final int[] hand = new int[3];
    /**
     * Map containing the 3 symbols of each player's hand
     */
    private final HashMap<String, String[]> handCardsColors = new HashMap<>();
    /**
     * First of the two face up Gold Cards Players can draw from at the end of their turns
     */
    private int faceUpGoldCard1;
    /**
     * Second of the two face up Gold Cards Players can draw from at the end of their turns
     */
    private int faceUpGoldCard2;
    /**
     * First of the two face up Resource Cards Players can draw from at the end of their turns
     */
    private int faceUpResourceCard1;
    /**
     * Second of the two face up Resource Cards Players can draw from at the end of their turns
     */
    private int faceUpResourceCard2;
    /**
     * Symbol of the face down card on top of the Gold Card Deck
     */
    private Symbol gTop;
    /**
     * Symbol of the face down card on top of the Resource Card Deck
     */
    private Symbol rTop;
    /**
     * 2 shared objectives, 1 objective chosen by this player, 2 possible objectives he could have chosen from (all represented as IDs)
     */
    private int sharedObj1, sharedObj2, personalObj, objectiveChoice1, objectiveChoice2;
    /**
     * List of all player's nicknames
     */
    private final LinkedList<String> names = new LinkedList<>();
    /**
     * nickname of the player whose field is being looked at
     */
    private String shownPayerNick;

    /**
     * Constructor, builds the lists of cards
     */
    public ClientDATA() {
        buildGoldList();
        buildResourceList();
        buildStarterList();
        buildObjectiveList();
    }

    /**
     * This constructor, using gson methods, takes the cards info from the json files, sends them the to the GoldCard
     * constructor and adds the card created in the Arraylist
     */
    public void buildGoldList() {
        Gson gson = new Gson();
        // Searches file in /src/main/resources/ directory common path for any device
        JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(GoldDeck.class.getClassLoader().getResourceAsStream("goldCard.json"))));
        JsonArray jsonArray = gson.fromJson(jsonReader, JsonArray.class);
        for(JsonElement element : jsonArray){ // for each element in the json file

            JsonObject jsonObject1, jsonObject2, jsonObject3, jsonObject4;
            jsonObject1 = element.getAsJsonObject();  //getting every "card" from the json

            String cardID = jsonObject1.get("cardID").getAsString();
            String kingdom = jsonObject1.get("kingdom").getAsString();
            String imgFront = "GameImages/front/" + cardID + "-front.png";
            String imgBack = "GameImages/back/" + cardID + "-back.png";
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

            this.goldCards.add(new GoldCard(ID, kingdom, imgFront, imgBack, condPointType, pointGiven, FNW, FNE, FSW, FSE, BNW, BNE, BSW, BSE, first, second, third, fourth, fifth));
        }
    }

    /**
     * This constructor, using gson methods, takes the cards info from the json files, sends them the to the ResourceCard
     * constructor and adds the card created in the Arraylist
     */
    public void buildResourceList(){
        Gson gson = new Gson();
        // Searches file in /src/main/resources/ directory common path for any device
        JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(ResourceDeck.class.getClassLoader().getResourceAsStream("resourceCard.json"))));
        JsonArray jsonArray = gson.fromJson(jsonReader, JsonArray.class);
        for(JsonElement element : jsonArray){ // for each element in the json file
            JsonObject jsonObject1, jsonObject2, jsonObject3;

            jsonObject1 = element.getAsJsonObject();  //getting every "card" from the json

            String cardID = jsonObject1.get("cardID").getAsString();
            String kingdom = jsonObject1.get("kingdom").getAsString();
            String imgFront = "GameImages/front/" + cardID + "-front.png";
            String imgBack = "GameImages/back/" + cardID + "-back.png";
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

            this.resourceCards.add(new ResourceCard(ID, kingdom, imgFront, imgBack, pointGiven, FNW, FNE, FSW, FSE, BNW, BNE, BSW, BSE)); // add the ResourceCard to the ArrayList
        }
    }

    /**
     * This constructor, using gson methods, takes the cards info from the json files, sends them the to the StarterCard
     * constructor and adds the card created in the Arraylist
     */
    public void buildStarterList(){

        Gson gson = new Gson();
        // Searches file in /src/main/resources/ directory common path for any device
        JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(StarterDeck.class.getClassLoader().getResourceAsStream("starterCard.json"))));
        JsonArray  jsonArray  = gson.fromJson(jsonReader, JsonArray.class);
        for(JsonElement element : jsonArray){ // for each element in the json file
            JsonObject jsonObject1, jsonObject2, jsonObject3, jsonObject4;

            jsonObject1 = element.getAsJsonObject();  //getting every "card" from the json

            String cardID = jsonObject1.get("cardID").getAsString();
            String imgFront = "GameImages/front/" + cardID + "-front.png";
            String imgBack = "GameImages/back/" + cardID + "-back.png";

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

            this.starterCards.add(new StarterCard(ID, imgFront, imgBack, FNW, FNE, FSW, FSE, BNW, BNE, BSW, BSE, first, second, third)); //adds a new starterCard to the List
        }
    }

    /**
     * This constructor, using gson methods, takes the cards info from the json files, sends them the to the ObjectiveCard
     * constructor and adds the card created in the Arraylist
     */
    public void buildObjectiveList() {
        Gson gson = new Gson();
        // Searches file in /src/main/resources/ directory common path for any device
        JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(ObjectiveDeck.class.getClassLoader().getResourceAsStream("objectiveCard.json"))));
        JsonArray jsonArray = gson.fromJson(jsonReader, JsonArray.class);
        for(JsonElement element : jsonArray){ // for each element in the json file
            JsonObject jsonObject1;

            jsonObject1 = element.getAsJsonObject();  //getting every "card" from the json

            String cardID = jsonObject1.get("cardID").getAsString();
            String description = jsonObject1.get("description").getAsString();
            String imgFront = "GameImages/front/" + cardID + "-front.png";
            String imgBack = "GameImages/back/" + cardID + "-back.png";

            this.objectiveCards.add(new ObjectiveCard(Integer.parseInt(cardID), "null", null, imgFront, imgBack, 0, "null", "null", "null", description));//adds the card to the ArrayList
        }
    }

    /**
     * Sets each Player's StarterCard by adding it in the (0, 0) position of their field
     *
     * @param sc HashMap containing each player (key) and their StarterCard's ID (value)
     */
    public void setStarterCards(HashMap<String, Integer> sc){
        sc.forEach((k, v) -> {
            addCardToPlayerField(k, v, 0, 0, true);
            names.add(k); players.put(k, null);
        });
    }
    /**
     * Adds the card with the given id at the given coordinates x and y to the given player's cardsOnField
     *
     * @param nickname of the player
     * @param cardID the id of the card added
     * @param x coordinate
     * @param y coordinate
     */
    public void addCardToPlayerField(String nickname, int cardID, int x, int y, boolean facing){
        if(!this.cardsOnFields.containsKey(nickname))
            initializeCardsOnFields(nickname);
        setCardFacing(cardID, facing);
        this.cardsOnFields.get(nickname).put(new Coords(x, y), cardID);
    }

    /**
     * initializer for the HashMap2 having
     * as keys: Coords of the cards played
     * as values: Integer containing the ID of the card played
     * @param nickname of the Player who owns the field-map
     */
    private void initializeCardsOnFields(String nickname){
        HashMap2<Coords, Integer> hm = new HashMap2<>();
        this.cardsOnFields.put(nickname, hm);
    }

    //-------------------------------------------------------------------------------------------------GetterMethods

    /**
     * getter method for a Player's score
     * @param nickname String containing the nickname of the Player
     * @return the int containing the Player's score
     */
    public int getScore(String nickname){
        return this.scores.get(nickname);
    }

    /**
     * Method used to get a PlayableCard from its cardID
     *
     * @param id of the PlayableCard wanted
     * @return the PlayableCard wanted
     */
    public PlayableCard getPlayableCardFromList(int id) {
        if (id < 41)
            return resourceCards.stream().filter(c -> c.getCardID() == id).toList().getFirst();
        else if(id < 81)
            return goldCards.stream().filter(c->c.getCardID() == id).toList().getFirst();
        else
            return starterCards.stream().filter(c->c.getCardID() == id).toList().getFirst();
    }

    /**
     * Getter for the ObjectiveCard with the id given as parameter
     *
     * @param id of the wanted card
     * @return the ObjectiveCard with given id
     */
    public ObjectiveCard getObjectiveCard(int id){
        return objectiveCards.stream().filter(c->c.getCardID() == id).toList().getFirst();
    }

    /**
     * Getter for the card with the given coordinates (x and y) and the given player
     *
     * @param x coordinate
     * @param y coordinate
     * @return the card at the given coordinates of the given player's field
     */
    public PlayableCard getCardFromPlayerField(String nickname, int x, int y){
        Coords c = new Coords(x, y);
        if(nickname.equals("onScreenForRealForReal"))
            return getPlayableCardFromList((Integer) cardsOnFields.get(shownPayerNick).get(cardsOnFields.get(shownPayerNick).getKey(c)));
        return getPlayableCardFromList((Integer) cardsOnFields.get(nickname).get(cardsOnFields.get(nickname).getKey(c)));
    }

    /**
     * Getter method for the list of nicknames
     *
     * @return the list of the players' nicknames
     */
    public LinkedList<String> getPlayersNicknames(){
        return names;
    }

    /**
     * Getter method for each player's nickname and color
     *
     * @return an HashMap of nicknames (key) and their associated color (value)
     */
    public HashMap<String, Color> getPlayersNickAndColor(){
        return players;
    }

    /**
     * Getter method for each player's Starter Card
     *
     * @return an HashMap with all nicknames (kay) and their StarterCard (value)
     */
    public HashMap<String, StarterCard> getStarters(){
        HashMap<String, StarterCard> cards = new HashMap<>();
        players.forEach((k, v) -> cards.put(k, getStarterCard(k)));
        return cards;
    }

    /**
     * Getter method for the nickname of this player
     *
     * @return a String containing the nickname of this Player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter method for any StarterCard that's been played
     *
     * @param nick the name of the Player that played the requested StarterCard
     * @return the StarterCard of the Player which nickname is given as a parameter
     */
    public StarterCard getStarterCard(String nick) {
        Coords c = new Coords(0, 0);
        for(StarterCard s : starterCards) {
            if (cardsOnFields.get(nick).get(cardsOnFields.get(nick).getKey(c)).equals(s.getCardID()))
                return s;
        }
        return null;
    }

    /**
     * Getter method for the first shared ObjectiveCard
     *
     * @return the first shared ObjectiveCard
     */
    public ObjectiveCard getSharedObj1(){
        return getObjectiveCard(this.sharedObj1);
    }

    /**
     * Getter method for the second shared ObjectiveCard
     *
     * @return the second shared ObjectiveCard
     */
    public ObjectiveCard getSharedObj2() {
        return getObjectiveCard(this.sharedObj2);
    }

    /**
     * Getter method for this Player's chosen personalObjective
     *
     * @return the ObjectiveCard this Player chose as his personal one (from the 2 choices)
     */
    public ObjectiveCard getPersonalObjective(){
        return getObjectiveCard(personalObj);
    }

    /**
     * Getter method for the first choice of ObjectiveCard
     *
     * @return the first choice of ObjectiveCard
     */
    public ObjectiveCard getObjectiveChoice1(){
        return getObjectiveCard(this.objectiveChoice1);
    }

    /**
     * Getter method for the second choice of ObjectiveCard
     *
     * @return the second choice of ObjectiveCard
     */
    public ObjectiveCard getObjectiveChoice2(){
        return getObjectiveCard(this.objectiveChoice2);
    }

    /**
     * Getter method for the Symbols of the cards in hand of each Player
     *
     *  @return an HashMap with each Player's nickname and their cards in hand's symbol
     */
    public HashMap<String, String[]> getHandCardsColors(){
        return this.handCardsColors;
    }

    /**
     * Getter method for the list of PlayableCard (Gold or Resource) this Player has in their Hand
     *
     * @return a LinkedList containing all this Player's PlayableCards in Hand (2 or 3 depending on the phase of the game)
     */
    public LinkedList<PlayableCard> getHand() {
        LinkedList<PlayableCard> hand = new LinkedList<>();
        for (int id : this.hand){
            if (id != 0)
                hand.add(getPlayableCardFromList(id));
            else
                hand.add(new ResourceCard(0, "null", null, null, 0, "null", "null", "null", "null", "null", "null", "null", "null"));
        }
        return hand;
    }

    /**
     * Getter method for the first face up Gold Card
     *
     * @return the first face up Gold Card
     */
    public GoldCard getFaceUpGoldCard1() {
        return (GoldCard) getPlayableCardFromList(faceUpGoldCard1);
    }

    /**
     * Getter method for the second face up Gold Card
     *
     * @return the second face up Gold Card
     */
    public GoldCard getFaceUpGoldCard2() {
        return (GoldCard) getPlayableCardFromList(faceUpGoldCard2);
    }

    /**
     * Getter method for the first face up Resource Card
     *
     * @return the first face up Resource Card
     */
    public ResourceCard getFaceUpResourceCard1() {
        return (ResourceCard) getPlayableCardFromList(faceUpResourceCard1);
    }

    /**
     * Getter method for the second face up Resource Card
     *
     * @return the second face up Resource Card
     */
    public ResourceCard getFaceUpResourceCard2() {
        return (ResourceCard) getPlayableCardFromList(faceUpResourceCard2);
    }

    /**
     * Getter method for the symbol of the Top Card of the Gold Card Deck
     *
     * @return the symbol of the Top Card of the Gold Card Deck
     */
    public Symbol getGTop() {
        return gTop;
    }

    /**
     * Getter method for the symbol of the Top Card of the Resource Card Deck
     *
     * @return the symbol of the Top Card of the Resource Card Deck
     */
    public Symbol getRTop() {
        return rTop;
    }

    /**
     * Getter for the colors of the hands of a player
     *
     * @param nickname of the player
     * @return an array with the color of the cards in the hand
     */
    public String[] getPlayerHandCardsColor(String nickname){
        return this.handCardsColors.get(nickname);
    }

    /**
     * Getter for the elements on the field of a player
     *
     * @param nickname of the player
     * @return VisibleElements of a player
     */
    public VisibleElements getSymbolTab(String nickname){
        return this.symbolsOnFields.get(nickname);
    }

    //--------------------------------------------------------------------------------------------------SetterMethods

    /**
     * Setter for the score of a player
     * @param nickname of the player
     * @param score
     */
    public void setScore(String nickname, int score){
        this.scores.put(nickname, score);
    }

    public void setShownPayerNick(String nick){
        this.shownPayerNick = nick;
    }

    /**
     * Setter method for each Player's StarterCard facing
     *
     * @param facings an HashMap containing each Player's nickname and the Boolean representing the facing chosen by them
     */
    public void setStarterCardsFacing(HashMap<String, Boolean> facings){
        facings.forEach((k, v) -> getStarterCard(k).setFace(v));
    }

    /**
     * Setter method for this Player's nickname
     *
     * @param nickname a String containing the nickname to be set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
        this.shownPayerNick = nickname;
    }

    /**
     * Setter method for the drawn objective cards (first 2 = shared, second 2 = personal choice of this player) each represented by their ID
     *
     * @param objectives array of int, representing the ObjectiveCards IDs
     */
    public void setObjectives(int[] objectives){
        this.sharedObj1 = objectives[0];
        this.sharedObj2 = objectives[1];
        this.objectiveChoice1 = objectives[2];
        this.objectiveChoice2 = objectives[3];
    }

    /**
     * Setter method for the personal ObjectiveCard chosen by this Player
     *
     * @param i the index(String {1;2) referring to the first or second objectiveChoice
     */
    public void setPersonalObjectiveChosen(String i){
        if(i.equals("1"))
            this.personalObj = objectiveChoice1;
        else
            this.personalObj = objectiveChoice2;
    }

    /**
     * setter method for this Player's ObjectiveCard (only used when reconnecting)
     * @param objCardID the int containing the ID of the ObjectiveCard chosen
     */
    public void setPersonalObj(int objCardID){
        this.personalObj = objCardID;
    }

    /**
     * setter method for the Symbols Tab
     * @param nickname String containing the nickname of the Player whose symTab is being set
     * @param symTab the VisibleElements to set as symTab
     */
    public void setSymbolTab(String nickname, VisibleElements symTab){
        this.symbolsOnFields.put(nickname, symTab);
    }

    /**
     * Setter method for each Player's chosen Color
     *
     * @param pc an HashMap of each Player's nickname (key) and their chosen Color (value)
     */
    public void setPlayersColors(HashMap<String, Color> pc){
        players.forEach((k, v) -> players.put(k, pc.get(k)));
    }

    /**
     * Setter method for a Player's chosen Color
     * @param nick a String containing the Player's nickname
     * @param color the Color that the Player chose
     */
    public void setPlayerColor(String nick, Color color){
        players.put(nick, color);
    }

    /**
     * Sets the PlayableCard with given id facing to the chosen one
     *
     * @param id card's facing to set
     * @param facing true is face up, false is face down
     */
    private void setCardFacing(int id, boolean facing){
        if (id < 41){
            resourceCards.stream().filter(c -> c.getCardID() == id).forEach(c->c.setFace(facing));
        }else if(id < 81){
            goldCards.stream().filter(c->c.getCardID() == id).forEach(c->c.setFace(facing));
        }else
            starterCards.stream().filter(c->c.getCardID() == id).forEach(c->c.setFace(facing));
    }

    /**
     * Setter method for each Player's cards in Hand Symbols
     *
     * @param hcc an HashMap of each Player's nickname and their cards in Hand Symbols
     */
    public void setHandsCardsColors(HashMap<String, String[]> hcc){
        this.handCardsColors.putAll(hcc);
    }

    /**
     * setter method for the color of the cards in the Hand of a Player
     * @param nickname String containing the nickname of the Player
     * @param cardsInHandColors an Array of Strings containing the information about the color (and type: gold or resource)
     */
    public  void setPlayerHandCardColors(String nickname, String[] cardsInHandColors){
        this.handCardsColors.put(nickname, cardsInHandColors);
    }

    /**
     * Setter method for this Player's PlayableCards in Hand
     *
     * @param cardsInHand an array of int representing this Player's PlayableCards in Hand IDs
     */
    public void setStartingHand(int[] cardsInHand){
        for(int i = 0 ; i < cardsInHand.length ; i++)
            this.hand[i] = cardsInHand[i];
    }

    /**
     * Setter method used to "remove" a card from this Player's Hand (done by setting the id saved in this.hand at 0)
     *
     * @param id an int representing the id of the card just played by this Player
     */
    public void cardPlayed(int id){
        for(int i = 0 ; i < 3 ; i ++)
            if(this.hand[i] == id)
                this.hand[i] = 0;
    }

    /**
     * Setter method used to add the id of the newly drawn card to this Player's Hand
     *
     * @param id an int containing the id of the newly drawn card
     */
    public void cardDrawn(int id){
        int i;
        for(i = 0 ; i < 3 && this.hand[i] != 0; i++);
        if(i < 3 && this.hand[i] == 0)
            this.hand[i] = id;
    }

    /**
     * Setter method for both face up GoldCards
     *
     * @param gs array of 2 int representing the IDs of the 2 GoldCards to be set
     */
    public void setGGround(int[] gs){
        this.faceUpGoldCard1 = gs[0];
        this.faceUpGoldCard2 = gs[1];
    }

    /**
     * Setter method for both face up ResourceCards
     *
     * @param rs array of 2 int representing the IDs of the 2 ResourceCards to be set
     */
    public void setRGround(int[] rs){
        this.faceUpResourceCard1 = rs[0];
        this.faceUpResourceCard2 = rs[1];
    }

    /**
     * Setter method for the first face up GoldCard
     *
     * @param gc an int representing the ID of the GoldCard to be set
     */
    public void setGGround1(int gc){
        this.faceUpGoldCard1 = gc;
    }

    /**
     * Setter method for the second face up GoldCard
     *
     * @param gc an int representing the ID of the GoldCard to be set
     */
    public void setGGround2(int gc){
        this.faceUpGoldCard2 = gc;
    }

    /**
     * Setter method for the first face up ResourceCard
     *
     * @param rc an int representing the ID of the ResourceCard to be set
     */
    public void setRGround1(int rc){
        this.faceUpResourceCard1 =rc;
    }

    /**
     * Setter method for the second face up ResourceCard
     *
     * @param rc an int representing the ID of the ResourceCard to be set
     */
    public void setRGround2(int rc){
        this.faceUpResourceCard2 = rc;
    }

    /**
     * Setter method for the Symbol of the top card of the Gold deck
     *
     * @param gt the Symbol of the top card of the Gold deck
     */
    public void setGTop(Symbol gt){
        this.gTop = gt;
    }

    /**
     * Setter method for the Symbol of the top card of the Resource deck
     *
     * @param rt the Symbol of the top card of the Resource deck
     */
    public void setRTop(Symbol rt){
        this.rTop = rt;
    }

}
class HashMap2<K, V> extends HashMap{
    /**
     * New getter method added to the HashMap ones that allows to get a key from the Map as long as it's Equal to the
     * one given as parameter
     *
     * @param key Object of comparison
     * @return the actual key (the "right pointer" to the actual key)
     */
    public Object getKey(Object key){
        for(Object o : this.keySet())
            if(key.equals(o))
                return o;
        return null;
    }
}

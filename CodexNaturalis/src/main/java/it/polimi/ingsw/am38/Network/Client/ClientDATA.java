package it.polimi.ingsw.am38.Network.Client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Cards.*;
import it.polimi.ingsw.am38.Model.Decks.GoldDeck;
import it.polimi.ingsw.am38.Model.Decks.ObjectiveDeck;
import it.polimi.ingsw.am38.Model.Decks.ResourceDeck;
import it.polimi.ingsw.am38.Model.Decks.StarterDeck;
import it.polimi.ingsw.am38.Model.Player;
import javafx.util.Pair;

import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class ClientDATA {
    /**
     * nickname chosen by this Player
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
     * Map matching each Color (Player) with his map of all PlayableCards played by them: Coords are the key and
     * Integer is the cardID
     */
    private final HashMap<String, HashMap2<Coords, Integer>> cardsOnFields = new HashMap<>();

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
    private final int[] hand = new int[3];
    private final HashMap<String, Symbol[]> handCardsColors = new HashMap<>();
    private int faceUpGoldCard1;
    private int faceUpGoldCard2;
    private int faceUpResourceCard1;
    private int faceUpResourceCard2;
    private Symbol gTop;
    private Symbol rTop;
    private String sharedObj1, sharedObj2, personalObj, objectiveChoice1, objectiveChoice2;


    public ClientDATA() {
        buildGoldCards();
        buildResourceDeck();
        buildStarterDeck();
    }

    /**
     * This constructor, using gson methods, takes the cards info from the json files, sends them the to the GoldCard
     * constructor and adds the card created in the Arraylist
     */
    public void buildGoldCards() {
        Gson gson = new Gson();
        // Searches file in /src/main/resources/ directory common path for any device
        JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(GoldDeck.class.getClassLoader().getResourceAsStream("goldCard.json"))));
        JsonArray jsonArray = gson.fromJson(jsonReader, JsonArray.class);
        int i = 0;
        for(JsonElement element : jsonArray){ // for each element in the json file

            JsonObject jsonObject1, jsonObject2, jsonObject3, jsonObject4;
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

            this.goldCards.add(new GoldCard(ID, kingdom, imgFront, imgBack, condPointType, pointGiven, FNW, FNE, FSW, FSE, BNW, BNE, BSW, BSE, first, second, third, fourth, fifth));
            i++;
        }
    }

    /**
     * This constructor, using gson methods, takes the cards info from the json files, sends them the to the ResourceCard
     * constructor and adds the card created in the Arraylist
     */
    public void buildResourceDeck(){
        Gson gson = new Gson();
        // Searches file in /src/main/resources/ directory common path for any device
        JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(ResourceDeck.class.getClassLoader().getResourceAsStream("resourceCard.json"))));
        JsonArray jsonArray = gson.fromJson(jsonReader, JsonArray.class);
        int i = 0;
        for(JsonElement element : jsonArray){ // for each element in the json file
            JsonObject jsonObject1, jsonObject2, jsonObject3;

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

            this.resourceCards.add(new ResourceCard(ID, kingdom, imgFront, imgBack, pointGiven, FNW, FNE, FSW, FSE, BNW, BNE, BSW, BSE)); // add the ResourceCard to the ArrayList
            i++;
        }
    }

    /**
     * This constructor, using gson methods, takes the cards info from the json files, sends them the to the StarterCard
     * constructor and adds the card created in the Arraylist
     */
    public void buildStarterDeck(){

        Gson gson = new Gson();
        // Searches file in /src/main/resources/ directory common path for any device
        JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(StarterDeck.class.getClassLoader().getResourceAsStream("starterCard.json"))));
        JsonArray  jsonArray  = gson.fromJson(jsonReader, JsonArray.class);
        int i = 0;
        for(JsonElement element : jsonArray){ // for each element in the json file
            JsonObject jsonObject1, jsonObject2, jsonObject3, jsonObject4;

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

            this.starterCards.add(new StarterCard(ID, imgFront, imgBack, FNW, FNE, FSW, FSE, BNW, BNE, BSW, BSE, first, second, third)); //adds a new starterCard to the List
            i++;
        }
    }

    public void setStarterCards(HashMap<String, Integer> sc){
        sc.forEach((k, v) -> addCardToPlayerField(k, v, 0, 0, true));
    }
    /**
     * adds the card with the given id at the given coordinates x and y to the given player's cardsOnField
     * @param nickname of the player
     * @param cardID the id of the card added
     * @param x coordinate
     * @param y coordinate
     */
    public void addCardToPlayerField(String nickname, int cardID, int x, int y, boolean facing){
        HashMap2<Coords, Integer> hm = new HashMap2<>();
        Coords c = new Coords(x, y);
        hm.put(c, cardID);
        setCardFacing(cardID, facing);
        cardsOnFields.put(nickname, hm);
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
            ResourceCard card = gc.getFirst();
            card.setFace(true);
            return card;
        }else if(id < 81){
            List<GoldCard> gc = goldCards.stream().filter(c->c.getCardID() == id).toList();
            GoldCard card = gc.getFirst();
            card.setFace(true);
            return card;
        }else{
            List<StarterCard> gc = starterCards.stream().filter(c->c.getCardID() == id).toList();
            return gc.getFirst();
        }
    }

    /**
     * getter for the card with the given coordinates (x and y) and the given player
     * @param nickname of the player from which field the card is gotten
     * @param x coordinate
     * @param y coordinate
     * @return the card at the given coordinates of the given player's field
     */
    public PlayableCard getCardFromPlayerField(String nickname, int x, int y){
        return getPlayableCardFromList((Integer) cardsOnFields.get(nickname).get(new Coords(x, y)));
    }

    public LinkedList<String> getPlayersNicknames(){
        return new LinkedList<String>(players.keySet().stream().toList());
    }

    public HashMap<String, Color> getPlayersNickAndColor(){
        return players;
    }

    public HashMap<String, StarterCard> getStarters(){
        HashMap<String, StarterCard> cards = new HashMap<>();
        players.forEach((k, v) -> cards.put(k, getStarterCard(k)));
        return cards;
    }

    public String getNickname() {
        return nickname;
    }

    /**
     * getter method for any StarterCard that's been played
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

    public String getSharedObj1(){
        return this.sharedObj1;
    }

    public String getSharedObj2() {
        return this.sharedObj2;
    }

    public String getObjectiveChoice1(){
        return this.objectiveChoice1;
    }

    public String getObjectiveChoice2(){
        return this.objectiveChoice2;
    }

    public HashMap<String, Symbol[]> getHandCardsColors(){
        return this.handCardsColors;
    }

    public LinkedList<PlayableCard> getHand(){
        LinkedList<PlayableCard> hand = new LinkedList<>();
        for (int id : this.hand)
            hand.add(getPlayableCardFromList(id));
        return hand;
    }

    public GoldCard getFaceUpGoldCard1() {
        return (GoldCard) getPlayableCardFromList(faceUpGoldCard1);
    }

    public GoldCard getFaceUpGoldCard2() {
        return (GoldCard) getPlayableCardFromList(faceUpGoldCard2);
    }

    public ResourceCard getFaceUpResourceCard1() {
        return (ResourceCard) getPlayableCardFromList(faceUpResourceCard1);
    }

    public ResourceCard getFaceUpResourceCard2() {
        return (ResourceCard) getPlayableCardFromList(faceUpResourceCard2);
    }

    public Symbol getGTop() {
        return gTop;
    }

    public Symbol getRTop() {
        return rTop;
    }

    //--------------------------------------------------------------------------------------------------SetterMethods

    public void setStarterCardsFacing(HashMap<String, Boolean> facings){
        facings.forEach((k, v) -> getStarterCard(k).setFace(v));
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setObjectives(ArrayList<String> objectives){
        this.sharedObj1 = "1) " + String.format("%-68s", objectives.getFirst());
        this.sharedObj2 = "2) " + String.format("%-68s", objectives.get(1));
        this.objectiveChoice1 = objectives.get(2);
        this.objectiveChoice2 = objectives.get(3);
    }

    public String setPersonalObjectiveChosen(String i){
        if(i.equals("1"))
            return this.personalObj = objectiveChoice1;
        else
            return this.personalObj = objectiveChoice2;
    }

    public void setPlayersNicknames(LinkedList<String> playersToAdd){
        for (String s : playersToAdd)
            players.put(s, null);
    }

    public void setPlayersColors(HashMap<String, Color> pc){
        players.forEach((k, v) -> players.put(k, pc.get(k)));
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

    public void setHandCardsColors(HashMap<String, Symbol[]> hcc){
        this.handCardsColors.putAll(hcc);
    }

    public void setHand(int[] cardsInHand){
        System.arraycopy(cardsInHand, 0, this.hand, 0, cardsInHand.length);
    }

    public void setGGround(int[] gs){
        this.faceUpGoldCard1 = gs[0];
        this.faceUpGoldCard2 = gs[1];
    }

    public void setRGround(int[] rs){
        this.faceUpResourceCard1 = rs[0];
        this.faceUpResourceCard2 = rs[1];
    }

    public void setGTop(Symbol gt){
        this.gTop = gt;
    }

    public void setRTop(Symbol rt){
        this.rTop = rt;
    }

}
class HashMap2<K, V> extends HashMap{
    public Object getKey(Object key){
        for(Object o : this.keySet())
            if(key.equals(o)) return o;
        return null;
    }
}

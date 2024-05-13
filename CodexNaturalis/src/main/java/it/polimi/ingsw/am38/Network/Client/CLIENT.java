package it.polimi.ingsw.am38.Network.Client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.am38.Enum.Color;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import it.polimi.ingsw.am38.Model.Decks.GoldDeck;
import it.polimi.ingsw.am38.Model.Decks.ResourceDeck;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.util.*;

/**
 * This is the main client class that create the clients thread based on the connection chosen
 */
public class CLIENT implements Serializable {

    private static final long serialVersionUID = 749383786771428581L;
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
    ArrayList<GoldCard> goldCards = new ArrayList<GoldCard>();
    /**
     * ArrayList of all ResourceCards
     */
    ArrayList<ResourceCard> resourceCards = new ArrayList<ResourceCard>();
    /**
     * Map of all PlayableCards played by P1: Coords are the key and Integer is the cardID
     */
    private final HashMap<Coords, Integer> cardsOnP1Field = new HashMap<Coords, Integer>();
    /**
     * Map of all PlayableCards played by P2: Coords are the key and Integer is the cardID
     */
    private final HashMap<Coords, Integer> cardsOnP2Field = new HashMap<Coords, Integer>();
    /**
     * Map of all PlayableCards played by P3: Coords are the key and Integer is the cardID
     */
    private final HashMap<Coords, Integer> cardsOnP3Field = new HashMap<Coords, Integer>();
    /**
     * Map of all PlayableCards played by P4: Coords are the key and Integer is the cardID
     */
    private final HashMap<Coords, Integer> cardsOnP4Field = new HashMap<>();
    /**
     * map linking each Color to the nickname of the player who chose that Color
     */
    private final HashMap<Color, String> players = new HashMap<>();
    /**
     * map linking each Color to its scores
     */
    private final HashMap<Color, Integer> scores = new HashMap<>();
    /**
     * map linking each Color to his hand represented by the 3 cardID of the cards in it
     */
    private final HashMap<Color, Integer[]> hands = new HashMap<>();
    private int faceUpGoldCard1;
    private int faceUpGoldCard2;
    private int faceUpResourceCard1;
    private int faceUpResourceCard2;


    /**
     * Create the clients thread base on the connection chosen
     * @param args are the input parameters
     * @throws IOException
     * @throws NotBoundException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, NotBoundException, InterruptedException {
        if(args.length < 2){
            System.out.println("Invalid input, try again: (TCP/RMI) (CLI/GUI)");
            return;
        }
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("ServerConfiguration.json"))));
        JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);
        String ip;

        ClientInterface clientInterface;

        if((args.length == 3) && (args[2] != null)){
            ip = args[2];
        }else{
            ip = jsonObject.get("ip").getAsString();
        }

        if(args[0].equalsIgnoreCase("rmi")){ //equalsIgnoreCase sostituisce toLowerCase e equals insieme
            ClientRMI clientRMI = new ClientRMI(ip, jsonObject.get("RMI").getAsInt());
            clientRMI.start();
            clientInterface = clientRMI;

            ClientInputStreamReader cisr = new ClientInputStreamReader(clientInterface);
            Thread t = new Thread(cisr);
            t.start();
        }
        else if(args[0].equalsIgnoreCase("tcp")){
            CNClient cnClient = new CNClient(ip, jsonObject.get("TCP").getAsInt());
            cnClient.start();
        }else{
            System.out.println("Invalid input, try again: (TCP/RMI) (CLI/GUI)");
            return;
        }

        if (args[1].equalsIgnoreCase("CLI")) {
        }


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
     * method used to get a PlayableCard from its cardID
     * @param id of the PlayableCard wanted
     * @return the PlayableCard wanted
     */
    public PlayableCard getCardFromList(int id) {
        if (id < 41){
            List<ResourceCard> gc = resourceCards.stream().filter(c -> c.getCardID() == id).toList();
            return gc.getFirst();
        }else{
            List<GoldCard> gc = goldCards.stream().filter(c->c.getCardID() == id).toList();
            return gc.getFirst() ;
        }
    }

    private void setCardFacing(int id, boolean facing){
        if (id < 41){
            resourceCards.stream().filter(c -> c.getCardID() == id).forEach(c->c.setFace(facing));
        }else{
            goldCards.stream().filter(c->c.getCardID() == id).forEach(c->c.setFace(facing));
        }
    }

    /**
     * adds the card with the given id at the given coordinates x and y to the given player's cardsOnField
     * @param player index of the player
     * @param cardID the id of the card added
     * @param x coordinate
     * @param y coordinate
     */
    public void addCardToPlayerField(int player, int cardID, int x, int y, boolean facing){
        Coords c = new Coords(x, y);
        setCardFacing(cardID, facing);
        switch (player) {
            case 1 -> {
                cardsOnP1Field.put(c, cardID);
            }
            case 2 -> {
                cardsOnP2Field.put(c, cardID);
            }
            case 3 -> {
                cardsOnP3Field.put(c, cardID);
            }
            case 4 -> {
                cardsOnP4Field.put(c, cardID);
            }
        }
    }

    /**
     * getter for the card with the given coordinates (x and y) and the given player
     * @param player from which field the card is gotten
     * @param x coordinate
     * @param y coordinate
     * @return the card at the given coordinates of the given player's field
     */
    public PlayableCard getCardFromPlayerField(int player, int x, int y){
        Coords c = new Coords(x, y);
        switch (player) {
            case 1 -> {
                return getCardFromList(cardsOnP1Field.get(c));
            }
            case 2 -> {
                return getCardFromList(cardsOnP2Field.get(c));
            }
            case 3 -> {
                return getCardFromList(cardsOnP3Field.get(c));
            }
            case 4 -> {
                return getCardFromList(cardsOnP4Field.get(c));
            }
            default -> {
                return null;
            }
        }
    }
}

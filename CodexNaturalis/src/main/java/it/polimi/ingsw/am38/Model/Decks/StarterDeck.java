package it.polimi.ingsw.am38.Model.Decks;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;

import java.io.InputStreamReader;
import java.util.Objects;

public class StarterDeck extends Deck{
    private StarterCard[] pool;

    public StarterDeck(){
        Gson gson = new Gson();
        // Search file in /src/main/resources/ directory, path is valid for every machine so that there's no need to
        // change this for each PC. Seems to be useful for .jar dependencies too
        JsonReader jsonReader = new JsonReader(new InputStreamReader(Objects.requireNonNull(StarterDeck.class.getClassLoader().getResourceAsStream("starterCard.json"))));
        JsonArray jsonArray = gson.fromJson(jsonReader, JsonArray.class);

        for(int i=0; i<6; i++) {
            JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();  //getting every "card" from the json

            String cardID = jsonObject1.get("cardID").getAsString();
            String imgFront = jsonObject1.get("imgFront").getAsString();
            String imgBack = jsonObject1.get("imgBack").getAsString();

            int ID = Integer.valueOf(cardID);

            JsonObject jsonObject2 = jsonObject1.get("cornerFront").getAsJsonObject();  //creating the obj for cornerFront and getting its info

            String FNW = jsonObject2.get("NW").getAsString();
            String FNE = jsonObject2.get("NE").getAsString();
            String FSW = jsonObject2.get("SW").getAsString();
            String FSE = jsonObject2.get("SE").getAsString();

            JsonObject jsonObject3 = jsonObject1.get("cornerBack").getAsJsonObject();  //same as for corner front

            String BNW = jsonObject3.get("NW").getAsString();
            String BNE = jsonObject3.get("NE").getAsString();
            String BSW = jsonObject3.get("SW").getAsString();
            String BSE = jsonObject3.get("SE").getAsString();//get data from json till here

            JsonObject jsonObject4 = jsonObject1.get("centralResource").getAsJsonObject();  //same as for corner front

            int first = jsonObject4.get("first").getAsInt();
            int second = jsonObject4.get("second").getAsInt();
            int third = jsonObject4.get("third").getAsInt();

            StarterCard starterCard = new StarterCard(ID, imgFront, imgBack, FNW, FNE, FSW, FSE,
                    BNW, BNE, BSW, BSE, first, second, third);  //create the gold card to be inserted in the deck

            this.pool[i] = starterCard;

        }
    }
}

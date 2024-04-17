package it.polimi.ingsw.am38.Model;

import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonTest {

    private GoldCard[] pool;
    public static String getJsonFile(String fileName){
        String jsonText = "";

        try{
            BufferedReader bufferedReader =
                    new BufferedReader(new FileReader(fileName));

            String line;

            while((line = bufferedReader.readLine()) != null){
                jsonText += line + "\n";
            }

            bufferedReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonText;
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        // Search file in /src/main/resources/ directory, path is valid for every machine so that there's no need to
        // change this for each PC. Seems to be useful for .jar dependencies too
        JsonReader jsonReader = new JsonReader(new InputStreamReader(JsonTest.class.getClassLoader().getResourceAsStream("goldCard.json")));
        JsonArray jsonArray = gson.fromJson(jsonReader, JsonArray.class);
        for(int i = 0; i < 40; i++){
            JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();  //getting every "card" from the json

            String cardID = jsonObject1.get("cardID").getAsString();
            String kingdom = jsonObject1.get("kingdom").getAsString();
            String imgFront = jsonObject1.get("imgFront").getAsString();
            String imgBack = jsonObject1.get("imgBack").getAsString();
            String condPointType = jsonObject1.get("conditionPointType").getAsString();
            int pointGiven = jsonObject1.get("pointGiven").getAsInt();

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
            String BSE = jsonObject3.get("SE").getAsString();

            JsonObject jsonObject4 = jsonObject1.get("conditionPlace").getAsJsonObject();  //same as previous

            String first = jsonObject4.get("first").getAsString();
            String second = jsonObject4.get("second").getAsString();
            String third = jsonObject4.get("third").getAsString();
            String fourth = jsonObject4.get("fourth").getAsString();
            String fifth = jsonObject4.get("fifth").getAsString();  //get data from json till here

            //System.out.printf("\n"+cardID+"\n"+FNW+"\n"+third);
            GoldCard goldCard = new GoldCard(ID, kingdom, imgFront, imgBack, condPointType, pointGiven, FNW, FNE, FSW, FSE,
                    BNW, BNE, BSW, BSE, first, second, third, fourth, fifth);  //create the gold card to be inserted in the deck

            //this.pool[i] = goldCard; //not sure of this command

        }
        /*
        JSONObject obj = new JSONObject(strJson);

        JSONObject cornerFront = obj.getJSONObject("cornerFront");  //cornerFront is an object in the json file, so crate a new obj to parse

        String FNW = cornerFront.getString("\"NW\"");
        String FNE = cornerFront.getString("\"NE\"");
        String FSW = cornerFront.getString("\"SW\"");
        String FSE = cornerFront.getString("\"SE\"");
*/
    }
    /*public static void main(String[] args) {
        String jsonString = "{\n" +
                "\t\t\"cardID\": \"081\",\n" +
                "\t\t\"imgFront\": \"null\",\n" +
                "\t\t\"imgBack\": \"null\",\n" +
                "\t\t\"cornerFront\": {\n" +
                "            \"NW\": \"none\",\n" +
                "            \"NE\": \"plant\",\n" +
                "            \"SW\": \"insect\",\n" +
                "            \"SE\": \"none\"\n" +
                "        },\n" +
                "        \"cornerBack\": {\n" +
                "            \"NW\": \"fungi\",\n" +
                "            \"NE\": \"plant\",\n" +
                "            \"SW\": \"insect\",\n" +
                "            \"SE\": \"animal\"\n" +
                "        },\n" +
                "\t\t\"centralResource\": {\n" +
                "\t\t\t\"first\": \"insect\",\n" +
                "\t\t\t\"second\": null,\n" +
                "\t\t\t\"third\": null\n" +
                "\t\t}\n" +
                "\t}";

        JSONObject obj = new JSONObject(jsonString);  //json obj to parse

        System.out.printf(obj.toString(1));

        System.out.printf("\n\n---Parsing---\n\n");

        int id = obj.getInt("cardID");                  //parsing every line
        String imgFront = obj.getString("imgFront");
        String imgBack = obj.getString("imgBack");

        System.out.printf("CardID: "+id+"\nFront Image: "+imgFront+"\nBack Image: "+imgBack);

        JSONObject cornerFront = obj.getJSONObject("cornerFront");  //cornerFront is an object in the json file, so crate a new obj to parse

        String FNW = cornerFront.getString("NW");
        String FNE = cornerFront.getString("NE");
        String FSW = cornerFront.getString("SW");
        String FSE = cornerFront.getString("SE");

        System.out.printf("\n\nFront Corners: \nNW: "+FNW+"\nNE: "+FNE+"\nSW: "+FSW+"\nSE: "+FSE);

        JSONObject cornerBack = obj.getJSONObject("cornerFront");

        String BNW = cornerBack.getString("NW");
        String BNE = cornerBack.getString("NE");
        String BSW = cornerBack.getString("SW");
        String BSE = cornerBack.getString("SE");

        System.out.printf("\n\nBack Corners: \nNW: "+BNW+"\nNE: "+BNE+"\nSW: "+BSW+"\nSE: "+BSE);

    }*/
}
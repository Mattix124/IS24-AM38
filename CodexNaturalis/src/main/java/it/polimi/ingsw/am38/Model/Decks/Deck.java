package it.polimi.ingsw.am38.Model.Decks;

import it.polimi.ingsw.am38.Model.Cards.Card;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;

public abstract class Deck {
    public Boolean isEmpty(){
        return false; //TBD
    }
    public Card draw(){
        return null; //TBD
    }

    public static String getJsonFile(String fileName){  //reading the json file
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
}

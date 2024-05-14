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
import it.polimi.ingsw.am38.Network.Server.Turnings;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serial;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.util.*;

/**
 * This is the main client class that create the clients thread based on the connection chosen
 */
public class CLIENT implements Serializable {
    @Serial
    private static final long serialVersionUID = 749383786771428581L;

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
            clientRMI.addCommandInterpreter(cisr.clientCommandInterpreter);
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
}

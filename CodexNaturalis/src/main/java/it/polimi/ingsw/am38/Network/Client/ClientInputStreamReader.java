package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Server.Turnings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Objects;

/**
 * This class read the command from the command line in the CLI, process and pass them to the ClientRMI
 * in order to send them to the ServerRMI.
 */
public class ClientInputStreamReader implements Runnable, Serializable {
    /**
     * Attribute obligatory for serialization
     */
    private static final long serialVersionUID = 8133446441839369630L;
    /**
     * Nickname
     */
    String nickname = null;
    /**
     * Boolean that control the while inside the reader
     */
    private boolean isRunning;
    /**
     * Instance of ClientInterface
     */
    private ClientInterface clientInterface;
    /**
     *  Game id
     */
    private int gameID;
    /**
     * Instance of ClientCommandInterpreter
     */
    ClientCommandInterpreter clientCommandInterpreter;

    /**
     * Constructor method called for each player only when the player
     * choose the CLI.
     */
    public ClientInputStreamReader(ClientInterface clientInterface){
        this.clientInterface = clientInterface;
        isRunning = false;
        clientCommandInterpreter = new ClientCommandInterpreter(clientInterface);
    }

    /**
     * This method, after have let the player do a first phase (login and join a game or
     * create one) keep read the input and send it to ClientCommandInterpreter that process it
     */
    @Override
    public void run() {
        String i;
        isRunning = true;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        try {
            login(bufferedReader); //player login
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("What do you want to do?\n1) Create a game\n2) Join a game");

        do{
            try {
                i = bufferedReader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(!i.equals("1") && !i.equals("2")) System.out.println("Your input is not valid. Retry:\n1) Create a game\n2)Join a game");

        }while(!i.equals("1") && !i.equals("2"));

        try {
            if(i.equals("1")){

                createGame(bufferedReader);

            }else if(i.equals("2")){

                join(bufferedReader);

            }
        } catch (IOException e) {
            System.out.println("Invalid input");
        }

        System.out.println("\nWaiting for other players...");

        String input;

        while(isRunning){
            try {
                input = bufferedReader.readLine();
                if (input.equals("exit")) isRunning = false;
                clientCommandInterpreter.checkCommand(input);//se è il turno del player leggi e elabora
            } catch (IOException e) {                     //ciò che viene scritto
                System.out.println("Error: invalid input...");
            }

        }
        try {
            bufferedReader.close();
            System.out.println("\nchiusura lettura");
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Login method that allow the user to register
     * @param bufferedReader to read the players command
     * @return the player instance (?)
     */
    public void login(BufferedReader bufferedReader) throws IOException {
        String input;
        do {
            do {
                System.out.println("Insert your username max 15 character:");
                input = bufferedReader.readLine();
            } while (input.length() > 15);

            nickname = clientInterface.login(input);

        }while(nickname == null);


    }

    /**
     * Method that creates the game
     * @param bufferedReader to read the players command
     * @return the gameID of the game created
     */
    public void createGame(BufferedReader bufferedReader) throws IOException {
        System.out.println("To create a game specify the number of players that will participate (from 2 to 4):");
        String input;
        do{
            input = bufferedReader.readLine();
            gameID = clientInterface.createGame(nickname, Integer.parseInt(input), clientInterface); //call the method on the client interface that send the in
        }while(gameID == -1); //check if the game exists

        System.out.println("You created a game successfully, show your GAMEID to your friend to let them join you!\nGAMEID: " + gameID);

    }

    /**
     * Method that allow a player to join a game
     * @param bufferedReader to read the players command
     * @return the evaluation of the method
     */
    public void join(BufferedReader bufferedReader) throws IOException {
        System.out.println("To join a game specify its GameId number:");
        String input;
        Boolean joined;
        do{
            input = bufferedReader.readLine();
            joined = clientInterface.join(nickname, Integer.parseInt(input), clientInterface); //call the method on the client interface that send the info to the server interface
        }while(!joined); //check if the player joined

        System.out.println("You joined a game successfully. Have fun!");
    }
}

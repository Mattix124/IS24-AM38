package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Player;

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

    private static final long serialVersionUID = 8133446441839369630L;

    Player player;
    String nickname = null;
    private boolean isRunning;

    private ClientInterface clientInterface;
    private int gameID;

    ClientCommandInterpreter clientCommandInterpreter;

    /**
     * Constructor method called for each player only when the player
     * choose the CLI.
     */
    public ClientInputStreamReader(ClientInterface clientInterface){
        this.clientInterface = clientInterface;
        isRunning = false;
    }

    /**
     * This method, after have let the player do a first phase (login and join a game or
     * create one) keep read the input and send it to ClientCommandInterpreter that process it
     */
    @Override
    public void run() {
        String i;
        Boolean joined;
        isRunning = true;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        while(player == null) {
            System.out.println("Insert your name: ");
            try {
                i = bufferedReader.readLine();
                player = login(i);
                nickname = player.getNickname();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
                System.out.println("To create a game specify the number of players that will participate (from 2 to 4):");

                do{
                    i = bufferedReader.readLine();
                    gameID = createGame(player, Integer.parseInt(i));
                }while(gameID == -1); //check if the game exists

                System.out.println("You created a game successfully, show your GAMEID to your friend to let them join you!\nGAMEID: " + gameID);

            }else if(i.equals("2")){

                System.out.println("To join a game specify its GameId number:");
                do{
                    i = bufferedReader.readLine();
                    joined = join(nickname, Integer.parseInt(i));
                }while(!joined); //check if the player joined

                System.out.println("You joined a game successfully. Have fun!");

            }
        } catch (IOException e) {
            System.out.println("Invalid input");
        }

        System.out.println("\nWaiting for other players...");

        clientCommandInterpreter = new ClientCommandInterpreter(clientInterface);

        while(isRunning){
            try {
                i = bufferedReader.readLine();
                if (i.equals("exit")) isRunning = false;
                else if (player.getGame().getCurrentPlayer().equals(player)) clientCommandInterpreter.checkCommand(i); //se è il turno del player leggi e elabora
            } catch (IOException e) {                                                                                  //ciò che viene scritto
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

    public Player login(String nickname){
        Player p = null;
        try {
            p = clientInterface.login(nickname);
        } catch (NicknameTakenException e) {
            System.out.println("Nickname already taken, retry:");
        } catch (NullNicknameException e) {
            System.out.println("Nickname not inserted, retry:");
        } catch (RemoteException e) {
            System.out.println("Error: connection with the server lost...");
        }
        return p;
    }

    public int createGame(Player player, int numOfPlayers){
        int gameid = 0;
        try {
            gameid = clientInterface.createGame(player, numOfPlayers); //call the method on the client interface that send the in
        } catch (RemoteException e) {
            System.out.println("Error: connection with the server lost...");
            gameid = -1;
        } catch (NumOfPlayersException e) {
            System.out.println("Your input is not valid. Retry:\nFrom 2 to 4 players.");
            gameid = -1;
        }
        return gameid;
    }

    public boolean join(String nickname, int gameid){
        try {
            clientInterface.join(nickname, gameid); //call the method on the client interface that send the info to the server interface
            return true;
        } catch (RemoteException e) {
            System.out.println("Error: connection with the server lost...");
            return false;
        } catch (NumOfPlayersException e) {
            System.out.println("The game you are trying to connect is full. Retry");
            return false;
        } catch (GameNotFoundException e) {
            System.out.println("Insert the IdGame you or your friend have exposed on the screen. Retry:");
            return false;
        }
    }
}

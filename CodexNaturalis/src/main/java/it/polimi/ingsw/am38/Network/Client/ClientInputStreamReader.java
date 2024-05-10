package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
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

    /**
     * Constructor method called for each player only when the player
     * choose the CLI.
     */
    public ClientInputStreamReader(){
        isRunning = false;
    }

    /**
     * This method keep read the input and send it to the translator that process it
     */
    @Override
    public void run() {
        isRunning = true;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        while(isRunning){
            try {
                String i = bufferedReader.readLine();
                if(i.equals("exit")) isRunning = false;
                translator(i);

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (EmptyDeckException e) {
                throw new RuntimeException(e);
            } catch (GameNotFoundException e) {
                throw new RuntimeException(e);
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
     * This method process the input and sand what's needed to the ClientRMI
     * @param i is the input string
     * @throws IOException
     * @throws EmptyDeckException
     * @throws GameNotFoundException
     */
    public void translator(String i) throws IOException, EmptyDeckException, GameNotFoundException {
        if(!i.contains(">")){
            System.out.println("Invalid command, try again!");
            return;
        }

        String[] input = i.split(">"); //split the command type and the parameters
        String command = input[0];

        if(input.length == 1 && !i.contains("help")){
            System.out.println("Invalid command, try again!");
            return;
        }

        if(i.equalsIgnoreCase("help>")){
            //print possible commands from view
        }

        if(input[1].length() >= 0 && input[1] != null){
            String toDo = input[1]; //here there are the parameters for the command (e.g. the type of card to draw and which one to draw)
            command = command.toLowerCase();
            switch (command){
                case "login" :
                    nickname = toDo;
                    if(nickname.length()>15){
                        System.out.println("It's a NICKNAME, not a poem, please retry...\n");
                        break;
                    }
                    try {
                        this.player = clientInterface.login(nickname); //call the method on the client interface that send the info to the server interface
                    } catch (NicknameTakenException e) {
                        throw new RuntimeException(e);
                    } catch (NullNicknameException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "create game" :
                    if(Objects.equals(nickname, null)){
                        System.out.println("Please login first");
                        return;
                    }
                    try {
                        gameID = clientInterface.createGame(player, Integer.parseInt(toDo)); //call the method on the client interface that send the info to the server interface
                        System.out.println("You created a game successfully, show your GAMEID to your friend to let them join you!\nGAMEID: " + gameID);
                    } catch (NumOfPlayersException e) {
                        System.out.println("Invalid number");
                    }
                    break;

                case "join" :
                    if(!toDo.matches("-?\\d+")){ //if the number is decimal then is not valid
                        System.out.println("Invalid number");
                        return;
                    }
                    if(Objects.equals(nickname, null)){
                        System.out.println("Please login first");
                        return;
                    }
                    try {
                        clientInterface.join(nickname, gameID); //call the method on the client interface that send the info to the server interface
                    } catch (NumOfPlayersException e) {
                        System.out.println("This game is already full of players");
                    } catch (GameNotFoundException e) {
                        System.out.println("Game ID not valid");
                    }
                    break;

                case "play" :
                    String[] play = toDo.split(" "); //split the information in order to process the command
                    try {
                        clientInterface.playACard(Integer.parseInt(play[0]), Integer.parseInt(play[1]), Integer.parseInt(play[2]), play[3], gameID); //call the method on the client interface that send the info to the server interface
                    } catch (NotPlaceableException e) {
                        System.out.println("You can't place a card here");
                    } catch (InvalidInputException e) {
                        System.out.println("You have to choose to play the card face 'up' or face 'down'!");
                    }
                    break;

                case "draw" :
                    if(Objects.equals(nickname, null)){
                        System.out.println("Please login first");
                        return;
                    }
                    String[] draw = toDo.split(" "); //split the information in order to process the command
                    try {
                        clientInterface.draw(player, draw[0], Integer.parseInt(draw[1]), gameID); //call the method on the client interface that send the info to the server interface
                    } catch (InvalidInputException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "show" :
                    break;

                case "chat" :
                    break;

                case "private chat" :
                    break;
            }
        }else System.out.println("Write a command...");
    }

    public void setConnection(ClientInterface clientInterface) {this.clientInterface = clientInterface;}
}

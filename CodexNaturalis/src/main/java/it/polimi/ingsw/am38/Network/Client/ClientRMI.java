package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Server.InterfaceRMI;
import it.polimi.ingsw.am38.Network.Server.Turnings;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This is the class for those clients who decide to connect via RMI
 */
public class ClientRMI extends UnicastRemoteObject implements ClientInterface {

    private Player player;
    private int starterCardID;
    private ArrayList<Integer> pair;
    private String nickname;
    private String ip;
    private int port;
    private Registry reg;
    private InterfaceRMI intRMI;

    ClientCommandInterpreter cmi;

    /**
     * Constructor of the ClientRMI
     * @param ip is the ip needed for the connection
     * @param port is the server port
     * @throws RemoteException
     */
    public ClientRMI(String ip, int port) throws RemoteException {
        this.ip = ip;
        this.port = port;
    }

    public void addCommandInterpreter(ClientCommandInterpreter cmi){
        this.cmi = cmi;
        this.cmi.setTurning(Turnings.STANDBY);
    }

    /**
     * This is the first method called, set the connection with the server
     */
    public void start(){
        try{
            reg = LocateRegistry.getRegistry(ip, port);
            this.intRMI = (InterfaceRMI) reg.lookup("server_RMI");
        }catch(RemoteException | NotBoundException e){
            System.out.println("Server unreachable");
        }
    }

    /**
     * This method communicates to the server the nickname of the player who wants to join a game
     * and the ID of the game
     * @param nickname is the player's nickname
     * @param gameID is the ID of the game that the player wants to join
     * @throws RemoteException
     * @throws NumOfPlayersException
     * @throws GameNotFoundException
     */
    public void join(String nickname, int gameID, ClientInterface ci) throws RemoteException, NumOfPlayersException, GameNotFoundException {
        intRMI.join(nickname, gameID, ci);
    }

    /**
     * This method communicates to the sever the player who wants to create a game and the number of players
     * @param nickname is the player that decide to create the game
     * @param numberOfPlayers is the number of players that the game will have
     * @return
     * @throws RemoteException
     * @throws NumOfPlayersException
     */
    public int createGame(String nickname, int numberOfPlayers, ClientInterface ci) throws RemoteException, NumOfPlayersException {
        int game = this.intRMI.createGame(nickname, numberOfPlayers, ci);
        return game;
    }

    /**
     * This method communicates to the server to create a player with the nickname given
     * @param player is the player nickname
     * @return the player created
     * @throws RemoteException
     */
    public Player login(String player) throws RemoteException{
        try {
            this.player = intRMI.login(player);
            nickname = this.player.getNickname();
        } catch (NicknameTakenException e) {
            throw new RuntimeException(e);
        } catch (NullNicknameException e) {
            throw new RuntimeException(e);
        }
        return this.player;
    }

    /**
     * This method communicates to the server the parameters it needs to perform a draw for the player
     * @param nickname is the player who wants to draw
     * @param cardType is the type of the card that the player wants to draw (i.e. gold or resource)
     * @param card is an integer that allows the controller to know which card draw
     * @throws RemoteException
     * @throws EmptyDeckException
     * @throws GameNotFoundException
     */
    public void draw(String nickname, String cardType, int card) throws RemoteException, InvalidInputException, EmptyDeckException, GameNotFoundException {
        intRMI.draw(nickname, cardType, card);
    }

    /**
     * This method communicates to the server the parameters it needs to play a card for a
     * players in its field
     * @param card is the card to play
     * @param x the x coordinates where to play the card
     * @param y the x coordinates where to play the card
     * @param face is how the card has to be played, face up or face down
     * @param nickname is the nickname of the player who wants to play the card
     * @throws NoPossiblePlacement
     * @throws RemoteException
     */
    public void playACard(int card, int x, int y, boolean face, String nickname) throws NoPossiblePlacement, RemoteException, InvalidInputException, NotPlaceableException {
        intRMI.playACard(card, x, y, face, nickname);
    }

    public void broadcastMessage(String message) throws RemoteException {

    }

    public void privateMessage(String message, String player) throws RemoteException {

    }
    public void getSarterCard(String nickname) throws RemoteException {
        starterCardID = intRMI.getSarterCard(nickname);
    }

    public void chooseFaceStarterCard(String nickname,String face) throws RemoteException {
        intRMI.chooseFaceStarterCard(nickname, face);
    }

    public void chooseColor(String nickname, String color, int gameID) throws RemoteException, ColorTakenException {
        intRMI.chooseColor(nickname, color, gameID);
    }


    public void getObjecgtiveCards(String nickname) throws RemoteException {
        pair = intRMI.getObjecgtiveCards(nickname);
    }


    public void chooseObjectiveCard(String nickname, int choose) throws RemoteException, InvalidInputException {
        intRMI.chooseObjectiveCard(nickname, choose);
    }

    public void startPLay() throws RemoteException {
        cmi.setTurning(Turnings.PLAYPHASE);
    }

    public void startDraw() throws RemoteException {
        cmi.setTurning(Turnings.DRAWPHASE);
    }

    public void setChoosingColorAndFace() throws RemoteException {
        cmi.setTurning(Turnings.CHOOSE1);
        System.out.println("Choose a face for your card (up or down)");
    }

    public void setChoosingObjective() throws RemoteException {
        cmi.setTurning(Turnings.CHOOSE3);
        System.out.println("You have drawn 2 Resource Card, 1 Gold Card, the two common Objective are displayed and you draw two personal Objective, chose one of them:\n (1 or 2)");
    }

    public void showCard(String nickname, int x, int y) throws RemoteException {
        intRMI.showCard(nickname, x, y);
    }

    public void showField() throws RemoteException {

    }

    public void placement() throws RemoteException {

    }

    public void setGameInfo(LinkedList<String> players, int gameID, String nickname)throws RemoteException{
        cmi.setGameID(gameID);
        cmi.setPlayersNicknames(players, nickname);
    }
}

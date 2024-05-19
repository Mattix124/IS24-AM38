package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Exception.*;
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
    public boolean join(String nickname, int gameID, ClientInterface ci) throws RemoteException{
        try {
            intRMI.join(nickname, gameID, ci);
            return true;
        } catch (NumOfPlayersException e) {
            System.out.println("The game you are trying to connect is full. Retry");
        } catch (GameNotFoundException e) {
            System.out.println("Insert the IdGame you or your friend have exposed on the screen. Retry:");
        }
        return false;
    }

    /**
     * This method communicates to the sever the player who wants to create a game and the number of players
     * @param nickname is the player that decide to create the game
     * @param numberOfPlayers is the number of players that the game will have
     * @return
     * @throws RemoteException
     * @throws NumOfPlayersException
     */
    public int createGame(String nickname, int numberOfPlayers, ClientInterface ci) throws RemoteException {
        int game = -1;
        try {
            game = intRMI.createGame(nickname, numberOfPlayers, ci);
            return game;
        } catch (NumOfPlayersException e) {
            System.out.println("Your input is not valid. Retry:\nFrom 2 to 4 players.");
            game = -1;
        }
        return game;
    }

    /**
     * This method communicates to the server to create a player with the nickname given
     *
     * @param player is the player nickname
     * @throws RemoteException
     */
    public String login(String player) throws RemoteException{
        try {
            nickname = intRMI.login(player);
            return nickname;
        } catch (NicknameTakenException e) {
            System.out.println("Nickname already taken, retry:");
        } catch (NullNicknameException e) {
            System.out.println("Nickname not inserted, retry:");
        }
        return null;
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
    public void draw(String nickname, String cardType, int card, int gameID) throws RemoteException, InvalidInputException, EmptyDeckException, GameNotFoundException {
        intRMI.draw(nickname, cardType, card, gameID);
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
    public void playACard(int card, int x, int y, boolean face, String nickname, int gameID) throws NoPossiblePlacement, RemoteException, InvalidInputException, NotPlaceableException {
        intRMI.playACard(card, x, y, face, nickname, gameID);
    }

    public void broadcastMessage(String message) throws RemoteException {

    }

    public void privateMessage(String message, String player) throws RemoteException {

    }
    public void getSarterCard(String nickname, int gameID) throws RemoteException {
        try {
            intRMI.getSarterCard(nickname, gameID);
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setStarterCard(int id)throws RemoteException{
        cmi.getClientData().setStarterCard(id);
        cmi.getCLI().printStarterCardChoice(cmi.getClientData().getStarterCard());
    }

    public void chooseFaceStarterCard(String nickname,String face, int gameID) throws RemoteException {
        intRMI.chooseFaceStarterCard(nickname, face, gameID);
    }

    public void chooseColor(String nickname, String color, int gameID) throws RemoteException {
        intRMI.chooseColor(nickname, color, gameID);
    }

    public void chooseObjectiveCard(String nickname, String choose, int gameID) throws RemoteException {
        intRMI.chooseObjectiveCard(nickname, choose, gameID);
    }

    public void setPhase(Turnings t) throws RemoteException {
        cmi.setTurning(t);
    }

    public void startDraw() throws RemoteException {
        cmi.setTurning(Turnings.DRAWPHASE);
    }

    public void setChoosingObjective(String obj1, String obj2) throws RemoteException {
        cmi.setTurning(Turnings.CHOOSE3);
        System.out.println(obj1);
        System.out.println(obj2);
        //show obj cards
    }

    public void showCard(String nickname, int x, int y, int gameID) throws RemoteException {
        intRMI.showCard(nickname, x, y, gameID);
    }

    public void showField(String nickname, String player, int gameID) throws RemoteException {
        intRMI.showField(nickname, player, gameID);
    }

    public void placement() throws RemoteException {

    }

    public void setGameInfo(LinkedList<String> players, int gameID, String nickname)throws RemoteException{
        cmi.setGameID(gameID);
        cmi.setPlayersNicknames(players, nickname);
    }

    public void printLine(String message){
        System.out.println(message);
    }
}

package it.polimi.ingsw.am38.Network.Client;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Network.Server.InterfaceRMI;
import it.polimi.ingsw.am38.Network.Server.Turnings;

import java.io.BufferedReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This is the class for those clients who decide to connect via RMI
 */
public class ClientRMI extends UnicastRemoteObject implements ClientInterface, CommonClientInterface {

    private String nickname;
    private String ip;
    private int port;
    private Registry reg;
    private InterfaceRMI intRMI;
    private final ClientWriter cw;

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
        cmi = new ClientCommandInterpreter(this);
        cw = new ClientWriter(cmi);
        cmi.getCLI().printTitle();
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
     * This method communicates to the server to create a player with the nickname given
     *
     * @throws RemoteException
     */
    public String getString() throws RemoteException{
        Scanner s = new Scanner(System.in);
        String name = s.nextLine();
        return name;
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
    public void draw(String nickname, String cardType, int card) throws RemoteException {
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
    public void playACard(int card, int x, int y, boolean face, String nickname) throws RemoteException {
        intRMI.playACard(card, x, y, face, nickname);
    }

    public void broadcastMessage(String message) throws RemoteException {

    }

    public void privateMessage(String message, String player) throws RemoteException {

    }

    public void setStarterCards(HashMap<String, Integer> starters, Symbol goldTop, Symbol resourceTop, int[] goldGround, int[] resourceGround)throws RemoteException{
        cw.start();

        cmi.getClientData().setStarterCards(starters);
        cmi.getClientData().setGGround(goldGround);
        cmi.getClientData().setRGround(resourceGround);
        cmi.getClientData().setGTop(goldTop);
        cmi.getClientData().setRTop(resourceTop);
        cmi.getCLI().printStarterCardChoice(cmi.getClientData().getStarterCard(cmi.getClientData().getNickname()),
                cmi.getClientData().getGTop(),
                cmi.getClientData().getRTop(),
                cmi.getClientData().getFaceUpGoldCard1(),
                cmi.getClientData().getFaceUpGoldCard2(),
                cmi.getClientData().getFaceUpResourceCard1(),
                cmi.getClientData().getFaceUpResourceCard2());
    }

    public void chooseFaceStarterCard(String nickname,String face) throws RemoteException {
        intRMI.chooseFaceStarterCard(nickname, face);
    }

    public void chooseColor(String nickname, String color) throws RemoteException {
        intRMI.chooseColor(nickname, color);
    }

    public void chooseObjectiveCard(String nickname, String choose) throws RemoteException {
        intRMI.chooseObjectiveCard(nickname, choose);
    }

    public void setPhase(Turnings t) throws RemoteException {
        cmi.setTurning(t);
    }

    public void setChoosingObjective(String obj1, String obj2) throws RemoteException {
        cmi.setTurning(Turnings.CHOOSE3);
        System.out.println(obj1);
        System.out.println(obj2);
        //show obj cards
    }

    public void showCard(String nickname, int x, int y) throws RemoteException {
        intRMI.showCard(nickname, x, y);
    }

    public void showField(String nickname, String player) throws RemoteException {
        intRMI.showField(nickname, player);
    }

    public void placement() throws RemoteException {

    }

    public void printLine(String message){
        System.out.println(message);
    }

    public void setSort(ClientInterface ci) throws RemoteException {
        intRMI.setSort(ci);
    }

    public void setNickname(String s) throws RemoteException{
        cmi.getClientData().setNickname(s);
    }
}

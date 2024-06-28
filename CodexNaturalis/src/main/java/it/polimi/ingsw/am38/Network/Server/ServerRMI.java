package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Controller.LobbyManager;
import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Board.Coords;
import it.polimi.ingsw.am38.Network.Client.ClientInterface;
import it.polimi.ingsw.am38.Network.Packet.CommunicationClasses.*;
import it.polimi.ingsw.am38.Network.Packet.Message;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static it.polimi.ingsw.am38.Network.Packet.Scope.*;

/**
 * This is the class that represents the server for RMI that communicate with GameThread in order to perform the commands
 */
public class ServerRMI implements InterfaceRMI, Serializable {

    private static final long serialVersionUID = 3708068332600924022L;
    /**
     * Port of the server used to establish a connection with the clients
     */
    private int port;
    /**
     * Registry used to allow the client to find the server and establish a connection
     */
    private Registry reg;
    /**
     * Instance of the LobbyManager
     */
    private final LobbyManager LM = LobbyManager.getLobbyManager();


    /**
     * Constructor of the server RMI
     *
     * @param port is the port on which the connection take life
     * @throws RemoteException
     */
    public ServerRMI(int port) throws RemoteException
    {
        this.port = port;
    }

    /**
     * The start method create the stub for the connection and put it in the registry so the client
     * could look for it and connect
     *
     * @throws RemoteException
     */
    public void start() throws RemoteException{ //crea la stub per la connessione col client
        InterfaceRMI stub = (InterfaceRMI) UnicastRemoteObject.exportObject(this, this.port);
        reg = LocateRegistry.createRegistry(this.port);
        try {
            reg.bind("ServerRMI", stub);
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        System.out.println("Server RMI ready");
    }

    /**
     * Method that create the Message with the information for the draw and send it to the ServerMessageSorter
     *
     * @param nickname is the player who wants to draw
     * @param cardType is the type of the card that the player wants to draw (i.e. gold or resource)
     * @param card is an integer that allows the controller to know which card draw
     * @throws RemoteException -
     */
    public void draw(String nickname, String cardType, int card) throws RemoteException {
        Message m = new Message(GAME, DRAWCARD, nickname, new MDrawCard(cardType, card));
        ServerMessageSorter sms;
        try {
            sms = LM.getGameThread(nickname).getServerMessageSorter();
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
        sms.addMessage(m);
    }

    /**
     * Method that create the Message with the information for the play of a card and send it to the ServerMessageSorter
     *
     * @param card is the card to play
     * @param x the x coordinates where to play the card
     * @param y the x coordinates where to play the card
     * @param face is how the card has to be played, face up or face down
     * @param nickname is the nickname of the player who wants to play the card, used to get the game id
     * @throws RemoteException remote
     */
    public void playACard(int card, int x, int y, boolean face, String nickname) throws RemoteException {
        Message m = new Message(GAME, PLAYCARD, nickname, new MPlayCard(card, new Coords(x, y), face));
        ServerMessageSorter sms;
        try {
            sms = LM.getGameThread(nickname).getServerMessageSorter();
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
        sms.addMessage(m);
    }

    /**
     * Method that create the Message with the chat message from the client and send it to the ServerMessageSorter
     *
     * @param message sent
     * @param nickname of the player who ha sent the chat message
     * @throws RemoteException
     */
    public void broadcastMessage(StringBuilder message, String nickname) throws RemoteException {
        Message mex = new Message(CHAT, BCHAT, nickname, new MSimpleString(message));
        try {
            LM.getGameThread(nickname).getServerMessageSorter().addMessage(mex);
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method that create the Message with the private chat message from the client and send it to the ServerMessageSorter
     *
     * @param message sent
     * @param receiver the player to send the message
     * @param nickname the player who has sent the message
     * @throws RemoteException
     */
    public void privateMessage(StringBuilder message, String receiver, String nickname) throws RemoteException {
        Message mex = new Message(CHAT, PCHAT, nickname, new MPrivateChat(receiver, message));
        try {
            LM.getGameThread(nickname).getServerMessageSorter().addMessage(mex);
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method that create the Message with the info for the choosing of the starter face and send
     * it to the ServerMessageSorter
     *
     * @param nickname of the player
     * @param face chosen
     * @throws RemoteException
     */
    public void chooseFaceStarterCard(String nickname, String face) throws RemoteException {
        Message message = new Message(GAME, STARTINGFACECHOICE, nickname, new MSimpleString(face));
        try {
            LM.getGameThread(nickname).getServerMessageSorter().addMessage(message);
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method that create the Message with the info for the choosing of the color of the pawn send
     * it to the ServerMessageSorter
     *
     * @param nickname of the player
     * @param color chosen
     * @throws RemoteException
     */
    public void chooseColor(String nickname, String color) throws RemoteException {
        Message message = new Message(GAME, COLORCHOICE, nickname, new MSimpleString(color));
        try {
            LM.getGameThread(nickname).getServerMessageSorter().addMessage(message);
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method that create the Message with the info for the choosing of the objective card and send
     * it to the ServerMessageSorter
     *
     * @param nickname
     * @param choose indicates which one of the 2 objective the player has chosen
     * @throws RemoteException
     */
    public void chooseObjectiveCard(String nickname, String choose) throws RemoteException {
        Message message = new Message(GAME, OBJECTIVECHOICE, nickname, new MSimpleString(choose));
        try {
            LM.getGameThread(nickname).getServerMessageSorter().addMessage(message);
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method that create the Message with the info of the ping received and send
     * it to the ServerMessageSorter
     *
     * @param nickname of the player who has sent the ping
     */
    @Override
    public void pingIn(String nickname)
    {
        Message message = new Message(CONNECTION, CONNECTION, nickname, null);
        ServerMessageSorter sms;
        try {
            sms = LM.getGameThread(nickname).getServerMessageSorter();
        } catch (GameNotFoundException e) {
            throw new RuntimeException(e);
        }
        sms.addMessage(message);
    }

    /**
     * Method that create the SortPlayerThread to allow the client to perform a login and create/join
     * a game
     *
     * @param ci the interface of the clientRMI
     * @throws RemoteException
     */
    public void setSort(ClientInterface ci) throws RemoteException {
        Thread spt = new Thread(new SortPlayerThread(ci));
        spt.start();
    }
}

package it.polimi.ingsw.am38.Controller;

import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;
import it.polimi.ingsw.am38.Network.Server.GameThread;
import it.polimi.ingsw.am38.Network.Server.ServerTCP;
import it.polimi.ingsw.am38.Network.Server.ServerRMI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static it.polimi.ingsw.am38.Enum.Color.*;

/**
 * Manages all the Games available and the Players joining them
 */
public class LobbyManager {
    /**
     * The only instance of LobbyManager
     */
    private static LobbyManager lobbyManager;
    /**
     * List of all available Games
     */
    private final ArrayList<Game> games;
    /**
     * List of Players choosing a Game or in a Game (disconnected ones too if their Game isn't over yet)
     */
    private final ArrayList<Player> players;
    /**
     * List of all active controllers, one per game
     */
    private final ArrayList<GameController> gameControllers;
    /**
     * Stores the next available ID for any next Game created, increased by 1 each time a gameController(=Game) is created
     */
    private int nextGameID = 0;

    /**
     * Contains all Thread reference to not allow garbage collector to delete them
     */
    private final ReferenceContainer referenceContainer;
    /**
     * Constructor of the LobbyManager class
     */
    private LobbyManager() {
        games = new ArrayList<>();
        players = new ArrayList<>();
        gameControllers = new ArrayList<>();
        referenceContainer = new ReferenceContainer();
    }
    /**
     * Creates a new Game given the number of Players, inserts it in the games list and creates a GameController,
     * which will also be added to the list of gameControllers, assigns them a new gameID and updates the nextGameID
     * so that there are no different Game instances with the same gameID
     * @param numOfPlayers number of players allowed in this Game (from 2 to 4)
     * @return the id of the game
     * @throws NumOfPlayersException if the numOfPlayers isn't between 2 and 4
     */
    public int createNewGame(int numOfPlayers, Player host) throws NumOfPlayersException{
        if(numOfPlayers<2 || numOfPlayers>4)
            throw new NumOfPlayersException("From 2 to 4 players can participate, try again!");
        Game game = new Game(nextGameID, numOfPlayers, host);
        nextGameID++;
        games.add(game);
        GameController gameController = new GameController(this, game);
        gameControllers.add(gameController);
        return game.getGameID();
    }

    /**
     * Create a new player with given nickname if none is already using that name, unless a disconnected player (one
     * present in the players arraylist which isn't playing, but has a color assigned to him) had that name, in which
     * case he reconnects to the game
     * @param nickname chosen by the Player
     * @return the initialized Player (or his existing instance if a reconnection occurs)
     * @throws NicknameTakenException when the nickname has been taken
     * @throws NullNicknameException  when the name is null or empty
     */
    public Player createPlayer(String nickname) throws NicknameTakenException, NullNicknameException {
        Player player;
        if(nickname.isEmpty())
            throw new NullNicknameException("");
        if (players.stream()
                .map(Player::getNickname)
                .noneMatch(name -> name.equals(nickname))) {
            //if there's no Player with this nickname
            player = new Player(nickname);
            players.add(player);
        } else {
                //if there already is a Player with this nickname
            if ((!players.get(players.indexOf(players
                    .stream().filter(u -> Objects.equals(u.getNickname(), nickname))
                    .toList().getFirst())).isPlaying())
                    && (players.get(players.indexOf(players
                    .stream().filter(u -> Objects.equals(u.getNickname(), nickname))
                    .toList().getFirst())).getGame() != null)){
                //if the Player isn't playing a game and if he has a color assigned to him
                player = players.get(players.indexOf(players
                        .stream().filter(u -> Objects.equals(u.getNickname(), nickname))
                        .toList().getFirst()));
                player.setIsPlaying(true);
            }else{
                throw new NicknameTakenException("This nickname is taken, try with a different one!");
            }
        }
        return player;
    }

    /**
     * Method that allows the Player p to join the Game identified by the gameID given
     * @param p Player that joins
     * @param gameID of the Game the Player p tries to join
     * @throws NumOfPlayersException when the Game already reached the set number of players needed
     */
    public void joinGame(int gameID ,Player p) throws NumOfPlayersException, GameNotFoundException {
        System.out.println("joinato");
        this.getGame(gameID).addPlayer(p);
        if(this.getGame(gameID).getPlayers().size() == this.getGame(gameID).getNumPlayers())
            this.getGame(gameID).gameStartConstructor();
    }

    //---------------------------------------------------------------------------------------PROTECTED

    /**
     * Method used to end a Game (and his controller), this method also removes all Players that were
     * playing that Game from the list of all players in the Server (this.players)
     * @param game to end
     */
    void endAGame(Game game){
        this.players.removeAll(game.getPlayers());
        this.games.remove(game);
        this.gameControllers.remove(getGameController(game.getGameID()));
    }

    //----------------------------------------------------------------------------------------SETTERS

    /**
     * Setter method for serverRMI
     * @param srmi serverRMI
     */
    public void setServerRMI(ServerRMI srmi){this.referenceContainer.add(srmi);}

    /**
     * Setter method for serverTCP
     * @param stcp serverTCP
     */
    public void setServerTCP(ServerTCP stcp){this.referenceContainer.add(stcp);}

    /**
     * Setter method for GameThread
     * @param gameThread
     */
    public void addGameThread(GameThread gameThread) {this.referenceContainer.add(gameThread);}
    //----------------------------------------------------------------------------------------GETTERS

    /**
     * Using a Singleton design pattern to simplify some other classes methods (networking)
     * @return the only instance of LobbyManager
     */
    public static LobbyManager getLobbyManager() {
        if(lobbyManager == null)
            lobbyManager = new LobbyManager();
        return lobbyManager;
    }

    /**
     * Getter for the GameController of the Game which ID is the parameter gameID.
     * @param gameID ID of the Game managed by the GameController we want to get
     * @return the GameController that manages the Game with gameID as his ID
     */
    public GameController getGameController(int gameID){
        GameController gc = null;
        for(GameController gameController:gameControllers)
            if(gameController.getGame().getGameID() == gameID) {
                gc = gameController;
            }
        return gc;
    }

    /**
     * Getter of Game from his gameID
     * @param hisGameID identifier of the Game it returns
     * @return the Game which ID is gameID
     * @throws GameNotFoundException if there's no game with the given gameID
     */
    public Game getGame(int hisGameID) throws GameNotFoundException {
        List<Game> gs = this.games.stream()
                .filter(g -> g.getGameID() == hisGameID)
                .toList();
        if(!gs.isEmpty())
            return gs.getFirst();
        else throw new GameNotFoundException("There are no games with the given ID, try a different one!");
    }

    /**
     * Getter method for a Player with the given nickname
     * @param nickname of the Player requested
     * @return the Player with the nickname given
     */
    public Player getPlayer(String nickname){
        for(Player p: players){
            if(p.getNickname().equalsIgnoreCase(nickname)) return p;
        }
        return null;
    }

    /**
     * Getter of GameThread list
     * @return the GameThread List
     */
    public LinkedList <GameThread> getGameThreadList()
    {
        return referenceContainer.getGameTreadList();
    }

    /**
     * Getter for a certain gameThread
     * @param nickname of the player whose gameThread wants to be returned
     * @return the game thread associated to the player
     * @throws GameNotFoundException
     */
    public GameThread getGameThread(String nickname) throws GameNotFoundException {
        for(GameThread gt : referenceContainer.getGameTreadList()) {
            ArrayList<Player> pl = gt.getGame().getPlayers();
            for(Player p : pl)
                if(p.getNickname().equals(nickname))
                    return gt;
        }
                throw new GameNotFoundException("No game with this player");
    }

    /**
     * Getter of the ReferenceContainer
     * @return the reference container
     */
    public ReferenceContainer getReferenceContainer(){
        return referenceContainer;
    }
}
package it.polimi.ingsw.am38.Controller;

import it.polimi.ingsw.am38.Exception.*;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static it.polimi.ingsw.am38.Enum.Color.*;

/**
 * Manages all the Games available and the Players joining them.
 */
public class LobbyManager {
    /**
     * List of all available Games.
     */
    private static LobbyManager lobbyManager;
    private final ArrayList<Game> games;
    /**
     * List of Players choosing a Game or in a Game (disconnected ones too if their Game isn't over yet).
     */
    private final ArrayList<Player> players;
    /**
     * List of all active controllers, one per game.
     */
    private final ArrayList<GameController> gameControllers;
    /**
     * Attribute used to manage each player when they join/reconnect to a game.
     */
    private Player player;
    /**
     * Stores the next available ID for any next Game created, increased by 1 each time a
     * gameController(=Game) is created.
     */
    private int nextGameID = 0;

    /**
     * Constructor of the LobbyManager class.
     */
    private LobbyManager() {
        games = new ArrayList<>();
        players = new ArrayList<>();
        gameControllers = new ArrayList<>();
    }

    /**
     * Using a Singleton design pattern to simplify some other classes methods (networking)
     * @return The only instance of LobbyManager.
     */
    public static LobbyManager getLobbyManager()
    {
        if(lobbyManager == null)
            lobbyManager = new LobbyManager();
        return lobbyManager;
    }
    /**
     * Creates a new Game given the number of Players, inserts it in the games list and creates a GameController,
     * which will also be added to the list of ameControllers, assigns them a new gameID and updates the nextGameID
     * so that there are no different Game instances with the same gameID.
     * @param numOfPlayers number of players allowed in this Game (from 2 to 4)
     * @throws NumOfPlayersException if the numOfPlayers isn't between 2 and 4
     */
    public void createNewGame(int numOfPlayers, Player host) throws NumOfPlayersException{
        if(numOfPlayers<2 || numOfPlayers>4)
            throw new NumOfPlayersException("From 2 to 4 players can participate, try again!");
        Game game = new Game(nextGameID, numOfPlayers, host);
        games.add(game);
        GameController gameController = new GameController(this, nextGameID, numOfPlayers, host);
        gameControllers.add(gameController);
        nextGameID++;
    }

    /**
     * Create a new player with given nickname if none is already using that name, unless a disconnected player (one
     * present in the players arraylist which isn't playing, but has a color assigned to him) had that name, in which
     * case he reconnects to the game.
     * @param nickname chosen by the Player
     * @return the initialized Player (or his existing instance if a reconnection occurs)
     * @throws NicknameTakenException when the nickname has been taken
     * @throws NullNicknameException  when the name is null or empty
     */
    public Player createPlayer(String nickname) throws NicknameTakenException, NullNicknameException {
        if (nickname == null || nickname.isEmpty())
            throw new NullNicknameException("nickname needed");
        else {
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
                        .toList().getFirst())).getColor() != NONE)){
                    //if the Player isn't playing a game and if he has a color assigned to him
                    player = players.get(players.indexOf(players
                            .stream().filter(u -> Objects.equals(u.getNickname(), nickname))
                            .toList().getFirst()));
                }else{
                    throw new NicknameTakenException("This nickname is taken, try with a different one!");
                }
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
    public void joinGame(int gameID ,Player p) throws NumOfPlayersException, EmptyDeckException {
        this.getGame(gameID).addPlayer(p);
        if(this.getGame(gameID).getPlayers().size() == this.getGame(gameID).getNumPlayers())
            this.getGame(gameID).gameStartConstructor();
    }

    //---------------------------------------------------------------------------------------PROTECTED

    /**
     * Method used to end a Game (and his controller) given his gameID, this method also removes all
     * Players that were playing that Game from the list of all players in the Server (this.players).
     * @param gameID of the Game to end
     * @throws GameNotFoundException if the given gameID isn't referring to any existing Game
     */
    void endAGame(int gameID) throws GameNotFoundException {
        this.players.removeAll(this.getGame(gameID).getPlayers());
        this.games.remove(getGame(gameID));
        this.gameControllers.remove(getGameController(gameID));
    }

    //----------------------------------------------------------------------------------------GETTERS

    /**
     * Getter for the GameController of the Game which ID is the parameter gameID.
     * @param gameID ID of the Game managed by the GameController we want to get
     * @return the GameController that manages the Game with gameID as his ID
     * @throws GameNotFoundException if there's no active Game with the given gameID
     */
    private GameController getGameController(int gameID) throws GameNotFoundException{
        for(GameController gameController:gameControllers)
            if(gameController.getGame().getGameID() == gameID)
                return gameController;
        throw new GameNotFoundException("game" + gameID + "not found");
    }

    /**
     * Getter of Game from his gameID.
     * @param hisGameID identifier of the Game it returns
     * @return the Game which ID is gameID
     */
    public Game getGame(int hisGameID) {
        List<Game> gs = this.games.stream()
                .filter(g -> g.getGameID() == hisGameID)
                .toList();
        return gs.getFirst();
    }
}
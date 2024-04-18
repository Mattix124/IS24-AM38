package it.polimi.ingsw.am38.Controller;

import it.polimi.ingsw.am38.Exception.GameNotFoundException;
import it.polimi.ingsw.am38.Exception.NullNicknameException;
import it.polimi.ingsw.am38.Exception.NumOfPlayersException;
import it.polimi.ingsw.am38.Exception.TakenNicknameException;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Model.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * where all clients will be before joining/ending a Game
 * manages all the Games available and the Players joining them
 */
public class LobbyManager {
    /**
     * list of all available Games
     */
    private final ArrayList<Game> games;
    /**
     * list of all online Players
     */
    private final ArrayList<Player> players;
    /**
     * list of all active controllers, one per game
     */
    private final ArrayList<GameController> gameControllers;
    /**
     * attribute used to manage each player when they join/reconnect to a game
     */
    private Player player;
    /**
     * stores the next available ID for any next Game created, increased by 1 each time a
     * gameController(=Game) is created
     */
    private int nextGameID = 0;

    /**
     * constructor of the LobbyManager class
     */
    public LobbyManager() {
        games = new ArrayList<>();
        players = new ArrayList<>();
        gameControllers = new ArrayList<>();
    }

    /**
     * creates a new Game given the number of Players, inserts it in the games list and creates a GameController,
     * which will also be added to the list of ameControllers, assigns them a new gameID and updates the nextGameID
     * so that there are no different Game instances with the same gameID
     * @param numOfPlayers number of players allowed in this Game (from 2 to 4)
     * @throws NumOfPlayersException if the numOfPlayers isn't between 2 and 4
     */
    public void createNewGame(int numOfPlayers) throws NumOfPlayersException{
        if(numOfPlayers<2 || numOfPlayers>4)
            throw new NumOfPlayersException("From 2 to 4 players can participate, try again!");
        Game game = new Game(nextGameID, numOfPlayers);
        games.add(game);
        GameController gameController = new GameController(nextGameID, numOfPlayers);
        gameControllers.add(gameController);
        nextGameID++;
    }
    /*public Game getGame(int gameID) throws GameNotFoundException{
        for(Game game:games)
            if(game.getGameID() == gameID)
                return game;
        throw new GameNotFoundException("game" + gameID + "not found");
    }*/

    /**
     * getter for the GameController of the Game which ID is the parameter gameID
     * @param gameID ID of the Game managed by the GameController we want to get
     * @return the GameController that manages the Game with gameID as his ID
     * @throws GameNotFoundException if there's no active Game with the given gameID
     */
    public GameController getGameController(int gameID) throws GameNotFoundException{
        for(GameController gameController:gameControllers)
            if(gameController.getGame().getGameID() == gameID)
                return gameController;
        throw new GameNotFoundException("game" + gameID + "not found");
    }

    /**
     * create a new player with given nickname if none is already using that name and if no disconnected
     * players had that name
     * @param nickname chosen by the Player
     * @return the initialized player
     * @throws TakenNicknameException when the nickname has been taken
     * @throws NullNicknameException  when the name is null or empty
     */
    public Player createPlayer(String nickname) throws TakenNicknameException, NullNicknameException {
        if (nickname == null || nickname.isEmpty())
            throw new NullNicknameException("nickname needed");
        else {
            if (players.stream()
                    .map(Player::getNickname)
                    .noneMatch(name -> name.equals(nickname))) {
                player = new Player(nickname);
                players.add(player);
            } else {
                if (!players.get(players.indexOf(players
                        .stream().filter(u -> Objects.equals(u.getNickname(), nickname))
                        .toList().getFirst())).getIsPlaying()) {
                    player = new Player(nickname);
                    //riconnessione alla partita
                }
            }


        }
        return player;
    }

    /**
     * getter for the nextGameID attribute
     * @return nextGameID
     */
    public int getNextGameID() {
        return nextGameID;
    }
}
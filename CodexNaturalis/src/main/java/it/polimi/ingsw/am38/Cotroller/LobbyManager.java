package it.polimi.ingsw.am38.Cotroller;

import it.polimi.ingsw.am38.Exception.NullNicknameException;
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
    private final ArrayList<GameController> gameControllers;
    private Player player;

    public LobbyManager() {
        games = new ArrayList<>();
        players = new ArrayList<>();
        gameControllers = new ArrayList<>();
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
                        .collect(Collectors.toList()).get(0))).getIsPlaying()) {
                    player = new Player(nickname);
                    //riconnessione alla partita
                }
            }


        }
        return player;
    }
}
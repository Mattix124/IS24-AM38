package it.polimi.ingsw.am38.Model;

import java.util.ArrayList;
import java.util.Random;

public class Game{
	/**
	 * array of Players taking part in this Game
	 */
	private ArrayList<Player> players;
	/**
	 * used to save the index referring to the starting Player
	 */
	private final int startingPlayer; //It counts the index of "players" array to set the turn, it increases its value by 1 every time.
	private int gameID;
	/**
	 * number of players allowed in this Game (chosen by the Player creating the Game)
	 */
	private final int numPlayers;

    public Game(int gameID, int numPlayers, Player host) {
        startingPlayer = (int)(Math.random() * 4);
		this.gameID = gameID;
        this.numPlayers = numPlayers;
    }


    public void addPlayer()
	{
		return; //How many players will be added? Client server communication needed.
	}

	public int getGameID() {
		return gameID;
	}
}

package it.polimi.ingsw.am38.View.GuiSupporDataClasses;

/**
 * Class that contains the score of the player
 */
public class ScorePlayers
{
	/**
	 * Score of the player
	 */
	private int score;
	/**
	 * Nickname of the player
	 */
	private String nick;

	/**
	 * Constructor method that set the score and the nickname
	 *
	 * @param score
	 * @param nick
	 */
	public ScorePlayers(int score, String nick)
	{
		this.score = score;
		this.nick = nick;
	}

	/**
	 * Getter for the attribute score
	 *
	 * @return score
	 */
	public int getScore()
	{
		return score;
	}

	/**
	 * Getter for the attribute nick
	 *
	 * @return nick
	 */
	public String getNick()
	{
		return nick;
	}
}

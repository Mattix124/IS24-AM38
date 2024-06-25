package it.polimi.ingsw.am38.View.GuiSupporDataClasses;

public class ScorePlayers
{
	private int score;
	private String nick;

	public ScorePlayers(int score, String nick)
	{
		this.score = score;
		this.nick = nick;
	}

	public int getScore()
	{
		return score;
	}

	public String getNick()
	{
		return nick;
	}
}

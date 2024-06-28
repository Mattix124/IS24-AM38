package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Model.Board.VisibleElements;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class that allows the client to place the card in the spot chosen and update the score
 */
public class MConfirmedPlacement extends MessageContent implements Serializable
{
	@Serial
	private static final long serialVersionUID = 698755425L;
	/**
	 * Id of the card placed
	 */
	private final int id;
	/**
	 * x position of the card
	 */
	private final int x;
	/**
	 * y position of the card
	 */
	private final int y;
	/**
	 * Points of the player
	 */
	private final int points;
	/**
	 * Face of the card placed
	 */
	private final boolean face;
	/**
	 * Symbol's tab update
	 */
	private final VisibleElements visibleElements;
	/**
	 * Nickname of the playing player
	 */
	private final String nickname;

	/**
	 * Constructor of MConfirmedPlacement
	 * @param s nickname
	 * @param id card id
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param face card face
	 * @param points points
	 * @param visibleElements symbol's tab
	 */
	public MConfirmedPlacement(String s,int id, int x, int y, boolean face, int points, VisibleElements visibleElements)
	{
		this.nickname = s;
		this.id = id;
		this.x = x;
		this.y = y;
		this.face = face;
		this.points = points;
		this.visibleElements = visibleElements;
	}

	/**
	 * Getter of the id
	 * @return id
	 */
	public int getId()
	{
		return id;
	}
	/**
	 * Getter of the x coordinate
	 * @return x
	 */
	public int getX()
	{
		return x;
	}
	/**
	 * Getter of the y coordinate
	 * @return y
	 */
	public int getY()
	{
		return y;
	}
	/**
	 * Getter of the face
	 * @return face
	 */
	public boolean isFace()
	{
		return face;
	}
	/**
	 * Getter of the id
	 * @return points
	 */
	public int getPoints()
	{
		return points;
	}
	/**
	 * Getter of the nickname
	 * @return nickname
	 */
	public String getNickname()
	{
		return nickname;
	}
	/**
	 * Getter of the symbol's tab
	 * @return symbol's tab
	 */
	public VisibleElements getVisibleElements()
	{
		return visibleElements;
	}
}

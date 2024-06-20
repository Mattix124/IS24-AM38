package it.polimi.ingsw.am38.Network.Packet;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class that allow the separation of every task both Server and Client side
 */
public class Header implements Serializable
{
	@Serial
	private static final long serialVersionUID = 16668714768L;

	/**
	 * Header 1: it can contain only the Scopes GAME,VIEWUPDATE,CHAT and KILL
	 */
	private final Scope header1;
	/**
	 * It contains all the specified Scopes
	 */
	private final Scope header2;
	/**
	 * Specifies (only on client side) the sender of the message
	 */
	private final String sender;

	/**
	 * Header constructor
	 *
	 * @param header1 Scope 1
	 * @param header2 Scope 2
	 * @param sender  nickname
	 */
	public Header(Scope header1, Scope header2, String sender)
	{
		this.header1 = header1;
		this.header2 = header2;
		this.sender = sender;
	}

	/**
	 * Getter for getHeader1
	 * @return Header1
	 */
	public Scope getHeader1()
	{
		return header1;
	}

	/**
	 * Getter for getHeader2
	 * @return Header2
	 */
	public Scope getHeader2()
	{
		return header2;
	}

	/**
	 * Getter for getSender
	 * @return Sender
	 */
	public String getSender()
	{
		return sender;
	}
}

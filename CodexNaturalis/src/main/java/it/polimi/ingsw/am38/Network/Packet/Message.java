package it.polimi.ingsw.am38.Network.Packet;

import java.io.Serializable;

/**
 * Message is a class that can be exchanged between clients servers. (Server side it also used to unified rmi and tcp answers)
 */
public class Message implements Serializable
{
	private static final long serialVersionUID = 1754354768L;
	/**
	 * Header that specifies what the message bring
	 */
	private Header header;
	/**
	 * The content needed
	 */
	private MessageContent content;

	/**
	 * Constructor of message Client side
	 * @param h main header
	 * @param h2 task
	 * @param sender sender
	 * @param c content
	 */
	public Message(Scope h, Scope h2, String sender, MessageContent c)
	{
		this.header = new Header(h, h2, sender);
		this.content = c;
	}
	/**
	 * Constructor of message Server side
	 * @param h main header
	 * @param h2 task
	 * @param c content
	 */
	public Message(Scope h, Scope h2, MessageContent c)
	{
		this.header = new Header(h, h2, null);
		this.content = c;
	}

	/**
	 * Get the main header
	 * @return header
	 */
	public Scope getHeader1()
	{
		return header.getHeader1();
	}
	/**
	 * Get the task header
	 * @return header
	 */
	public Scope getHeader2()
	{
		return header.getHeader2();
	}
	/**
	 * Get the sender
	 * @return sender
	 */
	public String getSender()
	{
		return header.getSender();
	}
	/**
	 * Get the content
	 * @return content
	 */
	public Object getContent()
	{
		return content;
	}
}

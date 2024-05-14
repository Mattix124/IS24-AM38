package it.polimi.ingsw.am38.Network.Packet;

import java.io.Serializable;

public class Message implements Serializable
{
	private static final long serialVersionUID = 175434768L;

	private Header header;
	private MessageContent content;

	public Message(Scope h, Scope h2, String sender, MessageContent c)
	{
		this.header = new Header(h, h2, sender);
		this.content = c;
	}

	public Message(Scope h, Scope h2, MessageContent c)
	{
		this.header = new Header(h, h2, null);
		this.content = c;
	}

	public Scope getHeader1()
	{
		return header.getHeader1();
	}

	public Scope getHeader2()
	{
		return header.getHeader2();
	}

	public String getSender()
	{
		return header.getSender();
	}

	public Object getContent()
	{
		return content;
	}
}

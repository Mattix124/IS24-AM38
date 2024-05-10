package it.polimi.ingsw.am38.Network.Packet;

import java.io.Serializable;

public class Header implements Serializable
{
	private static final long serialVersionUID = 1L;
	private final Scope header1;
	private final Scope header2;
	private final String sender;

	public Header(Scope header1, Scope header2, String sender)
	{
		this.header1 = header1;
		this.header2 = header2;
		this.sender = sender;
	}


	public Scope getHeader1()
	{
		return header1;
	}

	public Scope getHeader2()
	{
		return header2;
	}

	public String getSender()
	{
		return sender;
	}
}

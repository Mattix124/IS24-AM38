package it.polimi.ingsw.am38.Network.Packet;

public class Message
{
	private MessageHeader header;
	private MessageHeader header2;
	private Object content;

	public Message(MessageHeader h, MessageHeader h2, Object c)
	{
		this.header = h;
		this.header2 = h2;
		this.content = c;
	}

	public MessageHeader getHeader1()
	{
		return header;
	}

	public MessageHeader getHeader2()
	{
		return header2;
	}

	public Object getContent()
	{
		return content;
	}
}

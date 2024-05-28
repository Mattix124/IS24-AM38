package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class that allow the sending of private message inside Message Class
 */
public class MPrivateChat extends MessageContent implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1756574768L;
	/**
	 * Receiver of the direct message
	 */
	private String receiver;
	/**
	 * Content of the direct message
	 */
	private String message;

	/**
	 * Constructor of MPrivateChat
	 *
	 * @param receiver receiver
	 * @param message  content
	 */
	public MPrivateChat(String receiver, String message)
	{
		this.receiver = receiver;
		this.message = message;
	}

	/**
	 * Alternative constructor using StringBuilder
	 *
	 * @param receiver receiver
	 * @param message  content
	 */
	public MPrivateChat(String receiver, StringBuilder message)
	{
		this.receiver = receiver;
		this.message = message.toString();
	}

	/**
	 * Getter of the Receiver
	 * @return receiver
	 */
	public String getReceiver()
	{
		return receiver;
	}
	/**
	 * Getter of the Content
	 * @return content
	 */
	public String getText()
	{
		return message;
	}
}

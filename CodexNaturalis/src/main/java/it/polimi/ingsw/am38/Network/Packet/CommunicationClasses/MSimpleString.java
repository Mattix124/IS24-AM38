package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.io.Serial;
import java.io.Serializable;
/**
 * Class that allow the sending of String inside Message Class
 */
public class MSimpleString extends MessageContent implements Serializable
{
	@Serial
	private static final long serialVersionUID = 5464657768L;

	/**
	 * The string passed
	 */
	private final String string;

	/**
	 * Constructor of MSimpleString
	 * @param string the string passed
	 */
	public MSimpleString(String string)
	{
		this.string = string;
	}

	/**
	 * Alternative Constructor of MSimpleString
	 * @param sb message
	 */
	public MSimpleString(StringBuilder sb)
	{
		string = sb.toString();
	}

	/**
	 * Get the string passed
	 * @return the string passed
	 */
	public String getText()
	{
		return string;
	}
}

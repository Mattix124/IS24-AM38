package it.polimi.ingsw.am38.Network.Packet;

import java.io.Serializable;

/**
 * MessageContent is an abstract class that uses polymorphism to send Message. All the Content are child of MessageContent
 */
public abstract class MessageContent implements Serializable
{
	private static final long serialVersionUID = 4574668775354L;
}

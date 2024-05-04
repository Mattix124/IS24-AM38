package it.polimi.ingsw.am38.Network.TCP.Server;

import it.polimi.ingsw.am38.Model.Player;

import java.io.PrintWriter;
import java.util.HashMap;

public class ChatThread extends Thread
{
	private final MessageInterpreterServer messageInterpreter;
	private final HashMap <Player, PrintWriter> communicationMap;

	public ChatThread(HashMap <Player, PrintWriter> communicationMap, MessageInterpreterServer msgInterpreter)
	{
		this.communicationMap = communicationMap;
		this.messageInterpreter = msgInterpreter;
	}

	public void run()
	{
		String message;
		while (true)
		{
			message = getMessage();

			if (message.regionMatches(0, "b/", 0, 1))
			{
				for (Player p : communicationMap.keySet())
				{
					if (!message.regionMatches(2, p.getNickname(), 0, p.getNickname().length()))
					{
						communicationMap.get(p).println(message);
					}
				}
			}
			else
			{
				for (Player p : communicationMap.keySet())
				{
					if (message.regionMatches(2, p.getNickname(), 0, p.getNickname().length()))
					{
						communicationMap.get(p).println(message);
					}
				}
			}
		}

	}

	private String getMessage()
	{
		return messageInterpreter.getChatMessage();
	}
}

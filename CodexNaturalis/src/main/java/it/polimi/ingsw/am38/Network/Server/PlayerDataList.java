package it.polimi.ingsw.am38.Network.Server;

import it.polimi.ingsw.am38.Model.Player;

import java.util.LinkedList;

public class PlayerDataList extends LinkedList <PlayerData>
{

	public PlayerData get(Player player)
	{
		for(PlayerData playerData : this)
		{
			if(playerData.getPlayer().equals(player))
				return playerData;
		}
		return null;
	}
}

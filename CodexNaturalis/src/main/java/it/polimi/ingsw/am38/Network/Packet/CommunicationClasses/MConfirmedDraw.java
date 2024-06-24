package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.io.Serial;
import java.io.Serializable;

import static it.polimi.ingsw.am38.Enum.Symbol.FUNGI;
import static it.polimi.ingsw.am38.Enum.Symbol.NULL;

public class MConfirmedDraw extends MessageContent implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1754654354768L;
	private final int cardDrawnId;
	private final Symbol goldTopCardSymbol, resourceTopCardSymbol;
	private String[] playerHandCardColors;
	private String nickname;
	private final int goldFaceUp1Id, goldFaceUp2Id, resourceFaceUp1Id, resourceFaceUp2Id;

	public MConfirmedDraw(GameController gameController)
	{
		Game game = gameController.getGame();
		this.resourceFaceUp1Id = game.getResourceDeck().getGround0().getCardID();
		this.resourceFaceUp2Id = game.getResourceDeck().getGround1().getCardID();
		this.goldFaceUp1Id = game.getGoldDeck().getGround0().getCardID();
		this.goldFaceUp2Id = game.getGoldDeck().getGround1().getCardID();
		this.resourceTopCardSymbol = game.getResourceDeck().getTopCardKingdom();
		this.goldTopCardSymbol = game.getGoldDeck().getTopCardKingdom();
		this.cardDrawnId = gameController.getCardDrawn().getCardID();
		this.playerHandCardColors = null;

	}

	public MConfirmedDraw(GameController gameController,String[] phcc)
	{
		this.nickname = gameController.getGame().getCurrentPlayer().getNickname();
		Game game = gameController.getGame();
		this.resourceFaceUp1Id = game.getResourceDeck().getGround0().getCardID();
		this.resourceFaceUp2Id = game.getResourceDeck().getGround1().getCardID();
		this.goldFaceUp1Id = game.getGoldDeck().getGround0().getCardID();
		this.goldFaceUp2Id = game.getGoldDeck().getGround1().getCardID();
		this.resourceTopCardSymbol = game.getResourceDeck().getTopCardKingdom();
		this.goldTopCardSymbol = game.getGoldDeck().getTopCardKingdom();
		this.cardDrawnId = 0;
		this.playerHandCardColors = phcc;

	}

	public String getNickname(){
		return this.nickname;
	}
	public int getCardDrawnId()
	{
		return cardDrawnId;
	}

	public Symbol getGoldTopCardSymbol()
	{
		return goldTopCardSymbol;
	}

	public Symbol getResourceTopCardSymbol()
	{
		return resourceTopCardSymbol;
	}

	public int getGoldFaceUp1Id()
	{
		return goldFaceUp1Id;
	}

	public int getGoldFaceUp2Id()
	{
		return goldFaceUp2Id;
	}

	public int getResourceFaceUp1Id()
	{
		return resourceFaceUp1Id;
	}

	public int getResourceFaceUp2Id()
	{
		return resourceFaceUp2Id;
	}
	public String[] getPlayerHandCardColors(){
		return this.playerHandCardColors;
	}
}

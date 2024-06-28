package it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;

import it.polimi.ingsw.am38.Controller.GameController;
import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Game;
import it.polimi.ingsw.am38.Network.Packet.MessageContent;

import java.io.Serial;
import java.io.Serializable;

import static it.polimi.ingsw.am38.Enum.Symbol.FUNGI;
import static it.polimi.ingsw.am38.Enum.Symbol.NULL;

/**
 * TCP classes to help send the information of a drawing update
 */
public class MConfirmedDraw extends MessageContent implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1754654354768L;
	/**
	 * ID of the card drawn
	 */
	private final int cardDrawnId;
	/**
	 * Symbols that contains the information of the top cards of the decks
	 */
	private final Symbol goldTopCardSymbol, resourceTopCardSymbol;
	/**
	 *  Contains the player's hand to update the view
	 */
	private final String[] playerHandCardColors;
	/**
	 * String that contains the nikname of the playing player.
	 */
	private String nickname;
	/**
	 * id of the ground cards
	 */
	private final int goldFaceUp1Id, goldFaceUp2Id, resourceFaceUp1Id, resourceFaceUp2Id;

	/**
	 * Constructor of MConfirmed
	 *
	 * @param gameController
	 */
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

	/**
	 * Getter for nickname
	 * @return nickname
	 */
	public String getNickname(){
		return this.nickname;
	}
	/**
	 * Getter for drawnId
	 * @return id
	 */
	public int getCardDrawnId()
	{
		return cardDrawnId;
	}

	/**
	 * Getter for the gold top symbol
	 * @return symbol
	 */
	public Symbol getGoldTopCardSymbol()
	{
		return goldTopCardSymbol;
	}
	/**
	 * Getter for the resource top symbol
	 * @return symbol
	 */
	public Symbol getResourceTopCardSymbol()
	{
		return resourceTopCardSymbol;
	}

	/**
	 * Getter for the gold ground symbol
	 * @return symbol
	 */
	public int getGoldFaceUp1Id()
	{
		return goldFaceUp1Id;
	}
	/**
	 * Getter for the gold ground symbol
	 * @return symbol
	 */
	public int getGoldFaceUp2Id()
	{
		return goldFaceUp2Id;
	}
	/**
	 * Getter for the resource ground symbol
	 * @return symbol
	 */
	public int getResourceFaceUp1Id()
	{
		return resourceFaceUp1Id;
	}
	/**
	 * Getter for the resource ground symbol
	 * @return symbol
	 */
	public int getResourceFaceUp2Id()
	{
		return resourceFaceUp2Id;
	}
	/**
	 * Getter for the hands of the playing player
	 * @return The string array of the hand of the playing player
	 */
	public String[] getPlayerHandCardColors(){
		return this.playerHandCardColors;
	}
}

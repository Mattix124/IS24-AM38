package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Symbol;
import javafx.scene.image.ImageView;

/**
 * Class that contains the info of a card displayed
 */
public class ImageCard
{
	/**
	 * ImageView of the card
	 */
	 private ImageView image;
	 /**
	 * Type (gold or resource) and symbol (fungi, animal, insect or plant)
	 */
	private String symbolAndType;

	/**
	 * Constructor method that set the image of the card and symbolAndType
	 *
	 * @param image
	 * @param symbolAndType
	 */
	public ImageCard(ImageView image, String symbolAndType)
	{
		this.image = image;
		this.symbolAndType = symbolAndType;
	}

	/**
	 * Getter for the attribute image
	 *
	 * @return image
	 */
	public ImageView getImage()
	{
		return image;
	}

	/**
	 * Getter for the attribute symbolAndType
	 *
	 * @return symbolAndType
	 */
	public String getSymbol()
	{
		return symbolAndType;
	}

}

package it.polimi.ingsw.am38.View;

import javafx.scene.image.ImageView;

/**
 * Class that contains the info of a card displayed
 */
public class ImageCard
{
	/**
	 * ImageView of the card
	 */
	 private ImageView frontImage;
	 /**
	 * Type (gold or resource) and symbol (fungi, animal, insect or plant)
	 */
	private String symbolAndType;

	private ImageView backImage;

	/**
	 * Constructor method that set the image of the card and symbolAndType
	 *
	 * @param image
	 * @param symbolAndType
	 */
	public ImageCard(ImageView image, String symbolAndType)
	{
		this.frontImage = image;
		this.symbolAndType = symbolAndType;
	}

	/**
	 * Setter for the back Image
	 * @param backImage
	 */
	public void setBackImage(ImageView backImage)
	{
		this.backImage = backImage;
	}

	/**
	 * Getter of the back Image
	 * @return
	 */
	public ImageView getBackImage()
	{
		return backImage;
	}

	/**
	 * Getter for the attribute image
	 *
	 * @return image
	 */
	public ImageView getFrontImage()
	{
		return frontImage;
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

package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Symbol;
import javafx.scene.image.ImageView;

public class ImageCard
{
	private ImageView image;
	private String symbolAndType;

	public ImageCard(ImageView image, String symbolAndType)
	{
		this.image = image;
		this.symbolAndType = symbolAndType;
	}

	public ImageView getImage()
	{
		return image;
	}

	public String getSymbol()
	{
		return symbolAndType;
	}

	public void setImage(ImageView image)
	{
		this.image = image;
	}

	public void setSymbolAndType(String symbolAndType)
	{
		this.symbolAndType = symbolAndType;
	}
}

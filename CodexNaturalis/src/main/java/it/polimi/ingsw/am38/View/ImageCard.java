package it.polimi.ingsw.am38.View;

import javafx.scene.image.ImageView;

public class ImageCard
{
	private ImageView image;
	private int coordX;
	private int coordY;

	public ImageCard(ImageView image, int coordX, int coordY)
	{
		this.image = image;
		this.coordX = coordX;
		this.coordY = coordY;
	}

	public ImageView getImage()
	{
		return image;
	}

	public int getCoordX()
	{
		return coordX;
	}

	public int getCoordY()
	{
		return coordY;
	}
}

package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Model.Cards.ObjectiveCard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SetUpSceneController extends SceneController implements Initializable
{
	@FXML
	private VBox facingBox;
	@FXML
	private VBox colorBox;
	@FXML
	private VBox personalOjbBox;
	private final ImageView imageViewFront = new ImageView();
	private final ImageView imageViewBack = new ImageView();
	private final ImageView imageViewRed = new ImageView();
	private final ImageView imageViewGreen = new ImageView();
	private final ImageView imageViewBlue = new ImageView();
	private final ImageView imageViewYellow = new ImageView();
	private final ImageView imageViewObj1 = new ImageView();
	private final ImageView imageViewObj2 = new ImageView();
	private int cardWidth = 221;
	private int cardHeight = 148;
	Alert alert;

	/**
	 * This method initializes view of the setup phase of the game, where starter card facing, color and personal objective
	 * are chosen.
	 *
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		alert = new Alert(Alert.AlertType.CONFIRMATION);
		int id;

		Region region = new Region();
		region.setMinSize(cardWidth, 10);

		colorBox.setDisable(true);
		colorBox.setOpacity(0.5);
		personalOjbBox.setDisable(true);
		personalOjbBox.setOpacity(0.5);
		//id = HelloApplication.getStarterCardID();
		/* esempio */
		id = 100; // QUESTO È ASSOLUTAMENTE DA TOGLIERE!!!
		Image imageFront = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + id + "-front.png")), cardWidth, cardHeight, true, true);
		Image imageBack  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/" + id + "-back.png")), cardWidth, cardHeight, true, true);

		Image bluePawn   = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/pawn/bluePawn.png")), 100, 100, true, true);
		Image redPawn    = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/pawn/redPawn.png")), 100, 100, true, true);
		Image yellowPawn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/pawn/yellowPawn.png")), 100, 100, true, true);
		Image greenPawn  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/pawn/greenPawn.png")), 100, 100, true, true);

		// Images are painted in their respective ImageView and an ID is set to them
		imageViewFront.setImage(imageFront);
		imageViewFront.setId("front");
		imageViewBack.setImage(imageBack);
		imageViewBack.setId("back");

		imageViewBlue.setImage(bluePawn);
		imageViewBlue.setId("blue");
		imageViewRed.setImage(redPawn);
		imageViewRed.setId("red");
		imageViewGreen.setImage(greenPawn);
		imageViewGreen.setId("green");
		imageViewYellow.setImage(yellowPawn);
		imageViewYellow.setId("yellow");

		facingBox.getChildren().addAll(imageViewFront, region, imageViewBack);
		enableClickFacing(imageViewFront);
		enableClickFacing(imageViewBack);

		colorBox.getChildren().addAll(imageViewBlue, imageViewRed, imageViewGreen, imageViewYellow);

		enableClickColor(imageViewBlue);
		enableClickColor(imageViewRed);
		enableClickColor(imageViewGreen);
		enableClickColor(imageViewYellow);
	}

	/**
	 * Send the clicked facing card to the server
	 *
	 * @param imageView is sent so that is possible to differ between the "front" and "back" case
	 */
	private void enableClickFacing(ImageView imageView)
	{
		imageView.setOnMouseClicked(e -> {
			switch (imageView.getId())
			{
				case "front":
					cci.checkCommand("face up");
					break;

				case "back":
					cci.checkCommand("face down");
                    break;
			}
			facingBox.setDisable(true);
			facingBox.setOpacity(0.5);
			colorBox.setDisable(false);
			colorBox.setOpacity(1);
		});
	}

	/**
	 * Send the clicked color to the server
	 *
	 * @param imageView is sent so that is possible to differ between the colors
	 */
	private void enableClickColor(ImageView imageView)
	{
		imageView.setOnMouseClicked(e -> {
				cci.checkCommand("color " + imageView.getId().toString());

			colorBox.setDisable(true);
			colorBox.setOpacity(0.5);
			alert.setTitle("Waiting...");
			alert.setHeaderText("Successfully sent info to the server!");
			alert.setContentText("Waiting for all players to join...");
			alert.getDialogPane().getButtonTypes().clear();
			alert.show();
		});
	}

	public void personalObjectiveChoice(ObjectiveCard objChoice1, ObjectiveCard objChoice2)
	{
		alert.close();
		personalOjbBox.setDisable(false);
		personalOjbBox.setOpacity(1);

		Image imageObj1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + objChoice1.getCardID() + "-front.png")), cardWidth, cardHeight, true, true);
		Image imageObj2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + objChoice2.getCardID() + "-front.png")), cardWidth, cardHeight, true, true);

		imageViewObj1.setImage(imageObj1);
		imageViewObj2.setImage(imageObj2);
		imageViewObj1.setId("obj1");
		imageViewObj2.setId("obj2");

		personalOjbBox.getChildren().addAll(imageViewObj1, imageViewObj2);

		enableClickObj(imageViewObj1);
		enableClickObj(imageViewObj2);
	}

	public void enableClickObj(ImageView imageView)
	{
		imageView.setOnMouseClicked(e -> {
			switch (imageView.getId())
			{
				case "obj1":
				{
						cci.checkCommand("obj 1");
				}
				break;
				case "obj2":
				{
						cci.checkCommand("obj 2");
				}
				break;
			}

			alert.setTitle("Waiting...");
			alert.setHeaderText("Successfully sent info to the server!");
			alert.setContentText("Waiting for all players to join...");
			alert.getDialogPane().getButtonTypes().clear();
			alert.show();
		});
	}

}

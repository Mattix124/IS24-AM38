package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Cards.GoldCard;
import it.polimi.ingsw.am38.Model.Cards.ResourceCard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SetUpSceneController extends SceneController implements Initializable
{
	@FXML
	private Pane pr0;
	@FXML
	private Pane pr1;
	@FXML
	private Pane pr2;
	@FXML
	private Pane pg0;
	@FXML
	private Pane pg1;
	@FXML
	private Pane pg2;
	@FXML
	private VBox facingBox;
	@FXML
	private VBox colorBox;
	@FXML
	private HBox goldBox;
	@FXML
	private HBox resourceBox;

	private final ImageView imageViewFront = new ImageView();
	private final ImageView imageViewBack = new ImageView();
	private final ImageView imageViewRed = new ImageView();
	private final ImageView imageViewGreen = new ImageView();
	private final ImageView imageViewBlue = new ImageView();
	private final ImageView imageViewYellow = new ImageView();
	private final ImageView imageViewRes0 = new ImageView();
	private final ImageView imageViewRes1 = new ImageView();
	private final ImageView imageViewRes2 = new ImageView();
	private final ImageView imageViewGold0 = new ImageView();
	private final ImageView imageViewGold1 = new ImageView();
	private final ImageView imageViewGold2 = new ImageView();

	private final int cardWidth = 221;
	private final int cardHeight = 148;
	Alert alert;

	public SetUpSceneController(int sc, Symbol gt, Symbol rt, int g1, int g2, int r1, int r2){
		Image resImg0 = null;
		Image goldImg0 = null;

		Image imageFront = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + sc + "-front.png")), cardWidth, cardHeight, true, true);
		Image imageBack  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/" + sc + "-back.png")), cardWidth, cardHeight, true, true);

		Image bluePawn   = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/pawn/bluePawn.png")), 75, 75, true, true);
		Image redPawn    = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/pawn/redPawn.png")), 75, 75, true, true);
		Image yellowPawn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/pawn/yellowPawn.png")), 75, 75, true, true);
		Image greenPawn  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/pawn/greenPawn.png")), 75, 75, true, true);

		switch (gt){
			case Symbol.ANIMAL -> {
				resImg0  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/21-back.png")), cardWidth*0.85, cardHeight*0.85, true, true);
			}
			case Symbol.FUNGI -> {
				resImg0  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/1-back.png")), cardWidth*0.85, cardHeight*0.85, true, true);
			}
			case Symbol.PLANT -> {
				resImg0  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/11-back.png")), cardWidth*0.85, cardHeight*0.85, true, true);
			}
			case Symbol.INSECT -> {
				resImg0  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/31-back.png")), cardWidth*0.85, cardHeight*0.85, true, true);
			}
		}

		switch (gt){
			case Symbol.ANIMAL -> {
				goldImg0  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/61-back.png")), cardWidth*0.85, cardHeight*0.85, true, true);
			}
			case Symbol.FUNGI -> {
				goldImg0  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/41-back.png")), cardWidth*0.85, cardHeight*0.85, true, true);
			}
			case Symbol.PLANT -> {
				goldImg0  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/51-back.png")), cardWidth*0.85, cardHeight*0.85, true, true);
			}
			case Symbol.INSECT -> {
				goldImg0  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/71-back.png")), cardWidth*0.85, cardHeight*0.85, true, true);
			}
		}

		Image resImg1  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ r1 +"-front.png")), cardWidth*0.85, cardHeight*0.85, true, true);
		Image resImg2  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ r2 +"-front.png")), cardWidth*0.85, cardHeight*0.85, true, true);
		Image goldImg1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ g1 +"-front.png")), cardWidth*0.85, cardHeight*0.85, true, true);
		Image goldImg2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ g2 +"-front.png")), cardWidth*0.85, cardHeight*0.85, true, true);


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


		// here ids are not needed (I think...) since these cards are not clickable
		imageViewRes0.setImage(resImg0);
		imageViewRes1.setImage(resImg1);
		imageViewRes2.setImage(resImg2);

		imageViewGold0.setImage(goldImg0);
		imageViewGold1.setImage(goldImg1);
		imageViewGold2.setImage(goldImg2);

	}
	/**
	 * This method initializes view of the setup phase of the game, where starter card facing, color and personal objective
	 * are chosen.
	 *
	 * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		alert = new Alert(Alert.AlertType.CONFIRMATION);

		Region region = new Region();
		region.setMinSize(cardWidth, 10);

		colorBox.setDisable(true);
		colorBox.setOpacity(0.5);

		facingBox.getChildren().addAll(imageViewFront, region, imageViewBack);

		enableClickFacing(imageViewFront);
		enableClickFacing(imageViewBack);

		colorBox.getChildren().addAll(imageViewBlue, imageViewRed, imageViewGreen, imageViewYellow);
		enableClickColor(imageViewBlue);
		enableClickColor(imageViewRed);
		enableClickColor(imageViewGreen);
		enableClickColor(imageViewYellow);

		pr0.getChildren().add(imageViewRes0);
		pr1.getChildren().add(imageViewRes1);
		pr2.getChildren().add(imageViewRes2);

		resourceBox.spacingProperty().set(2);

		pg0.getChildren().add(imageViewGold0);
		pg1.getChildren().add(imageViewGold1);
		pg2.getChildren().add(imageViewGold2);

		goldBox.spacingProperty().set(2);
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
			cci.checkCommand("color " + imageView.getId());

			colorBox.setDisable(true);
			colorBox.setOpacity(0.5);
			alert.setTitle("Waiting...");
			alert.setHeaderText("Successfully sent info to the server!");
			alert.setContentText("Waiting for all players to join...");
			alert.getDialogPane().getButtonTypes().clear();
			alert.show();
		});
	}

//	public void personalObjectiveChoice(ObjectiveCard objChoice1, ObjectiveCard objChoice2)
//	{
//		alert.close();
//		personalOjbBox.setDisable(false);
//		personalOjbBox.setOpacity(1);
//
//		Image imageObj1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + objChoice1.getCardID() + "-front.png")), cardWidth, cardHeight, true, true);
//		Image imageObj2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + objChoice2.getCardID() + "-front.png")), cardWidth, cardHeight, true, true);
//
//		imageViewObj1.setImage(imageObj1);
//		imageViewObj2.setImage(imageObj2);
//		imageViewObj1.setId("obj1");
//		imageViewObj2.setId("obj2");
//
//		personalOjbBox.getChildren().addAll(imageViewObj1, imageViewObj2);
//
//		enableClickObj(imageViewObj1);
//		enableClickObj(imageViewObj2);
//	}

//	public void enableClickObj(ImageView imageView)
//	{
//		imageView.setOnMouseClicked(e -> {
//			switch (imageView.getId())
//			{
//				case "obj1":
//				{
//						cci.checkCommand("obj 1");
//				}
//				break;
//				case "obj2":
//				{
//						cci.checkCommand("obj 2");
//				}
//				break;
//			}
//
//			alert.setTitle("Waiting...");
//			alert.setHeaderText("Successfully sent info to the server!");
//			alert.setContentText("Waiting for all players to join...");
//			alert.getDialogPane().getButtonTypes().clear();
//			alert.show();
//		});
//	}

}

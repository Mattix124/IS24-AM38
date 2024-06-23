package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.View.GuiSupporDataClasses.StarterChoiceData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SetUpSceneController extends SceneController implements PropertyChangeListener
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

	private final int cardWidth = 221;
	private final int cardHeight = 148;
	Alert alert;

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		Platform.runLater(() -> {
			switch ((String) evt.getPropertyName())
			{
				case "Start" ->
				{
					StarterChoiceData scd = (StarterChoiceData) evt.getNewValue();






				}
			}

		});
	}
	/**
	 * This method initializes view of the setup phase of the game, where starter card facing, color and personal objective
	 * are chosen.
	 *
	 * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if the root object was not localized.
	 */
	/*
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
*/
	/**
	 * Send the clicked facing card to the server
	 *
	 * @param imageView is sent so that is possible to differ between the "front" and "back" case
	 */
/*	private void enableClickFacing(ImageView imageView)
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
*/
	/**
	 * Send the clicked color to the server
	 *
	 * @param imageView is sent so that is possible to differ between the colors
	 */
/*	private void enableClickColor(ImageView imageView)
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
*/
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

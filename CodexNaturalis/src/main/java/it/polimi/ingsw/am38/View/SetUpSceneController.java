package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.View.GuiSupporDataClasses.StarterChoiceData;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

import static it.polimi.ingsw.am38.View.SceneController.cci;

/**
 * Controller of the SetUpScene
 */
public class SetUpSceneController implements PropertyChangeListener
{
	@FXML
	private HBox topHBox;
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
	private String f;
	private String b;
	/**
	 * Width of the card based on the width of the screen
	 */
	private final double cardWidth = Screen.getPrimary().getBounds().getWidth()/5;
	/**
	 * Height of the card based on the height of the screen
	 */
	private final double cardHeight = Screen.getPrimary().getBounds().getHeight()/5;
	private Popup popup = new Popup();

	/**
	 *
	 * @param evt A PropertyChangeEvent object describing the event source
	 *          and the property that has changed.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		Platform.runLater(() -> {
			switch (evt.getPropertyName())
			{
				case "Start" ->
				{
					StarterChoiceData scd = (StarterChoiceData) evt.getNewValue();
					setupScene(scd);

				}
				case "Color" ->
				{
					colorBox.setDisable(false);
					colorBox.setOpacity(1);
				}
				case "Chosen" ->
				{
					Label l     = new Label((String) evt.getNewValue());
					Popup popup = new Popup();
					l.setFont(new Font(22));
					l.setTextFill(Color.BLACK);
					popup.getContent().add(l);
					colorBox.setDisable(false);
					colorBox.setOpacity(1);
					PauseTransition delay = new PauseTransition(Duration.seconds(0.7));
					delay.setOnFinished(event -> popup.hide());
					popup.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_LEFT);
					popup.show(colorBox.getScene().getWindow());
					delay.playFromStart();
				}
				case "Wait" ->
				{
					Label l = new Label((String) evt.getNewValue());
					l.setFont(new Font(22));
					l.setTextFill(Color.BLACK);
					FadeTransition fade = new FadeTransition(new Duration(500),colorBox.getScene().getRoot());
					fade.setToValue(0.7);
					fade.setFromValue(1);
					fade.playFromStart();
					popup.getContent().add(l);
					popup.show(colorBox.getScene().getWindow());
				}
				case "RemoveLabel" ->
				{
					popup.hide();
				}
			}
		});
	}

	/**
	 * Method that set up the scene
	 *
	 * @param scd class from which to take the info to display
	 */
	private void setupScene(StarterChoiceData scd)
	{
		ImageView gold0, gold1, gold2;
		ImageView res0, res1, res2;
		ImageView fStarter, bStarter;
		ImageView red, blue, yellow, green;

		topHBox.setPrefHeight(Screen.getPrimary().getBounds().getHeight()*5/11);
		resourceBox.setPrefHeight(Screen.getPrimary().getBounds().getHeight()*3/11);
		goldBox.setPrefHeight(Screen.getPrimary().getBounds().getHeight()*3/11);

		int fixedId = 0;
		switch (scd.getGoldTop())
		{
			case FUNGI -> fixedId = 41;
			case PLANT -> fixedId = 51;
			case ANIMAL -> fixedId = 61;
			case INSECT -> fixedId = 71;
		}
		gold0 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/" + fixedId + "-back.png")), cardWidth * 0.85, cardHeight * 0.85, true, true));
		switch (scd.getResourceTop())
		{
			case FUNGI -> fixedId = 1;
			case PLANT -> fixedId = 11;
			case ANIMAL -> fixedId = 21;
			case INSECT -> fixedId = 31;
		}
		res0 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/" + fixedId + "-back.png")), cardWidth * 0.85, cardHeight * 0.85, true, true));

		res1 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + scd.getRes1().getCardID() + "-front.png")), cardWidth * 0.85, cardHeight * 0.85, true, true));
		GUI.guiData.setFirstRes1("GameImages/front/" + scd.getRes1().getCardID() + "-front.png");
		res2 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + scd.getRes2().getCardID() + "-front.png")), cardWidth * 0.85, cardHeight * 0.85, true, true));
		GUI.guiData.setFirstRes2("GameImages/front/" + scd.getRes2().getCardID() + "-front.png");
		gold1 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + scd.getGold1().getCardID() + "-front.png")), cardWidth * 0.85, cardHeight * 0.85, true, true));
		GUI.guiData.setFirstGold1("GameImages/front/" + scd.getGold1().getCardID() + "-front.png");
		gold2 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + scd.getGold2().getCardID() + "-front.png")), cardWidth * 0.85, cardHeight * 0.85, true, true));
		GUI.guiData.setFirstGold2("GameImages/front/" + scd.getGold2().getCardID() + "-front.png");
		fStarter = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + scd.getStarterCard().getCardID() + "-front.png")), cardWidth, cardHeight, true, true));
		fStarter.setId("front");
		f = "GameImages/front/" + scd.getStarterCard().getCardID() + "-front.png";
		bStarter = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/" + scd.getStarterCard().getCardID() + "-back.png")), cardWidth, cardHeight, true, true));
		bStarter.setId("back");
		b = "GameImages/back/" + scd.getStarterCard().getCardID() + "-back.png";
		red = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/pawn/redPawn.png")), 75, 75, true, true));
		red.setId("red");
		blue = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/pawn/bluePawn.png")), 75, 75, true, true));
		blue.setId("blue");
		green = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/pawn/greenPawn.png")), 75, 75, true, true));
		green.setId("green");
		yellow = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/pawn/yellowPawn.png")), 75, 75, true, true));
		yellow.setId("yellow");

		enableClickFacing(fStarter);
		enableClickFacing(bStarter);
		enableClickColor(red);
		enableClickColor(blue);
		enableClickColor(green);
		enableClickColor(yellow);

//@		pr0.getChildren().add(res0);
//@		pr1.getChildren().add(res1);
//@		pr2.getChildren().add(res2);
//@		pg0.getChildren().add(gold0);
//@		pg1.getChildren().add(gold1);
//@		pg2.getChildren().add(gold2);
		resourceBox.getChildren().addAll(res0, res1, res2);
		goldBox.getChildren().addAll(gold0, gold1, gold2);

		facingBox.getChildren().add(fStarter);
		facingBox.getChildren().add(bStarter);
		colorBox.getChildren().add(blue);
		colorBox.getChildren().add(green);
		colorBox.getChildren().add(red);
		colorBox.getChildren().add(yellow);
		facingBox.setSpacing(2);
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
					GUI.guiData.setStarter(f);

					break;

				case "back":
					cci.checkCommand("face down");
					GUI.guiData.setStarter(b);
					break;
			}
			facingBox.setDisable(true);
			facingBox.setOpacity(0.5);

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
		});
	}
}

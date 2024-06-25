package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import it.polimi.ingsw.am38.View.GuiSupporDataClasses.GuiPlacedConfirm;
import it.polimi.ingsw.am38.View.GuiSupporDataClasses.ObjChoiceData;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.util.Duration;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.*;

import static it.polimi.ingsw.am38.View.GUI.guiData;

public class ControllerGameView implements PropertyChangeListener, Initializable
{
	@FXML
	private GridPane mainPane;
	@FXML
	private TextArea chatIn;
	@FXML
	private VBox chatArea;
	@FXML
	private ScrollPane chatScrollPane;
	@FXML
	private HBox handBox;
	@FXML
	private ScrollPane fieldScrollPane;
	private Pane field;
	@FXML
	private Button centerField;
	@FXML
	private ImageView border1;
	@FXML
	private ImageView border2;
	@FXML
	private ImageView border3;
	@FXML
	private ImageView border4;
	@FXML
	private Button putCardButton;
	@FXML
	private Button resetCardsButton;
	@FXML
	private VBox scoreBox;
	@FXML
	private HBox playerBox;
	@FXML
	private VBox box1p;
	@FXML
	private HBox box2p;
	@FXML
	private Pane backPanePlayersAndScore;
	@FXML
	private Pane scorePanel;
	private HashMap <ImageView, Pair <Integer, Integer>> borders;

	private int wCard = 221;
	private int hCard = 148;
	private int wCell = 173;  //ratio 0,783
	private int hCell = 89;  //ratio 0,594
	private int wField = wCell * 81;
	private int hField = hCell * 81;

	//logic
	private int n = 0;
	private ImageView cardToPlace;
	private Node cardToRemove;
	private boolean allowPlace = false;
	private boolean alreadyChoice = false;

	private Image RanimalBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/21-back.png")), wCard, hCard, true, true);
	private Image RfungiBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/1-back.png")), wCard, hCard, true, true);
	private Image RplantBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/11-back.png")), wCard, hCard, true, true);
	private Image RinsectBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/31-back.png")), wCard, hCard, true, true);
	private Image GanimalBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/61-back.png")), wCard, hCard, true, true);
	private Image GfungiBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/41-back.png")), wCard, hCard, true, true);
	private Image GplantBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/51-back.png")), wCard, hCard, true, true);
	private Image GinsectBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/71-back.png")), wCard, hCard, true, true);
	//Data-------------------------------------------------------------------------------
	private String nickname;
	private final HashMap <String, LinkedList <ImageView>> playersField = new HashMap <>();
	private final HashMap <String, LinkedList <ImageView>> playersHands = new HashMap <>();
	private final HashMap <String, Integer> inserts = new HashMap <>();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		setPanels();
		setBorders();
	}

	private void enableDrag(ImageView imageView)
	{

		imageView.setOnDragDetected(event -> {
			Dragboard        db      = imageView.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.putImage(imageView.getImage());
			content.put(DataFormat.RTF, imageView);
			db.setContent(content);
			event.consume();
		});
		Glow g = new Glow(0.65);
		imageView.setOnMouseEntered(event -> imageView.setEffect(g));
		imageView.setOnMouseExited(event -> imageView.setEffect(null));

	}

	private ImageView pickCard(boolean c)
	{
		ImageView imageView = new ImageView();
		Random    r         = new Random();
		int       n         = Math.abs(r.nextInt() % 102) + 1;
		Image     image     = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + n + "-front.png")), wCard, hCard, true, true);
		imageView.setImage(image);
		imageView.setPreserveRatio(true);
		if (c)
			enableDrag(imageView);
		return imageView;
	}


	private void setBorders()
	{
		borders = new HashMap <>();
		borders.put(border1, new Pair <>(1, 0));
		borders.put(border2, new Pair <>(0, 4));

		border1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/V_vines.png"))));
		border1.setScaleX(2);
		border1.setScaleY(2.5);
		border2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/H_vines.png"))));
		border2.setScaleX(1.25);
		border2.setScaleY(1.1);
		borders.forEach((border, pair) -> {
			Region region = new Region();
			region.setStyle("-fx-background-color: grey;");
			region.setMinSize(0, 0);
			mainPane.add(region, pair.getKey(), pair.getValue());
			border.fitHeightProperty().bind(region.heightProperty());
			border.fitWidthProperty().bind(region.widthProperty());
		});

	}

	private void setPanels()
	{
//FIELD  PANEL--------------------------------------------------------------------------------------------------------------------------------------
		BackgroundImage bg = new BackgroundImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/mainWall.jpg"))), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		mainPane.setBackground(new Background(bg));
		BackgroundImage bImage = new BackgroundImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/TopT1.jpg"))), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		Pane            p      = new Pane();
		field = p;
		fieldDrag();
		p.setPrefHeight(hField);
		p.setPrefWidth(wField);
		p.setBackground(new Background(bImage));

		for (int i = wCell ; i < wField ; i = i + wCell)
		{
			Line l = new Line();
			l.setFill(Color.BLUE);
			l.setStrokeWidth(5);
			l.startXProperty().set(i);
			l.startYProperty().set(0);
			l.endXProperty().set(i);
			l.endYProperty().set(hField);
			field.getChildren().add(l);
			n++;
		}
		for (int j = hCell ; j < hField ; j = j + hCell)
		{
			Line l = new Line();
			l.setFill(Color.BLUE);
			l.setStrokeWidth(5);
			l.startXProperty().set(0);
			l.startYProperty().set(j);
			l.endXProperty().set(wField);
			l.endYProperty().set(j);
			field.getChildren().add(l);
			n++;
		}
		fieldScrollPane.setVvalue(0.5);
		fieldScrollPane.setHvalue(0.5);
		fieldScrollPane.setContent(p);
//SCOREBOARD AND PLAYERS-----------------------------------------------------------------------------------------------------
		ImageView backScoreIm;
		Image     emptyBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/EmptyScreen.png")));
		backScoreIm = new ImageView(emptyBack);
		backScoreIm.fitHeightProperty().bind(backPanePlayersAndScore.heightProperty());
		backScoreIm.fitWidthProperty().bind(backPanePlayersAndScore.widthProperty());
		backScoreIm.setViewOrder(1);
		backPanePlayersAndScore.getChildren().add(backScoreIm);
		box1p.prefHeightProperty().bind(backPanePlayersAndScore.heightProperty());
		box1p.prefWidthProperty().bind(backPanePlayersAndScore.widthProperty());
		scoreBox.prefHeightProperty().bind(backPanePlayersAndScore.heightProperty().multiply(0.8));
		scoreBox.prefWidthProperty().bind(backPanePlayersAndScore.widthProperty());
		playerBox.prefHeightProperty().bind(backPanePlayersAndScore.heightProperty().multiply(0.2));
		playerBox.prefWidthProperty().bind(backPanePlayersAndScore.widthProperty());

		//	box1p.setStyle("-fx-background-color: grey;");
		//box2p.prefHeightProperty().bind(box1p.heightProperty());
		//box2p.prefWidthProperty().bind(box1p.widthProperty());
		////box2.setStyle("-fx-background-color: blue;");
		//scoreBox.prefWidthProperty().bind(box2p.widthProperty().multiply(0.66));
		//scoreBox.prefHeightProperty().bind(box2p.heightProperty());
		//playerBox.prefHeightProperty().bind(box2p.heightProperty());
		//playerBox.prefWidthProperty().bind(box2p.widthProperty().multiply(0.25));
		//scoreBox.setStyle("-fx-background-color: red;");
		Image     im         = new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/Scoretrack.png")));
		ImageView imageView1 = new ImageView(im);
		//scorePanel.setStyle("-fx-background-color: purple;");
		imageView1.setPreserveRatio(true);
		imageView1.fitHeightProperty().bind(scoreBox.heightProperty());
		imageView1.fitWidthProperty().bind(scoreBox.widthProperty());
		scoreBox.getChildren().add(imageView1);
		Region region = new Region();
		//region.setStyle("-fx-background-color: grey;");
		region.setMinSize(0, 0);
		mainPane.add(region, 0, 5);
		region.setDisable(true);
		handBox.spacingProperty().bind((region.widthProperty().divide(7)));

	}

	private void fieldDrag()
	{
		field.setOnDragOver(event -> {
			if (event.getGestureSource() != field && event.getDragboard().hasImage())
			{
				event.acceptTransferModes(TransferMode.MOVE);
			}
			event.consume();
		});

		field.setOnDragDropped(event -> {
			if (allowPlace && !alreadyChoice)
			{

				Dragboard db      = event.getDragboard();
				boolean   success = false;
				if (db.hasImage())
				{
					double tmpX = event.getX();
					double tmpY = event.getY();

					int x = 40 + wCell * ((int) tmpX / wCell);
					int y = 40 - hCell * ((int) tmpY / hCell);

					if ((x + y) % 2 == 0)
					{
						Image     image     = db.getImage();
						ImageView imageView = new ImageView(image);
						imageView.setPreserveRatio(true);
						cardToPlace = imageView;
						alreadyChoice = true;
						Node im = (Node) db.getContent(DataFormat.RTF);
						im.getId();
						cardToRemove = handBox.getChildren().get(handBox.getChildren().indexOf(im));
						success = true;
						event.setDropCompleted(success);
						event.consume();
					}
				}
			}
		});

	}

	public void centerView()
	{
		fieldScrollPane.setVvalue(0.5);
		fieldScrollPane.setHvalue(0.5);
	}

	public void sendChatMessage()
	{
		if (chatIn.getText().contains("\n"))
		{
			String[] s = chatIn.getText().split("\n");

			if (s.length > 0)
			{
				Text t       = new Text(s[0]);
				HBox message = new HBox();
				message.getChildren().add(t);
				message.setAlignment(Pos.CENTER_RIGHT);
				message.setPadding(new Insets(5, 5, 5, 5));
				chatArea.getChildren().add(message);
				chatScrollPane.setVvalue(1.0);
			}
			chatIn.setText("");

			//send

		}
	}

	public void receiveChatMessage(ActionEvent e)
	{
		Text t       = new Text("Pippo pluto");
		HBox message = new HBox();
		message.getChildren().add(t);
		message.setAlignment(Pos.CENTER_LEFT);
		message.setPadding(new Insets(5, 5, 5, 5));
		chatArea.getChildren().add(message);
		chatScrollPane.setVvalue(1.0);

		//send
	}



	private void startSetup(PropertyChangeEvent evt)
	{
		ObjChoiceData objChoiceData = guiData.getObjd();
		this.nickname = guiData.getNickname();
		//set dei deck

		HashMap <String, StarterCard> playersStarter = objChoiceData.getPsc();
		HashMap <String, String[]>    hands          = objChoiceData.getHcc();
		playersStarter.forEach((x, y) -> playersField.put(x, new LinkedList <>()));
		hands.forEach((x, y) -> playersHands.put(x, new LinkedList <>()));

		playersStarter.forEach((x, y) -> {
			ImageView starter = generateCoordinateImageCard(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(y.getImg())), wCard, hCard, true, true)), 0, 0);
			playersField.get(x).add(starter);
			inserts.put(x, 0);
			if (x.equals(nickname))
				field.getChildren().add(starter);
		});

		//your hand
		int i = 0;
		LinkedList <PlayableCard> myOwnHand = objChoiceData.getOwnHand();
		myOwnHand.forEach(x -> {
			ImageView im = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(x.getImg())), wCard, hCard, true, true));
			enableDrag(im);
			im.setId(String.valueOf(i));
			handBox.getChildren().add(im);
		});

		//hands setup
		hands.forEach((x, y) -> {
			ImageView im;
			for (String s : y)
			{
				im = createFirstImageView(s);
				playersHands.get(x).add(im);
			}
		});

		//create button
		LinkedList <String> otherNames = new LinkedList <>(hands.keySet());
		otherNames.forEach(x -> {
			Button b = new Button(x);
			b.setOnAction(event -> {
				field.getChildren().remove(n, n + inserts.get(x));
				playersField.get(x).forEach(y -> field.getChildren().add(y));
				//mancano el mani
			});
			playerBox.getChildren().add(b);
		});
	}

	private ImageView createFirstImageView(String s)
	{
		ImageView im = new ImageView();

		switch (s)
		{
			case "RA" -> im.setImage(RanimalBack);
			case "RF" -> im.setImage(RfungiBack);
			case "RP" -> im.setImage(RplantBack);
			case "RI" -> im.setImage(RinsectBack);
			case "GA" -> im.setImage(GanimalBack);
			case "GF" -> im.setImage(GfungiBack);
			case "GP" -> im.setImage(GplantBack);
			case "GI" -> im.setImage(GinsectBack);
		}

		return im;
	}

	private ImageView generateCoordinateImageCard(ImageView im, int x, int y)
	{

		int X = (40 + x) * wCell - (wCard - wCell) / 2;
		int Y = (40 - y) * hCell - (hCard - hCell) / 2;

		im.setX(X);
		im.setY(Y);

		return im;

	}

	private void placeCard(PropertyChangeEvent evt)
	{
		GuiPlacedConfirm gpc  = ((GuiPlacedConfirm) evt.getNewValue());
		String           nick = gpc.getNick();
		int              x    = gpc.getX();
		int              y    = gpc.getY();
		if (nick.equals(nickname))
		{
			generateCoordinateImageCard(cardToPlace, x, y);
			FadeTransition fadeIn = new FadeTransition(new Duration(1000), cardToPlace);
			fadeIn.setFromValue(0.0);
			fadeIn.setToValue(1);
			field.getChildren().add(cardToPlace);
			fadeIn.play();

			FadeTransition fadeOut = new FadeTransition(new Duration(1000), cardToPlace);
			fadeOut.setFromValue(1);
			fadeOut.setToValue(0);
			fadeOut.setOnFinished(end -> handBox.getChildren().remove(cardToRemove));
			inserts.put(nickname, inserts.get(nickname) + 1);
			fadeIn.play();
			fadeOut.play();
		}
		else
		{
			ImageView card = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(gpc.getCard().getImg())), wCard, hCard, true, true));
			generateCoordinateImageCard(card, x, y);
			playersField.get(nick).add(card);
			inserts.put(nick, inserts.get(nick) + 1);
		}

	}

	private void popUpTurn(PropertyChangeEvent evt)
	{
		Label  l          = new Label();
		String playerName = (String) evt.getNewValue();
		if (!playerName.equals(nickname))
			l.setText("It's " + playerName + " turn!");
		else
			l.setText("It's your turn!");
		Popup p = new Popup();
		p.setAnchorX(handBox.getScene().getWidth() / 2);
		p.setAnchorX(handBox.getScene().getHeight() / 4);

		PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
		delay.setOnFinished(event -> p.hide());
		p.show(handBox.getScene().getWindow());
		delay.playFromStart();

	}

	private void play(PropertyChangeEvent evt)
	{
		allowPlace = true;
		alreadyChoice = false;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		Platform.runLater(() -> {

			switch (evt.getPropertyName())
			{
				case "Start" -> startSetup(evt);
				case "Placed" -> placeCard(evt);
				case "TurnName" -> popUpTurn(evt);
				case "Play" -> play(evt);
			}
		});
	}
}

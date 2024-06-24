package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.View.GuiSupporDataClasses.FirstScreenContainer;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import javafx.util.Duration;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.*;

import static it.polimi.ingsw.am38.View.GUI.guiData;

public class ControllerGameView implements Initializable, PropertyChangeListener
{
	@FXML
	private GridPane mainPane;
	@FXML
	private TextArea chatIn;
	@FXML
	private Button pippo;
	@FXML
	private VBox chatArea;
	@FXML
	private ScrollPane chatScrollPane;
	@FXML
	private HBox handBox;
	//@FXML
	//private VBox objBox;
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

	private HBox objectiveContainer;
	private HashMap <ImageView, Pair <Integer, Integer>> borders;

	private int childReset = 0;

	private int wCard;
	private int hCard;
	private int wCell;  //ratio 0,783
	private int hCell;  //ratio 0,594
	private int wField;
	private int hField;
	private final String nickname = guiData.getNickname();
	private final HashMap <String, LinkedList <ImageCard>> playersFields = new HashMap <>();

	public ControllerGameView()
	{
		this.wCard = 221;
		this.hCard = 148;
		this.wCell = 173;
		this.hCell = 89;
		this.wField = wCell * 81;
		this.hField = hCell * 81;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		setPanels();
		setBorders();
		setHand();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		Platform.runLater(() -> {
			switch (evt.getPropertyName())
			{
				case "Start" ->
				{

					FirstScreenContainer fsc = (FirstScreenContainer) evt.getNewValue();

					//for per ogni player inserisci starter card in PlayersFields e aggiungila alla mappa playersField.
					//aggiungi la tua al field pannello.

					//per ogni player, aggiungi pulsante di cambio campo. e pedina colorata (pikola)

					//aggiungi la tua mano

				}
/*
				case "UpdateCard"-> //String nick, PlayableCard card, int x, int y


				1,1  X = (x+40)*wCell   		Y = (-y+40)*hCell
				 X - (wCard - wCell) / 2    Y- - (hCard - hCell) / 2
*/
			}
		});
	}

	public void resetCards()
	{
		field.getChildren().remove(childReset, field.getChildren().size());
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
		//DropShadow shadow = new DropShadow(30, 10, 10, Color.GRAY);
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

	private void setHand()
	{

		Region region  = new Region();
		Region region2 = new Region();
		//region.setStyle("-fx-background-color: grey;");
		region.setMinSize(0, 0);
		mainPane.add(region, 0, 4);
		region.setDisable(true);
		region2.setDisable(true);
		handBox.spacingProperty().bind((region.widthProperty().divide(9)));
		for (int i = 0 ; i < 3 ; i++)
		{
			createCard();
		}

	}

	private void createCard()
	{
		ImageView imageView;
		imageView = pickCard(true);
		imageView.fitHeightProperty().bind(handBox.heightProperty().divide(1.5));
		imageView.fitWidthProperty().bind(handBox.widthProperty().divide(1.5));
		imageView.setCursor(Cursor.HAND);

		handBox.getChildren().add(imageView);
	}

	private void setBorders()
	{
		borders = new HashMap <>();
		borders.put(border1, new Pair <>(1, 0));
		borders.put(border2, new Pair <>(0, 3));
		//borders.put(border3, new Pair <>(1, 3));
		//borders.put(border4, new Pair <>(2, 2));

		border1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/V_vines.png"))));
		border1.setScaleX(2);
		border1.setScaleY(2.5);
		border2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/H_vines.png"))));
		border2.setScaleX(1.25);
		border2.setScaleY(1.1);
		borders.forEach((border, pair) -> {
			Region region = new Region();
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
		Image     im         = new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/Scoretrack.png")));
		ImageView imageView1 = new ImageView(im);
		imageView1.setPreserveRatio(true);
		imageView1.fitHeightProperty().bind(scoreBox.heightProperty());
		imageView1.fitWidthProperty().bind(scoreBox.widthProperty());
		scoreBox.getChildren().add(imageView1);
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
			Dragboard db      = event.getDragboard();
			boolean   success = false;
			if (db.hasImage())
			{
				double tmpX = event.getX();
				double tmpY = event.getY();

				int x = wCell * ((int) tmpX / wCell);
				int y = hCell * ((int) tmpY / hCell);

				System.out.println(x + " " + y);

				if ((x / wCell + y / hCell) % 2 == 0)
				{ // per tornare all'originale togli questo if
					Image     image     = db.getImage();
					ImageView imageView = new ImageView(image);
					imageView.setPreserveRatio(true);
					int X = x - (wCard - wCell) / 2;
					int Y = y - (hCard - hCell) / 2;
					imageView.setX(X);
					imageView.setY(Y);
					setCardinField(nickname, X, Y, imageView);
					FadeTransition fade = new FadeTransition(new Duration(1000), imageView);
					fade.setFromValue(0.0);
					fade.setToValue(1);

					field.getChildren().add(imageView);
					fade.play();
					success = true;
				}
				else
				{
					System.out.println("non piazzabile qui"); //debug
				}

			}
			event.setDropCompleted(success);
			if (success)
			{
				Node           imageView = handBox.getChildren().get(handBox.getChildren().indexOf(db.getContent(DataFormat.RTF)));
				FadeTransition fade      = new FadeTransition(new Duration(1000), imageView);
				fade.setFromValue(1);
				fade.setToValue(0);
				fade.setOnFinished(end -> {
					handBox.getChildren().remove(imageView);
					createCard();
				});
				fade.play();

			}
			event.consume();
		});

	}

	private void setCardinField(String nickname, int x, int y, ImageView image)
	{
		ImageCard card = new ImageCard(image, x, y);
		playersFields.get(nickname).add(card);
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

	public void putCard()
	{
		return;
	}

}

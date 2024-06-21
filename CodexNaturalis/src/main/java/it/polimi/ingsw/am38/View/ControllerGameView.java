package it.polimi.ingsw.am38.View;

import javafx.animation.FadeTransition;
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

import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class ControllerGameView extends SceneController implements Initializable
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
	@FXML
	private VBox objBox;
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
	private VBox playerBox;
	@FXML
	private VBox box1p;
	@FXML
	private HBox box2p;
	@FXML
	private Pane backPanePlayersAndScore;
	@FXML
	private Pane scorePanel;
	@FXML
	private Pane backPaneObjective;
	@FXML
	private HBox objectiveContainer;

	private HashMap <ImageView, Pair <Integer, Integer>> borders;

	private int childReset = 0;

	private int wCard = 221;
	private int hCard = 148;
	private int wCell = 173; //ratio 0,783
	private int hCell = 89; //ratio 0,594
	private int wField = wCell * 41;
	private int hField = hCell * 41;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		setPanels();
		setBorders();
		setHand();
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
		region2.setMinSize(0, 0);
		mainPane.add(region, 0, 4);
		mainPane.add(region2, 2, 4);
		region.setDisable(true);
		region2.setDisable(true);
		handBox.spacingProperty().bind((region.widthProperty().add(region2.widthProperty()).divide(9)));
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
		int n = 0;
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
		ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/81-front.png")), wCard, hCard, true, true));
		imageView.setX(wCell * 20 - (wCard - wCell) / 2);
		imageView.setY(hCell * 20 - (hCard - hCell) / 2);

		childReset = n + 1;
		p.getChildren().add(imageView);
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
		//	box1p.setStyle("-fx-background-color: grey;");
		box2p.prefHeightProperty().bind(box1p.heightProperty());
		box2p.prefWidthProperty().bind(box1p.widthProperty());
		//box2.setStyle("-fx-background-color: blue;");
		scoreBox.prefWidthProperty().bind(box2p.widthProperty().multiply(0.66));
		scoreBox.prefHeightProperty().bind(box2p.heightProperty());
		playerBox.prefHeightProperty().bind(box2p.heightProperty());
		playerBox.prefWidthProperty().bind(box2p.widthProperty().multiply(0.25));
		//scoreBox.setStyle("-fx-background-color: red;");
		Image     im         = new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/Scoretrack.png")));
		ImageView imageView1 = new ImageView(im);
		//scorePanel.setStyle("-fx-background-color: purple;");
		imageView1.setPreserveRatio(true);
		imageView1.fitHeightProperty().bind(scorePanel.heightProperty().add(-20));

		imageView1.setLayoutX(50);
		scorePanel.getChildren().add(imageView1);
//OBJECTIVE PANEL--------------------------------------------------------------------------------------------------------------
		ImageView backObjIm;
		backObjIm = new ImageView(emptyBack);
		backObjIm.fitHeightProperty().bind(backPaneObjective.heightProperty());
		backObjIm.fitWidthProperty().bind(backPaneObjective.widthProperty());
		backObjIm.setViewOrder(1);
		backPaneObjective.getChildren().add(backObjIm);
		objBox.prefWidthProperty().bind(backPaneObjective.widthProperty());
		objBox.prefHeightProperty().bind(backPaneObjective.heightProperty());
		ImageView obj   = new ImageView();
		Random    r     = new Random();
		int       o     = Math.abs(r.nextInt() % 16) + 87;
		Image     image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + o + "-front.png")), wCard, hCard, true, true);
		obj.setImage(image);
		obj.setPreserveRatio(true);
		obj.fitHeightProperty().bind(objectiveContainer.heightProperty().multiply(0.75));
		obj.fitWidthProperty().bind(objectiveContainer.widthProperty().multiply(0.75));
		objectiveContainer.getChildren().add(obj);

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
					imageView.setX(x - (wCard - wCell) / 2);
					imageView.setY(y - (hCard - hCell) / 2);

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

	public void centerView()
	{
		fieldScrollPane.setVvalue(0.5);
		fieldScrollPane.setHvalue(0.5);
	}

	public void press(ActionEvent e)
	{
		changeScene(e);
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

		Image cardImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + 1 + "-front.png")));
		Image cardImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + 2 + "-front.png")));
		Image cardImage3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + 3 + "-front.png")));

		// Create ImageView objects
		ImageView card1 = new ImageView(cardImage1);
		ImageView card2 = new ImageView(cardImage2);
		ImageView card3 = new ImageView(cardImage3);
		//card1.setScaleX(0.25);
		//card1.setScaleY(0.25);
		// Set positions for diagonal placement
		card1.setX(field.getWidth() / 2);
		card1.setY(field.getHeight() / 2);

		card2.setX(100);
		card2.setY(100);

		card3.setX(150);
		card3.setY(150);

		// Optional: Rotate or transform if needed
		card1.setTranslateX(600);
		card2.setRotate(-10);
		card3.setRotate(5);

		// Add cards to the pane
		field.getChildren().addAll(card1, card2, card3);

	/*	ImageView imageView = new ImageView();
		Random    r         = new Random();
		int       n         = Math.abs(r.nextInt() % 102) + 1;
		System.out.println(n);
		imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/GameImages/front/" + n + "-front.png"))));

		HBox box = new HBox();
		box.getChildren().add(imageView);
		//imageView.setScaleY(0.3);
		//imageView.setScaleX(0.3);
		box.setPadding(new Insets(0, 250, 0, 250));
		box.setLayoutX(field.getWidth() / 2);
		box.setLayoutY(field.getHeight() / 2);
*/
		//field.getChildren().add(box);
	}

}

package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Model.Cards.PlayableCard;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import it.polimi.ingsw.am38.View.GuiSupporDataClasses.DeckandHand;
import it.polimi.ingsw.am38.View.GuiSupporDataClasses.GuiPlacedConfirm;
import it.polimi.ingsw.am38.View.GuiSupporDataClasses.ObjChoiceData;
import it.polimi.ingsw.am38.View.GuiSupporDataClasses.ScorePlayers;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.util.Duration;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.*;

import static it.polimi.ingsw.am38.View.GUI.guiData;
import static it.polimi.ingsw.am38.View.SceneController.cci;

public class ControllerGameView implements PropertyChangeListener, Initializable
{
	@FXML
	private VBox playerScoreVBox;
	@FXML
	private HBox sharedObjHBox;
	@FXML
	private ChoiceBox choiceBox;
	@FXML
	private VBox resourceBox;
	@FXML
	private VBox goldBox;
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
	private HBox headerHandBox;
	@FXML
	private VBox handBigBox;
	@FXML
	private Button faceCard;

	private HashMap <ImageView, Pair <Integer, Integer>> borders;

	private final int wCard = (int) ((Screen.getPrimary().getBounds().getWidth())/7);
	private final int hCard = (int) (wCard*0.669); //momentaneo
	private final int wCell = (int)(wCard*0.762);  //ratio 0,783
	private final int hCell = (int)(hCard*0.601);  //ratio 0,594

    //logic
	private int n = 0;
	private ImageView cardToPlace;
	private Node cardToRemove;
	private boolean allowPlace = false;
	private boolean alreadyChoice = false;
	private boolean allowDraw = false;
	private boolean face = true;
	private String watchedPlayer;
	private String idPlayed;
	private ImageView glowingCard;

	private final Popup p = new Popup();
	private final Image RanimalBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/21-back.png")), wCard, hCard, true, true);
	private final Image RfungiBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/1-back.png")), wCard, hCard, true, true);
	private final Image RplantBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/11-back.png")), wCard, hCard, true, true);
	private final Image RinsectBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/31-back.png")), wCard, hCard, true, true);
	private final Image GanimalBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/61-back.png")), wCard, hCard, true, true);
	private final Image GfungiBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/41-back.png")), wCard, hCard, true, true);
	private final Image GplantBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/51-back.png")), wCard, hCard, true, true);
	private final Image GinsectBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/71-back.png")), wCard, hCard, true, true);

	//for decks
	private final Image DRanimalBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/21-back.png")), wCard * 0.5, hCard * 0.5, true, true);
	private final Image DRfungiBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/1-back.png")), wCard * 0.5, hCard * 0.5, true, true);
	private final Image DRplantBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/11-back.png")), wCard * 0.5, hCard * 0.5, true, true);
	private final Image DRinsectBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/31-back.png")), wCard * 0.5, hCard * 0.5, true, true);
	private final Image DGanimalBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/61-back.png")), wCard * 0.5, hCard * 0.5, true, true);
	private final Image DGfungiBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/41-back.png")), wCard * 0.5, hCard * 0.5, true, true);
	private final Image DGplantBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/51-back.png")), wCard * 0.5, hCard * 0.5, true, true);
	private final Image DGinsectBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/71-back.png")), wCard * 0.5, hCard * 0.5, true, true);

	//Deck
	ImageView TopR = new ImageView();
	ImageView TopG = new ImageView();
	ImageView Res1 = new ImageView();
	ImageView Res2 = new ImageView();
	ImageView Gold1 = new ImageView();
	ImageView Gold2 = new ImageView();

	//Data-------------------------------------------------------------------------------
	private String nickname;
	private final HashMap <String, LinkedList <ImageView>> playersField = new HashMap <>();/*contiene le carte in ordine di giocata associate al nick*/
	private final HashMap <String, LinkedList <ImageCard>> playersHands = new HashMap <>();/* contiene una lista associata al nome di una classe che contiene l'immagine della carta (retro per gli avversari) per te invece è il fronte.  (le tue hanno anche un attributo stringa per il retro ("RA"--->  retro animal). l'atteibuto degli avversari é null (vedi solo il retro basta l'immagine) */
	private final HashMap <String, Integer> playerPoints = new HashMap <>();
/*punti associati ai nomi*/
	private boolean reconnectionState = false;
/*inizializza la parte statica (sfondi ecc)*/
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) 
	{
		setPanels();
		setBorders();
	}
/* permette il drag della tua mano (funziona)*/
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
/*gira le carte(faccia su e giú) qui potrebbero esserci problemi (controllate) (potrebbe essere per le mapps aggiornate male) (forse)*/
	public void flipCard() 
	{
		if (watchedPlayer.equals(nickname))
		{
			handBox.getChildren().removeAll(handBox.getChildren());
			if (face)
			{
				LinkedList <String> sAT = new LinkedList <>(playersHands.get(nickname).stream().map(ImageCard::getSymbol).toList());
				int                 i   = 0;
				for (String s : sAT)
				{

					ImageView im = createImageView(s, null, false);
					enableDrag(im);
					im.setId(String.valueOf(i));
					i++;
					handBox.getChildren().add(im);
				}
				face = false;
			}
			else
			{
				handBox.getChildren().removeAll(handBox.getChildren());
//				for(ImageCard card : playersHands.get(nickname)){
//					handBox.getChildren().add(card.getImage());
//				}
				handBox.getChildren().addAll(playersHands.get(nickname).stream().map(x -> x.getImage()).toList());
				face = true;
			}
		}
	}

	private void setBorders(){} /*rende scalabili le 2 immagini con le foglie (funziona)/*
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
/*roba statica (funziona)*/
	private void setPanels() 
	{
//FIELD  PANEL--------------------------------------------------------------------------------------------------------------------------------------
		BackgroundImage bg = new BackgroundImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/mainWall.jpg"))), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		mainPane.setBackground(new Background(bg));
		BackgroundImage bImage = new BackgroundImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/TopT1.jpg"))), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		Pane            p      = new Pane();
		field = p;
		fieldDrag();
        int hField = hCell * 81;
        p.setPrefHeight(hField);
        int wField = wCell * 81;
        p.setPrefWidth(wField);
		p.setBackground(new Background(bImage));

		for (int i = wCell; i < wField; i = i + wCell)
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
		for (int j = hCell; j < hField; j = j + hCell)
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
		headerHandBox.prefHeightProperty().bind(handBigBox.heightProperty().multiply(0.2));
		handBox.prefHeightProperty().bind(handBigBox.heightProperty().multiply(0.8));
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
		//@ Image     im         = new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/Scoretrack.png")));
		//@ ImageView imageView1 = new ImageView(im);
		//@ imageView1.setPreserveRatio(true);
		//@ imageView1.fitHeightProperty().bind(scoreBox.heightProperty());
		//@ imageView1.fitWidthProperty().bind(scoreBox.widthProperty());
		//@ scoreBox.getChildren().add(imageView1);
		ImageView sharedObj1 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(guiData.getObjd().getSharedObj1().getImg())), 100, 100, true, true));
		ImageView sharedObj2 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(guiData.getObjd().getSharedObj2().getImg())), 100, 100, true, true));
		ImageView personalObj = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + guiData.getObjective() + "-front.png")), 100, 100, true, true));
		sharedObjHBox.getChildren().addAll(sharedObj1, sharedObj2);
		scoreBox.getChildren().addAll(personalObj);

		// Print Players score (0 when initializing)
		for(String name : playerPoints.keySet()){
			playerScoreVBox.getChildren().add(new Label(name + ": 0"));
		}

		Region region = new Region();
		region.setMinSize(0, 0);
		mainPane.add(region, 0, 5);
		region.setDisable(true);
		handBox.spacingProperty().bind((region.widthProperty().divide(14)));

	}
/* la giocata di una carta si divide in 2 parti (scelta e piazzamento) questa é scelta. e ciò che permette la comunicazione con il server in attesa di risposta. (funziona)*/
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

					int x = ((int) tmpX / wCell) - 40;
					int y = 40 - ((int) tmpY / hCell);

					if ((x + y) % 2 == 0)
					{
						Image     image     = db.getImage();
						ImageView imageView = new ImageView(image);
						imageView.setPreserveRatio(true);
						cardToPlace = imageView;
						alreadyChoice = true;
						Node   im = (Node) db.getContent(DataFormat.RTF);
						String f;
						if (face)
							f = "up";
						else
							f = "down";

						cardToRemove = handBox.getChildren().get(handBox.getChildren().indexOf(im)); /*segna quale carta verrà rimossa in un altro metodo (vedi placement)*/
						cci.checkCommand("play " + im.getId() + " " + x + " " + y + " " + f);
						success = true;

					}
					event.consume();
					event.setDropCompleted(success);
				}
			}
		});

	}
/*centra il campo (funziona)*/
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
				if (reconnectionState)
				{
					if (s[0].equals("reconnect"))
					{
						cci.loginCommand("reconnect");
						Platform.exit();
					}
					if (s[0].equals("exit"))
					{
						cci.loginCommand("exit");
						Platform.exit();
						return;
					}
				}
				if(!choiceBox.getValue().equals("All")) {
					cci.checkCommand("w " + choiceBox.getValue() + " " + s[0]);
				}
				else if(choiceBox.getValue().equals("All"))
					cci.checkCommand("all " + s[0]);
			}
			chatIn.setText("");

		}
	}
/*displaya il messaggio che ricevi a sinistra della chat (dovrebbe funzionare) (se avete tempo guardate di far scorrere tutto lo scrollpane, speltozio dovrebbe sapere)*/
	public void receiveChatMessage(PropertyChangeEvent e) 
	{
		Text t       = new Text((String) e.getNewValue());
		HBox message = new HBox();
		message.getChildren().add(t);
		message.setAlignment(Pos.CENTER_LEFT);
		message.setPadding(new Insets(5, 5, 5, 5));
		chatArea.getChildren().add(message);
		chatScrollPane.setVvalue(1.0);

	}
/*metodo dinamico che setta la scena appena entrati (dovrebbe funzionare) al massimo problemi con funzione dei bottoni in combo con flipcard e pescate. provate*/
	private void startSetup(PropertyChangeEvent evt)
	{
		ObjChoiceData objChoiceData = guiData.getObjd();
		this.nickname = guiData.getNickname();
		watchedPlayer = nickname;
		HashMap <String, StarterCard> playersStarter = objChoiceData.getPsc();
		HashMap <String, String[]>    hands          = objChoiceData.getHcc();
		playersStarter.forEach((x, y) -> playersField.put(x, new LinkedList <>()));
		hands.forEach((x, y) -> playersHands.put(x, new LinkedList <>()));

		playersStarter.forEach((x, y) -> {
			ImageView starter = generateCoordinateImageCard(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(y.getImg())), wCard*0.98, hCard, true, true)), 0, 0);
			playersField.get(x).add(starter);
			//inserts
			if (x.equals(nickname))
				field.getChildren().add(starter);
		});

		//your hand
		handRefresh(objChoiceData.getOwnHand());

		//hands setup
		hands.forEach((x, y) -> {
			ImageView im;
			int       i = 0;
			for (String s : y)
			{
				im = createImageView(s, null, false);
				if (!x.equals(nickname))
					playersHands.get(x).add(new ImageCard(im, null));
			}
		});

		//create button
		LinkedList <String> otherNames = new LinkedList <>(hands.keySet());
		otherNames.forEach(x -> {

			Button b = new Button(x);
			b.setOnAction(event -> {

				field.getChildren().remove(n, field.getChildren().size());
				playersField.get(x).forEach(y -> field.getChildren().add(y));
				handBox.getChildren().removeAll(handBox.getChildren());
				handBox.getChildren().addAll(playersHands.get(x).stream().map(ImageCard::getImage).toList());
				watchedPlayer = x;
				face = true;
			});
			playerBox.getChildren().add(b);
		});

		// create and show decks
		TopR.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(guiData.getFirstTopR())), wCard * 0.5, hCard * 0.5, true, true));
		TopG.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(guiData.getFirstTopG())), wCard * 0.5, hCard * 0.5, true, true));
		Res1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(guiData.getFirstRes1())), wCard * 0.85, hCard * 0.85, true, true));
		Res2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(guiData.getFirstRes2())), wCard * 0.85, hCard * 0.85, true, true));
		Gold1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(guiData.getFirstGold1())), wCard * 0.85, hCard * 0.85, true, true));
		Gold2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(guiData.getFirstGold2())), wCard * 0.85, hCard * 0.85, true, true));
		resourceBox.getChildren().addAll(Res1, Res2, TopR);
		goldBox.getChildren().addAll(Gold1, Gold2, TopG);
		TopR.setId("0");
		TopG.setId("0");
		Res1.setId("1");
		Res2.setId("2");
		Gold1.setId("1");
		Gold2.setId("2");
		drawable(TopR);
		drawable(TopG);
		drawable(Res1);
		drawable(Res2);
		drawable(Gold1);
		drawable(Gold2);

		resourceBox.setSpacing(4);
		goldBox.setSpacing(4);

		// choicebox for all/whispering choice
		for(String n : playersField.keySet()){
			if(!n.equals(nickname))
				choiceBox.getItems().add(n);
			else {
				choiceBox.getItems().add("All");
				choiceBox.setValue("All");
			}
		}
	}
/*rimpiazza le immagini nella mano crandone nuove (dovrebbe funzionare) */
	private void handRefresh(LinkedList <PlayableCard> myOwnHand)
	{
		handBox.getChildren().removeAll(handBox.getChildren());
		myOwnHand.forEach(x -> {
			ImageView im = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(x.getImg())), wCard*0.98, hCard, true, true));
			enableDrag(im);
			im.setId(String.valueOf(myOwnHand.indexOf(x)));
			String    s        = getStringFromId(x);
			ImageCard cardHand = new ImageCard(im, s);
			playersHands.get(nickname).add(cardHand);
			handBox.getChildren().add(im);
		});
	}
/*roba per creare le stringhe per le carte retro (credo non ricordo)(dovrebbe funzionare)*/
	private String getStringFromId(PlayableCard card)
	{
		StringBuilder s = new StringBuilder();
		if (card.getCardID() < 41)
		{
			s.append("R");
		}
		else
		{
			s.append("G");
		}
		switch (card.getKingdom())
		{
			case FUNGI -> s.append("F");
			case PLANT -> s.append("P");
			case ANIMAL -> s.append("A");
			case INSECT -> s.append("I");
		}
		return s.toString();
	}
/*setta le immagini per il retro delle carte in base alla stringa (dovrebbe funzionare)*/
	private ImageView createImageView(String s, ImageView image, boolean deck)
	{
		ImageView im;
		if (image == null)
		{
			im = new ImageView();
		}
		else
		{
			im = image;
		}
		if (!deck)
		{

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
		switch (s)
		{
			case "RA" -> im.setImage(DRanimalBack);

			case "RF" -> im.setImage(DRfungiBack);

			case "RP" -> im.setImage(DRplantBack);

			case "RI" -> im.setImage(DRinsectBack);

			case "GA" -> im.setImage(DGanimalBack);

			case "GF" -> im.setImage(DGfungiBack);

			case "GP" -> im.setImage(DGplantBack);

			case "GI" -> im.setImage(DGinsectBack);
		}
		return im;
	}
/* rende i deck drwable, solo quando necessario (controllate che quando si pesca il flip funzioni (ieri mi dava alcuni problemi) (cercate di romperlo)*/
	private void drawable(ImageView im)
	{
		Glow g = new Glow(0.65);
		im.setOnMouseEntered(e -> {
			if (allowDraw)
				im.setEffect(g);
		});
		im.setOnMouseExited(e -> {
			if (allowDraw)
				im.setEffect(null);
		});
		im.setOnMouseClicked(e -> {
			if (allowDraw)
			{
				VBox box = (VBox) im.getParent();
				cci.checkCommand("draw " + box.getId() + " " + im.getId());
				allowDraw = false;
				glowingCard = im;
			}
		});
	}
/*setta le coordinate vere (pixel) di un imageView in base a coordinate "reali" (1,1 2,2 ecc) ( dovrebbe funzionare)*/
	private ImageView generateCoordinateImageCard(ImageView im, int x, int y)
	{

		double X = (40 + x) * wCell - (wCard - wCell) / 2;
		double Y = (40 - y) * hCell - (hCard - hCell) / 2;

		im.setX(X);
		im.setY(Y);

		return im;

	}
/* il server ha inviato l'esito della giocata fatta sul metodo sopra (quello del panel che accetta)  piazza e rimuove dalla mano la carta giocata (funziona) magari controllate le varie mappe per gli aggiornamenti*/
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

			FadeTransition fadeOut = new FadeTransition(new Duration(1000), cardToRemove);
			fadeOut.setFromValue(1);
			fadeOut.setToValue(0);
			fadeOut.setOnFinished(end -> handBox.getChildren().remove(cardToRemove));
			//inserts.put(nickname, inserts.get(nickname) + 1);
			fadeIn.play();
			fadeOut.play();
		}
		else
		{
			ImageView card = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(gpc.getCard().getImg())), wCard*0.98, hCard, true, true));
			cardToPlace = generateCoordinateImageCard(card, x, y);
		}
		playersField.get(nick).add(cardToPlace);
		//playersHands.get(nick).remove(playersHands.get(nick).stream().filter(z -> z.getImage().equals(cardToRemove)).toList().getFirst());
		for(ImageCard card : playersHands.get(nick)){
			if(card.getImage().equals(cardToRemove)){
				playersHands.get(nick).remove(card);
				break;
			}
		}
	}
/*alza un popup quando cambia il turno (controllate sia dritto)*/
	private void popUpTurn(PropertyChangeEvent evt)
	{
		Label  l          = new Label();
		String playerName = (String) evt.getNewValue();
		l.setFont(new Font(25));
		l.setTextFill(Color.WHITE);
		if (!playerName.equals(nickname))
			l.setText("It's " + playerName + " turn!");
		else
			l.setText("It's your turn!");
		p.getContent().add(l);
		p.setAnchorX(handBox.getScene().getWidth() / 2);
		p.setAnchorX(handBox.getScene().getHeight() / 4);

		PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
		delay.setOnFinished(event -> {
			p.hide();
			p.getContent().removeFirst();
		});
		p.show(handBox.getScene().getWindow());
		delay.playFromStart();

	}
/*mette a posto i check per il play*/
	private void play()
	{
		allowPlace = true;
		alreadyChoice = false;
	}
/*rende tutto inutilizzabile a seguito del blocco nel gioco (anche se la chat dovrebbe essere disponibile, errore mio) se riuscite rimediate*/
	private void noPossiblePlacement(PropertyChangeEvent evt)
	{
		allowPlace = false;
		alreadyChoice = true;
		popUpAlert(evt, false);
		Parent         root = playerBox.getScene().getRoot();
		FadeTransition fade = new FadeTransition(new Duration(500), playerBox.getScene().getRoot());
		fade.setFromValue(1);
		fade.setToValue(0.5);
		root.setDisable(true);

	}
/*popup per vari eventi (controllate sia dritto)*/
	private void popUpAlert(PropertyChangeEvent evt, boolean fade)
	{
		Label l = new Label((String) evt.getNewValue());
		l.setFont(new Font(25));
		l.setTextFill(Color.WHITE);
		p.getContent().add(l);
		//p.setAnchorX(handBox.getScene().getWidth() / 2);
		//p.setAnchorY(handBox.getScene().getHeight() / 3);
		if (fade)
		{
			PauseTransition delay = new PauseTransition(Duration.seconds(5));
			delay.setOnFinished(event -> {
				p.hide();
				p.getContent().removeFirst();
			});
			p.show(handBox.getScene().getWindow());
			delay.playFromStart();
		}
		else
		{
			p.show(handBox.getScene().getWindow());
		}
	}

	private void draw()
	{
		allowDraw = true;
	}
/*setta le immagini del deck dopo la pescata (potrebbe essere rotto (non credo funzioni come dovrebbe)) fate qualche prova*/
	private void deckRefresh(DeckandHand daH)
	{
		Image im = new Image(Objects.requireNonNull(getClass().getResourceAsStream(daH.getGold1().getImg())), wCard * 0.85, hCard * 0.85, true, true);
		Gold1.setImage(im);
		im = new Image(Objects.requireNonNull(getClass().getResourceAsStream(daH.getGold2().getImg())), wCard * 0.85, hCard * 0.85, true, true);
		Gold2.setImage(im);
		im = new Image(Objects.requireNonNull(getClass().getResourceAsStream(daH.getRes1().getImg())), wCard * 0.85, hCard * 0.85, true, true);
		Res1.setImage(im);
		im = new Image(Objects.requireNonNull(getClass().getResourceAsStream(daH.getRes2().getImg())), wCard * 0.85, hCard * 0.85, true, true);
		Res2.setImage(im);
		StringBuilder s = new StringBuilder();
		s.append("G");
		switch (daH.getGoldT())
		{
			case FUNGI -> s.append("F");
			case PLANT -> s.append("P");
			case ANIMAL -> s.append("A");
			case INSECT -> s.append("I");
		}
		createImageView(s.toString(), TopG, true);
		StringBuilder d = new StringBuilder();
		d.append("R");
		switch (daH.getResT())
		{
			case FUNGI -> d.append("F");
			case PLANT -> d.append("P");
			case ANIMAL -> d.append("A");
			case INSECT -> d.append("I");
		}
		createImageView(d.toString(), TopR, true);
		if (glowingCard != null)
			glowingCard.setEffect(null);
	}
/*abbastanza auto esplicativo*/
	private void updateScore(PropertyChangeEvent evt)
	{
		ScorePlayers sp = (ScorePlayers) evt.getNewValue();
		playerPoints.put(sp.getNick(), sp.getScore());

		playerScoreVBox.getChildren().clear();
		for(String name : playerPoints.keySet()){
			playerScoreVBox.getChildren().add(new Label(name + ": " + playerPoints.get(name)));
		}
		//cambia qualcosa a livello grafico
	}
/*avvisa il giocatore che é avvenuta una disconnessione (rileggendo ora, non fa un tubo mi sa che manca il popup da mostrare)*/
	private void disconnectionManager(PropertyChangeEvent evt)
	{
		StringBuilder message = new StringBuilder((String) evt.getNewValue());
		message.append("\nType 'reconnect' or 'exit' in the chat");
		reconnectionState = true;

	}
/*mette a sinistra il tuo messaggio (fa il giro con il metodo di viewable (dovrebbe funzionare)*/
	private void displayYourMessage(PropertyChangeEvent evt)
	{

		Text t       = new Text((String) evt.getNewValue());
		HBox message = new HBox();
		message.getChildren().add(t);
		message.setAlignment(Pos.CENTER_RIGHT);
		message.setPadding(new Insets(5, 5, 5, 5));
		chatArea.getChildren().add(message);
		chatScrollPane.setVvalue(1.0);

	}
/*ingressi (li conoscete) (non credo ne manchino)*/
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		Platform.runLater(() -> {

			switch (evt.getPropertyName())
			{
				case "Start" -> startSetup(evt);
				case "Placed" -> placeCard(evt);
				case "Score" -> updateScore(evt);
				case "TurnName" -> popUpTurn(evt);
				case "Play" -> play();
				case "NotPlaceable" ->
				{
					popUpAlert(evt, true);
					play();
				}
				case "NoOtherPosPlac" -> noPossiblePlacement(evt);
				case "Draw" -> draw();
				case "DrawConf" ->
				{
					DeckandHand daH = (DeckandHand) evt.getNewValue();
					deckRefresh(daH);
					playersHands.get(nickname).remove(playersHands.get(nickname).getFirst());
					playersHands.get(nickname).remove(playersHands.get(nickname).getFirst());
					handRefresh(daH.getHand());
				}
				case "OtherDraw" ->
				{
					DeckandHand daH = (DeckandHand) evt.getNewValue();
					deckRefresh(daH);
					String nick = daH.getNickname();
					playersHands.get(nick).removeAll(playersHands.get(nick));
					Arrays.stream(daH.getOtherHands()).toList().forEach(x -> playersHands.get(nick).add(new ImageCard(createImageView(x, null, false), null)));
				}
				case "Empty" ->
				{
					allowDraw = true;
					disconnectionManager(evt);
				}
				case "ChatIn" -> receiveChatMessage(evt);
				case "ChatOut" -> displayYourMessage(evt);
				case "OtherDisconnection" ->
				{
					//avviso in chat
				}
				case "Disconnection" ->
				{
					popUpAlert(evt, false);
					//fargli scrivere reconnect nella chat o vedete voi (nella chat è implementato (okkio che ci ho messo 5 minuti)
				}
				case "Winner" ->
				{
					popUpAlert(evt, true); // (Guardate se riuscite a centrarli in qualche modo sono tutti storti)
				}
				case "NotPlay" -> popUpAlert(evt, true);
				case "Addresse" -> popUpAlert(evt, true);
			}
		});
	}

}

package it.polimi.ingsw.am38;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
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
	private HBox objBox;
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

	private HashMap <ImageView, Pair <Integer, Integer>> borders;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		setPanels();
		setBorders();
		setHand();
	}

	private void setHand()
	{

		Region region = new Region();
		region.setMinSize(0, 0);
		mainPane.add(region, 4, 0);

		handBox.spacingProperty().bind(region.widthProperty().divide(3));
		for (int i = 0 ; i < 3 ; i++)
		{
			ImageView imageView = new ImageView();
			Random    r         = new Random();
			int       n         = Math.abs(r.nextInt() % 102) + 1;
			Image     image     = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/front/" + n + "-front.png")), 331, 221, true, true);
			imageView.setImage(image);
			imageView.setPreserveRatio(true);
			imageView.fitHeightProperty().bind(handBox.heightProperty().divide(1.5));
			imageView.fitWidthProperty().bind(handBox.widthProperty().divide(1.5));
			//imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/front/" + n + "-front.png")),331,221,true,true));
			HBox box = new HBox();
			box.getChildren().add(imageView);
			box.setPadding(new Insets(0, 0, 0, 20));
			handBox.getChildren().add(box);
		}
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

		BackgroundImage bg = new BackgroundImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/mainWall.jpg"))), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		mainPane.setBackground(new Background(bg));
		BackgroundImage bImage = new BackgroundImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("ViewImage/TopT1.jpg"))), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		Pane            p      = new Pane();
		field = p;
		p.setPrefHeight(13600);
		p.setPrefWidth(9100);
		p.setBackground(new Background(bImage));
		ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/front/81-front.png")),331, 221, true, true));
		imageView.setX(p.getPrefWidth() / 2-imageView.getImage().getWidth()/2);
		imageView.setY(p.getPrefHeight() / 2-imageView.getImage().getHeight()/2);
		p.getChildren().add(imageView);

		fieldScrollPane.setVvalue(0.5);
		fieldScrollPane.setHvalue(0.5);
		fieldScrollPane.setContent(p);

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

		Image cardImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/front/" + 1 + "-front.png")));
		Image cardImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/front/" + 2 + "-front.png")));
		Image cardImage3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/front/" + 3 + "-front.png")));

		// Create ImageView objects
		ImageView card1 = new ImageView(cardImage1);
		ImageView card2 = new ImageView(cardImage2);
		ImageView card3 = new ImageView(cardImage3);
		card1.setScaleX(0.25);
		card1.setScaleY(0.25);
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
		imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/front/" + n + "-front.png"))));

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

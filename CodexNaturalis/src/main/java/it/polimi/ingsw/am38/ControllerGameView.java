package it.polimi.ingsw.am38;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ControllerGameView
{
	@FXML
	private TextArea chatIn;
	@FXML
	private Button button;
	@FXML
	private VBox chatArea;
	@FXML
	private VBox score;
	@FXML
	private AnchorPane mainPane;
	@FXML
	private ScrollPane chatScrollPane;
	@FXML
	private HBox handBox;
	@FXML
	private HBox objBox;
	@FXML
	private ScrollPane fieldScrollPane;

	private int chatMessage = 0;

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
				message.setPadding(new Insets(5, 5, 10, 10));
				chatArea.getChildren().add(message);
				chatScrollPane.setVvalue(1.0);
				checkChatVolume();
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
		message.setPadding(new Insets(5, 5, 5, 10));
		chatArea.getChildren().add(message);
		chatScrollPane.setVvalue(1.0);
		checkChatVolume();
		//send
	}

	private void checkChatVolume()
	{
		chatMessage++;
		if(chatMessage == 20)
		{
			// reset chatArea.getChildren().removeAll();
			// chatMessage = 0;
		}

	}
}

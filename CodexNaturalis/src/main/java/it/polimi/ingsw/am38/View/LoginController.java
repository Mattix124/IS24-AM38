package it.polimi.ingsw.am38.View;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.am38.View.SceneController.cci;

/**
 * Controller of the login-view
 */
public class LoginController implements PropertyChangeListener, Initializable
{
	public TextArea textArea;
	public Button createButton;
	public Button joinButton;
	public Button okButton;
	public Label promptLabel;
	public Button backButton;
	public BorderPane borderPane;
	@FXML
	private Label dynamicLabel;
	private FadeTransition fadeBack;
	/**
	 * Boolean to check if a nickname is already taken
	 */
	private boolean nicknameTaken = false;
	/**
	 * Boolean that permits to activate a button to go back in the scene
	 */
	private boolean backable = false;

	/**
	 * Initialize method
	 *
	 * @param url
	 * @param resourceBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		fadeBack = new FadeTransition(new Duration(500), dynamicLabel);
		fadeBack.setFromValue(1);
		fadeBack.setToValue(0);
	}

	/**
	 * This method run when the join button is clicked
	 */
	public void joinButtonClicked()
	{
		cci.loginCommand("2");
	}

	/**
	 * This method run when the create button is clicked
	 */
	public void createButtonClicked()
	{
		cci.loginCommand("1");
	}

	/**
	 * This method run when the back button is clicked
	 */
	public void backButtonClicked(ActionEvent e)
	{
		if (backable)
			cci.loginCommand("e");
	}

	/**
	 * This method checks that the text field contains no spaces and its length is no greater than 15.
	 * It is run every time a key is typed in the login text field.
	 */
	public void checkEnter()
	{

		if (textArea.getText().contains("\n"))
		{
			String[] s = textArea.getText().split("\n");
			if (s.length > 0)
			{
				String nick = s[0];
				if ((nick.length() < 16 && nick.length() > 2 && !nick.contains(" ")) || nicknameTaken)
				{
					cci.loginCommand(nick);
				}
				else
					setDynamicLabel("Max 15 characters, min 3 characters: (no space)", true);
			}
			textArea.setText("");
		}

	} // not enough, the check still needs to be done in other ways since you can bypass the limit by pasting a string

	/**
	 * This method changes the login page background when clicking the credits label
	 */
	public void changeBackgroundEasterEgg()
	{
		System.out.println("Style class: \"" + borderPane.getStyleClass() + "\"");
		if (borderPane.getStyleClass().contains("root"))
		{
			borderPane.getStyleClass().clear();
			borderPane.getStyleClass().add("easter-egg-background");
		}
		else
		{
			borderPane.getStyleClass().clear();
			borderPane.getStyleClass().add("root");
		}
	}

	/**
	 * Based on the event makes different action for the gui to work properly
	 *
	 * @param evt A PropertyChangeEvent object describing the event source
	 *          and the property that has changed.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		Platform.runLater(() -> {

			String[] tokens = ((String) evt.getNewValue()).split(" ");
			switch (tokens[0])
			{
				case "Taken" -> setDynamicLabel("Nickname already taken, retry:", true);

				case "NotIn" -> setDynamicLabel("Nickname not inserted, retry:", true);

				case "What" ->
				{
					promptLabel.setVisible(false);
					nicknameTaken = true;
					backButton.setVisible(true);
					joinButton.setVisible(true);
					createButton.setVisible(true);
					textArea.setVisible(false);
					FadeTransition backT   = new FadeTransition(new Duration(600), backButton);
					FadeTransition joinT   = new FadeTransition(new Duration(600), joinButton);
					FadeTransition createT = new FadeTransition(new Duration(600), createButton);
					FadeTransition textT   = new FadeTransition(new Duration(600), textArea);

					backT.setFromValue(0);
					backT.setToValue(1);
					joinT.setFromValue(0);
					joinT.setToValue(1);
					createT.setFromValue(0);
					createT.setToValue(1);
					textT.setFromValue(1);
					textT.setToValue(0);

					joinT.playFromStart();
					createT.playFromStart();
					textT.playFromStart();
					setDynamicLabel("What do you want to do? Create or Join a game?", false);

				}
				case "Create" ->
				{
					backable = true;
					fadeBack.playFromStart();
					fadingElements();
					setDynamicLabel("Specify the number of players that will participate (from 2 to 4):", false);
					textArea.setVisible(true);
					textArea.requestFocus();
				}

				case "NotValidCreate" -> setDynamicLabel("Your input is not valid. Retry: From 2 to 4 players.", true);

				case "Join" ->
				{
					backable = true;
					fadeBack.playFromStart();
					fadingElements();
					setDynamicLabel("To join a game specify its GameId number", false);
					textArea.setVisible(true);
					textArea.requestFocus();
				}

				case "Full" -> setDynamicLabel("The game you are trying to connect is full. Retry", true);
				case "NotFound" ->
						setDynamicLabel("The id you specified doesn't exists. Insert the IdGame you or your friend have exposed on the screen", true);
				case "NotNumber" -> setDynamicLabel("The argument you have given is not a number please retry", true);
				case "SuccCreate" ->
				{
					backable = false;
					fadeBack.playFromStart();
					fadingScene("You created a game successfully\nShow your GAMEID to your friend to let them join you!\nGAMEID: " + tokens[1]);
				}
				case "SuccJoin" ->
				{
					backable = false;
					fadeBack.playFromStart();
					fadingScene("You joined a game successfully. Have fun!");
				}
			}
		});
	}

	/**
	 * Method to make the view fade in certain moments
	 *
	 * @param s string to display
	 */
	private void fadingScene(String s)
	{
		FadeTransition fadeScene = new FadeTransition(new Duration(500), borderPane);
		fadeScene.setFromValue(1);
		fadeScene.setToValue(0.5);
		Label l = new Label(s);
		l.setAlignment(Pos.CENTER);
		l.setFont(new Font(22));
		l.setTextFill(Color.WHITE);
		l.setTextAlignment(TextAlignment.CENTER);
		FadeTransition fadeLabel = new FadeTransition(new Duration(500), l);
		fadeLabel.setFromValue(0);
		fadeLabel.setToValue(1);
		borderPane.setCenter(l);
		fadeScene.playFromStart();
		fadeLabel.playFromStart();


	}

	/**
	 * Setter for a dynamic label
	 *
	 * @param s string to put in the label
	 * @param fadingback true if the label has to disappear
	 */
	private void setDynamicLabel(String s, boolean fadingback)
	{

		dynamicLabel.setText(s);
		FadeTransition fade = new FadeTransition(new Duration(500), dynamicLabel);
		fade.setFromValue(0);
		fade.setToValue(1);
		if (fadingback)
		{
			PauseTransition delay = new PauseTransition(Duration.seconds(5));
			delay.setOnFinished(event -> fadeBack.playFromStart());
			fade.setOnFinished(f -> delay.playFromStart());
		}
		fade.playFromStart();

	}

	/**
	 * Method to make some objects fade
	 */
	private void fadingElements()
	{

		FadeTransition joinT   = new FadeTransition(new Duration(600), joinButton);
		FadeTransition createT = new FadeTransition(new Duration(600), createButton);
		FadeTransition textT   = new FadeTransition(new Duration(600), textArea);

		joinT.setFromValue(1);
		joinT.setToValue(0);
		createT.setFromValue(1);
		createT.setToValue(0);
		textT.setFromValue(0);
		textT.setToValue(1);

		joinT.playFromStart();
		createT.playFromStart();
		textT.playFromStart();
	}
}
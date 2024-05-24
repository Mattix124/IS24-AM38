package it.polimi.ingsw.am38;

import it.polimi.ingsw.am38.Exception.NicknameTakenException;
import it.polimi.ingsw.am38.Exception.NullNicknameException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import it.polimi.ingsw.am38.Controller.LobbyManager;

public class HelloController {
    public TextField textField;
    public Button createButton;
    public Button joinButton;
    public Button okButton;
    public Label promptLabel;
    public Button backButton;
    private String nickname;

    public void joinButtonClicked() {
        nickname = textField.getText();
        System.out.println("Join button clicked by " + nickname);
        backButton.setVisible(true);

        createButton.setVisible(false);
        joinButton.setVisible(false);

        okButton.setText("Join Game");
        okButton.setVisible(true);

        promptLabel.setText("Insert GameID");
        textField.setPromptText("GameID");
        textField.setText("");
        // LobbyManager.getLobbyManager().createPlayer(textField.getText());
    }

    public void createButtonClicked() {
        nickname = textField.getText();
        System.out.println("Create button clicked by " + nickname);
        backButton.setVisible(true);

        createButton.setVisible(false);
        joinButton.setVisible(false);

        okButton.setText("Create Game");
        okButton.setVisible(true);

        promptLabel.setText("Insert player number (2-4)");
        textField.setPromptText("player number");
        textField.setText("");
        // LobbyManager.getLobbyManager().createPlayer(textField.getText());
    }

    public void backButtonClicked() {
        System.out.println("Back button clicked by " + textField.getText());
        backButton.setVisible(false);

        createButton.setVisible(true);
        joinButton.setVisible(true);

        okButton.setText("HIDDEN");
        okButton.setVisible(false);

        promptLabel.setText("Nickname");
        textField.setPromptText("Nickname");
        textField.setText(nickname);
    }
}
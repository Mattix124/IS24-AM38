package it.polimi.ingsw.am38;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    public TextField textField;
    public Button createButton;
    public Button joinButton;
    public Button okButton;
    public Label promptLabel;
    public Button backButton;
    private String nickname = "";

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
        // LobbyManager.getLobbyManager().createPlayer(nickname);
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
        // LobbyManager.getLobbyManager().createPlayer(nickname);
    }

    public void backButtonClicked() {
        System.out.println("Back button clicked by " + nickname);
        backButton.setVisible(false);

        createButton.setVisible(true);
        joinButton.setVisible(true);

        okButton.setText("HIDDEN");
        okButton.setVisible(false);

        promptLabel.setText("Nickname");
        textField.setPromptText("Nickname");
        textField.setText(nickname);
    }

    public void checkNicknameLength() { // this method is ran every time a key is typed
        final int LIMIT = 15;
        textField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()){ // if the textField text length increased
                if (textField.getText().length() > LIMIT){
                    textField.setText(textField.getText().substring(0, LIMIT)); // if there's more than LIMIT chars then set the text to the previous one
                }
            }
        });
    } // not enough, the check still needs to be done in other ways since you can bypass the limit by pasting a string
}
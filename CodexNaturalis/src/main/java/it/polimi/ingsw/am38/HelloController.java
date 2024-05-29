package it.polimi.ingsw.am38;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;

public class HelloController {
    public TextField textField;
    public Button createButton;
    public Button joinButton;
    public Button okButton;
    public Label promptLabel;
    public Button backButton;
    public BorderPane borderPane;
    private String nickname = "";

    /**
     * This method is ran when the join button is clicked
     */
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

    /**
     * This method is ran when the create button is clicked
     */
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

    /**
     * This method is ran when the back button is clicked
     */
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

    /**
     * This method checks that the text field contains no spaces and its length is no greater than 15.
     * It is run every time a key is typed in the login text field.
     */
    public void checkNicknameLengthNoSpace() {
        final int LIMIT = 15;
        textField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()){ // if the textField text length increased
                if (textField.getText().length() > LIMIT){
                    textField.setText(textField.getText().substring(0, LIMIT)); // if there's more than LIMIT chars then set the text to the previous one
                }
            }
        });

        textField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change; // this return is needed
        }));
    } // not enough, the check still needs to be done in other ways since you can bypass the limit by pasting a string

    /**
     * This method changes the login page background when clicking the credits label
     */
    public void changeBackgroundEasterEgg() {
        System.out.println("Style class: \"" + borderPane.getStyleClass() + "\"");
        if(borderPane.getStyleClass().contains("root")) {
            borderPane.getStyleClass().clear();
            borderPane.getStyleClass().add("easter-egg-background");
        }
        else {
            borderPane.getStyleClass().clear();
            borderPane.getStyleClass().add("root");
        }
    }
}
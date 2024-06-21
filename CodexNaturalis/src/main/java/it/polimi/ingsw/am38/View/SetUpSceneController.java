package it.polimi.ingsw.am38.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SetUpSceneController extends SceneController implements Initializable {
    @FXML
    private VBox facingBox;
    @FXML
    private VBox colorBox;
    @FXML
    private VBox personalOjbBox;
    private final ImageView imageViewFront = new ImageView();
    private final ImageView imageViewBack = new ImageView();
    private final ImageView imageViewRed = new ImageView();
    private final ImageView imageViewGreen = new ImageView();
    private final ImageView imageViewBlue = new ImageView();
    private final ImageView imageViewYellow = new ImageView();
    Dialog dialog = new Dialog();

    /**
     * This method initializes view of the setup phase of the game, where starter card facing, color and personal objective
     * are chosen.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int id;
        int cardWidth = 221;
        int cardHeight = 148;
        Region region = new Region();
        region.setMinSize(cardWidth, 10);

        colorBox.setDisable(true);
        colorBox.setOpacity(0.5);
        personalOjbBox.setDisable(true);
        personalOjbBox.setOpacity(0.5);
        //id = HelloApplication.getStarterCardID();
        /* esempio */ id = 100; // QUESTO È ASSOLUTAMENTE DA TOGLIERE!!!
        Image imageFront = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/" + id + "-front.png")), cardWidth, cardHeight, true, true);
        Image imageBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/" + id + "-back.png")), cardWidth, cardHeight, true, true);

        Image bluePawn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/pawn/bluePawn.png")), 100, 100, true, true);
        Image redPawn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/pawn/redPawn.png")), 100, 100, true, true);
        Image yellowPawn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/pawn/yellowPawn.png")), 100, 100, true, true);
        Image greenPawn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/pawn/greenPawn.png")), 100, 100, true, true);

        // Images are painted in their respective ImageView and an ID is set to them
        imageViewFront.setImage(imageFront); imageViewFront.setId("front");
        imageViewBack.setImage(imageBack); imageViewBack.setId("back");

        imageViewBlue.setImage(bluePawn); imageViewBlue.setId("blue");
        imageViewRed.setImage(redPawn); imageViewRed.setId("red");
        imageViewGreen.setImage(greenPawn); imageViewGreen.setId("green");
        imageViewYellow.setImage(yellowPawn); imageViewYellow.setId("yellow");

        facingBox.getChildren().addAll(imageViewFront, region, imageViewBack);
        enableClickFacing(imageViewFront);
        enableClickFacing(imageViewBack);

        colorBox.getChildren().addAll(imageViewBlue, imageViewRed, imageViewGreen, imageViewYellow);

        enableClickColor(imageViewBlue);
        enableClickColor(imageViewRed);
        enableClickColor(imageViewGreen);
        enableClickColor(imageViewYellow);
    }

    /**
     * Send the clicked facing card to the server
     * @param imageView is sent so that is possible to differ between the "front" and "back" case
     */
    private void enableClickFacing(ImageView imageView){
        imageView.setOnMouseClicked(e -> {
            switch(imageView.getId()){
                case "front":
                case "back":
            }
            facingBox.setDisable(true);
            facingBox.setOpacity(0.5);
            colorBox.setDisable(false);
            colorBox.setOpacity(1);
        });
    }

    /**
     * Send the clicked color to the server
     * @param imageView is sent so that is possible to differ between the colors
     */
    private void enableClickColor(ImageView imageView){
        imageView.setOnMouseClicked(e -> {
            switch(imageView.getId()){
                case "blue":
                case "red":
                case "yellow":
                case "green":
            }
            colorBox.setDisable(true);
            colorBox.setOpacity(0.5);
            personalOjbBox.setDisable(false);
            personalOjbBox.setOpacity(1);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Waiting...");
            dialog.setHeaderText("Successfully sent info to the server!");
            dialog.setContentText("Waiting for all players to join...");
            dialog.show();
        });
    }
}

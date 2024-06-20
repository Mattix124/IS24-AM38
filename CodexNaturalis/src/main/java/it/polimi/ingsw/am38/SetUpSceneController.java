package it.polimi.ingsw.am38;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SetUpSceneController extends SceneController implements Initializable {
    public VBox facingBox;
    public VBox colorBox;
    public VBox personalOjbBox;
    private ImageView imageViewFront = new ImageView();
    private ImageView imageViewBack = new ImageView();
    private ImageView imageViewRed = new ImageView();
    private ImageView imageViewGreen = new ImageView();
    private ImageView imageViewBlue = new ImageView();
    private ImageView imageViewYellow = new ImageView();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int id;
        //id = HelloApplication.getStarterCardID();
        id = 4; // QUESTO È ASSOLUTAMENTE DA TOGLIERE!!!
        Image imageFront = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/front/" + id + "-front.png")), 221, 148, true, true);
        Image imageBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/back/" + id + "-back.png")), 221, 148, true, true);

        Image bluePawn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/pawn/bluePawn.png")), 100, 100, true, true);
        Image redPawn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/pawn/redPawn.png")), 100, 100, true, true);
        Image yellowPawn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/pawn/yellowPawn.png")), 100, 100, true, true);
        Image greenPawn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/pawn/greenPawn.png")), 100, 100, true, true);

        imageViewFront.setImage(imageFront); imageViewFront.setId("front");
        imageViewBack.setImage(imageBack); imageViewBack.setId("back");

        imageViewBlue.setImage(bluePawn); imageViewBlue.setId("blue");
        imageViewRed.setImage(redPawn); imageViewRed.setId("red");
        imageViewGreen.setImage(greenPawn); imageViewGreen.setId("green");
        imageViewYellow.setImage(yellowPawn); imageViewYellow.setId("yellow");

        facingBox.getChildren().addAll(imageViewFront, imageViewBack);
        enableClickFacing(imageViewFront);
        enableClickFacing(imageViewBack);

        colorBox.getChildren().addAll(imageViewBlue, imageViewRed, imageViewGreen, imageViewYellow);
        colorBox.setDisable(true);
        colorBox.setOpacity(0.5);
    }

    private void enableClickFacing(ImageView imageView){
        imageView.setOnMouseClicked(e -> {
            if (imageView.getId().equals("front")){
                // send choosing to server
            } else if (imageView.getId().equals("back")){
                // send choosing to server
            }
            facingBox.setDisable(true);
            facingBox.setOpacity(0.5);
            colorBox.setDisable(false);
            colorBox.setOpacity(1);
        });
    }

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

        });
    }
}

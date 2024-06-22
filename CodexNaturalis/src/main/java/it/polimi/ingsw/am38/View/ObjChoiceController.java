package it.polimi.ingsw.am38.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ObjChoiceController extends SceneController implements Initializable {
    @FXML
    private HBox resourceBox;
    @FXML
    private HBox goldBox;
    @FXML
    private Pane pr0;
    @FXML
    private Pane pr1;
    @FXML
    private Pane pr2;
    @FXML
    private Pane pg0;
    @FXML
    private Pane pg1;
    @FXML
    private Pane pg2;


    private final ImageView imageViewRes0 = new ImageView();
    private final ImageView imageViewRes1 = new ImageView();
    private final ImageView imageViewRes2 = new ImageView();
    private final ImageView imageViewGold0 = new ImageView();
    private final ImageView imageViewGold1 = new ImageView();
    private final ImageView imageViewGold2 = new ImageView();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int cardWidth = 221;
        int cardHeight = 148;

        // qui bisogna cambiare gli id in quelli "effettivi"
        Image resImg0  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/1-back.png")), cardWidth *0.85, cardHeight *0.85, true, true);
        Image resImg1  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/2-front.png")), cardWidth *0.85, cardHeight *0.85, true, true);
        Image resImg2  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/20-front.png")), cardWidth *0.85, cardHeight *0.85, true, true);
        Image goldImg0 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/50-back.png")), cardWidth *0.85, cardHeight *0.85, true, true);
        Image goldImg1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/60-front.png")), cardWidth *0.85, cardHeight *0.85, true, true);
        Image goldImg2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/70-front.png")), cardWidth *0.85, cardHeight *0.85, true, true);

        imageViewRes0.setImage(resImg0);
        imageViewRes1.setImage(resImg1);
        imageViewRes2.setImage(resImg2);
        pr0.getChildren().add(imageViewRes0);
        pr1.getChildren().add(imageViewRes1);
        pr2.getChildren().add(imageViewRes2);

        resourceBox.spacingProperty().set(2);

        imageViewGold0.setImage(goldImg0);
        imageViewGold1.setImage(goldImg1);
        imageViewGold2.setImage(goldImg2);
        pg0.getChildren().add(imageViewGold0);
        pg1.getChildren().add(imageViewGold1);
        pg2.getChildren().add(imageViewGold2);

        goldBox.spacingProperty().set(2);
    }
}

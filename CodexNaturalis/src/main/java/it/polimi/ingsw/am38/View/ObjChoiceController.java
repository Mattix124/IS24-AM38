package it.polimi.ingsw.am38.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ObjChoiceController implements Initializable {
    @FXML
    private Pane personalObjPane1;
    @FXML
    private Pane personalObjPane2;
    @FXML
    private Pane commonObjPane1;
    @FXML
    private Pane commonObjPane2;
    @FXML
    private VBox playersVBox;
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

    private HBox myCardHBox;
    private Pane myCardPane1;
    private Pane myCardPane2;
    private Pane myCardPane3;
    private Pane myCardPane4;

    private HBox otherFirstHBox;
    private Pane otherFirstCardPane1;
    private Pane otherFirstCardPane2;
    private Pane otherFirstCardPane3;
    private Pane otherFirstCardPane4;

    private HBox otherSecondHBox;
    private Pane otherSecondCardPane1;
    private Pane otherSecondCardPane2;
    private Pane otherSecondCardPane3;
    private Pane otherSecondCardPane4;

    private HBox otherThirdHBox;
    private Pane otherThirdCardPane1;
    private Pane otherThirdCardPane2;
    private Pane otherThirdCardPane3;
    private Pane otherThirdCardPane4;

    private final ImageView imageViewMyCard1 = new ImageView();
    private final ImageView imageViewMyCard2 = new ImageView();
    private final ImageView imageViewMyCard3 = new ImageView();
    private final ImageView imageViewMyCard4 = new ImageView();

    private final ImageView imageViewRes0 = new ImageView();
    private final ImageView imageViewRes1 = new ImageView();
    private final ImageView imageViewRes2 = new ImageView();
    private final ImageView imageViewGold0 = new ImageView();
    private final ImageView imageViewGold1 = new ImageView();
    private final ImageView imageViewGold2 = new ImageView();

    private final ImageView imageViewCommonObj1 = new ImageView();
    private final ImageView imageViewCommonObj2 = new ImageView();

    private final ImageView imageViewPersonalObj1 = new ImageView();
    private final ImageView imageViewPersonalObj2 = new ImageView();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int cardWidth = 221;
        int cardHeight = 148;
        int playerNum = 0; // needs to be imported from ClientDATA


        // create set and shows my card (so facing front). 1 2 3 are gold/resource while 4 is starter
        Image myCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/50-front.png")), cardWidth >> 1, cardHeight >> 1, true, true);
        Image myCard2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/6-front.png")), cardWidth >> 1, cardHeight >> 1, true, true);
        Image myCard3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/16-front.png")), cardWidth >> 1, cardHeight >> 1, true, true);
        Image myCard4 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/82-front.png")), cardWidth >> 1, cardHeight >> 1, true, true);

        imageViewMyCard1.setImage(myCard1);
        imageViewMyCard2.setImage(myCard2);
        imageViewMyCard3.setImage(myCard3);
        imageViewMyCard4.setImage(myCard4);

        myCardPane1.getChildren().add(imageViewMyCard1);
        myCardPane2.getChildren().add(imageViewMyCard2);
        myCardPane3.getChildren().add(imageViewMyCard3);
        myCardPane4.getChildren().add(imageViewMyCard4);
        myCardHBox.getChildren().addAll(myCardPane1, myCardPane2, myCardPane3, myCardPane4);
        playersVBox.getChildren().add(myCardHBox);
        myCardHBox.setAlignment(Pos.CENTER);


        //here, "else if" are wrong!
        if(playerNum > 1) {
            Image firstOtherCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/000-back.png")));
            Image firstOtherCard2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/000-back.png")));
            Image firstOtherCard3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/000-back.png")));
            Image firstOtherCard4 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/000-back.png")));

            ImageView imageViewFirstOtherCard1 = new ImageView();
            ImageView imageViewFirstOtherCard2 = new ImageView();
            ImageView imageViewFirstOtherCard3 = new ImageView();
            ImageView imageViewFirstOtherCard4 = new ImageView();

            imageViewFirstOtherCard1.setImage(firstOtherCard1);
            imageViewFirstOtherCard2.setImage(firstOtherCard2);
            imageViewFirstOtherCard3.setImage(firstOtherCard3);
            imageViewFirstOtherCard4.setImage(firstOtherCard4);


            otherFirstCardPane1.getChildren().add(imageViewFirstOtherCard1);
            otherFirstCardPane2.getChildren().add(imageViewFirstOtherCard2);
            otherFirstCardPane3.getChildren().add(imageViewFirstOtherCard3);
            otherFirstCardPane4.getChildren().add(imageViewFirstOtherCard4);
            otherFirstHBox.getChildren().addAll(otherFirstCardPane1, otherFirstCardPane2, otherFirstCardPane3, otherFirstCardPane4);
            playersVBox.getChildren().add(otherFirstHBox);
            otherFirstHBox.setAlignment(Pos.CENTER);
        }
        if(playerNum > 2) {
            Image secondOtherCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/000-back.png")));
            Image secondOtherCard2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/000-back.png")));
            Image secondOtherCard3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/000-back.png")));
            Image secondOtherCard4 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/000-back.png")));

            ImageView imageViewSecondOtherCard1 = new ImageView();
            ImageView imageViewSecondOtherCard2 = new ImageView();
            ImageView imageViewSecondOtherCard3 = new ImageView();
            ImageView imageViewSecondOtherCard4 = new ImageView();

            imageViewSecondOtherCard1.setImage(secondOtherCard1);
            imageViewSecondOtherCard2.setImage(secondOtherCard2);
            imageViewSecondOtherCard3.setImage(secondOtherCard3);
            imageViewSecondOtherCard4.setImage(secondOtherCard4);

            otherSecondCardPane1.getChildren().add(imageViewSecondOtherCard1);
            otherSecondCardPane2.getChildren().add(imageViewSecondOtherCard2);
            otherSecondCardPane3.getChildren().add(imageViewSecondOtherCard3);
            otherSecondCardPane4.getChildren().add(imageViewSecondOtherCard4);
            otherSecondHBox.getChildren().addAll(otherSecondCardPane1, otherSecondCardPane2, otherSecondCardPane3, otherSecondCardPane4);

            playersVBox.getChildren().add(otherSecondHBox);
            otherSecondHBox.setAlignment(Pos.CENTER);
        }
        if(playerNum > 3) {
            Image thirdOtherCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/000-back.png")));
            Image thirdOtherCard2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/000-back.png")));
            Image thirdOtherCard3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/000-back.png")));
            Image thirdOtherCard4 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/000-back.png")));

            ImageView imageViewThirdOtherCard1 = new ImageView();
            ImageView imageViewThirdOtherCard2 = new ImageView();
            ImageView imageViewThirdOtherCard3 = new ImageView();
            ImageView imageViewThirdOtherCard4 = new ImageView();

            imageViewThirdOtherCard1.setImage(thirdOtherCard1);
            imageViewThirdOtherCard2.setImage(thirdOtherCard2);
            imageViewThirdOtherCard3.setImage(thirdOtherCard3);
            imageViewThirdOtherCard4.setImage(thirdOtherCard4);

            otherThirdCardPane1.getChildren().add(imageViewThirdOtherCard1);
            otherThirdCardPane2.getChildren().add(imageViewThirdOtherCard2);
            otherThirdCardPane3.getChildren().add(imageViewThirdOtherCard3);
            otherThirdCardPane4.getChildren().add(imageViewThirdOtherCard4);
            otherThirdHBox.getChildren().addAll(otherThirdCardPane1, otherThirdCardPane2, otherThirdCardPane3, otherThirdCardPane4);
            playersVBox.getChildren().add(otherThirdHBox);
            otherThirdHBox.setAlignment(Pos.CENTER);
        }

        // Common objectives
        Image commonObjImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/000.png")), cardWidth, cardHeight, true, true);
        Image commonObjImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/000.png")), cardWidth, cardHeight, true, true);

        imageViewCommonObj1.setImage(commonObjImage1);
        imageViewCommonObj2.setImage(commonObjImage2);

        commonObjPane1.getChildren().add(imageViewCommonObj1);
        commonObjPane2.getChildren().add(imageViewCommonObj2);

        // Personal objectives
        Image personalObjImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/000.png")), cardWidth, cardHeight, true, true);
        Image personalObjImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/000.png")), cardWidth, cardHeight, true, true);

        imageViewPersonalObj1.setImage(personalObjImage1);
        imageViewPersonalObj2.setImage(personalObjImage2);

        personalObjPane1.getChildren().add(imageViewPersonalObj1);
        personalObjPane2.getChildren().add(imageViewPersonalObj2);

        // no need to add to VBox since they are already added in fxml

        // Decks
        Image resImg0  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/1-back.png")), cardWidth * 0.85, cardHeight * 0.85, true, true);
        Image resImg1  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/2-front.png")), cardWidth * 0.85, cardHeight * 0.85, true, true);
        Image resImg2  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/20-front.png")), cardWidth * 0.85, cardHeight * 0.85, true, true);
        Image goldImg0 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/50-back.png")), cardWidth * 0.85, cardHeight * 0.85, true, true);
        Image goldImg1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/60-front.png")), cardWidth * 0.85, cardHeight * 0.85, true, true);
        Image goldImg2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/70-front.png")), cardWidth * 0.85, cardHeight * 0.85, true, true);

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

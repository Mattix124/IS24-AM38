package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Enum.Symbol;
import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import it.polimi.ingsw.am38.View.GuiSupporDataClasses.ObjChoiceData;
import it.polimi.ingsw.am38.View.GuiSupporDataClasses.StarterChoiceData;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.util.Duration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ObjChoiceController implements PropertyChangeListener {
    @FXML
    public VBox commonObjBox;
    @FXML
    private Pane personalObjPane1;
    @FXML
    private Pane personalObjPane2;
    @FXML
    private Pane commonObjPane1;
    @FXML
    private Pane commonObjPane2;
    @FXML
    private VBox personalObjBox;
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
    private String nickname;
    private HBox myCardHBox = new HBox();
    private Pane myCardPane1 = new Pane();
    private Pane myCardPane2 = new Pane();
    private Pane myCardPane3 = new Pane();
    private Pane myCardPane4 = new Pane();

    private HBox otherFirstHBox = new HBox();
    private Pane otherFirstCardPane1 = new Pane();
    private Pane otherFirstCardPane2 = new Pane();
    private Pane otherFirstCardPane3 = new Pane();
    private Pane otherFirstCardPane4 = new Pane();

    private HBox otherSecondHBox = new HBox();
    private Pane otherSecondCardPane1 = new Pane();
    private Pane otherSecondCardPane2 = new Pane();
    private Pane otherSecondCardPane3 = new Pane();
    private Pane otherSecondCardPane4 = new Pane();

    private HBox otherThirdHBox = new HBox();
    private Pane otherThirdCardPane1 = new Pane();
    private Pane otherThirdCardPane2 = new Pane();
    private Pane otherThirdCardPane3 = new Pane();
    private Pane otherThirdCardPane4 = new Pane();

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

    public void setupScene(ObjChoiceData obd) {
        double cardWidth = 221;
        double cardHeight = 148;
        int playerNum = obd.getPc().size();
        HashMap<String, StarterCard> psc = obd.getPsc();
        LinkedList<String> nicknames = new LinkedList<>();
        nicknames.addAll(psc.keySet());

        this.nickname = obd.getNickname();
        nicknames.remove(nickname);

        // create set and shows my card (so facing front). 1 2 3 are gold/resource while 4 is starter
        Image myCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ obd.getOwnHand().get(0).getCardID() +"-front.png")), cardWidth / 4 , cardHeight /4, true, true);
        Image myCard2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ obd.getOwnHand().get(1).getCardID() +"-front.png")), cardWidth /4, cardHeight/4, true, true);
        Image myCard3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ obd.getOwnHand().get(2).getCardID() +"-front.png")), cardWidth /4, cardHeight /4, true, true);

        Image starterCard1;

        if(psc.get(nickname).getFace())
            starterCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ psc.get(nickname).getCardID()+"-front.png")), cardWidth /2, cardHeight /2, true, true);
        else
            starterCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/"+ psc.get(nickname).getCardID()+"-back.png")), cardWidth /2, cardHeight /2, true, true);
        //textfield per nicknames.get(0)

        imageViewMyCard1.setImage(myCard1);//ownHand
        imageViewMyCard2.setImage(myCard2);
        imageViewMyCard3.setImage(myCard3);

        imageViewMyCard4.setImage(starterCard1);

        myCardPane1.getChildren().add(imageViewMyCard1);
        myCardPane2.getChildren().add(imageViewMyCard2);
        myCardPane3.getChildren().add(imageViewMyCard3);
        myCardPane4.getChildren().add(imageViewMyCard4);
        myCardHBox.getChildren().addAll(myCardPane1, myCardPane2, myCardPane3, myCardPane4);
        playersVBox.getChildren().add(myCardHBox);
        myCardHBox.setAlignment(Pos.CENTER);

        Image animalBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/61-back.png")), cardWidth/4, cardHeight/4, true, true);
        Image plantBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/51-back.png")), cardWidth/4, cardHeight/4, true, true);
        Image fungiBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/41-back.png")), cardWidth/4, cardHeight/4, true, true);
        Image insectBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/71-back.png")), cardWidth/4, cardHeight/4, true, true);
        //here, "else if" are wrong!
        if(playerNum > 1) {
            ImageView imageViewFirstOtherCard1 = new ImageView();
            ImageView imageViewFirstOtherCard2 = new ImageView();
            ImageView imageViewFirstOtherCard3 = new ImageView();
            ImageView imageViewFirstOtherStarterCard = new ImageView();

            Symbol[] playerHand = obd.getHcc().get(nicknames.get(0));
            switch (playerHand[0]){
                case ANIMAL -> imageViewFirstOtherCard1.setImage(animalBack);
                case FUNGI -> imageViewFirstOtherCard1.setImage(fungiBack);
                case PLANT -> imageViewFirstOtherCard1.setImage(plantBack);
                case INSECT -> imageViewFirstOtherCard1.setImage(insectBack);
            }

            switch (playerHand[1]){
                case ANIMAL -> imageViewFirstOtherCard2.setImage(animalBack);
                case FUNGI -> imageViewFirstOtherCard2.setImage(fungiBack);
                case PLANT -> imageViewFirstOtherCard2.setImage(plantBack);
                case INSECT -> imageViewFirstOtherCard2.setImage(insectBack);
            }

            switch (playerHand[2]){
                case ANIMAL -> imageViewFirstOtherCard3.setImage(animalBack);
                case FUNGI -> imageViewFirstOtherCard3.setImage(fungiBack);
                case PLANT -> imageViewFirstOtherCard3.setImage(plantBack);
                case INSECT -> imageViewFirstOtherCard3.setImage(insectBack);
            }

            Image otherStarter1;
            if(psc.get(nicknames.get(0)).getFace())
                otherStarter1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ psc.get(nicknames.get(0)).getCardID() +"-front.png")), cardWidth/2, cardHeight/2, true, true);
            else
                otherStarter1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/"+ psc.get(nicknames.get(0)).getCardID() +"-back.png")), cardWidth/2, cardHeight/2, true, true);
            imageViewFirstOtherStarterCard.setImage(otherStarter1);
            //textfield per nicknames.get(0)


            otherFirstCardPane1.getChildren().add(imageViewFirstOtherCard1);
            otherFirstCardPane2.getChildren().add(imageViewFirstOtherCard2);
            otherFirstCardPane3.getChildren().add(imageViewFirstOtherCard3);
            otherFirstCardPane4.getChildren().add(imageViewFirstOtherStarterCard);
            otherFirstHBox.getChildren().addAll(otherFirstCardPane1, otherFirstCardPane2, otherFirstCardPane3, otherFirstCardPane4);
            playersVBox.getChildren().add(otherFirstHBox);
            otherFirstHBox.setAlignment(Pos.CENTER);
        }
        if(playerNum > 2) {
            ImageView imageViewSecondOtherCard1 = new ImageView();
            ImageView imageViewSecondOtherCard2 = new ImageView();
            ImageView imageViewSecondOtherCard3 = new ImageView();
            ImageView imageViewSecondOtherStarterCard = new ImageView();

            Symbol[] playerHand = obd.getHcc().get(nicknames.get(1));
            switch (playerHand[0]){
                case ANIMAL -> imageViewSecondOtherCard1.setImage(animalBack);
                case FUNGI -> imageViewSecondOtherCard1.setImage(fungiBack);
                case PLANT -> imageViewSecondOtherCard1.setImage(plantBack);
                case INSECT -> imageViewSecondOtherCard1.setImage(insectBack);
            }

            switch (playerHand[1]){
                case ANIMAL -> imageViewSecondOtherCard2.setImage(animalBack);
                case FUNGI -> imageViewSecondOtherCard2.setImage(fungiBack);
                case PLANT -> imageViewSecondOtherCard2.setImage(plantBack);
                case INSECT -> imageViewSecondOtherCard2.setImage(insectBack);
            }

            switch (playerHand[2]){
                case ANIMAL -> imageViewSecondOtherCard3.setImage(animalBack);
                case FUNGI -> imageViewSecondOtherCard3.setImage(fungiBack);
                case PLANT -> imageViewSecondOtherCard3.setImage(plantBack);
                case INSECT -> imageViewSecondOtherCard3.setImage(insectBack);
            }

            Image otherStarter2;
            if(psc.get(nicknames.get(1)).getFace())
                otherStarter2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ psc.get(nicknames.get(1)).getCardID()+"-front.png")), cardWidth/2, cardHeight/2, true, true );
            else
                otherStarter2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/"+ psc.get(nicknames.get(1)).getCardID()+"-back.png")), cardWidth/2, cardHeight/2, true, true );
            imageViewSecondOtherStarterCard.setImage(otherStarter2);
            //textfield with nicknames.get(2)

            otherSecondCardPane1.getChildren().add(imageViewSecondOtherCard1);
            otherSecondCardPane2.getChildren().add(imageViewSecondOtherCard2);
            otherSecondCardPane3.getChildren().add(imageViewSecondOtherCard3);
            otherSecondCardPane4.getChildren().add(imageViewSecondOtherStarterCard);
            otherSecondHBox.getChildren().addAll(otherSecondCardPane1, otherSecondCardPane2, otherSecondCardPane3, otherSecondCardPane4);

            playersVBox.getChildren().add(otherSecondHBox);
            otherSecondHBox.setAlignment(Pos.CENTER);
        }
        if(playerNum > 3) {
            ImageView imageViewThirdOtherCard1 = new ImageView();
            ImageView imageViewThirdOtherCard2 = new ImageView();
            ImageView imageViewThirdOtherCard3 = new ImageView();
            ImageView imageViewThirdOtherStarterCard = new ImageView();

            Symbol[] playerHand = obd.getHcc().get(nicknames.get(2));
            switch (playerHand[0]){
                case ANIMAL -> imageViewThirdOtherCard1.setImage(animalBack);
                case FUNGI -> imageViewThirdOtherCard1.setImage(fungiBack);
                case PLANT -> imageViewThirdOtherCard1.setImage(plantBack);
                case INSECT -> imageViewThirdOtherCard1.setImage(insectBack);
            }

            switch (playerHand[1]){
                case ANIMAL -> imageViewThirdOtherCard2.setImage(animalBack);
                case FUNGI -> imageViewThirdOtherCard2.setImage(fungiBack);
                case PLANT -> imageViewThirdOtherCard2.setImage(plantBack);
                case INSECT -> imageViewThirdOtherCard2.setImage(insectBack);
            }

            switch (playerHand[2]){
                case ANIMAL -> imageViewThirdOtherCard3.setImage(animalBack);
                case FUNGI -> imageViewThirdOtherCard3.setImage(fungiBack);
                case PLANT -> imageViewThirdOtherCard3.setImage(plantBack);
                case INSECT -> imageViewThirdOtherCard3.setImage(insectBack);
            }

            Image otherStarter3;
            if(psc.get(nicknames.get(2)).getFace())
                otherStarter3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/"+ psc.get(nicknames.get(2)).getCardID() +"-back.png")), cardWidth/2, cardHeight/2, true, true);
            else
                otherStarter3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ psc.get(nicknames.get(2)).getCardID() +"-front.png")), cardWidth/2, cardHeight/2, true, true);

            imageViewThirdOtherStarterCard.setImage(otherStarter3);
            //textfield with nicknames.get(3)

            otherThirdCardPane1.getChildren().add(imageViewThirdOtherCard1);
            otherThirdCardPane2.getChildren().add(imageViewThirdOtherCard2);
            otherThirdCardPane3.getChildren().add(imageViewThirdOtherCard3);
            otherThirdCardPane4.getChildren().add(imageViewThirdOtherStarterCard);
            otherThirdHBox.getChildren().addAll(otherThirdCardPane1, otherThirdCardPane2, otherThirdCardPane3, otherThirdCardPane4);
            playersVBox.getChildren().add(otherThirdHBox);
            otherThirdHBox.setAlignment(Pos.CENTER);
        }

        playersVBox.setSpacing(2);

        // Common objectives
        Image commonObjImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ obd.getSharedObj1().getCardID() +"-front.png")), cardWidth/2, cardHeight/2, true, true);
        Image commonObjImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ obd.getSharedObj2().getCardID() +"-front.png")), cardWidth/2, cardHeight/2, true, true);

        imageViewCommonObj1.setImage(commonObjImage1);
        imageViewCommonObj2.setImage(commonObjImage2);

        commonObjPane1.getChildren().add(imageViewCommonObj1);
        commonObjPane2.getChildren().add(imageViewCommonObj2);

        commonObjBox.getChildren().addAll(imageViewCommonObj1, imageViewCommonObj2);

        // Personal objectives
        Image personalObjImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ obd.getObjChoice1().getCardID() +"-front.png")), cardWidth/2, cardHeight/2, true, true);
        Image personalObjImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ obd.getObjChoice1().getCardID() +"-front.png")), cardWidth/2, cardHeight/2, true, true);

        imageViewPersonalObj1.setImage(personalObjImage1);
        imageViewPersonalObj2.setImage(personalObjImage2);
        imageViewPersonalObj1.setId("obj 1");
        imageViewPersonalObj2.setId("obj 2");

        personalObjPane1.getChildren().add(imageViewPersonalObj1);
        personalObjPane2.getChildren().add(imageViewPersonalObj2);
        enableClickObjective(imageViewPersonalObj1);
        enableClickObjective(imageViewPersonalObj2);

        // no need to add to VBox since they are already added in fxml

        // Decks
        Image resImg0  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/1-back.png")), cardWidth * 0.75, cardHeight * 0.75, true, true);
        Image resImg1  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/2-front.png")), cardWidth * 0.75, cardHeight * 0.75, true, true);
        Image resImg2  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/20-front.png")), cardWidth * 0.75, cardHeight * 0.75, true, true);
        Image goldImg0 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/50-back.png")), cardWidth * 0.75, cardHeight * 0.75, true, true);
        Image goldImg1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/60-front.png")), cardWidth * 0.75, cardHeight * 0.75, true, true);
        Image goldImg2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/70-front.png")), cardWidth * 0.75, cardHeight * 0.75, true, true);

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

    private void enableClickObjective(ImageView imageView){
        imageView.setOnMouseClicked(e -> {
            SceneController.cci.checkCommand(imageView.toString());
            //set opacity e disable the box
        });
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        Platform.runLater(() -> {
            switch (evt.getPropertyName())
            {
                case "Start" ->
                {
                    ObjChoiceData ocd = (ObjChoiceData) evt.getNewValue();
                    setupScene(ocd);
                }
            }
        });
    }

}

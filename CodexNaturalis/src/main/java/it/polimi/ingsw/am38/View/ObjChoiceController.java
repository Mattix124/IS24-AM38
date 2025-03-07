package it.polimi.ingsw.am38.View;

import it.polimi.ingsw.am38.Model.Cards.StarterCard;
import it.polimi.ingsw.am38.View.GuiSupporDataClasses.ObjChoiceData;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.Screen;
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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

import static it.polimi.ingsw.am38.View.GUI.guiData;
import static java.lang.Integer.parseInt;

/**
 * Controller of the objChoice scene
 */
public class ObjChoiceController implements PropertyChangeListener {
    @FXML
    private HBox startAndObjBox;
    @FXML
    public VBox commonObjBox;
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
    private final Label mySelf = new Label();
    private final Label player2 = new Label();
    private final Label player3 = new Label();
    private final Label player4 = new Label();
    /**
     * Nickname of the player
     */
    private String nickname;
    /**
     * Box in which to put the hand of the player
     */
    private final HBox myCardHBox = new HBox();
    /**
     * Box in which to put the hand of the second player
     */
    private final HBox otherFirstHBox = new HBox();
    /**
     * Box in which to put the hand of a possible third player
     */
    private final HBox otherSecondHBox = new HBox();
    /**
     * Box in which to put the hand of a possible fourth player
     */
    private final HBox otherThirdHBox = new HBox();

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

    private final Popup popup = new Popup();

    /**
     * Method that set up the view
     *
     * @param obd class from which to take the info to show
     */
    public void setupScene(ObjChoiceData obd) {
        double cardWidth = Screen.getPrimary().getBounds().getWidth()/6;
        double cardHeight = cardWidth*0.669;
        int playerNum = obd.getPc().size();
        double resizeCard = 0;

        switch (playerNum){
            case 2: resizeCard = 1; break;
            case 3: resizeCard = 0.9; break;
            case 4: resizeCard = 0.8; break;
        }

        HashMap<String, StarterCard> psc = obd.getPsc();
        LinkedList<String> nicknames = new LinkedList<>();
        nicknames.addAll(psc.keySet());

        startAndObjBox.setPrefHeight(Screen.getPrimary().getBounds().getHeight()*3/5);
        resourceBox.setPrefHeight(Screen.getPrimary().getBounds().getHeight()*1/5);
        goldBox.setPrefHeight(Screen.getPrimary().getBounds().getHeight()*1/5);
        playersVBox.setPrefWidth(Screen.getPrimary().getBounds().getWidth()/2);

        this.nickname = obd.getNickname();
        nicknames.remove(nickname);
        mySelf.setText(nickname);
        switch (obd.getPc().get(nickname)){
            case RED:
                mySelf.setStyle("-fx-text-fill: #ff0000; -fx-background-color: black;"); break;
            case BLUE:
                mySelf.setStyle("-fx-text-fill: #0066ff; -fx-background-color: black;"); break;
            case GREEN:
                mySelf.setStyle("-fx-text-fill: #008000; -fx-background-color: black;"); break;
            case YELLOW:
                mySelf.setStyle("-fx-text-fill: #ffff00; -fx-background-color: black;"); break;
        }

        // create set and shows my card (so facing front). 1 2 3 are gold/resource while 4 is starter
        Image myCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ obd.getOwnHand().get(0).getCardID() +"-front.png")), cardWidth*0.6*resizeCard, cardHeight*0.6*resizeCard, true, true);
        Image myCard2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ obd.getOwnHand().get(1).getCardID() +"-front.png")), cardWidth*0.6*resizeCard, cardHeight*0.6*resizeCard, true, true);
        Image myCard3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ obd.getOwnHand().get(2).getCardID() +"-front.png")), cardWidth*0.6*resizeCard, cardHeight*0.6*resizeCard, true, true);

        Image starterCard1;

        if(psc.get(nickname).getFace())
            starterCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ psc.get(nickname).getCardID()+"-front.png")), cardWidth*0.8*resizeCard, cardHeight*0.8*resizeCard, true, true);
        else
            starterCard1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/"+ psc.get(nickname).getCardID()+"-back.png")), cardWidth*0.8*resizeCard, cardHeight*0.8*resizeCard, true, true);
        //textfield per nicknames.get(0)

        imageViewMyCard1.setImage(myCard1);//ownHand
        imageViewMyCard2.setImage(myCard2);
        imageViewMyCard3.setImage(myCard3);

        imageViewMyCard4.setImage(starterCard1);

        myCardHBox.setAlignment(Pos.CENTER);
        myCardHBox.setSpacing(2);
        //@ myCardPane1.getChildren().add(imageViewMyCard1);
        //@ myCardPane2.getChildren().add(imageViewMyCard2);
        //@ myCardPane3.getChildren().add(imageViewMyCard3);
        //@ myCardPane4.getChildren().add(imageViewMyCard4);
        myCardHBox.getChildren().addAll(mySelf, imageViewMyCard1, imageViewMyCard2, imageViewMyCard3, imageViewMyCard4);

        playersVBox.getChildren().add(myCardHBox);


        Image GanimalBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/61-back.png")), cardWidth*0.6*resizeCard, cardHeight*0.6*resizeCard, true, true);
        Image GplantBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/51-back.png")), cardWidth*0.6*resizeCard, cardHeight*0.6*resizeCard, true, true);
        Image GfungiBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/41-back.png")), cardWidth*0.6*resizeCard, cardHeight*0.6*resizeCard, true, true);
        Image GinsectBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/71-back.png")), cardWidth*0.6*resizeCard, cardHeight*0.6*resizeCard, true, true);

        Image RfungiBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/1-back.png")), cardWidth*0.6*resizeCard, cardHeight*0.6*resizeCard, true, true);
        Image RplantBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/11-back.png")), cardWidth*0.6*resizeCard, cardHeight*0.6*resizeCard, true, true);
        Image RanimalBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/21-back.png")), cardWidth*0.6*resizeCard, cardHeight*0.6*resizeCard, true, true);
        Image RinsectBack = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/31-back.png")), cardWidth*0.6*resizeCard, cardHeight*0.6*resizeCard, true, true);

        //here, "else if" are wrong!
        if(playerNum > 1) {
            player2.setText(nicknames.get(0));
            switch (obd.getPc().get(nicknames.get(0))){
                case RED:
                    player2.setStyle("-fx-text-fill: #ff0000; -fx-background-color: black;"); break;
                case BLUE:
                    player2.setStyle("-fx-text-fill: #0066ff; -fx-background-color: black;"); break;
                case GREEN:
                    player2.setStyle("-fx-text-fill: #008000; -fx-background-color: black;"); break;
                case YELLOW:
                    player2.setStyle("-fx-text-fill: #ffff00; -fx-background-color: black;"); break;
            }

            ImageView imageViewFirstOtherCard1 = new ImageView();
            ImageView imageViewFirstOtherCard2 = new ImageView();
            ImageView imageViewFirstOtherCard3 = new ImageView();
            ImageView imageViewFirstOtherStarterCard = new ImageView();

            String[] playerHand = obd.getHcc().get(nicknames.get(0));
            switch (playerHand[0]){
                case "RA" -> imageViewFirstOtherCard1.setImage(RanimalBack);
                case "RF" -> imageViewFirstOtherCard1.setImage(RfungiBack);
                case "RP" -> imageViewFirstOtherCard1.setImage(RplantBack);
                case "RI" -> imageViewFirstOtherCard1.setImage(RinsectBack);
                case "GA" -> imageViewFirstOtherCard1.setImage(GanimalBack);
                case "GF" -> imageViewFirstOtherCard1.setImage(GfungiBack);
                case "GP" -> imageViewFirstOtherCard1.setImage(GplantBack);
                case "GI" -> imageViewFirstOtherCard1.setImage(GinsectBack);
            }


            switch (playerHand[1]){
                case "RA" -> imageViewFirstOtherCard2.setImage(RanimalBack);
                case "RF" -> imageViewFirstOtherCard2.setImage(RfungiBack);
                case "RP" -> imageViewFirstOtherCard2.setImage(RplantBack);
                case "RI" -> imageViewFirstOtherCard2.setImage(RinsectBack);
                case "GA" -> imageViewFirstOtherCard2.setImage(GanimalBack);
                case "GF" -> imageViewFirstOtherCard2.setImage(GfungiBack);
                case "GP" -> imageViewFirstOtherCard2.setImage(GplantBack);
                case "GI" -> imageViewFirstOtherCard2.setImage(GinsectBack);
            }

            switch (playerHand[2]){
                case "RA" -> imageViewFirstOtherCard3.setImage(RanimalBack);
                case "RF" -> imageViewFirstOtherCard3.setImage(RfungiBack);
                case "RP" -> imageViewFirstOtherCard3.setImage(RplantBack);
                case "RI" -> imageViewFirstOtherCard3.setImage(RinsectBack);
                case "GA" -> imageViewFirstOtherCard3.setImage(GanimalBack);
                case "GF" -> imageViewFirstOtherCard3.setImage(GfungiBack);
                case "GP" -> imageViewFirstOtherCard3.setImage(GplantBack);
                case "GI" -> imageViewFirstOtherCard3.setImage(GinsectBack);
            }

            Image otherStarter1;
            if(psc.get(nicknames.get(0)).getFace())
                otherStarter1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ psc.get(nicknames.get(0)).getCardID() +"-front.png")), cardWidth*0.8*resizeCard, cardHeight*0.8*resizeCard,true, true);
            else
                otherStarter1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/"+ psc.get(nicknames.get(0)).getCardID() +"-back.png")), cardWidth*0.8*resizeCard, cardHeight*0.8*resizeCard, true, true);
            imageViewFirstOtherStarterCard.setImage(otherStarter1);

            otherFirstHBox.getChildren().addAll(player2, imageViewFirstOtherCard1, imageViewFirstOtherCard2, imageViewFirstOtherCard3, imageViewFirstOtherStarterCard);
            otherFirstHBox.setAlignment(Pos.CENTER);
            otherFirstHBox.setSpacing(2);
            playersVBox.getChildren().add(otherFirstHBox);
        }
        if(playerNum > 2) {
            player3.setText(nicknames.get(1));
            switch (obd.getPc().get(nicknames.get(1))){
                case RED:
                    player3.setStyle("-fx-text-fill: #ff0000; -fx-background-color: black;"); break;
                case BLUE:
                    player3.setStyle("-fx-text-fill: #0066ff; -fx-background-color: black;"); break;
                case GREEN:
                    player3.setStyle("-fx-text-fill: #008000; -fx-background-color: black;"); break;
                case YELLOW:
                    player3.setStyle("-fx-text-fill: #ffff00; -fx-background-color: black;"); break;
            }

            ImageView imageViewSecondOtherCard1 = new ImageView();
            ImageView imageViewSecondOtherCard2 = new ImageView();
            ImageView imageViewSecondOtherCard3 = new ImageView();
            ImageView imageViewSecondOtherStarterCard = new ImageView();

            String[] playerHand = obd.getHcc().get(nicknames.get(1));
            switch (playerHand[0]){
                case "RA" -> imageViewSecondOtherCard1.setImage(RanimalBack);
                case "RF" -> imageViewSecondOtherCard1.setImage(RfungiBack);
                case "RP" -> imageViewSecondOtherCard1.setImage(RplantBack);
                case "RI" -> imageViewSecondOtherCard1.setImage(RinsectBack);
                case "GA" -> imageViewSecondOtherCard1.setImage(GanimalBack);
                case "GF" -> imageViewSecondOtherCard1.setImage(GfungiBack);
                case "GP" -> imageViewSecondOtherCard1.setImage(GplantBack);
                case "GI" -> imageViewSecondOtherCard1.setImage(GinsectBack);
            }

            switch (playerHand[1]){
                case "RA" -> imageViewSecondOtherCard2.setImage(RanimalBack);
                case "RF" -> imageViewSecondOtherCard2.setImage(RfungiBack);
                case "RP" -> imageViewSecondOtherCard2.setImage(RplantBack);
                case "RI" -> imageViewSecondOtherCard2.setImage(RinsectBack);
                case "GA" -> imageViewSecondOtherCard2.setImage(GanimalBack);
                case "GF" -> imageViewSecondOtherCard2.setImage(GfungiBack);
                case "GP" -> imageViewSecondOtherCard2.setImage(GplantBack);
                case "GI" -> imageViewSecondOtherCard2.setImage(GinsectBack);
            }

            switch (playerHand[2]){
                case "RA" -> imageViewSecondOtherCard3.setImage(RanimalBack);
                case "RF" -> imageViewSecondOtherCard3.setImage(RfungiBack);
                case "RP" -> imageViewSecondOtherCard3.setImage(RplantBack);
                case "RI" -> imageViewSecondOtherCard3.setImage(RinsectBack);
                case "GA" -> imageViewSecondOtherCard3.setImage(GanimalBack);
                case "GF" -> imageViewSecondOtherCard3.setImage(GfungiBack);
                case "GP" -> imageViewSecondOtherCard3.setImage(GplantBack);
                case "GI" -> imageViewSecondOtherCard3.setImage(GinsectBack);
            }

            Image otherStarter2;
            if(psc.get(nicknames.get(1)).getFace())
                otherStarter2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ psc.get(nicknames.get(1)).getCardID()+"-front.png")), cardWidth*0.8*resizeCard, cardHeight*0.8*resizeCard, true, true );
            else
                otherStarter2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/"+ psc.get(nicknames.get(1)).getCardID()+"-back.png")), cardWidth*0.8*resizeCard, cardHeight*0.8*resizeCard, true, true );
            imageViewSecondOtherStarterCard.setImage(otherStarter2);

            otherSecondHBox.getChildren().addAll(player3, imageViewSecondOtherCard1, imageViewSecondOtherCard2, imageViewSecondOtherCard3, imageViewSecondOtherStarterCard);
            otherSecondHBox.setSpacing(2);
            otherSecondHBox.setAlignment(Pos.CENTER);
            playersVBox.getChildren().add(otherSecondHBox);
        }
        if(playerNum > 3) {
            player4.setText(nicknames.get(2));
            switch (obd.getPc().get(nicknames.get(2))){
                case RED:
                    player4.setStyle("-fx-text-fill: #ff0000; -fx-background-color: black;"); break;
                case BLUE:
                    player4.setStyle("-fx-text-fill: #0066ff; -fx-background-color: black;"); break;
                case GREEN:
                    player4.setStyle("-fx-text-fill: #008000; -fx-background-color: black;"); break;
                case YELLOW:
                    player4.setStyle("-fx-text-fill: #ffff00; -fx-background-color: black;"); break;
            }

            ImageView imageViewThirdOtherCard1 = new ImageView();
            ImageView imageViewThirdOtherCard2 = new ImageView();
            ImageView imageViewThirdOtherCard3 = new ImageView();
            ImageView imageViewThirdOtherStarterCard = new ImageView();

            String[] playerHand = obd.getHcc().get(nicknames.get(2));
            switch (playerHand[0]){
                case "RA" -> imageViewThirdOtherCard1.setImage(RanimalBack);
                case "RF" -> imageViewThirdOtherCard1.setImage(RfungiBack);
                case "RP" -> imageViewThirdOtherCard1.setImage(RplantBack);
                case "RI" -> imageViewThirdOtherCard1.setImage(RinsectBack);
                case "GA" -> imageViewThirdOtherCard1.setImage(GanimalBack);
                case "GF" -> imageViewThirdOtherCard1.setImage(GfungiBack);
                case "GP" -> imageViewThirdOtherCard1.setImage(GplantBack);
                case "GI" -> imageViewThirdOtherCard1.setImage(GinsectBack);
            }

            switch (playerHand[1]){
                case "RA" -> imageViewThirdOtherCard2.setImage(RanimalBack);
                case "RF" -> imageViewThirdOtherCard2.setImage(RfungiBack);
                case "RP" -> imageViewThirdOtherCard2.setImage(RplantBack);
                case "RI" -> imageViewThirdOtherCard2.setImage(RinsectBack);
                case "GA" -> imageViewThirdOtherCard2.setImage(GanimalBack);
                case "GF" -> imageViewThirdOtherCard2.setImage(GfungiBack);
                case "GP" -> imageViewThirdOtherCard2.setImage(GplantBack);
                case "GI" -> imageViewThirdOtherCard2.setImage(GinsectBack);
            }

            switch (playerHand[2]){
                case "RA" -> imageViewThirdOtherCard3.setImage(RanimalBack);
                case "RF" -> imageViewThirdOtherCard3.setImage(RfungiBack);
                case "RP" -> imageViewThirdOtherCard3.setImage(RplantBack);
                case "RI" -> imageViewThirdOtherCard3.setImage(RinsectBack);
                case "GA" -> imageViewThirdOtherCard3.setImage(GanimalBack);
                case "GF" -> imageViewThirdOtherCard3.setImage(GfungiBack);
                case "GP" -> imageViewThirdOtherCard3.setImage(GplantBack);
                case "GI" -> imageViewThirdOtherCard3.setImage(GinsectBack);
            }

            Image otherStarter3;
            if(psc.get(nicknames.get(2)).getFace())
                otherStarter3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/"+ psc.get(nicknames.get(2)).getCardID() +"-back.png")), cardWidth*0.8*resizeCard, cardHeight*0.8*resizeCard, true, true);
            else
                otherStarter3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ psc.get(nicknames.get(2)).getCardID() +"-front.png")), cardWidth*0.8*resizeCard, cardHeight*0.8*resizeCard, true, true);

            imageViewThirdOtherStarterCard.setImage(otherStarter3);

            otherThirdHBox.getChildren().addAll(player4, imageViewThirdOtherCard1, imageViewThirdOtherCard2, imageViewThirdOtherCard3, imageViewThirdOtherStarterCard);
            otherThirdHBox.setSpacing(2);
            otherThirdHBox.setAlignment(Pos.CENTER);
            playersVBox.getChildren().add(otherThirdHBox);
        }

        playersVBox.setSpacing(2);

        // Common objectives
        Image commonObjImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ obd.getSharedObj1().getCardID() +"-front.png")), cardWidth*0.85, cardHeight*0.85, true, true);
        Image commonObjImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ obd.getSharedObj2().getCardID() +"-front.png")), cardWidth*0.85, cardHeight*0.85, true, true);

        imageViewCommonObj1.setImage(commonObjImage1);
        imageViewCommonObj2.setImage(commonObjImage2);

        commonObjBox.setSpacing(5);
        commonObjBox.getChildren().addAll(imageViewCommonObj1, imageViewCommonObj2);

        // Personal objectives
        Image personalObjImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ obd.getObjChoice1().getCardID() +"-front.png")), cardWidth*0.85, cardHeight*0.85, true, true);
        Image personalObjImage2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/front/"+ obd.getObjChoice2().getCardID() +"-front.png")), cardWidth*0.85, cardHeight*0.85, true, true);

        imageViewPersonalObj1.setImage(personalObjImage1);
        imageViewPersonalObj2.setImage(personalObjImage2);
        imageViewPersonalObj1.setId("obj 1");
        imageViewPersonalObj2.setId("obj 2");

        personalObjBox.setSpacing(5);
        personalObjBox.getChildren().addAll(imageViewPersonalObj1, imageViewPersonalObj2);
        enableClickObjective(imageViewPersonalObj1, obd);
        enableClickObjective(imageViewPersonalObj2, obd);

        // no need to add to VBox since they are already added in fxml

        // Decks
        int tempId = 0;
        switch(obd.getTopR())
        {
            case FUNGI -> tempId = 1;
            case PLANT -> tempId = 11;
            case ANIMAL -> tempId = 21;
            case INSECT -> tempId = 31;
        }
        Image resImg0  = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/"+tempId+"-back.png")), cardWidth * 0.75, cardHeight * 0.75, true, true);
        guiData.setFirstTopR("GameImages/back/" + tempId + "-back.png");
        switch(obd.getTopG())
        {
            case FUNGI -> tempId = 41;
            case PLANT -> tempId = 51;
            case ANIMAL -> tempId = 61;
            case INSECT -> tempId = 71;
        }

        Image goldImg0 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("GameImages/back/"+tempId+"-back.png")), cardWidth * 0.75, cardHeight * 0.75, true, true);
        guiData.setFirstTopG("GameImages/back/" + tempId + "-back.png");

        Image resImg1  = new Image(Objects.requireNonNull(getClass().getResourceAsStream(guiData.getFirstRes1())), cardWidth * 0.75, cardHeight * 0.75, true, true);
        Image resImg2  = new Image(Objects.requireNonNull(getClass().getResourceAsStream(guiData.getFirstRes2())), cardWidth * 0.75, cardHeight * 0.75, true, true);
        Image goldImg1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(guiData.getFirstGold1())), cardWidth * 0.75, cardHeight * 0.75, true, true);
        Image goldImg2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(guiData.getFirstGold2())), cardWidth * 0.75, cardHeight * 0.75, true, true);

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

    /**
     * This method sends the chosen personal objective to the server and sets it in guiData so that it is
     * displayable in the GUI
     *
     * @param imageView
     * @param objChoiceData
     */
    private void enableClickObjective(ImageView imageView, ObjChoiceData objChoiceData){
        imageView.setOnMouseClicked(e -> {
            SceneController.cci.checkCommand(imageView.getId());
            personalObjBox.setOpacity(0.5);
            personalObjBox.setDisable(true);
            String[] s = imageView.getId().split(" ");
            switch(parseInt(s[1])){
                case 1 -> guiData.setObjective(objChoiceData.getObjChoice1().getCardID());
                case 2 -> guiData.setObjective(objChoiceData.getObjChoice2().getCardID());
            }
        });
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
            switch (evt.getPropertyName())
            {
                case "Start" ->
                {
                    ObjChoiceData ocd = (ObjChoiceData) evt.getNewValue();
                    setupScene(ocd);
                }
                case "Wait" ->
                {
                    Label l = new Label("Waiting for other players...");
                    l.setFont(new Font(22));
                    l.setTextFill(Color.BLACK);
                    FadeTransition fade = new FadeTransition(new Duration(500),personalObjBox.getScene().getRoot());
                    fade.setToValue(0.7);
                    fade.setFromValue(1);
                    fade.playFromStart();
                    popup.getContent().add(l);
                    popup.show(personalObjBox.getScene().getWindow());
                }
                case "RemoveLabel" -> popup.hide();
            }
        });
    }

}

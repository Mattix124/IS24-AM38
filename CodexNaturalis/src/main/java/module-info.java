module it.polimi.ingsw.am38 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens it.polimi.ingsw.am38 to javafx.fxml;
    exports it.polimi.ingsw.am38;
    exports it.polimi.ingsw.am38.Cards;
    opens it.polimi.ingsw.am38.Cards to javafx.fxml;
}
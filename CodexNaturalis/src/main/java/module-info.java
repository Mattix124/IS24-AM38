module it.polimi.ingsw.am38 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens it.polimi.ingsw.am38 to javafx.fxml;
    exports it.polimi.ingsw.am38;
}
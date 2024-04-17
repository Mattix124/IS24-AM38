module it.polimi.ingsw.am38 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    //requires org.json;
    requires com.google.gson;

    opens it.polimi.ingsw.am38 to javafx.fxml;
    exports it.polimi.ingsw.am38;
    exports it.polimi.ingsw.am38.Model;
    opens it.polimi.ingsw.am38.Model to javafx.fxml;
    exports it.polimi.ingsw.am38.Model.Cards;
    opens it.polimi.ingsw.am38.Model.Cards to javafx.fxml;
}
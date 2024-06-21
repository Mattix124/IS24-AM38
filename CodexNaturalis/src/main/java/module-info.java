module it.polimi.ingsw.am38 {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.controlsfx.controls;
	requires com.google.gson;
	requires java.rmi;
	requires java.desktop;
	exports it.polimi.ingsw.am38.View;
	opens it.polimi.ingsw.am38.View to javafx . fxml, javafx .
graphics;
	exports it.polimi.ingsw.am38;
	exports it.polimi.ingsw.am38.Model;
	opens it.polimi.ingsw.am38.Model to javafx.fxml;
	exports it.polimi.ingsw.am38.Model.Cards;
	opens it.polimi.ingsw.am38.Model.Cards to javafx.fxml;
	exports it.polimi.ingsw.am38.Network.Server;
	opens it.polimi.ingsw.am38.Network.Server to javafx.fxml;
	exports it.polimi.ingsw.am38.Network.Client;
	opens it.polimi.ingsw.am38.Network.Client to javafx.fxml;
	exports it.polimi.ingsw.am38.Controller;
	exports it.polimi.ingsw.am38.Network.Packet.CommunicationClasses;
	opens it.polimi.ingsw.am38.Network.Packet.CommunicationClasses to javafx.fxml;
	exports it.polimi.ingsw.am38.Network.Packet;
	opens it.polimi.ingsw.am38.Network.Packet to javafx.fxml;
	opens it.polimi.ingsw.am38 to javafx.fxml, javafx.graphics;
}
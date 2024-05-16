package it.polimi.ingsw.am38;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application
{
	@Override
	public void start(Stage stage) throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
		Scene      scene      = new Scene(fxmlLoader.load(), 320, 240);
		stage.setTitle("Hello!");
		stage.setScene(scene);


		stage.show();


		String gameTitle3=
				" \u001B[32m .oooooo.     .oooooo.   oooooooooo.   oooooooooooo ooooooo  ooooo      ooooo      ooo       .o.       ooooooooooooo ooooo     ooo ooooooooo.         .o.       ooooo        ooooo  .oooooo..o \n" +
						" d8P'  `Y8b   d8P'  `Y8b  `888'   `Y8b  `888'     `8  `8888    d8'       `888b.     `8'      .888.      8'   888   `8 `888'     `8' `888   `Y88.      .888.      `888'        `888' d8P'    `Y8 \n" +
						"888          888      888  888      888  888            Y888..8P          8 `88b.    8      .8\"888.          888       888       8   888   .d88'     .8\"888.      888          888  Y88bo.      \n" +
						"888          888      888  888      888  888oooo8        `8888'           8   `88b.  8     .8' `888.         888       888       8   888ooo88P'     .8' `888.     888          888   `\"Y8888o.  \n" +
						"888          888      888  888      888  888    \"       .8PY888.          8     `88b.8    .88ooo8888.        888       888       8   888`88b.      .88ooo8888.    888          888       `\"Y88b \n" +
						"`88b    ooo  `88b    d88'  888     d88'  888       o   d8'  `888b         8       `888   .8'     `888.       888       `88.    .8'   888  `88b.   .8'     `888.   888       o  888  oo     .d8P \n" +
						" `Y8bood8P'   `Y8bood8P'  o888bood8P'   o888ooooood8 o888o  o88888o      o8o        `8  o88o     o8888o     o888o        `YbodP'    o888o  o888o o88o     o8888o o888ooooood8 o888o 8\"\"88888P'  \n" +
						"                                                                                                                                                                                                \n" +
						"                                                                                                                                                                                                \n";
	}


	public static void main(String[] args)
	{
		launch();
	}
}
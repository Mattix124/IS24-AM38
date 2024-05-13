package it.polimi.ingsw.am38.Network.Server;

public enum RMITURNING
{
	LOGINPHASE, //Login+Create/Join Game
	SETUPPHASE, //StartCard face choice, Color choice, personal obj choice
	PLAYPHASE,    //Play card (Effective game phase)
	DRAWPHASE,
	EXTRA //if needed
}

package it.polimi.ingsw.am38.Network.Server;

public enum Turnings
{
	CHOOSE1, //choose the face of the starter card
	CHOOSE2, //choose the color
	CHOOSE3, //choose the objective card
	NOCTURN, //only chat, show cards and enemy's field
	PLAYPHASE,    //Play card (Effective game phase)S
	DRAWPHASE,
	STANDBY //if needed
}

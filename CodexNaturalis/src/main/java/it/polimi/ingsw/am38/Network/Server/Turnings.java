package it.polimi.ingsw.am38.Network.Server;

/**
 * This enum is used to scan every phase of a player turn or moment
 */
public enum Turnings
{
	CHOOSE1, //choose the face of the starter card
	CHOOSE2, //choose the color
	CHOOSE3, //choose the objective card
	NOCTURN, //only chat, show cards and enemy's field
	PLAYPHASE,    //Play card (Actual game phase)
	DRAWPHASE,
	STANDBY //if needed
}

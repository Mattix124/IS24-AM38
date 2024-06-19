package it.polimi.ingsw.am38.Network.Packet;

/**
 * This Enum Class lists all the type of request Client/Server of the TCP Message
 */
public enum Scope
{
    LOGIN,
    NICKNAME,

    INFOMESSAGE,  //Server->Client answer
    START,


    //Game---------------------------
    GAME,

	STARTINGFACECHOICE,
	COLORCHOICE,
	OBJECTIVECHOICE,

    PLAYCARD,
	PLACEMENT,
	NOPOSSIBLEPLACEMENT,
	NOTPLACEABLE,
    DRAWCARD,
    EMPTYDECK,
    DRAWCONFIRMED,
   //---------------------------
    //View
    VIEWUPDATE,


    //Chat--------------
    CHAT,
    BCHAT,
    PCHAT,

    //Extra
    KILL,
    EXCEPTION,
	CONNECTION,
    WINNER

    }

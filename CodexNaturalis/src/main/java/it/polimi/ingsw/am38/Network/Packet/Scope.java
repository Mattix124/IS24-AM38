package it.polimi.ingsw.am38.Network.Packet;

import java.io.Serializable;

/**
 * This Enum Class lists all the type of request Client/Server of the TCP Message
 */
public enum Scope implements Serializable
{
	LOGIN,
    NICKNAME,

    INFOMESSAGE,
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

package it.polimi.ingsw.am38.Network.Packet;

/**
 * This Enum Class lists all the type of request Client/Server of the TCP Message
 */
public enum Scope
{
    INFOMESSAGE,  //Server->Client answer

    //Game---------------------------
    GAME,
    STARTINGFACECHOICE,
    COLORCHOICE,
    OBJECTIVECHOICE,
    PLAYCARD,
    DRAWCARD,
   //---------------------------
    //View
    VIEWUPDATE,
    PLACEMENT,
    SHOWCARD,
    SHOWFIELD,

    //Chat--------------
    CHAT,
    BCHAT,
    PCHAT,

    //Extra
    KILL,
    EXCEPTION,
    CONNECTION, //not yet used
    WINNER

    }

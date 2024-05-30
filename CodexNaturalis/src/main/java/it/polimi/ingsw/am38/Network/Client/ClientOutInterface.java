package it.polimi.ingsw.am38.Network.Client;

public interface ClientOutInterface
{
	//Login phase
	void login();

	void join();

	void create();

	//Out-Login phase
	void sendStarterCardResponse();

	void sendColorResponse();

	void sendObjResponse();

	void sendChatAll();

	void sendChatWhisper();

	void sendDrawRequest();

	void sendPlayRequest();

}

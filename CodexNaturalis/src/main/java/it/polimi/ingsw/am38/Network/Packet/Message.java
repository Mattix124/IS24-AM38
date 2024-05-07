package it.polimi.ingsw.am38.Network.Packet;

public class Message {
    private MessageHeader header;
    private MessageContent content;

    public Message(MessageHeader h){
        this.header = h;
    }

    public void setMessage(MessageContent c){
        this.content = c;
    }

    public MessageHeader getHeader() {
        return header;
    }
}

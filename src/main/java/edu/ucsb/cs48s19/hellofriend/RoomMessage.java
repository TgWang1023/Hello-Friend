package edu.ucsb.cs48s19.hellofriend;

public class RoomMessage {
    private String roomNumber;
    private String roomMessage;
    private String messageEN;
    private String messageCH;

    public RoomMessage() { }

    public RoomMessage(String messageCH, String messageEN) {
        this.messageEN = messageEN;
        this.messageCH = messageCH;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomMessage() {
        return roomMessage;
    }

    public void setRoomMessage(String roomMessage) {
        this.roomMessage = roomMessage;
    }

    public String getMessageEN() {
        return messageEN;
    }

    public void setMessageEN(String messageEN) {
        this.messageEN = messageEN;
    }

    public String getMessageCH() {
        return messageCH;
    }

    public void setMessageCH(String messageCH) {
        this.messageCH = messageCH;
    }
}
